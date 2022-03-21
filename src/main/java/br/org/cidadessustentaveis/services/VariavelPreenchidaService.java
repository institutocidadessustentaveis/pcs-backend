package br.org.cidadessustentaveis.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.ListJoin;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.security.sasl.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.wololo.geojson.Feature;

import br.org.cidadessustentaveis.dto.ObservacaoVariavelDTO;
import br.org.cidadessustentaveis.dto.VariaveisDadosAbertosDTO;
import br.org.cidadessustentaveis.dto.VariavelJaPreenchidaSimplesDTO;
import br.org.cidadessustentaveis.dto.VariavelPreenchidaDTO;
import br.org.cidadessustentaveis.dto.VariavelPreenchidaDuplicadaDTO;
import br.org.cidadessustentaveis.dto.VariavelPreenchidaFiltradaIntegracaoDTO;
import br.org.cidadessustentaveis.dto.VariavelPreenchidaMapaDTO;
import br.org.cidadessustentaveis.dto.VariavelPreenchidaMunicipioDTO;
import br.org.cidadessustentaveis.dto.VariavelPreenchidaSimplesDTO;
import br.org.cidadessustentaveis.model.administracao.Cidade;
import br.org.cidadessustentaveis.model.administracao.InstituicaoFonte;
import br.org.cidadessustentaveis.model.administracao.Orgao;
import br.org.cidadessustentaveis.model.administracao.Prefeitura;
import br.org.cidadessustentaveis.model.administracao.ProvinciaEstado;
import br.org.cidadessustentaveis.model.administracao.SubdivisaoCidade;
import br.org.cidadessustentaveis.model.administracao.Usuario;
import br.org.cidadessustentaveis.model.enums.TipoVariavel;
import br.org.cidadessustentaveis.model.indicadores.Indicador;
import br.org.cidadessustentaveis.model.indicadores.IndicadorPreenchido;
import br.org.cidadessustentaveis.model.indicadores.SubdivisaoVariavelPreenchida;
import br.org.cidadessustentaveis.model.indicadores.VariaveisOpcoes;
import br.org.cidadessustentaveis.model.indicadores.Variavel;
import br.org.cidadessustentaveis.model.indicadores.VariavelPreenchida;
import br.org.cidadessustentaveis.repository.SubdivisaoVariavelPreenchidaRepository;
import br.org.cidadessustentaveis.repository.VariaveisOpcoesRepository;
import br.org.cidadessustentaveis.repository.VariavelPreenchidaRepository;
import br.org.cidadessustentaveis.services.exceptions.DataIntegrityException;
import br.org.cidadessustentaveis.services.exceptions.ObjectNotFoundException;
import br.org.cidadessustentaveis.util.NumeroUtil;
import br.org.cidadessustentaveis.util.UsuarioContextUtil;
import br.org.cidadessustentaveis.util.VariavelPreenchidaUtil;

@Service
public class VariavelPreenchidaService {
	@Autowired
	private VariavelPreenchidaRepository repository;
	@Autowired
	private SubdivisaoVariavelPreenchidaRepository subdivisaoVPRepository;

	@Autowired
	private IndicadorPreenchidoService indicadorPreenchidoService;
	@Autowired
	private IndicadorService indicadorService;
	@Autowired
	private VariavelService variavelService;
	@Autowired
	private ShapeFileService shapeFileService;
	@Autowired
	private InstituicaoFonteService instituicaoFonteService;
	@Autowired
	private OrgaoService orgaoService;
	@Autowired
	private VariaveisOpcoesRepository variaveisOpcoesRepository;
	@Autowired
	private SubdivisaoService subdivisaoService;

	@Autowired
	private EntityManager em;
	@Autowired
	private UsuarioContextUtil usuarioContextUtil;

	public Optional<VariavelPreenchida> buscarVariavel(Variavel variavel, Short ano, Prefeitura prefeitura){
		Optional<VariavelPreenchida> optional = repository.findByVariavelAndAnoAndPrefeitura(variavel, ano,prefeitura);
		return optional;
	}

	public Optional<VariavelPreenchida> buscarVariavel(Variavel variavel, Short ano, Cidade cidade){
		Optional<VariavelPreenchida> optional = repository.findByVariavelAndAnoAndPrefeituraCidade(variavel, ano, cidade);
		return optional;
	}

	public VariavelPreenchida inserirVariavelPreenchida(VariavelPreenchidaDTO variavelPreenchidaDTO) {
		VariavelPreenchida variavelPreenchida = variavelPreenchidaDTO.toEntityInsert();
		variavelPreenchida = repository.save(variavelPreenchida);
		return variavelPreenchida;
	}

	public List<VariavelPreenchida> buscar() {
		return repository.findAll();
	}

	public VariavelPreenchida buscarPorId(Long id) {
		Optional<VariavelPreenchida> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException("Variável preenchida não encontrada!"));
	}

	public VariavelPreenchida editar(VariavelPreenchidaDTO variavelPreenchidaDTO, Long id) {
		VariavelPreenchida varRef = buscarPorId(id);
		varRef = variavelPreenchidaDTO.toEntityUpdate(varRef);
		repository.save(varRef);
		indicadorPreenchidoService.atualizarIndicadoresPreenchidos(varRef);
		return varRef;
	}

	public void deletar(Long id) {
		buscarPorId(id);
		try {
			repository.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("O registro está relacionado com outra entidade");
		}
	}

	public void salvar(VariavelPreenchida variavelPreenchida) throws Exception {
		if(variavelPreenchida.getPrefeitura() == null) {
			throw new Exception("É Necessário possuir vinculo com uma prefeitura para preencher variáveis");
		}
		if( variavelPreenchida.getAno() == null) {
			throw new Exception("É Registro sem ano ");
		}
		if( variavelPreenchida.getVariavel() == null) {
			throw new Exception("Preenchimento sem variável ");
		}
		repository.save(variavelPreenchida);
	}
	public void inserir(List<VariavelPreenchida> listPreenchidas) throws Exception {
		for(VariavelPreenchida variavelPreenchida : listPreenchidas) {
			salvar(variavelPreenchida);
		}
	}
	
	public void inserirComSubdivisao(List<SubdivisaoVariavelPreenchida> listPreenchidas) throws Exception {
		for(SubdivisaoVariavelPreenchida variavelPreenchida : listPreenchidas) {
			salvarComSubdivisao(variavelPreenchida);
		}
	}

	
	public void salvarComSubdivisao(SubdivisaoVariavelPreenchida variavelPreenchida) throws Exception {
		if(variavelPreenchida.getPrefeitura() == null) {
			throw new Exception("É Necessário possuir vinculo com uma prefeitura para preencher variáveis");
		}
		if(variavelPreenchida.getSubdivisao() == null) {
			throw new Exception("É Necessário possuir vinculo com uma subdivisão para preencher variáveis");
		}
		if( variavelPreenchida.getAno() == null) {
			throw new Exception("É Registro sem ano ");
		}
		if( variavelPreenchida.getVariavel() == null) {
			throw new Exception("Preenchimento sem variável ");
		}
		subdivisaoVPRepository.save(variavelPreenchida);
	}

	public List<InstituicaoFonte> buscaVariavelPreenchidaPorIdIndicador(Long idIndicador){

		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<InstituicaoFonte> query = cb.createQuery(InstituicaoFonte.class);

		Root<IndicadorPreenchido> indicadorPreenchido = query.from(IndicadorPreenchido.class);

		ListJoin<IndicadorPreenchido, VariavelPreenchida> joinVariavelPreenchida = indicadorPreenchido.joinList("variaveisPreenchidas");

		Join<VariavelPreenchida, InstituicaoFonte> joinInstituicaoFonte = joinVariavelPreenchida.join("instituicaoFonte");

		Join<IndicadorPreenchido, Indicador> joinIndicador = indicadorPreenchido.join("indicador");

		query.multiselect(joinInstituicaoFonte.get("id"), joinInstituicaoFonte.get("nome")).distinct(true);

		List<Predicate> predicateList = new ArrayList<>();

		Path<Long> idIndicadorBanco = joinIndicador.get("id");
		predicateList.add(cb.equal(idIndicadorBanco, idIndicador));

		List<Order> orderList = new ArrayList<Order>();
		orderList.add(cb.asc(joinInstituicaoFonte.get("nome")));

		query.orderBy(orderList);

		Predicate[] predicates = new Predicate[predicateList.size()];
		predicateList.toArray(predicates);
		query.where(predicates);

		List<InstituicaoFonte> lista = em.createQuery(query).getResultList();

		return lista;
	}

	public VariavelPreenchidaDTO buscaVariavelPreenchidaPorAnoeCidade(Long ano, Long idCidade, Long idVariavel){

		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<VariavelPreenchidaDTO> query = cb.createQuery(VariavelPreenchidaDTO.class);


		Root<VariavelPreenchida> variavelPreenchida = query.from(VariavelPreenchida.class);

		Join<VariavelPreenchida, Prefeitura> joinPrefeitura = variavelPreenchida.join("prefeitura");

		Join<Prefeitura, Cidade> joinCidade = joinPrefeitura.join("cidade");

		query.multiselect(variavelPreenchida.get("valor"));

		List<Predicate> predicateList = new ArrayList<>();

		Path<Long> anoVariavel1 = variavelPreenchida.get("ano");

		Path<Long> idCidade1 = joinCidade.get("id");

		Path<Long> idVariavel1 = variavelPreenchida.get("variavel");


		predicateList.add(cb.equal(anoVariavel1, ano));

		predicateList.add(cb.equal(idCidade1, idCidade));

		predicateList.add(cb.equal(idVariavel1, idVariavel));


		Predicate[] predicates = new Predicate[predicateList.size()];
		predicateList.toArray(predicates);
		query.where(predicates);

		try {
			VariavelPreenchidaDTO valorVariavel = em.createQuery(query).getSingleResult();
			return valorVariavel;
		} catch (Exception e) {
			return null;
		}

	}
	
	public  List<VariavelPreenchida> recalcularIDH() {
		List<VariavelPreenchida> variaveis = repository.findByVariavelId((long) 30);
		for(VariavelPreenchida vp : variaveis) {
			if (vp.getValor() > 1) {
				Double valor = vp.getValor() / 1000;
				repository.atualizaValorPorIdVariavel(vp.getId(), valor);
			}
		}
		return variaveis;
	}

	public List<VariavelPreenchida> buscarPorIndicadorCidadeAnoInicialEFinal(Long idIndicador, Long idCidade,
																			 Short anoInicial, Short anoFinal) {
		List<VariavelPreenchida> variaveisPreenchidas = repository.findByIndicadorCidadeAno(idIndicador, idCidade, anoInicial, anoFinal);
		
		return variaveisPreenchidas;
	}

	public List<List<String>> serieHistorica(Long idIndicador, Long idCidade, Short anoInicial, Short anoFinal) {
		String anoInicialMandato = Short.toString(anoInicial);
		String anoFinalMandato = Short.toString(anoFinal);
		
		List<String> mandato = new ArrayList<>();
		String[] anos = new String[2];
		mandato = Arrays.asList(anos);
		mandato.set(0, anoInicialMandato);
		mandato.set(1, anoFinalMandato);
		
		Indicador indicador = indicadorService.listarById(idIndicador);
		List<List<String>> tabela = new ArrayList<>();
		List<String> cabecalho = new ArrayList<>();
		if( indicador != null && indicador.getVariaveis() != null) {
			List<Variavel> variaveis = indicador.getVariaveis().stream()
					.sorted(Comparator.comparing(Variavel::getNome))
					.collect(Collectors.toList());

			String[] linhaArray = new String[variaveis.size()+2];
			cabecalho = Arrays.asList(linhaArray);
			cabecalho.set(0, "Período");
			for(int i = 0 ; i < variaveis.size(); i++ ) {
				Variavel v = variaveis.get(i);
				cabecalho.set(i+1, v.getNome());
			}
			cabecalho.set(cabecalho.size()-1, "Resultado");
			tabela.add(cabecalho);
		}

		List<VariavelPreenchida> listaVP= buscarPorIndicadorCidadeAnoInicialEFinal(idIndicador, idCidade, anoInicial, anoFinal);
		for(Short ano = anoInicial ; ano <= anoFinal; ano++) {
			String[] linhaArray = new String[cabecalho.size()];
			List<String> linha = Arrays.asList(linhaArray);
			linha.set(0, ano+"");
			for(int i = 1; i< cabecalho.size()-1; i++) {
				String nomeVariavel = cabecalho.get(i);
				for(VariavelPreenchida vp : listaVP) {
					if(vp.getAno().equals(ano) && vp.getVariavel().getNome().equals(nomeVariavel)) {
						linha.set(i, VariavelPreenchidaUtil.valorApresentacao(vp));
						break;
					}
				}
			}
			tabela.add(linha);
		}

		List<IndicadorPreenchido> listaIndicadorPreenchido = indicadorPreenchidoService.buscarPorIndicadorCidadeInicioFim(idIndicador, idCidade, anoInicial, anoFinal);
		for(IndicadorPreenchido ip : listaIndicadorPreenchido) {
			for(int i = 1 ; i < tabela.size() ; i++) {
				List<String> linha = tabela.get(i);
				if(linha.get(0).equals(ip.getAno()+"")) {
					linha.set(linha.size()-1, ip.getResultadoApresentacao());
					break;
				}

			}
		}
		return tabela;
	}

	List<VariaveisDadosAbertosDTO> buscarVariaveisDadosAbertos(Long idCidade, Long idIndicador) {
		
		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<VariaveisDadosAbertosDTO> query = cb.createQuery(VariaveisDadosAbertosDTO.class);

		Root<VariavelPreenchida> variavelPreenchida = query.from(VariavelPreenchida.class);

		Join<VariavelPreenchida, Prefeitura> prefeituraJoin = variavelPreenchida.join("prefeitura");
		Join<Prefeitura, Cidade> cidadeJoin = prefeituraJoin.join("cidade");
		Join<Cidade, ProvinciaEstado> provinciaEstadoJoin = cidadeJoin.join("provinciaEstado");
		Join<VariavelPreenchida, Variavel> variavelJoin = variavelPreenchida.join("variavel");
		Join<VariavelPreenchida, InstituicaoFonte> instituicaoFonteJoin =
															variavelPreenchida.join("instituicaoFonte");
		
		query.multiselect(cidadeJoin.get("codigoIbge"), cidadeJoin.get("nome"), variavelPreenchida.get("id"),
							provinciaEstadoJoin.get("sigla"), provinciaEstadoJoin.get("nome"),
							variavelJoin.get("tipo"), variavelJoin.get("unidade"),
							variavelJoin.get("nome"), variavelPreenchida.get("ano"),
							variavelPreenchida.get("valor"), variavelPreenchida.get("observacao"),
							instituicaoFonteJoin.get("nome"),variavelPreenchida.get("fonteMigracao"),
							variavelPreenchida.get("valorTexto"));

		List<javax.persistence.criteria.Predicate> predicateList = new ArrayList<>();

		if(idCidade != null && idCidade != -1) {
			Path<String> idCidadePath = cidadeJoin.get("id");
			predicateList.add(cb.equal(idCidadePath, idCidade));
		}

		if(idIndicador != null && idIndicador != -1) {
			List<Long> idsVariaveisDoIndicador = variavelService.buscarIdsPorIdIndicador(idIndicador);
			Path<String> idVariavelPath = variavelJoin.get("id");
			predicateList.add(idVariavelPath.in(idsVariaveisDoIndicador));
		}
		
		List<Order> orderList = new ArrayList<Order>();
		orderList.add(cb.asc(variavelPreenchida.get("ano")));
		orderList.add(cb.asc(variavelJoin.get("nome")));
		query.orderBy(orderList);

		javax.persistence.criteria.Predicate[] predicates = new javax.persistence.criteria.Predicate[
				predicateList.size()];
		predicateList.toArray(predicates);
		query.where(predicates);

		TypedQuery<VariaveisDadosAbertosDTO> typedQuery = em.createQuery(query);

		return typedQuery.getResultList();
	}

	public List<VariavelPreenchidaMunicipioDTO> buscarVariavelPreenchidaPorVariavel(Long idVariavel, List<Long> idCidades) {
		List<VariavelPreenchida> listaVariavelPreenchida = new ArrayList<>();
		if(idCidades != null &&  !idCidades.isEmpty()) {
			 listaVariavelPreenchida = repository.findByVariavelIdAndPrefeituraCidadeIdIn(idVariavel,idCidades);
		} else {
			listaVariavelPreenchida = repository.findByVariavelId(idVariavel);
		}
		List<VariavelPreenchidaMunicipioDTO> dtos = new ArrayList<>();
		for(VariavelPreenchida vp : listaVariavelPreenchida) {
			dtos.add(new VariavelPreenchidaMunicipioDTO(vp));
		}
		
		
		return dtos;
	}

	public List<String> buscarFontesPreenchidas(Long idIndicador, Long idCidade) {
		List<String > fontes = repository.buscarFontesPreenchidas(idIndicador, idCidade);
		fontes.remove(null);
		return fontes;
	}

	public List<VariavelPreenchidaDuplicadaDTO> buscarDuplicadas() {
		List<VariavelPreenchidaDuplicadaDTO> duplicadas = repository.buscarDuplicadas();
		return duplicadas;
	}

	public List<VariavelPreenchida> findByVariavelIdAndAnoAndPrefeituraid(Long variavel,Long prefeitura, Short ano ){
		List<VariavelPreenchida> vps = repository.findByVariavelIdAndAnoAndPrefeituraid(variavel, prefeitura, ano);
		return vps;
	}



	public List<Short> carregarComboAnosPreenchidos() {
		return repository.carregarComboAnosPreenchidos();
	}

	public List<VariavelPreenchidaMapaDTO> buscarCidadesComVariavelPreenchida(Long idVariavelSelecionada, String valorPreenchido, Short anoSelecionado, boolean visualizarComoPontos) {

		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<VariavelPreenchidaMapaDTO> query = cb.createQuery(VariavelPreenchidaMapaDTO.class);

		Root<VariavelPreenchida> variavelPreenchida = query.from(VariavelPreenchida.class);

		Join<VariavelPreenchida, Variavel> joinVariavel = variavelPreenchida.join("variavel",JoinType.LEFT);
		Join<VariavelPreenchida, Prefeitura> joinPrefeitura = variavelPreenchida.join("prefeitura",JoinType.LEFT);
		Join<VariavelPreenchida, Cidade> joinCidade = joinPrefeitura.join("cidade",JoinType.LEFT);

		query.multiselect(joinCidade.get("id"), joinCidade.get("nome"), joinVariavel.get("nome"), joinCidade.get("latitude"),
				joinCidade.get("longitude"), variavelPreenchida.get("valor"), variavelPreenchida.get("valorTexto"));

		List<javax.persistence.criteria.Predicate> predicateList = new ArrayList<>();

		if(idVariavelSelecionada == null || anoSelecionado == null) {
			return new ArrayList<>();
		}

		Path<Long> idVariavel = joinVariavel.get("id");
		predicateList.add(cb.equal(idVariavel, idVariavelSelecionada));

		Path<Long> ano = variavelPreenchida.get("ano");
		predicateList.add(cb.equal(ano, anoSelecionado));

		javax.persistence.criteria.Predicate[] predicates = new javax.persistence.criteria.Predicate[predicateList.size()];
		predicateList.toArray(predicates);
		query.where(predicates);

		TypedQuery<VariavelPreenchidaMapaDTO> typedQuery = em.createQuery(query);

		List<VariavelPreenchidaMapaDTO> lista = typedQuery.getResultList();

		if(valorPreenchido != "" && NumeroUtil.isANumber(valorPreenchido)) {
			return lista.stream().parallel().filter(o -> o.getValorTextoPreenchido().equals(valorPreenchido)).collect(Collectors.toList());
		}else if (valorPreenchido != ""){
			return lista.stream().parallel().filter(o -> o.getValorTextoPreenchido().toLowerCase().contains(valorPreenchido.toLowerCase())).collect(Collectors.toList());
		}

		if (!visualizarComoPontos) {
			for (VariavelPreenchidaMapaDTO variavelPreenchidaMapaDTO : lista) {
				List<Feature> shapeZoneamento = shapeFileService.buscarShapeZoneamento(variavelPreenchidaMapaDTO.getIdCidade());
				variavelPreenchidaMapaDTO.setShapeZoneamento(shapeZoneamento);
				variavelPreenchidaMapaDTO.setVisualizarComoPontos(false);
			}
		}

		return lista;
	}
	
	public List<ObservacaoVariavelDTO> buscarObservacaoVariavel(Long idIndicador,Long idCidade){
		List<ObservacaoVariavelDTO> ovp = repository.buscarObservacaoVariavel(idIndicador, idCidade);
		return ovp;
	}

	public void deletarIncluindoIndicadoresPreenchidos(Long id) throws AuthenticationException {
		try {
			Usuario usuario = usuarioContextUtil.getUsuario();
			if(usuario.getPrefeitura() == null) {
				throw new AuthenticationException("Você não é um usuário de uma prefeitura.");
			}
			VariavelPreenchida vp = buscarPorId(id);
			if(vp == null) {
				throw new DataIntegrityException("Registro não encontrado");
			}
			if(!vp.getPrefeitura().getId().equals(usuario.getPrefeitura().getId())) {
				throw new AuthenticationException("Você não pode excluir esse registro.");
			}
			List<IndicadorPreenchido> ips = indicadorPreenchidoService.buscarPorAnoVpVariavelIdCidadeId(vp.getAno(),vp.getVariavel().getId(), vp.getPrefeitura().getCidade().getId());
			indicadorPreenchidoService.excluirLista(ips);
			repository.delete(vp);
		} catch (Exception e) {
			throw new AuthenticationException("Você precisa estar Logado.");
		}
		
		
	}
	
	public List<VariavelPreenchida> listarPorIndicadorAndCidade(Long idIndicador,Long idCidade){
		List<VariavelPreenchida> lista = repository.findByIndicadorAndCidade(idCidade, idIndicador);
		return lista;
	}
	
	public List<Short> carregarComboAnosPreenchidosPorIdVariavel(Long idVariavelSelecionada) {
		return repository.carregarComboAnosPreenchidosPorIdVariavel(idVariavelSelecionada);
	}

	public List<VariavelJaPreenchidaSimplesDTO> variaveisParaPreencher() throws Exception {
		Usuario usuario = this.usuarioContextUtil.getUsuario();
		Prefeitura prefeitura = usuario.getPrefeitura();
		Long idCidade = prefeitura.getCidade().getId();
		Short ano = Short.parseShort(LocalDate.now().getYear()-1+"");
		
		List<VariavelJaPreenchidaSimplesDTO> dtos = this.repository.getVariaveisParaPreencher( idCidade , ano );
		return dtos;
	}
	public List<VariavelJaPreenchidaSimplesDTO> variaveisParaPreencher(Long idSubdivisao) throws Exception {
		Usuario usuario = this.usuarioContextUtil.getUsuario();
		Prefeitura prefeitura = usuario.getPrefeitura();
		Long idCidade = prefeitura.getCidade().getId();
		Short ano = Short.parseShort(LocalDate.now().getYear()-1+"");
		
		List<VariavelJaPreenchidaSimplesDTO> dtos = this.repository.getVariaveisParaPreencher( idCidade , ano, idSubdivisao );
		return dtos;
	}

	public List<VariavelPreenchidaSimplesDTO> seriePreenchida(Long id) throws Exception {
		Usuario usuario = this.usuarioContextUtil.getUsuario();
		Prefeitura prefeitura = usuario.getPrefeitura();
		Long idCidade = prefeitura.getCidade().getId();
		List<VariavelPreenchida> preenchidas = this.repository.findByVariavelIdAndPrefeituraCidadeIdOrderByAnoDesc( id , idCidade);
		List<VariavelPreenchidaSimplesDTO> dtos = new ArrayList<>();
		if(preenchidas != null) {
			for(VariavelPreenchida vp : preenchidas) {
				VariavelPreenchidaSimplesDTO dto = new VariavelPreenchidaSimplesDTO(vp);
				dtos.add(dto);
			}
		}
		return dtos;
	}
	
	public List<VariavelPreenchidaSimplesDTO> seriePreenchidaSubdivisao(Long id, Long idSubdivisao) throws Exception {
		Usuario usuario = this.usuarioContextUtil.getUsuario();
		Prefeitura prefeitura = usuario.getPrefeitura();
		Long idCidade = prefeitura.getCidade().getId();
		List<SubdivisaoVariavelPreenchida> preenchidas = this.subdivisaoVPRepository.findByVariavelIdAndPrefeituraCidadeIdAndSubdivisaoIdOrderByAnoDesc( id , idCidade, idSubdivisao);
		List<VariavelPreenchidaSimplesDTO> dtos = new ArrayList<>();
		if(preenchidas != null) {
			for(SubdivisaoVariavelPreenchida vp : preenchidas) {
				VariavelPreenchidaSimplesDTO dto = new VariavelPreenchidaSimplesDTO(vp);
				dtos.add(dto);
			}
		}
		return dtos;
	}

	public VariavelPreenchidaDTO buscaVariavelPreenchidaPorAnoVariavel(Long ano, Long idVariavel) throws Exception {
		Usuario usuario = this.usuarioContextUtil.getUsuario();
		
		VariavelPreenchida vp = this.repository.findByVariavelIdAndPrefeituraCidadeIdAndAno(idVariavel, usuario.getPrefeitura().getCidade().getId(), Short.parseShort(ano.toString()));
		VariavelPreenchidaDTO dto = new VariavelPreenchidaDTO(vp);
		return dto;
	}

	public VariavelPreenchidaDTO buscaVariavelPreenchidaPorAnoVariavelSubdivisao(Long ano, Long idVariavel, Long idSubdivisao) throws Exception {
		Usuario usuario = this.usuarioContextUtil.getUsuario();
		Long idCidade =	usuario.getPrefeitura().getCidade().getId();
		SubdivisaoVariavelPreenchida vp = buscaSubdivisaoVariavelPreenchidaPorAnoVariavelSubdivisao(ano, idVariavel,idSubdivisao, idCidade );
		VariavelPreenchidaDTO dto = new VariavelPreenchidaDTO(vp);
		dto.setSubdivisao(vp.getSubdivisao().getId());
		return dto;
	}
	
	public SubdivisaoVariavelPreenchida buscaSubdivisaoVariavelPreenchidaPorAnoVariavelSubdivisao(Long ano, Long idVariavel, Long idSubdivisao, Long idCidade) throws Exception {
		SubdivisaoVariavelPreenchida vp = this.subdivisaoVPRepository.findByVariavelIdAndPrefeituraCidadeIdAndAnoAndSubdivisaoId(idVariavel, idCidade, Short.parseShort(ano.toString()), idSubdivisao);
		return vp;
	}
	

	public VariavelPreenchida preencher(VariavelPreenchidaDTO dto) throws Exception {
		Usuario usuario = this.usuarioContextUtil.getUsuario();
		Prefeitura prefeitura = usuario.getPrefeitura();
		Long idCidade = prefeitura.getCidade().getId();
		
		Variavel variavel = this.variavelService.listarById(dto.getIdVariavel());
		TipoVariavel tipoVariavel = TipoVariavel.fromString(variavel.getTipo());
		VariavelPreenchida vp = this.repository.findByVariavelIdAndPrefeituraCidadeIdAndAno(dto.getIdVariavel(), idCidade, dto.getAno());

		InstituicaoFonte instituicaoFonte = new InstituicaoFonte();
		if(vp == null){
			vp = new VariavelPreenchida();
		} 
		if (dto.getFonte() != null) {
			instituicaoFonte = instituicaoFonteService.getById(dto.getFonte());
		} else {
			instituicaoFonte = instituicaoFonteService.getByNomeAndCidadeId(dto.getFonteTexto(), idCidade);
			if(instituicaoFonte == null) {
				instituicaoFonte = new InstituicaoFonte();
				Orgao o = dto.getOrgao() != null ? orgaoService.buscarPorId(dto.getOrgao()): null;
				instituicaoFonte.setOrgao(o);
				instituicaoFonte.setNome(dto.getFonteTexto());
				instituicaoFonte.setCidade(prefeitura.getCidade());
				instituicaoFonteService.inserirInstituicaoFonte(instituicaoFonte);
			}
		}
		vp.setAno(dto.getAno());
		vp.setObservacao(dto.getObservacao());
		vp.setDataPreenchimento(new Date());
		vp.setStatus(dto.getStatus());
		vp.setInstituicaoFonte(instituicaoFonte);
		vp.setPrefeitura(prefeitura);
		vp.setVariavel(variavel);

		if (tipoVariavel == TipoVariavel.SIM_NAO) {
			vp.setRespostaSimples(dto.getRespostaSimples());
		}

		if (tipoVariavel == TipoVariavel.SIM_NAO_COM_LISTA_OPCOES) {
			vp.setRespostaSimples(dto.getRespostaSimples());
			if (variavel.isMultiplaSelecao()) {
				vp.setOpcoes(this.atribuirOpcao(dto.getIdOpcoes()));
			} else {
				if (dto.getIdOpcao() != null && dto.getIdOpcao() != 0) {
					VariaveisOpcoes opcao = variavel.getVariavelResposta().getListaOpcoes().stream()
							.filter(variavelOpcao -> variavelOpcao.getId().equals(dto.getIdOpcao())).findFirst()
							.orElseThrow(() -> new ObjectNotFoundException(
									"Opção não encontrada para a variável " + variavel.getNome()));
					vp.setOpcao(opcao);
				}
			}

		}

		if (tipoVariavel == TipoVariavel.LISTA_OPCOES) {
			if (variavel.isMultiplaSelecao()) {
				vp.setOpcoes(this.atribuirOpcao(dto.getIdOpcoes()));
			} else {
				VariaveisOpcoes opcao = variavel.getVariavelResposta().getListaOpcoes().stream()
						.filter(variavelOpcao -> variavelOpcao.getId().equals(dto.getIdOpcao())).findFirst()
						.orElseThrow(() -> new ObjectNotFoundException(
								"Opção não encontrada para a variável " + variavel.getNome()));
				vp.setOpcao(opcao);
			}
		}

		if (tipoVariavel == TipoVariavel.INTEIRO || tipoVariavel == TipoVariavel.DECIMAL) {
			vp.setValor(dto.getValor());
		}
		if (tipoVariavel == TipoVariavel.TEXTO_LIVRE) {
			vp.setValorTexto(dto.getValorTexto());
			vp.setValor(null);
		}	

		this.repository.save(vp);

		return vp;
	}

	public SubdivisaoVariavelPreenchida preencherComSubdivisao(VariavelPreenchidaDTO dto) throws Exception {
		Usuario usuario = this.usuarioContextUtil.getUsuario();
		Prefeitura prefeitura = usuario.getPrefeitura();
		Long idCidade = prefeitura.getCidade().getId();
		
		Variavel variavel = this.variavelService.listarById(dto.getIdVariavel());
		TipoVariavel tipoVariavel = TipoVariavel.fromString(variavel.getTipo());
		SubdivisaoVariavelPreenchida svp = this.subdivisaoVPRepository.findByVariavelIdAndPrefeituraCidadeIdAndAnoAndSubdivisaoId(dto.getIdVariavel(), idCidade, dto.getAno(),dto.getSubdivisao());

		InstituicaoFonte instituicaoFonte = new InstituicaoFonte();
		if(svp == null){
			svp = new SubdivisaoVariavelPreenchida();
		} 
		if (dto.getFonte() != null) {
			instituicaoFonte = instituicaoFonteService.getById(dto.getFonte());
		} else {
			instituicaoFonte = instituicaoFonteService.getByNomeAndCidadeId(dto.getFonteTexto(), idCidade);
			if(instituicaoFonte == null) {
				instituicaoFonte = new InstituicaoFonte();
				Orgao o = dto.getOrgao() != null ? orgaoService.buscarPorId(dto.getOrgao()): null;
				instituicaoFonte.setOrgao(o);
				instituicaoFonte.setNome(dto.getFonteTexto());
				instituicaoFonte.setCidade(prefeitura.getCidade());
				instituicaoFonteService.inserirInstituicaoFonte(instituicaoFonte);
			}
		}
		SubdivisaoCidade subdivisao = this.subdivisaoService.buscarPorId(dto.getSubdivisao());
		svp.setSubdivisao(subdivisao);
		svp.setAno(dto.getAno());
		svp.setObservacao(dto.getObservacao());
		svp.setDataPreenchimento(new Date());
		svp.setStatus(dto.getStatus());
		svp.setInstituicaoFonte(instituicaoFonte);
		svp.setPrefeitura(prefeitura);
		svp.setVariavel(variavel);

		if (tipoVariavel == TipoVariavel.SIM_NAO) {
			svp.setRespostaSimples(dto.getRespostaSimples());
		}

		if (tipoVariavel == TipoVariavel.SIM_NAO_COM_LISTA_OPCOES) {
			svp.setRespostaSimples(dto.getRespostaSimples());
			if (variavel.isMultiplaSelecao()) {
				svp.setOpcoes(this.atribuirOpcao(dto.getIdOpcoes()));
			} else {
				if (dto.getIdOpcao() != null && dto.getIdOpcao() != 0) {
					VariaveisOpcoes opcao = variavel.getVariavelResposta().getListaOpcoes().stream()
							.filter(variavelOpcao -> variavelOpcao.getId().equals(dto.getIdOpcao())).findFirst()
							.orElseThrow(() -> new ObjectNotFoundException(
									"Opção não encontrada para a variável " + variavel.getNome()));
					svp.setOpcao(opcao);
				}
			}

		}

		if (tipoVariavel == TipoVariavel.LISTA_OPCOES) {
			if (variavel.isMultiplaSelecao()) {
				svp.setOpcoes(this.atribuirOpcao(dto.getIdOpcoes()));
			} else {
				VariaveisOpcoes opcao = variavel.getVariavelResposta().getListaOpcoes().stream()
						.filter(variavelOpcao -> variavelOpcao.getId().equals(dto.getIdOpcao())).findFirst()
						.orElseThrow(() -> new ObjectNotFoundException(
								"Opção não encontrada para a variável " + variavel.getNome()));
				svp.setOpcao(opcao);
			}
		}

		if (tipoVariavel == TipoVariavel.INTEIRO || tipoVariavel == TipoVariavel.DECIMAL) {
			svp.setValor(dto.getValor());
		}
		if (tipoVariavel == TipoVariavel.TEXTO_LIVRE) {
			svp.setValorTexto(dto.getValorTexto());
			svp.setValor(null);
		}	

		this.subdivisaoVPRepository.save(svp);

		return svp;
	}
	
	private List<VariaveisOpcoes> atribuirOpcao(List<Long> opcoes) {
		List<VariaveisOpcoes> lista = new ArrayList<>();
		if (opcoes != null) {
			for (Long id : opcoes) {
				Optional<VariaveisOpcoes> op = this.variaveisOpcoesRepository.findById(id);
				VariaveisOpcoes var = op.get();
				lista.add(var);
			}
		} else {
			return null;
		}
		return lista;
	}
	
	public List<VariavelPreenchidaFiltradaIntegracaoDTO> buscaVariavelPreenchidaPorAnoVariavelIntegracao(Short ano, String nomeVariavel, String nomeCidade) {
		
		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<VariavelPreenchidaFiltradaIntegracaoDTO> query = cb.createQuery(VariavelPreenchidaFiltradaIntegracaoDTO.class);

		Root<VariavelPreenchida> variavelPreenchida = query.from(VariavelPreenchida.class);
		
		Join<VariavelPreenchida, Variavel> joinVariavel = variavelPreenchida.join("variavel",JoinType.LEFT);
		Join<VariavelPreenchida, InstituicaoFonte> joinInstituicaoFonte = variavelPreenchida.join("instituicaoFonte",JoinType.LEFT);
		Join<VariavelPreenchida, Prefeitura> joinPrefeitura = variavelPreenchida.join("prefeitura",JoinType.LEFT);
		Join<Prefeitura, Cidade> joinCidade = joinPrefeitura.join("cidade",JoinType.LEFT);
		
		query.multiselect(variavelPreenchida.get("id"), joinVariavel.get("id"), variavelPreenchida.get("ano"), variavelPreenchida.get("valor"), 
				joinVariavel.get("nome"), variavelPreenchida.get("observacao"), joinInstituicaoFonte.get("nome"),
				joinCidade.get("nome")).distinct(true);
		
		List<javax.persistence.criteria.Predicate> predicateList = new ArrayList<>();
		
		if (ano != null) {
			Path<Short> anoVariavel = variavelPreenchida.get("ano");
			Predicate predicateForAno = cb.equal(anoVariavel, ano);
			predicateList.add(predicateForAno);
		}
		
		if (nomeVariavel != null && !nomeVariavel.equals("")) {
			Path<String> nomeVariavelPath = joinVariavel.get("nome");
			javax.persistence.criteria.Predicate predicateForNome = cb.like(cb.lower(nomeVariavelPath), "%" + nomeVariavel.toLowerCase() + "%");
			predicateList.add(predicateForNome);
		}	
		
		if (nomeCidade != null && !nomeCidade.equals("")) {
			Path<String> nomeCidadePath = joinCidade.get("nome");
			javax.persistence.criteria.Predicate predicateForNomeCidade = cb.like(cb.lower(nomeCidadePath), "%" + nomeCidade.toLowerCase() + "%");
			predicateList.add(predicateForNomeCidade);
		}

		List<Order> orderList = new ArrayList<Order>();
		orderList.add(cb.desc(variavelPreenchida.get("id")));
		query.orderBy(orderList);
		
		javax.persistence.criteria.Predicate[] predicates = new javax.persistence.criteria.Predicate[predicateList.size()];
		predicateList.toArray(predicates);
		query.where(predicates);

		TypedQuery<VariavelPreenchidaFiltradaIntegracaoDTO> typedQuery = em.createQuery(query);

		List<VariavelPreenchidaFiltradaIntegracaoDTO> listVariaveis = typedQuery.getResultList(); 
		
		return listVariaveis;
	}

	public List<SubdivisaoVariavelPreenchida> buscarPorIndicadorCidadeAnoInicialEFinalSubdivisao(Long idIndicador,
			Long idCidade, Short anoInicial, Short anoFinal, Long idSubdivisao) {
		List<SubdivisaoVariavelPreenchida> lista = this.subdivisaoVPRepository.findByIndicadorCidadeAnoSubdivisao(idIndicador, idCidade, anoInicial, anoFinal, idSubdivisao);
		return lista;
	}

	public List<SubdivisaoVariavelPreenchida> buscarPorIndicadorCidadeSubdivisao(Long idIndicador, Long idCidade,
			Long idSubdivisao) {
		List<SubdivisaoVariavelPreenchida> lista = this.subdivisaoVPRepository.findByIndicadorCidadeSubdivisao(idIndicador, idCidade, idSubdivisao);
		return lista;
	}

}
