package br.org.cidadessustentaveis.services;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import org.apache.commons.mail.EmailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.wololo.geojson.Feature;

import br.org.cidadessustentaveis.dto.AjusteGeralDTO;
import br.org.cidadessustentaveis.dto.ArquivoDTO;
import br.org.cidadessustentaveis.dto.CidadeComBoasPraticasDTO;
import br.org.cidadessustentaveis.dto.CidadeComboDTO;
import br.org.cidadessustentaveis.dto.CidadeDTO;
import br.org.cidadessustentaveis.dto.CidadeDetalheDTO;
import br.org.cidadessustentaveis.dto.CidadeIbgeDTO;
import br.org.cidadessustentaveis.dto.CidadeProvinciaEstadoDTO;
import br.org.cidadessustentaveis.dto.CidadeQtIndicadorPreenchidoDTO;
import br.org.cidadessustentaveis.dto.CidadesPorEstadoDTO;
import br.org.cidadessustentaveis.dto.ContagemCidadesCidadesParticipantesNoEstadoDTO;
import br.org.cidadessustentaveis.dto.ExibirCidadeProvinciaEstadoDTO;
import br.org.cidadessustentaveis.dto.ItemComboDTO;
import br.org.cidadessustentaveis.dto.PaisDTO;
import br.org.cidadessustentaveis.dto.PlanoDeMetasDTO;
import br.org.cidadessustentaveis.dto.PorcentagemCidadesSignatariasDTO;
import br.org.cidadessustentaveis.dto.ProvinciaEstadoDTO;
import br.org.cidadessustentaveis.model.administracao.Alerta;
import br.org.cidadessustentaveis.model.administracao.Cidade;
import br.org.cidadessustentaveis.model.administracao.CidadeDetalhes;
import br.org.cidadessustentaveis.model.administracao.Pais;
import br.org.cidadessustentaveis.model.administracao.Prefeitura;
import br.org.cidadessustentaveis.model.administracao.ProvinciaEstado;
import br.org.cidadessustentaveis.model.administracao.SubDivisao;
import br.org.cidadessustentaveis.model.administracao.Usuario;
import br.org.cidadessustentaveis.model.enums.TipoAlerta;
import br.org.cidadessustentaveis.model.institucional.Arquivo;
import br.org.cidadessustentaveis.model.noticias.Imagem;
import br.org.cidadessustentaveis.repository.CidadeDetalhesRepository;
import br.org.cidadessustentaveis.repository.CidadeRepository;
import br.org.cidadessustentaveis.repository.SubdivisaoRepository;
import br.org.cidadessustentaveis.services.exceptions.BusinessLogicErrorException;
import br.org.cidadessustentaveis.services.exceptions.DataIntegrityException;
import br.org.cidadessustentaveis.services.exceptions.ObjectNotFoundException;
import br.org.cidadessustentaveis.util.EmailUtil;
import br.org.cidadessustentaveis.util.ImageUtils;
import br.org.cidadessustentaveis.util.PDFUtils;

@Service
public class CidadeService {

	@Autowired
	private CidadeRepository cidadeRepository;
	
	@Autowired
	private SubdivisaoRepository subdivisaoRepository;
	
	@Autowired
	private ProvinciaEstadoService provinciaEstadoService;
	
	@Autowired
	private PaisService paisService;
	
	@Autowired
	private CidadeDetalhesRepository cidadeDetalhesRepository;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private PrefeituraService prefeituraService;
	
	@Autowired
	private ShapeFileService shapeFileService;
	
	@Autowired
	private HistoricoPlanoMetasPrestacaoContasService historicoPlanoMetasPrestacaoContasService;
	
	@Autowired
	private AjusteGeralService ajusteGeralService;
	
	
	
	@Autowired
	private EmailUtil emailUtil;
	
	public static final String EMAIL_PLANO_METAS = "EMAIL-ALERTA-PLANO-METAS";

	public final String BRASIL = "Brasil";

	public final String AMERICA = "America";
	

	public void atualizarCidadesIbge(List<CidadeIbgeDTO>  cidadesDoIbge){
		List<Cidade> cidadesDoBanco = cidadeRepository.findAllCidadesIbge();
		
		for (CidadeIbgeDTO cidadeIbge : cidadesDoIbge) {
			Cidade cidade;
			Optional<ProvinciaEstado> provinciaEstado = null;
			Optional<Pais> pais = null;
			Optional<Cidade> cidadeJaCadastrada = cidadesDoBanco.stream().filter(o -> o.getCodigoIbge().equals(cidadeIbge.getId())).findFirst();
			if (cidadeJaCadastrada.isPresent()) {
				cidade = cidadeJaCadastrada.get();
			} else {
			  cidade = new Cidade();
			  cidade.setCodigoIbge(cidadeIbge.getId());
			  if (provinciaEstado == null || (provinciaEstado.isPresent() && provinciaEstado.get().getNome() != cidadeIbge.getMicrorregiao().getMesorregiao().getUf().getNome())) {
				provinciaEstado = provinciaEstadoService.buscarPorNome(cidadeIbge.getMicrorregiao().getMesorregiao().getUf().getNome());
				if (!provinciaEstado.isPresent()) {
				  ProvinciaEstadoDTO novoEstado = new ProvinciaEstadoDTO();
				  novoEstado.setNome(cidadeIbge.getMicrorregiao().getMesorregiao().getUf().getNome());
				  pais = paisService.buscarPorNomePais(BRASIL);
				  if (!pais.isPresent()) {
				    Pais novoPais = new Pais();
				    novoPais.setContinente(AMERICA);
				    novoPais.setNome(BRASIL);
				    paisService.inserirPais(new PaisDTO(novoPais));
				    pais = paisService.buscarPorNomePais(BRASIL);
				  }
				  novoEstado.setPais(pais.get());
				  novoEstado.setSigla(cidadeIbge.getMicrorregiao().getMesorregiao().getUf().getSigla());
				  novoEstado.setCidades(new ArrayList<Cidade>());
				  provinciaEstadoService.inserirProvinciaEstado(novoEstado);
				  provinciaEstado = provinciaEstadoService.buscarPorNome(novoEstado.getNome());
				}
			    cidade.setProvinciaEstado(provinciaEstado.get());
			  }
			}
			cidade.setNome(cidadeIbge.getNome());
			CidadeDetalhes cidadeDetalhes = cidadeDetalhesRepository.findByCodigoIbge(cidade.getCodigoIbge());
			if(cidadeDetalhes != null) {
				cidade.setLatitude(cidadeDetalhes.getLatitude());
				cidade.setLongitude(cidadeDetalhes.getLongitude());
			}
			cidadeRepository.saveAndFlush(cidade);
		}
	}
	
	public List<Cidade> listar() {
	  return cidadeRepository.findAll();
	}
	
	public Page<Cidade> listarComPaginacao(Integer page, Integer linesPerPage, String orderBy, String direction){
		PageRequest pageRequest = null;
		if(orderBy == null || orderBy == "" || direction == null || direction == "") {
			 pageRequest = PageRequest.of(page, linesPerPage);
		}else {
			 pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		}
		return cidadeRepository.findAll(pageRequest);
	}

	public Cidade buscarPorId(final Long id) {
	  Optional<Cidade> cidade = cidadeRepository.findById(id);
	  return cidade.orElseThrow(() -> new ObjectNotFoundException("Cidade não encontrada!"));
	}

	public Cidade inserir(final CidadeDTO cidadeDTO) throws IOException {
		if(cidadeDTO.getFotoCidade() != null &&
				ImageUtils.guessImageFormat(cidadeDTO.getFotoCidade()).equalsIgnoreCase("GIF")) {
			throw new DataIntegrityException("Formato de imagem GIF não suportado");
		}

		Cidade cidade = cidadeDTO.toEntityInsert();

		CidadeDetalhes cidadeDetalhes = cidadeDetalhesRepository.findByCodigoIbge(cidadeDTO.getCodigoIbge());
		if(cidadeDetalhes != null) {
			cidade.setLatitude(cidadeDetalhes.getLatitude());
			cidade.setLongitude(cidadeDetalhes.getLongitude());
		}

		if (!cidade.getSubdivisoes().isEmpty()) {
			for (SubDivisao subdivisao: cidade.getSubdivisoes()) {
				subdivisao.setCidade(cidade);
			}
		}

		cidade.setProvinciaEstado(provinciaEstadoService.buscarPorId(cidade.getProvinciaEstado().getId()));

		cidade.setTextoCidadeDefault(cidadeDTO.getTextoCidade());

		if(cidadeDTO.getFotoCidade() != null && !cidadeDTO.getFotoCidade().isEmpty()) {
			cidade.setImagemCidade(new Imagem(ImageUtils.compressBase64Image(cidadeDTO.getFotoCidade())));
		}

		return cidadeRepository.save(cidade);
	}

	public Cidade salvar(Cidade cidade) {
		return cidadeRepository.save(cidade);
	}

	public Cidade alterar(final Long id, final CidadeDTO cidadeDTO) throws Exception {
		if (cidadeDTO.getFotoCidade() != null &&
				ImageUtils.guessImageFormat(cidadeDTO.getFotoCidade()).equalsIgnoreCase("GIF")) {
			throw new DataIntegrityException("Formato de imagem GIF não suportado");
		}

		if (cidadeDTO.getArquivoPlanoMetas() != null
			&& !(cidadeDTO.getArquivoPlanoMetas().getNomeArquivo().toLowerCase().contains(".pdf")
			|| cidadeDTO.getArquivoPlanoMetas().getNomeArquivo().toLowerCase().contains(".doc")
			|| cidadeDTO.getArquivoPlanoMetas().getNomeArquivo().toLowerCase().contains(".docx")
			|| cidadeDTO.getArquivoPlanoMetas().getNomeArquivo().toLowerCase().contains(".xls")
			|| cidadeDTO.getArquivoPlanoMetas().getNomeArquivo().toLowerCase().contains(".xlsx"))) {
			throw new DataIntegrityException("Formato não suportado - Plano de Metas");
		}

		if (cidadeDTO.getArquivoRelatorioContas() != null 
				&& !(cidadeDTO.getArquivoRelatorioContas().getNomeArquivo().toLowerCase().contains(".pdf")
				|| cidadeDTO.getArquivoRelatorioContas().getNomeArquivo().toLowerCase().contains(".doc")
				|| cidadeDTO.getArquivoRelatorioContas().getNomeArquivo().toLowerCase().contains(".docx")
				|| cidadeDTO.getArquivoRelatorioContas().getNomeArquivo().toLowerCase().contains(".xls")
				|| cidadeDTO.getArquivoRelatorioContas().getNomeArquivo().toLowerCase().contains(".xlsx"))) {
			throw new DataIntegrityException("Formato não suportado - Relatório de prestação de contas");
		}

		String user = SecurityContextHolder.getContext().getAuthentication().getName();
		Usuario usuario = usuarioService.buscarPorEmailCredencial(user);

		List<SubDivisao> listaRemover = new ArrayList<SubDivisao>();
		Cidade versaoAntiga = buscarPorId(id);

		versaoAntiga.setProvinciaEstado(cidadeDTO.getProvinciaEstado());

		for (SubDivisao subdivisao : versaoAntiga.getSubdivisoes()) {
			if (cidadeDTO.getSubdivisoes().stream().filter(x -> x.getId() == subdivisao.getId()).count() == 0
					&& subdivisao.getId() != null) {
				listaRemover.add(subdivisao);
			}
		}

		for (SubDivisao subdivisao : listaRemover) {
			subdivisao.getCidade().getSubdivisoes().remove(subdivisao);
			subdivisaoRepository.delete(subdivisao);
		}

		Cidade cidadeNova;

		if (usuario.getPrefeitura() != null && isVinculadoCidade(cidadeDTO.getId(), usuario)) {
			cidadeNova = cidadeDTO.toEntityUpdateViaPrefeitura(versaoAntiga);
			cidadeNova.setTextoCidade(cidadeDTO.getTextoCidade());
		} else {
			cidadeNova = cidadeDTO.toEntityUpdate(versaoAntiga);
			cidadeNova.setTextoCidadeDefault(cidadeDTO.getTextoCidade());
		}

		if (cidadeDTO.getFotoCidade() != null && !cidadeDTO.getFotoCidade().isEmpty()) {
			if (versaoAntiga.getImagemCidade() != null) {
				Imagem imagem = versaoAntiga.getImagemCidade();
				imagem.setImagePayload(ImageUtils.compressBase64Image(cidadeDTO.getFotoCidade()));
				cidadeNova.setImagemCidade(imagem);
			} else {
				cidadeNova.setImagemCidade(new Imagem(ImageUtils.compressBase64Image(cidadeDTO.getFotoCidade())));
			}
		} else {
			cidadeNova.setImagemCidade(null);
		}


		if (cidadeDTO.getArquivoPlanoMetas() != null && cidadeDTO.getArquivoPlanoMetas().getId() == null) {
			cidadeNova.setPlanoMetas(cidadeDTO.getArquivoPlanoMetas().toEntityInsert());
			
			try {
				this.enviarEmailDeNotificacaoDePlanoDeMetas(cidadeDTO);
			} catch(Exception e) {
				
			}
			
			
			
		} else if (cidadeDTO.getArquivoPlanoMetas() == null && versaoAntiga.getPlanoMetas() != null) {
			cidadeNova.setPlanoMetas(null);
		}

		if (cidadeDTO.getArquivoRelatorioContas() != null && cidadeDTO.getArquivoRelatorioContas().getId() == null) {
			cidadeNova.setRelatorioContas(cidadeDTO.getArquivoRelatorioContas().toEntityInsert());
		} else if (cidadeDTO.getArquivoRelatorioContas() == null && versaoAntiga.getRelatorioContas() != null) {
			cidadeNova.setRelatorioContas(null);
		}


		for (SubDivisao subdivisao : cidadeDTO.getSubdivisoes()) {
			if (subdivisao.getCidade() == null) {
				subdivisao.setCidade(cidadeNova);
			}
		}
		
		if (usuario.getPrefeitura() != null) {
			shapeFileService.salvarShapeZoneamentoCidade(cidadeDTO.getShapeZoneamento(), versaoAntiga);
		}
		
		return cidadeRepository.saveAndFlush(cidadeNova);
	}
	
	public void enviarEmailDeNotificacaoDePlanoDeMetas(CidadeDTO cidadeDTO) throws EmailException {
		List<AjusteGeralDTO> listaAjusteGeralDto = ajusteGeralService.buscarListaAjustes(EMAIL_PLANO_METAS);
		
		List<String> listaDestinatarios = new ArrayList<>();
		
		for(AjusteGeralDTO ajuste : listaAjusteGeralDto) {
			listaDestinatarios.add(ajuste.getConteudo());
		}
		
		String mensagem = "A prefeitura de " + cidadeDTO.getNome() + " cadastrou seu Plano De Metas.";
		
		emailUtil.enviarEmailHTML(listaDestinatarios, "Cadastro de Plano de Metas", mensagem);
	}
	
	public boolean isVinculadoCidade(Long idCidade, Usuario usuario) {
		Long idPrefeitura = usuario.getPrefeitura().getId();
		Prefeitura prefeitura = prefeituraService.buscarPorId(idPrefeitura);
		if (!idCidade.equals(prefeitura.getCidade().getId())) {
			throw new BusinessLogicErrorException("Usuário não está associado a prefeitura que está sendo editada.");
		}
		
		return true;
	}
	
	public void alterarCidadeParaSignataria(final Long id) {
		Cidade cidade = buscarPorId(id);
		cidade.setIsSignataria(true);
		cidadeRepository.save(cidade);
	}

	public void deletar(final Long id) {
		buscarPorId(id);
		cidadeRepository.deleteById(id);
	}

	public List<Cidade> listarPorEstado(Long idProvinciaEstado) {
		  return cidadeRepository.findByProvinciaEstadoIdOrderByNomeAsc(idProvinciaEstado);
	}
	
	public List<Cidade> listarPorEstadoNome(String nomeProvinciaEstado) {
		  return cidadeRepository.findByProvinciaEstadoNomeOrderByNomeAsc(nomeProvinciaEstado);
	}

	public List<Cidade> buscarComboBox(Long idProvinciaEstado) {
		  return cidadeRepository.findCidadeByIdProvinciaEstadoOrderByNomeAsc(idProvinciaEstado);		
	}
	public List<Cidade> buscarEstadoPCSComboBox(Long idProvinciaEstado) {
		  return cidadeRepository.findCidadeByIdProvinciaEstadoPCSOrderByNomeAsc(idProvinciaEstado);		
	}

	public List<ContagemCidadesCidadesParticipantesNoEstadoDTO> countCidadeSignatariaPorEstado() {
		return cidadeRepository.countCidadeSignatariaPorEstado();
	}

	public List<PorcentagemCidadesSignatariasDTO> calcularPorcentagemCidadesSignatariasPorEstado() {
		List<CidadesPorEstadoDTO> contagemSignatarias = 
										cidadeRepository.countTotalCidadesSignatariasPorEstado();
		List<CidadesPorEstadoDTO> contagemTotal = cidadeRepository.countTotalCidadesPorEstado();

		List<PorcentagemCidadesSignatariasDTO> porcentagens = new LinkedList<>();

		for(CidadesPorEstadoDTO numeroTotal : contagemTotal) {
			Optional<CidadesPorEstadoDTO> numeroSignatarias =
												contagemSignatarias.stream()
																		.filter(t -> t.getIdEstado()
																						.equals(numeroTotal
																									.getIdEstado()))
																	.findFirst();

			if(numeroSignatarias.isPresent()) {
				Long quantidadeTotalCidades = numeroTotal.getQuantidadeCidades();
				Long quantidadeCidadesSignatarias = numeroSignatarias.get().getQuantidadeCidades();

				Double porcentagemSignatarias = 0.0;

				if(quantidadeTotalCidades > 0) {
					porcentagemSignatarias = new BigDecimal(quantidadeCidadesSignatarias)
																		.multiply(new BigDecimal(100))
																		.divide(new BigDecimal(quantidadeTotalCidades),
																				2, RoundingMode.HALF_UP)
																		.doubleValue();
				}

				PorcentagemCidadesSignatariasDTO dto =
											new PorcentagemCidadesSignatariasDTO(numeroSignatarias.get().getIdEstado(),
																				numeroSignatarias.get().getNome(),
																				porcentagemSignatarias);
				porcentagens.add(dto);
			} else {
				PorcentagemCidadesSignatariasDTO dto =
											new PorcentagemCidadesSignatariasDTO(numeroTotal.getIdEstado(),
																				numeroTotal.getNome(), 0.0);
				porcentagens.add(dto);
			}
		}

		return porcentagens;
	}

	public List<Cidade> buscarSignatarias() {
		return this.cidadeRepository.findByIsSignatariaOrderByNome(true);
	}

	public List<Cidade> buscarSignatariasPorEstado(Long idEstado) {
		return cidadeRepository.findSignatariaPorEstado(idEstado);
	}

	public List<CidadeQtIndicadorPreenchidoDTO> buscarQuantidadeDeIndicadoresPreenchidosCidadesSignatarias() {
		return this.cidadeRepository.buscarQuantidadeDeIndicadoresPreenchidosCidadesSignatarias();
	}

	public Page<Cidade> buscarPorNome(String nome, Integer page, Integer linesPerPage) {
		return cidadeRepository.findByNomeLike(nome.toLowerCase(), 
												PageRequest.of(page, linesPerPage));
	}

	public List<Cidade> listar(Integer page, Integer linesPerPage, String orderBy , String direction) {
		return listarComPaginacao(page, linesPerPage, orderBy, direction).getContent();
	}
	
	public Long count() {
		return cidadeRepository.count();
	}

	public List<Cidade> buscarPorNome(String nome) {
		return cidadeRepository.findByNomeLike(nome.toLowerCase());
	}
	
	public CidadeDTO buscarPorNomeIgual(String nome) {
		return cidadeRepository.buscarPorNomeIgual(nome.toLowerCase());
	}
	
	public Cidade findByNome(String nome) {
		return cidadeRepository.findByNome(nome);
	}
	
//	public List<Cidade> buscarPorNomeESignataria(String nome) {
//		if(nome.equals(null) || nome.equals("")) {
//			nome = "%";
//		}
//		return cidadeRepository.findByNomeLikeAndSignataria(nome.toLowerCase());
//	}
	
	public HashMap<String, List<CidadeDTO>> buscarPorNomeESignataria(String nome) {

		if(nome.equals(null) || nome.equals("")) {
			nome = "";
		}

		List<CidadeDTO> cidades = cidadeRepository.findByNomeLikeAndSignataria(nome.toLowerCase());

		HashMap<String, List<CidadeDTO>> chaveValor = new HashMap<String, List<CidadeDTO>>();
		
		//Monta o chave valor
		cidades.forEach(cidade -> {
			String estado = cidade.getProvinciaEstado().getNome() + " - " + cidade.getProvinciaEstado().getSigla();
			if(!chaveValor.containsKey(estado)) {
				chaveValor.put(estado, new ArrayList<CidadeDTO>());
			}
			
			List<CidadeDTO> cidadesDTO = chaveValor.get(estado);
			
			cidadesDTO.add(new CidadeDTO(cidade.getId(), cidade.getNome()));
			
			chaveValor.put(estado, cidadesDTO);
		});
		
		return chaveValor;
	}
	
	public void alterarCoordenadas(Cidade cidadeNew) {
		Cidade cidade = buscarPorId(cidadeNew.getId());
		cidade.setEixoX(cidadeNew.getEixoX());
		cidade.setEixoY(cidadeNew.getEixoY());
		cidadeRepository.save(cidade);
	}

	public Cidade findByNomeAndProvinciaEstadoSigla(String nome, String sigla) {
		Cidade cidade = cidadeRepository.findByNomeAndProvinciaEstadoSigla(nome, sigla);
		return cidade;
	}
	
	public Long buscarIdPorNomeSigla(String nome, String sigla) {
		Cidade cidade = cidadeRepository.findByNomeAndProvinciaEstadoSiglaUrl(nome, sigla);
		Long idCidade = cidade.getId();
		return idCidade;
	}
	
	public ExibirCidadeProvinciaEstadoDTO buscarParaEditarViaPrefeitura(Long idCidade) {
		
		String user = SecurityContextHolder.getContext().getAuthentication().getName();
		Usuario usuario = usuarioService.buscarPorEmailCredencial(user);
		if (usuario.getPrefeitura() == null || (usuario.getPrefeitura() != null && !usuario.getPrefeitura().getCidade().getId().equals(idCidade))){
			throw new BusinessLogicErrorException("Usuário logado não possui permissão para editar dados desta cidade");
		}
		
		Cidade cidade = buscarPorId(idCidade);
		ExibirCidadeProvinciaEstadoDTO cidadeDTO = ExibirCidadeProvinciaEstadoDTO.builder()
				.provinciaEstado(new CidadeProvinciaEstadoDTO(cidade.getProvinciaEstado()))
				.id(cidade.getId())
				.nome(cidade.getNome())
				.codigoIbge(cidade.getCodigoIbge())
				.provinciaEstado(new CidadeProvinciaEstadoDTO(cidade.getProvinciaEstado()))
				.populacao(cidade.getPopulacao())
				.anoDaPopulacao(cidade.getAnoDaPopulacao())
				.enderecoDaPrefeitura(cidade.getEnderecoDaPrefeitura())
				.isSignataria(cidade.getIsSignataria())
				.subdivisoes(cidade.getSubdivisoes())
				.latitude(cidade.getLatitude())
				.longitude( cidade.getLongitude())
				.site(cidade.getSitePrefeitura())
				.area(cidade.getArea())
				.densidadeDemografica(cidade.getDensidadeDemografica())
				.salarioMedioMensal(cidade.getSalarioMedioMensal())
				.populacaoOcupada(cidade.getPopulacaoOcupada())
				.pibPerCapita(cidade.getPibPerCapita())
				.idhM(cidade.getIdhM())
				.fotoCidade(cidade.getFotoCidade())
				.textoCidade(cidade.getTextoCidade())
				.nomeContato(cidade.getNomeContato())
				.emailContato(cidade.getEmailContato())
				.telFixoContato(cidade.getTelFixoContato())
				.telMovelContato(cidade.getTelMovelContato())
				.campoObservacao(cidade.getCampoObservacao())
				.build();
		
				if(cidade.getPlanoMetas() != null) {
					ArquivoDTO arquivoPlanoMetas = new ArquivoDTO(cidade.getPlanoMetas());
					cidadeDTO.setArquivoPlanoMetas(arquivoPlanoMetas);
				}
				
				if(cidade.getRelatorioContas() != null) {
					ArquivoDTO arquivoRelatorioContas = new ArquivoDTO(cidade.getRelatorioContas());
					cidadeDTO.setArquivoRelatorioContas(arquivoRelatorioContas);
				}
				
				if(cidade.getImagemCidade() != null) {
					cidadeDTO.setUrlFotoCidade("imagens/" + cidade.getImagemCidade().getId());
				}
		

		List<Feature> shapeZoneamento = shapeFileService.buscarShapeZoneamento(idCidade);
		cidadeDTO.setShapeZoneamento(shapeZoneamento);
		
		return cidadeDTO;
		
	}
	
	public List<String> buscarNomeCidadesSignatarias() {
		return this.cidadeRepository.buscaNomeCidadeSignataria();
	}

	public List<ItemComboDTO> buscarTodasCidadesSignatariasParaCombo() {
		return cidadeRepository.buscarTodasCidadesSignatariasParaCombo();
	}
	
	public List<CidadeComboDTO> buscarPorCidadeProvinciaEstado(Long idCidade, Long idProvinciaEstado) {
		return cidadeRepository.buscarPorCidadeProvinciaEstado(idCidade, idProvinciaEstado);

	}

	@Cacheable("cidades")
	public List<ItemComboDTO> listarComboPorEstado(Long idProvinciaEstado) {
		  return cidadeRepository.listarComboPorEstado(idProvinciaEstado);
	}
	
	public CidadeComboDTO buscarCidadeComboPorId(final Long idCidade) {
		return cidadeRepository.buscarCidadeComboPorId(idCidade);
	}
	
	public List<ItemComboDTO> buscarCidadeComboBox() {
		return cidadeRepository.buscarCidadeComboBox();
	}
	
	public List<ItemComboDTO> buscarCidadeEstadoComboBox() {
		return cidadeRepository.buscarCidadeEstadoComboBox();
	}
	
	public CidadeDetalheDTO findByIdCidade(Long idCidade) {
		return cidadeRepository.findByIdCidade(idCidade);
	}
	
	public List<CidadeComBoasPraticasDTO> findCidadesByIds(List<Long> idsCidade) {
		return cidadeRepository.findCidadesPorIds(idsCidade);
	}

	public List<List<String>> geraListaPlanosMeta(String nomeArquivoCSV){
		File arquivoCSV = new File (nomeArquivoCSV);
		List<List<String>> lista = new ArrayList<List<String>>();

		try{
			String linhasDoArquivo = new String();
			Scanner leitor = new Scanner(arquivoCSV);
			while(leitor.hasNext()){
				linhasDoArquivo = leitor.nextLine();
				lista.add(Arrays.asList(linhasDoArquivo.split(",")));
			}
			leitor.close();

		}catch(Exception e){
			e.printStackTrace();
		}
		return lista;
	}

	public void criarPlanosDeMeta(List<List<String>> registros, String path){
		for( List<String> linha : registros ) {
			try {
				if(linha.size() > 1) {
					Long id = Long.parseLong(linha.get(0));
					String extension = linha.get(1);
					String filename = linha.get(2);

					Cidade cidade = this.buscarPorId(id);

					if(cidade != null) {
						Arquivo arquivo = new Arquivo();
						arquivo.setNomeArquivo(filename);
						arquivo.setExtensao(extension);

						File file = new File(path + arquivo.getNomeArquivo());

						if(file.exists()) {
							arquivo.setConteudo(
									PDFUtils.Pdf2String(path + arquivo.getNomeArquivo()));

							cidade.setPlanoMetas(arquivo);
							this.salvar(cidade);
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
	public void criarRelatorioPrestacao(List<List<String>> registros, String path){
		for( List<String> linha : registros ) {
			try {
				if(linha.size() > 1) {
					Long id = Long.parseLong(linha.get(0));
					String extension = linha.get(1);
					String filename = linha.get(2);

					Cidade cidade = this.buscarPorId(id);

					if(cidade != null) {
						Arquivo arquivo = new Arquivo();
						arquivo.setNomeArquivo(filename);
						arquivo.setExtensao(extension);

						File file = new File(path + arquivo.getNomeArquivo());

						if(file.exists()) {
							arquivo.setConteudo(
									PDFUtils.Pdf2String(path + arquivo.getNomeArquivo()));

							cidade.setRelatorioContas(arquivo);
							this.salvar(cidade);
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
	public List<Long> buscarIdCidadesPreencheramUmaDasVariaveisDoIndicador(Long indicador) {
		List<Long> cidades = cidadeRepository.findCidadesQueJaPreencheramUmaDasVariaveisDoIndicador(indicador);
		return cidades;
	}
	
	public List<ItemComboDTO> buscarCidadeParaComboPorListaIdsEstados(List<Long> idsEstados){
		List<ItemComboDTO> cidades = cidadeRepository.buscarCidadeParaComboPorListaIdsEstados(idsEstados);
		return cidades;
	}

	public Cidade findByCodigoIbge(String codigo){
		Long codigoLong = Long.parseLong(codigo);
		Cidade cidade = this.cidadeRepository.findByCodigoIbge(codigoLong);
		return cidade;
	}
	
	public List<ItemComboDTO> buscarSignatariasComboPorIdEstado(Long idEstado) {
		  return cidadeRepository.buscarSignatariasComboPorIdEstado(idEstado);
	}
	
	public List<CidadeDTO> buscarSignatariasDTO() {
		return this.cidadeRepository.findByIsSignatariaOrderByNomeCompleto(true);
	}
	
	public Cidade findByCodigoIbge(Long codigo){
		Cidade cidade = this.cidadeRepository.findByCodigoIbge(codigo);
		return cidade;
	}
}