package br.org.cidadessustentaveis.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.mail.EmailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.org.cidadessustentaveis.dto.AprovacaoPrefeituraDTO;
import br.org.cidadessustentaveis.dto.AprovacaoPrefeituraFiltroDTO;
import br.org.cidadessustentaveis.dto.AprovacaoPrefeituraPendenteDTO;
import br.org.cidadessustentaveis.dto.AprovacaoPrefeituraSimplesDTO;
import br.org.cidadessustentaveis.model.administracao.AprovacaoPrefeitura;
import br.org.cidadessustentaveis.model.administracao.Cidade;
import br.org.cidadessustentaveis.model.administracao.EmailToken;
import br.org.cidadessustentaveis.model.administracao.Prefeitura;
import br.org.cidadessustentaveis.model.enums.FuncionalidadeToken;
import br.org.cidadessustentaveis.repository.AprovacaoPrefeituraRepository;
import br.org.cidadessustentaveis.services.exceptions.ObjectNotFoundException;
import br.org.cidadessustentaveis.util.EmailUtil;
import br.org.cidadessustentaveis.util.ProfileUtil;
import br.org.cidadessustentaveis.util.SenhaUtil;

@Service
public class AprovacaoPrefeituraService {

	@Autowired
	private AprovacaoPrefeituraRepository repository;
	@Autowired
	private ProfileUtil profileUtil;
	@Autowired
	private EmailUtil emailUtil;
	@Autowired
	private EmailTokenService emailTokenService;
	@Autowired
	private PrefeituraService prefeituraService;
	@Autowired
	private EntityManager em;
	@Autowired
	private CidadeService cidadeService;

	public AprovacaoPrefeitura criarPedidoAprovacao(Prefeitura prefeitura) {
		AprovacaoPrefeitura aprovacaoPrefeitura = AprovacaoPrefeitura.builder().prefeitura(prefeitura).data(new Date())
				.status("Pendente").build();
		AprovacaoPrefeitura entity = repository.save(aprovacaoPrefeitura);
		return entity;
	}

	public AprovacaoPrefeitura aprovar(AprovacaoPrefeituraSimplesDTO aprovacao) {
		AprovacaoPrefeitura aprovacaoPrefeitura = alterarStatus(aprovacao.getId(), "Aprovada", null);
		prefeituraService.alterarDataMandato(aprovacaoPrefeitura.getPrefeitura(), aprovacao.getInicioMandato(),
				aprovacao.getFimMandato());
		EmailToken emailToken = EmailToken.builder().ativo(Boolean.TRUE)
				.funcionalidadeToken(FuncionalidadeToken.APROVACAO_PREFEITURA)
				.hash(SenhaUtil.criptografarSHA2(aprovacaoPrefeitura.getId() + ""
						+ aprovacaoPrefeitura.getPrefeitura().getNome() + "" + LocalDateTime.now().getNano()))
				.aprovacaoPrefeitura(aprovacaoPrefeitura).build();
		emailTokenService.salvar(emailToken);
		try {
			emailAprovacaoPrefeituraV2(emailToken);
		} catch (EmailException e) {
			e.printStackTrace();
		}

		return aprovacaoPrefeitura;
	}
	
	@Transactional
	public AprovacaoPrefeitura aprovarAlterarDados(AprovacaoPrefeituraPendenteDTO aprovacao) {
		AprovacaoPrefeitura aprovacaoPrefeitura = alterarStatus(aprovacao.getId(), "Aprovada", null);
		Prefeitura prefeitura = prefeituraService.buscarPorId(aprovacao.getPrefeitura().getId());
		aprovacao.getPrefeitura().setPartidoPolitico(prefeitura.getPartidoPolitico());
		aprovacao.getPrefeitura().setCidade(prefeitura.getCidade());
		prefeituraService.alterarDataMandato(aprovacao.getPrefeitura(), aprovacao.getInicioMandato(),
				aprovacao.getFimMandato());
		EmailToken emailToken = EmailToken.builder().ativo(Boolean.TRUE)
				.funcionalidadeToken(FuncionalidadeToken.APROVACAO_PREFEITURA)
				.hash(SenhaUtil.criptografarSHA2(aprovacaoPrefeitura.getId() + ""
						+ aprovacaoPrefeitura.getPrefeitura().getNome() + "" + LocalDateTime.now().getNano()))
				.aprovacaoPrefeitura(aprovacaoPrefeitura).build();
		emailTokenService.salvar(emailToken);
		try {
			emailAprovacaoPrefeituraV2(emailToken);
		} catch (EmailException e) {
			e.printStackTrace();
		}

		return aprovacaoPrefeitura;
	}

	public AprovacaoPrefeitura reprovar(Long idAprovacaoPrefeitura, String justificativa) {
		AprovacaoPrefeitura aprovacaoPrefeitura = alterarStatus(idAprovacaoPrefeitura, "Reprovada", justificativa);
		try {
			emailReprovacaoPrefeitura(aprovacaoPrefeitura, justificativa);
		} catch (EmailException ex) {
			ex.printStackTrace();
		}
		return aprovacaoPrefeitura;
	}

	public boolean reenviarEmailPrefeitura(Long idPrefeitura, String listaEmail) {
		Prefeitura prefeitura = prefeituraService.buscarPorId(idPrefeitura);
		AprovacaoPrefeitura pedido = repository.findByPrefeituraAndStatus(prefeitura, "Aprovada");
		if (pedido != null) {
			pedido.getPrefeitura().setEmail(listaEmail);
			EmailToken emailToken = emailTokenService.reenviarEmailPrefeitura(pedido.getPrefeitura(),
					FuncionalidadeToken.APROVACAO_PREFEITURA, true);
			if (emailToken != null) {
				emailTokenService.salvar(emailToken);
				try {
					emailAprovacaoPrefeitura(emailToken);
					return true;
				} catch (EmailException e) {
					e.printStackTrace();
				}
			}

		}
		return false;
	}

	public List<AprovacaoPrefeitura> getAprovacoesPrefeituras() {
		return repository.findAllByOrderByDataDesc();
	}
	
	public List<AprovacaoPrefeituraDTO> filtrarAprovacaoPrefeitura(AprovacaoPrefeituraFiltroDTO aprovacaoPrefeituraFiltroDTO) {
		
		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<AprovacaoPrefeituraDTO> query = cb.createQuery(AprovacaoPrefeituraDTO.class);
		
		Root<AprovacaoPrefeitura> aprovacaoPrefeitura = query.from(AprovacaoPrefeitura.class);
		
		Join<AprovacaoPrefeitura, Prefeitura> joinPrefeitura = aprovacaoPrefeitura.join("prefeitura",JoinType.LEFT);
		Join<Prefeitura, Cidade> joinCidade = joinPrefeitura.join("cidade",JoinType.LEFT);
		
		
		query.multiselect(aprovacaoPrefeitura);
		
		List<javax.persistence.criteria.Predicate> predicateList = new ArrayList<>();
		
		DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");	
		
		if (aprovacaoPrefeituraFiltroDTO.getNomePrefeitura() != null && !aprovacaoPrefeituraFiltroDTO.getNomePrefeitura().equals("")) {
			Path<String> nomePrefeitura = joinCidade.get("nome");
			Predicate predicateNomePrefeitura = cb.like(cb.lower(nomePrefeitura), "%" + aprovacaoPrefeituraFiltroDTO.getNomePrefeitura().toLowerCase() + "%");
			predicateList.add(predicateNomePrefeitura);
		}
		
		if (aprovacaoPrefeituraFiltroDTO.getStatus() != null && !aprovacaoPrefeituraFiltroDTO.getStatus().equals("")) {
			Path<String> status = aprovacaoPrefeitura.get("status");
			Predicate predicateStatus = cb.equal(status, aprovacaoPrefeituraFiltroDTO.getStatus());
			predicateList.add(predicateStatus);
		}
		
		if(aprovacaoPrefeituraFiltroDTO.getDataInicioMandato() != null && !aprovacaoPrefeituraFiltroDTO.getDataInicioMandato().equals("")) {
			Expression<LocalDate> campoDataHora = cb.function("date", LocalDate.class, joinPrefeitura.get("inicioMandato"));
			LocalDate dataInicioFormatada = LocalDate.parse(aprovacaoPrefeituraFiltroDTO.getDataInicioMandato(), df);
			predicateList.add(cb.greaterThanOrEqualTo(campoDataHora, dataInicioFormatada));
		}
		
		if(aprovacaoPrefeituraFiltroDTO.getDataFimMandato() != null && !aprovacaoPrefeituraFiltroDTO.getDataFimMandato().equals("")) {
			Expression<LocalDate> campoDataHora = cb.function("date", LocalDate.class, joinPrefeitura.get("fimMandato"));
			LocalDate dataFimFormatada = LocalDate.parse(aprovacaoPrefeituraFiltroDTO.getDataFimMandato(), df);
			predicateList.add(cb.lessThanOrEqualTo(campoDataHora, dataFimFormatada));
		}
		
		if(aprovacaoPrefeituraFiltroDTO.getDataPedidoCadastramento() != null && !aprovacaoPrefeituraFiltroDTO.getDataPedidoCadastramento().equals("")) {
			Expression<LocalDate> campoDataHora = cb.function("date", LocalDate.class, aprovacaoPrefeitura.get("data"));
			LocalDate dataPedidoCadastramentoFormatada = LocalDate.parse(aprovacaoPrefeituraFiltroDTO.getDataPedidoCadastramento(), df);
			predicateList.add(cb.equal(campoDataHora, dataPedidoCadastramentoFormatada));
		}

		javax.persistence.criteria.Predicate[] predicates = new javax.persistence.criteria.Predicate[predicateList.size()];
		predicateList.toArray(predicates);
		query.where(predicates);

		TypedQuery<AprovacaoPrefeituraDTO> typedQuery = em.createQuery(query);
		List<AprovacaoPrefeituraDTO> listaAprovacoesPrefeiturasDTO = typedQuery.getResultList();

		return listaAprovacoesPrefeiturasDTO;
	}
	
	 public AprovacaoPrefeitura buscarPorId(Long id) {
		Optional<AprovacaoPrefeitura> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException("Aprovação de prefeitura não encontrada!"));
	}

	public List<AprovacaoPrefeitura> getAprovacoesPendentesByCidade(Cidade cidade) {
		return repository.findByPrefeituraCidadeAndStatus(cidade, "Pendente");
	}

	private AprovacaoPrefeitura alterarStatus(Long idAprovacaoPrefeitura, String status, String justificativa) {
		Optional<AprovacaoPrefeitura> aprovacao = repository.findById(idAprovacaoPrefeitura);

		if (aprovacao.isPresent()) {
			aprovacao.get().setStatus(status);
			aprovacao.get().setJustificativa(justificativa);
			aprovacao.get().setDataAprovacao(new Date());
			AprovacaoPrefeitura entity = repository.save(aprovacao.get());
			return entity;
		}

		return null;
	}

	public boolean emailAprovacaoPrefeitura(EmailToken emailToken) throws EmailException {
		try {
			String urlPCS = profileUtil.getProperty("profile.frontend");

			String aoA = "Ao";
			String prefeitoA = emailToken.getAprovacaoPrefeitura().getPrefeitura().getCargo();
			String nomeCidade = emailToken.getAprovacaoPrefeitura().getPrefeitura().getCidade().getNome();
			String uf = emailToken.getAprovacaoPrefeitura().getPrefeitura().getCidade().getProvinciaEstado().getNome();
			String excelentissimoA = "Excelentíssimo";
			String senhorA = "Senhor";
			String parabenizaloA = "Parabenizá-lo";
			if (emailToken.getAprovacaoPrefeitura().getPrefeitura().getCargo().equals("Prefeita")) {
				aoA = "À";
				prefeitoA = "Prefeita";
				excelentissimoA = "Excelentíssima";
				senhorA = "Senhora";
				parabenizaloA = "Parabenizá-la";
			}
			String nomePrefeito = emailToken.getAprovacaoPrefeitura().getPrefeitura().getNome();
			String urlSeloCidadeParticipante = "http://www.cidadessustentaveis.org.br/downloads/selos/selo-cidade-participante.pdf";
			String urlNovosIndicadores = "http://www.cidadessustentaveis.org.br/arquivos/260-Indicadores-do-Programa-Cidades Sustent%C3%A1veis.pdf";
			String urlPortal = urlPCS;
			String urlFormularioGestorPublico = urlPCS + "/add-responsavel?token=" + emailToken.getHash();
			String urlGuiaOrientadorParaConstrucaoObservatorios = "http://www.cidadessustentaveis.org.br/downloads/arquivos/guia-uso-sistema-indicadores.pdf";
			String urlGuiaOrientadorParaMapaDesigualdade = "http://www.cidadessustentaveis.org.br/noticias/mapa-da-desigualdade-orienta-construcao-de-politicas-publicas-na-cidade.pdf";
			String urlAcessoJusticaBrasil = "http://www.cidadessustentaveis.org.br/arquivos/acessoajusticanobrasil.pdf";
			String urlPlataformaODS = "http://agenda2030.com.br/contato.php";
			String urlCidadesSustentaveis = urlPCS;
			String urlEixosIndicadores = "https://www.cidadessustentaveis.org.br/institucional/pagina/eixos-do-pcs";
			String urlPremioPcs2019 = " https://www.cidadessustentaveis.org.br/premio-pcs-2019/";

			List<String> emails = new ArrayList<String>();
			String[] listaEmail = emailToken.getAprovacaoPrefeitura().getPrefeitura().getEmail().trim().split(";");
			for (String email : listaEmail) {
				emails.add(email.replace(";", " ").trim());
			}

			/*
			 * emails.add(emailToken.getAprovacaoPrefeitura().getPrefeitura().getEmail().
			 * trim());
			 */
			String mensagem = "<p style='font-family:Arial, Helvetica, sans-serif; font-size:16px; color:#000000;'>"
				+ aoA + "<br />" + "<br />" + prefeitoA + " de " + nomeCidade + "/" + uf + "<br /><br />"
				+ senhorA + " "	+ nomePrefeito + "<br />"
				+ "<p>" + "Queremos " + parabenizaloA +  " pela adesão ao Programa Cidades Sustentáveis. Com essa iniciativa, sua administração poderá "
				+ "contar com um conjunto de ferramentas para aprimorar os instrumentos de gestão, tais como: um software para a inclusão dos indicadores sociais, "
				+ "econômicos, políticos, ambientais e culturais de sua cidade, a Plataforma do Conhecimento; um banco de boas práticas de políticas públicas "
				+ "que alcançaram resultados positivos em várias cidades do Brasil e do mundo; um conjunto de diretrizes para colaborar e inspirar suas propostas "
				+ "de políticas públicas e seu Plano de Metas." + "</p>"
				+ "<p>" + "Os(as) prefeitos(as) que assinam a Carta Compromisso se comprometem também a elaborar o diagnóstico do município a partir dos "
				+ "indicadores do PCS, além do Plano de Metas para os quatros anos de gestão." + "<p>"
				+ "<p>" + "Por meio desses compromissos, ressaltamos a importância de se prestar contas das ações desenvolvidas e dos avanços alcançados "
				+ "por meio de relatório, revelando a evolução dos indicadores básicos relacionados a cada eixo. Este relatório deverá ser publicado e "
				+ "divulgado no final do segundo ano de mandato e também apresentado em audiência pública, de acordo com o item 3 da Carta Compromisso." + "</p>"
				+ "<p>" + "Os gestores que aderem ao Programa Cidades Sustentáveis têm 120 dias após a posse, ou a data da adesão, para apresentarem o "
				+ "diagnóstico e o Plano de Metas. O ideal é que todos os indicadores básicos propostos sejam definidos. Entretanto, "
				+ "se alguns ainda não estiverem disponíveis, é importante que os gestores informem o estágio do levantamento que está sendo feito "
				+ "para a obtenção de cada um deles." + "</p>"
				+ "<p>" + "Em 2016, o Programa Cidades Sustentáveis ingressou em uma nova etapa, ao correlacionar seus eixos e indicadores "
				+ "aos 17 objetivos e 169 metas dos ODS (Objetivos de Desenvolvimento Sustentável), da Organização das Nações Unidas. "
				+ "Desse modo, o PCS cumpre um papel fundamental para a implementação da Agenda 2030 em nível local e para a municipalização "
				+ "dos ODS nas cidades brasileiras." + "</p>"
				+ "<p>" + "Aprovada pela Assembleia Geral da ONU, a Agenda 2030 tem o propósito de acabar com a pobreza e promover, "
				+ "universalmente, a prosperidade econômica, o desenvolvimento social e a proteção ambiental. Ao incorporar os objetivos e "
				+ "metas dos ODS em sua plataforma, principalmente aqueles em que as prefeituras têm o protagonismo central no monitoramento e "
				+ "implementação, o PCS dá uma importante contribuição para o fortalecimento da gestão pública em nível local e "
				+ "para a construção de cidades mais justas, democráticas e sustentáveis." + "</p>"
				+ "<p>" + "<strong>" + "Indicadores" + "</strong>" + "</p>"
				+ "<p>" + "O número mínimo de indicadores básicos varia de acordo com três categorias populacionais: 50 para cidades pequenas "
				+ "(até 100 mil habitantes), 75 para cidades médias (de 101 mil a 500 mil habitantes) e 100 para cidades grandes e "
				+ "metrópoles (acima de 500 mil habitantes). A seleção dos indicadores será de responsabilidade da gestão, a partir de um conjunto "
				+ "de 260 indicadores classificados como básicos pelo Programa Cidades Sustentáveis. Confira aqui os <a href=' " + urlEixosIndicadores + " '>eixos e indicadores do PCS." + "</a>" + "</p>"
				+ "<p>" + "<strong>" + "Área exclusiva no novo portal do Programa Cidades Sustentáveis" + "</strong>" + "</p>"
				+ "<p>" + "Os signatários do Programa Cidades Sustentáveis tem à disposição um espaço virtual (software) no portal "
				+ "<a href='" + "www.cidadessustentaveis.org.br" +"'>www.cidadessustentaveis.org.br</a> para apresentar o diagnóstico do município por meio dos indicadores, "
				+ "o Plano de Metas e divulgar boas práticas. Este espaço virtual cumpre uma dupla função: é fonte de informação para o planejamento, "
				+ "gestão e tomada de decisão da administração pública, assim como de transparência, acompanhamento e fiscalização por parte da sociedade." + "</p>"
				+ "<p>" + "Para ter acesso ao sistema, é necessário o preenchimento do <a href=' " + urlFormularioGestorPublico + " '>formulário. " + "</a>"
				+ "Após o preenchimento, a senha de acesso ao sistema será enviada automaticamente por e-mail." + "</p>"
				+ "<p>" + "<strong>" + "Programa de Formação e Capacitação de profissionais nas áreas de políticas públicas" + "</strong>" + "</p>"
				+ "<p>" + "Para auxiliar os trabalhos nas diversas cidades que estão seguindo os princípios do PCS, desde 2013, o programa vem promovendo "
				+ "pelo País cursos de capacitação dirigidos aos gestores públicos e técnicos da administração municipal das cidades signatárias. "
				+ "A capacitação é realizada em dois momentos – um para Capacitação Teórica e outro para Capacitação Técnica. "
				+ "Ambos são estruturados em quatro módulos. Aos participantes são oferecidos materiais de apoio técnico: Guia GPS – "
				+ "Gestão Pública Sustentável, alinhados aos ODS, Guia Temático de Indicadores e Guia de Referência para a produção de "
				+ "Indicadores e para Metas de Sustentabilidade para os municípios brasileiros." + "</p>"
				+ "<p>" + "A seguir, seguem as informações e as novas ferramentas lançadas que estão à disposição das prefeituras signatárias:" + "</p>"
				+ "<p>" + "Guia de Usuário do Sistema – Elaborado para facilitar o uso da plataforma de dados do PCS, intencionalmente construída em um "
				+ "software aberto, de modo que permita a inclusão de novos indicadores pelos seus usuários – gestores e técnicos que atuam nos municípios brasileiros." + "</p>"
				+ "<p>" + "Gestão Pública Sustentável (GPS) – Apresenta um conjunto de conceitos, ferramentas, metas, indicadores e práticas exemplares para que a "
				+ "gestão pública municipal possa avançar em planejamentos inovadores, com destaque para a implementação dos ODS em nível local." + "</p>"
				+ "<p>" + "Anexo GPS: atualizado com os ODS/Indicadores – Detalhamento dos 260 indicadores do PCS e sua correlação com os ODS, incluindo as metas propostas pela ONU." + "</p>"
				+ "<p>" + "Guia orientador para a construção de Plano de metas – Informações práticas para a construção dos Planos de Metas, seleção dos indicadores, participação social, "
				+ "o sistema de monitoramento e a prestação de contas." + "</p>"
				+ "<p>" + "Guia orientador para construção de Mapas da Desigualdade nos municípios brasileiros – Elaborado com o apoio da Fundação Ford, "
				+ "o objetivo é orientar e incentivar os municípios brasileiros a reunirem os indicadores e concretizarem seus próprios mapas. "
				+ "Com essa ferramenta em mãos, as cidades terão a oportunidade de elaborar um diagnóstico preciso de suas regiões administrativas e, com isso, "
				+ "implementar políticas públicas que contribuam para a superação da desigualdade. " + "</p>"
				+ "<p>" + "Acesso à Justiça no Brasil: Índice de Fragilidade dos Municípios – Resultado de uma parceria entre a Open Society Foundations, "
				+ "o Programa Cidades Sustentáveis e a Rede Nossa São Paulo, a publicação sistematiza os dados existentes sobre o tema "
				+ "e propõe um índice para medir o nível de acesso à Justiça em cada município do país. O objetivo é contribuir para a reflexão sobre as dificuldades "
				+ "para universalizar o acesso à Justiça, bem como sobre o seu impacto na construção de uma sociedade mais igualitária, republicana e democrática. "
				+ "Além de traçar um panorama do acesso à justiça no Brasil, o trabalho analisa também as iniciativas institucionais destinadas a tornar esse direito mais efetivo." + "</p>"
				+ "<p>" + "Ação Local pelo Clima – Auxilia os gestores públicos municipais na execução e/ou revisão de ações relacionadas às transformações "
				+ "do clima, para que possam preparar suas cidades para lidar melhor com os efeitos e impactos das mudanças climáticas." + "</p>"
				+ "<p>" + "<strong>" + "Prêmio Cidades Sustentáveis" + "</strong>"  +"</p>"
				+ "<p>" + "Reconhece políticas públicas inovadoras e bem-sucedidas nas cidades brasileiras signatárias que demonstram resultados concretos, "
				+ "baseados em indicadores de diversas áreas da administração." + "</p>"
				+ "<p>" + "Três edições já foram realizadas (2014, 2016 e 2019). Para mais informações sobre a terceira edição, realizada em setembro de 2019, "
				+ "acesse <a href=' " + urlPremioPcs2019 + " '>https://www.cidadessustentaveis.org.br/premio-pcs-2019/</a>" + "</p>"
				+ "<p>" + "A metodologia do PCS já foi adotada em sete cidades da América Latina: Pilar, Encarnação, Cidade de Leste, São Lorenzo, Concepção, Paraguari e Assunção." + "</p>"
				+ "<p>" + "Benefícios para as cidades participantes:" + "</p>"
				+ "<p>" + "– Alinha o planejamento da cidade à mais avançada plataforma de desenvolvimento sustentável e à Agenda 2030, "
				+ "das Nações Unidas, considerando como critérios básicos a promoção da sustentabilidade, a inclusão social e o respeito aos direitos humanos;" + "</p>"
				+ "<p>" + "– Amplia o diálogo e a participação da sociedade para a construção conjunta de políticas públicas e de mecanismos de transparência e controle social;" + "</p>"
				+ "<p>" + "– Possibilita o bom planejamento e execução orçamentária, proporcionando maior capacidade de previsibilidade, "
				+ "supressão de desperdícios e ganhos de produtividade. Isso permitirá ampliar a capacidade de realização da gestão e traz benefícios e "
				+ "economias importantes para a máquina pública;" + "</p>"
				+ "<p>" + "– Amplia as possibilidades de captação de novos recursos públicos, privados ou de organismos internacionais, em função de uma gestão planejada e do compromisso com os ODS;" + "</p>"
				+ "<p>" + "Em 2018, com o apoio do Fundo Global para o Meio Ambiente, o PCS iniciou a ampliação da Plataforma Cidades Sustentáveis, "
				+ "para oferecer mais funcionalidades e ferramentas para a construção de políticas públicas voltadas ao desenvolvimento sustentável "
				+ "e à implementação dos ODS no Brasil." + "</p>"
				+ "<p>" + "<strong>" + "Sobre a Plataforma do Conhecimento:" + "</strong>" + "</p>"
				+ "<p>" + "&nbsp;&nbsp;&nbsp;&nbsp;O Citinova é um projeto multilateral, realizado pelo Ministério da Ciência, Tecnologia, Inovações e Comunicações (MCTIC), com apoio do Fundo Global para o Meio Ambiente "
				+ "(GEF, na sigla em inglês), gestão da ONU Meio Ambiente, e participação dos parceiros coexecutores Agência Recife para Inovação e Estratégia (ARIES) e Porto Digital, "
				+ "Centro de Gestão de Estudos Estratégicos (CGEE), Secretaria do Meio Ambiente (SEMA/GDF) e Programa Cidades Sustentáveis (PCS).\r\n "
				+ "&nbsp;&nbsp;&nbsp;&nbsp;O projeto está sendo desenvolvido no âmbito do programa GEF-6 e tem como um dos objetivos desenvolver um ambiente web chamado Plataforma Cidades Sustentáveis, "
				+ "no qual serão disponibilizadas tecnologias, ferramentas e metodologias em planejamento urbano integrado para gestores públicos municipais, "
				+ "conteúdos técnicos e teóricos, além de notícias e informações sobre sustentabilidade urbana para o público geral.\r\n" + "</p>"
				+ "<p>" + "&nbsp;&nbsp;&nbsp;&nbsp;A primeira versão da plataforma foi lançada em setembro de 2019 e incorporará, ao longo dos próximos anos, novos conteúdos, "
				+ "ferramentas, metodologias e funcionalidades para os usuários. Dentre os recursos oferecidos, as prefeituras contam com sistemas para o monitoramento e "
				+ "análise de dados e indicadores, construção de metas e planejamento integrado de ações em diferentes áreas da administração municipal – como transportes, "
				+ "habitação, assistência social, saúde e educação, entre outras." + "</p>"
				+ "<p>" + "&nbsp;&nbsp;&nbsp;&nbsp;A nova plataforma é integrada ao Observatório da Inovação, um conjunto de tecnologias desenvolvidas para diferentes tipologias de cidades, "
				+ "a fim de apoiar gestores municipais na produção de diagnósticos e identificação de soluções em planejamento urbano. O observatório está sendo desenvolvido pelo CGEE, "
				+ "organização social que produz estudos e pesquisas prospectivas, avaliações de estratégias em políticas públicas e outras atividades nas áreas de educação, "
				+ "ciência, tecnologia e inovação." + "</p>"
				+ "<p>" + "Módulos atuais da nova Plataforma Cidades Sustentáveis" + "</p>"
				+ "<ol>" + "<li>" + "Indicadores/Metas" + "</li>"
				+ "<p>" + "&nbsp;&nbsp;&nbsp;&nbsp;Sistema desenvolvido para auxiliar gestores públicos no planejamento urbano municipal, por meio de um conjunto de "
				+ "indicadores para monitoramento do desempenho de políticas públicas e apoio ao diagnóstico municipal." + "</p>"
				+ "<p>" + "&nbsp;&nbsp;&nbsp;&nbsp;Permite a elaboração de análises, a geração de relatórios e o estabelecimento de metas, bem como o monitoramento de dados e "
				+ "informações por parte da sociedade civil e da prefeitura." + "</p>"
				+ "<li>" + "Boas práticas" + "</li>"
				+ "<p>" + "&nbsp;&nbsp;&nbsp;&nbsp;Banco de boas práticas em políticas públicas do Brasil e do mundo, produzido pela equipe do Programa Cidades Sustentáveis "
				+ "com o objetivo de divulgar casos exemplares de ações que geraram impacto positivo no espaço urbano." + "</p>"
				+ "<p>" + "&nbsp;&nbsp;&nbsp;&nbsp;Contará também com seção específica para a publicação de boas práticas produzidas pelas prefeituras signatárias do PCS, "
				+ "além de links para soluções sustentáveis e inovadoras contextualizadas no território nacional por meio de tipologias de cidades-região, no Observatório de Inovação." + "</p>"
				+ "<p>" + "Módulos que serão incorporados nos próximos anos" + "</p>"
				+ "<li>" + "Planejamento integrado " + "</li>"
				+ "<p>" + "&nbsp;&nbsp;&nbsp;&nbsp;Disponibiliza informações e ferramentas para o entendimento e a implantação de um sistema de planejamento integrado em nível municipal. "
				+ "Além de metodologia e conteúdos técnicos e conceituais sobre o tema (guias, manuais, pesquisas e aplicações), oferecerá ferramentas matemáticas "
				+ "(equações, funções, modelos), estatísticas e de geoprocessamento (SIG) que permitam a integração de dados e "
				+ "informações para o desenvolvimento do planejamento integrado. " + "</p>"
				+ "<li>" + "Participação cidadã" + "</li>"
				+ "<p>" + "&nbsp;&nbsp;&nbsp;&nbsp;Tem o objetivo de sensibilizar e capacitar a sociedade civil e os gestores municipais para que a participação cidadã seja incorporada como "
				+ "método de gestão municipal, por meio do estabelecimento e fortalecimento de sistemas municipais de participação da sociedade civil "
				+ "(conselhos, audiências públicas, acesso à informação, etc.)." + "</p>"
				+ "<p>" + "&nbsp;&nbsp;&nbsp;&nbsp;O módulo oferece instrumentos conceituais e administrativos para o processo de capacitação dos gestores e da sociedade civil, "
				+ "além de ferramentas interativas para os usuários (fórum, consultas públicas, testemunhos, etc.)." + "</p>"
				+ "<li>" + "Financiamento municipal" + "</li>"
				+ "<p>" + "&nbsp;&nbsp;&nbsp;&nbsp;Banco de dados para consulta com indicação de fontes de financiamento para a esfera pública municipal e orientações sobre gestão orçamentária. "
				+ "Serve como suporte à busca de recursos e à capacitação para a gestão orçamentária local." + "</p>"
				+ "<p>" + "&nbsp;&nbsp;&nbsp;&nbsp;O módulo também permite a identificação de fontes de financiamento (nacionais e internacionais, públicas e privadas) "
				+ "para o desenvolvimento e implantação de planos e programas municipais. Traz ainda orientações sobre cooperação técnica e "
				+ "modelos de projetos que atendem às exigências dos programas de financiamento público." + "</p>"
				+ "<li>" + "Treinamento e capacitação" + "</li>"
				+ "<p>" + "&nbsp;&nbsp;&nbsp;&nbsp;Oferece acesso a todos os materiais utilizados nas diversas atividades de capacitação do Programa Cidades Sustentáveis, "
				+ "com acesso livre para usuários em geral e, para os gestores municipais, acesso por meio de login à seção exclusiva em que poderão "
				+ "acompanhar o desenvolvimento das atividades de capacitação. " + "</p>"
				+ "<li>" + "Colaborações acadêmicas " + "</li>"
				+ "<p>" + "&nbsp;&nbsp;&nbsp;&nbsp;Traz orientações aos gestores públicos e materiais de mobilização da comunidade acadêmica para a elaboração de convênios e "
				+ "parcerias entre prefeituras e instituições de pesquisa para o desenvolvimento, divulgação e aplicação de novas tecnologias e "
				+ "metodologias para a gestão local. Prevê ações de transferência, desenvolvimento e aplicação de novas tecnologias voltadas para a gestão pública municipal." + "</p>"
				+ "<li>" + "Colaborações privadas" + "</li>"
				+ "<p>" + "&nbsp;&nbsp;&nbsp;&nbsp;Ambiente para estimular o desenvolvimento de parcerias e a troca de experiências entre o poder público municipal e o "
				+ "setor privado, com foco no entendimento de PPPs, Arranjos Produtivos Locais (APLs), cadeias produtivas e instrumentos de "
				+ "gestão que direcionem para a modernização e eficiência administrativa. Também aborda o papel da responsabilidade empresarial na "
				+ "construção de cidades mais justas e sustentáveis, e abre espaço para o monitoramento e controle social de parcerias entre o poder público e o setor privado. " + "</p>"
				+ "<li>" + "Leis, planos e afins" + "</li>"
				+ "<p>" + "&nbsp;&nbsp;&nbsp;&nbsp;Oferece suporte e orientação à leitura integrada de leis e planos estaduais e federais determinantes para as atividades de "
				+ "planejamento municipal no âmbito da Plataforma do Programa Cidades Sustentáveis." + "</p>"
				+ "<li>" + "Agenda de eventos" + "</li>"
				+ "<p>" + "&nbsp;&nbsp;&nbsp;&nbsp;Oferece manuais e guias para orientação à produção de eventos no âmbito do Programa Cidades Sustentáveis e apresenta "
				+ "diversas ferramentas para criação, organização, divulgação e desenvolvimento de eventos." + "</p>" + "</ol>"
				+ "<p>" + "Para eventuais esclarecimentos, por favor, entre em contato pelos telefones (11) 3894.2400 ou 99457.6085, ou ainda pelo e-mail zuleica@isps.org.br" + "</p>"
				+ "<p>" + "Esperamos contribuir com o êxito de sua gestão e com a melhoria da qualidade de vida nas cidades brasileiras." + "</p>"
				+ "<p>" + "Abraços, " + "</p>"
				+ "<p>" + "Zuleica Goulart" + "</p>"
				+ "<p>" + "Coordenadora de Mobilização do Programa Cidades Sustentáveis" + "</p>"
				+ "<p>" + "<a href='" + "www.cidadessustentaveis.org.br" +"'>www.cidadessustentaveis.org.br</a>" + "</p>";

			emailUtil.enviarEmailHTML(emails, "Aprovação no PCS!", mensagem);

		} catch (Exception ex) {
			return false;
		}

		return true;
	}

	public boolean emailAprovacaoPrefeituraV2(EmailToken emailToken) throws EmailException {
		try {
			String urlPCS = profileUtil.getProperty("profile.frontend");

			String aoA = "Ao";
			String ao = "o";
			String prefeitoA = emailToken.getAprovacaoPrefeitura().getPrefeitura().getCargo();
			String nomeCidade = emailToken.getAprovacaoPrefeitura().getPrefeitura().getCidade().getNome();
			String uf = emailToken.getAprovacaoPrefeitura().getPrefeitura().getCidade().getProvinciaEstado().getNome();
			String excelentissimoA = "Excelentíssimo";
			String prezadoA = "Prezado";
			String parabenizaloA = "Parabenizá-lo";
			String senhorA = "senhor";
			if (emailToken.getAprovacaoPrefeitura().getPrefeitura().getCargo().equals("Prefeita")) {
				aoA = "À";
				ao = "a";
				prefeitoA = "Prefeita";
				excelentissimoA = "Excelentíssima";
				prezadoA = "Prezada";
				parabenizaloA = "Parabenizá-la";
				senhorA = "senhora";
			}
			String nomePrefeito = emailToken.getAprovacaoPrefeitura().getPrefeitura().getNome();
			String urlSeloCidadeParticipante = "http://www.cidadessustentaveis.org.br/downloads/selos/selo-cidade-participante.pdf";
			String urlNovosIndicadores = "http://www.cidadessustentaveis.org.br/arquivos/260-Indicadores-do-Programa-Cidades Sustent%C3%A1veis.pdf";
			String urlPortal = urlPCS;
			String urlFormularioGestorPublico = urlPCS + "/add-responsavel?token=" + emailToken.getHash();
			String urlGuiaOrientadorParaConstrucaoObservatorios = "http://www.cidadessustentaveis.org.br/downloads/arquivos/guia-uso-sistema-indicadores.pdf";
			String urlGuiaOrientadorParaMapaDesigualdade = "http://www.cidadessustentaveis.org.br/noticias/mapa-da-desigualdade-orienta-construcao-de-politicas-publicas-na-cidade.pdf";
			String urlAcessoJusticaBrasil = "http://www.cidadessustentaveis.org.br/arquivos/acessoajusticanobrasil.pdf";
			String urlPlataformaODS = "http://agenda2030.com.br/contato.php";
			String urlCidadesSustentaveis = urlPCS;
			String urlEixosIndicadores = "https://www.cidadessustentaveis.org.br/institucional/pagina/eixos-do-pcs";
			String urlPremioPcs2019 = " https://www.cidadessustentaveis.org.br/premio-pcs-2019/";
			String urlGuiaUsoSistema = "https://www.cidadessustentaveis.org.br/arquivos/Publicacoes/Guia_de_Uso_do_Sistema.pdf";
			List<String> emails = new ArrayList<String>();
			String[] listaEmail = emailToken.getAprovacaoPrefeitura().getPrefeitura().getEmail().trim().split(";");
			for (String email : listaEmail) {
				emails.add(email.replace(";", " ").trim());
			}

			/*
			 * emails.add(emailToken.getAprovacaoPrefeitura().getPrefeitura().getEmail().
			 * trim());
			 */
			String mensagem = "<p><a href='"+ urlCidadesSustentaveis +"'><img src='https://www.cidadessustentaveis.org.br/assets/logos/f-logo__programa-cidades-sustentaveis.jpg' style='height: 80px;margin-left: 2%;'></a></p>"
				+ "<p>" + aoA + "<br />" + "<br />" + prefeitoA + " de " + nomeCidade + ", " + uf + "<br /><br />"
				+ prezadoA + " "	+ nomePrefeito + ",<br /></p>"
				+ "<p>Seja bem-vind"+ao+" ao Programa Cidades Sustentáveis. É com grande entusiasmo que recebemos sua adesão, por meio da assinatura da carta-compromisso. Estamos prontos para seguir trabalhando na construção de cidades mais justas, democráticas e sustentáveis. </p>"
				+ "<p>Ao assinar a carta compromisso do PCS, "+ao+" prefeit"+ao+" se compromete em adotar uma agenda de desenvolvimento urbano com foco na construção de políticas públicas estruturantes, alinhadas aos objetivos e metas da Agenda 2030, da Organização das Nações Unidas (ONU). Para isso, conta com o apoio dos conteúdos, ferramentas, metodologias e capacitações desenvolvidos pelo programa e disponibilizados gratuitamente em nosso ambiente web, a <a href='" +urlPCS+ "'>Plataforma Cidades Sustentáveis</a>.  <p>"
				+ "<p><strong>Primeiro Acesso</strong><br />Antes de iniciar os trabalhos, leia as orientações iniciais para o cadastro de usuários da prefeitura na Plataforma.</p>"
				+ "<p>O primeiro passo para ter acesso ao sistema é definir os responsáveis da prefeitura para algumas atribuições pré-definidas, relacionadas ao uso das ferramentas online e alimentação das bases de dados, de indicadores e demais informações da cidade. Para isso, basta preencher e enviar este <a href='" +urlFormularioGestorPublico+ "'>formulário</a>. Em seguida, uma senha de acesso ao sistema será enviada automaticamente aos e-mails fornecidos por meio deste formulário.</p>"
				+ "<p>Após a criação dos usuários o " + senhorA + " também receberá um e-mail para a criação de sua senha. Neste caso, será utilizado o e-mail indicado na carta-compromisso, assinada no momento de adesão ao PCS. </p>"
				+ "<p>Uma vez feito o login no sistema, os usuários terão acesso a ferramentas, seções e módulos desenvolvidos exclusivamente para gestores públicos e técnicos das prefeituras.</p>"
				+ "<p>Na plataforma do PCS, as cidades podem criar suas próprias páginas, disponibilizar dados e informações municipais (como indicadores, Plano de Metas e boas práticas locais), subir e baixar camadas do Sistema de Informações Geográficas (SIG) – uma nova ferramenta criada por nossa equipe –, além de continuar navegando pelas seções e conteúdos abertos ao público geral.</p>"
				+ "<p>Para se familiarizar com a plataforma, navegue pelas diferentes opções do Menu e leia o <a href=' "+urlGuiaUsoSistema+ "'>Guia de Uso do Sistema</a>. Em caso de dúvidas, escreva para contato@cidadessustentaveis.org.br</p>"
				+ "<p>Desejamos muito sucesso em sua jornada ao longo dos próximos quatro anos e esperamos contribuir para a melhoria da qualidade de vida da população de "+nomeCidade+" nesse período. Temos certeza de que o PCS pode dar um grande apoio nesse sentido. Conte com a gente.</p>"
				+ "<p>" + "Atenciosamente, " + "</p>"
				+ "<p>" + "Equipe do Programa Cidades Sustentáveis" + "</p>"
				+ "<p>" + "<a href='" + "www.cidadessustentaveis.org.br" +"'>www.cidadessustentaveis.org.br</a>" + "</p>";

			emailUtil.enviarEmailHTML(emails, "Aprovação no PCS!", mensagem);

		} catch (Exception ex) {
			return false;
		}

		return true;
	}

	public boolean emailReprovacaoPrefeitura(AprovacaoPrefeitura aprovacaoPrefeitura, String justificativa)
			throws EmailException {
		try {
			String urlPCS = profileUtil.getProperty("profile.frontend");

			String aoA = "Ao";
			String prefeitoA = aprovacaoPrefeitura.getPrefeitura().getCargo();
			String nomeCidade = aprovacaoPrefeitura.getPrefeitura().getCidade().getNome();
			String uf = aprovacaoPrefeitura.getPrefeitura().getCidade().getProvinciaEstado().getNome();
			String excelentissimoA = "Excelentíssimo";
			String senhorA = "Senhor";
			String parabenizaloA = "Parabenizá-lo";
			if (aprovacaoPrefeitura.getPrefeitura().getCargo().equals("Prefeita")) {
				aoA = "À";
				prefeitoA = "Prefeita";
				excelentissimoA = "Excelentíssima";
				senhorA = "Senhora";
				parabenizaloA = "Parabenizá-la";
			}
			String nomePrefeito = aprovacaoPrefeitura.getPrefeitura().getNome();
			String urlCidadesSustentaveis = urlPCS;

			List<String> emails = new ArrayList<String>();
			emails.add(aprovacaoPrefeitura.getPrefeitura().getEmail().trim());

			String mensagem = "<p style='font-family:Arial, Helvetica, sans-serif; font-size:16px; color:#000000;'>"
					+ aoA + " " + prefeitoA + " de " + nomeCidade + "/" + uf + "<br><br>" 
					+ excelentissimoA + " " + "<b>" + senhorA + "</b>" + " " + nomePrefeito + ","
					+ "Queremos lhe informar que a sua solicitação de adesão ao Programa Cidades Sustentáveis foi <b style='color:#ff0000;'>negada</b> pelo motivo abaixo:"
					+ "<br><br>" + "<b>Motivo da reprovação: </b>" + justificativa + "<br><br>"
					+ "Para eventuais esclarecimentos, por favor, entre em contato pelos telefones (11) 3894.2400 ou 99457.6085."
					+ "<br><br>" + "Esperamos contribuir com o êxito de sua gestão e com a melhoria da qualidade de vida nas cidades brasileiras no futuro."
					+ "<br />Abraços, " + "<br />Zuleica Goulart "
					+ "<br />Coordenadora do Programa Cidades Sustentáveis " + "<br /><a href='"
					+ urlCidadesSustentaveis + "'>" + urlCidadesSustentaveis + "</a>" + "</p>";

			emailUtil.enviarEmailHTML(emails, "Reprovação no PCS!", mensagem);
		} catch (Exception ex) {
			return false;
		}

		return true;
	}
}
