package br.org.cidadessustentaveis.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import br.org.cidadessustentaveis.dto.CombosMaterialApoioDTO;
import br.org.cidadessustentaveis.dto.FiltroMaterialApoioDTO;
import br.org.cidadessustentaveis.dto.ItemComboDTO;
import br.org.cidadessustentaveis.dto.MaterialApoioDTO;
import br.org.cidadessustentaveis.dto.MaterialApoioDetalhadoDTO;
import br.org.cidadessustentaveis.dto.UsuarioDTO;
import br.org.cidadessustentaveis.model.administracao.AreaInteresse;
import br.org.cidadessustentaveis.model.administracao.Cidade;
import br.org.cidadessustentaveis.model.administracao.Eixo;
import br.org.cidadessustentaveis.model.administracao.MetaObjetivoDesenvolvimentoSustentavel;
import br.org.cidadessustentaveis.model.administracao.ObjetivoDesenvolvimentoSustentavel;
import br.org.cidadessustentaveis.model.administracao.Pais;
import br.org.cidadessustentaveis.model.administracao.ProvinciaEstado;
import br.org.cidadessustentaveis.model.administracao.Usuario;
import br.org.cidadessustentaveis.model.indicadores.Indicador;
import br.org.cidadessustentaveis.model.institucional.Arquivo;
import br.org.cidadessustentaveis.model.planjementoIntegrado.MaterialApoio;
import br.org.cidadessustentaveis.repository.MaterialApoioRepository;
import br.org.cidadessustentaveis.services.exceptions.DataIntegrityException;
import br.org.cidadessustentaveis.services.exceptions.ObjectNotFoundException;
import br.org.cidadessustentaveis.util.EmailUtil;
import br.org.cidadessustentaveis.util.ProfileUtil;
import br.org.cidadessustentaveis.util.UsuarioContextUtil;

@Service
public class MaterialApoioService {

	@Autowired
	private MaterialApoioRepository repository;

	@Autowired
	private PaisService paisService;
	
	@Autowired
	private ProvinciaEstadoService provinciaEstadoService;

	@Autowired
	private CidadeService cidadeService;

	@Autowired
	private AreaInteresseService areaInteresseService;

	@Autowired
	private EixoService eixoService;

	@Autowired
	private IndicadorService indicadorService;

	@Autowired
	private ObjetivoDesenvolvimentoSustentavelService odsService;

	@Autowired
	private MetaObjetivoDesenvolvimentoSustentavelService metasOdsService;

	@Autowired
	private ArquivoService arquivoService;
	@Autowired
	private UsuarioContextUtil usuarioContextUtil;

	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private EntityManager em;
	
	@Autowired
	private EmailUtil emailUtil;
	
	@Autowired
	private ProfileUtil profileUtil;

	public List<MaterialApoio> buscar() {
		return repository.findAll();
	}

	public MaterialApoio buscarPorId(Long id) {
		Optional<MaterialApoio> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException("Material de apoio não encontrado!"));
	}

	public CombosMaterialApoioDTO carregarCombosMaterialApoio() {
		CombosMaterialApoioDTO combosMaterialApoio = new CombosMaterialApoioDTO();

		combosMaterialApoio.setListaPaises(paisService.buscarPaisesCombo());
		combosMaterialApoio.setListaAreasInteresse(areaInteresseService.buscaAreaInteresses());
		combosMaterialApoio.setListaEixos(eixoService.buscarEixosParaComboBox());
		combosMaterialApoio.setListaOds(odsService.buscarOdsParaCombo());

		return combosMaterialApoio;
	}

	public MaterialApoio inserirMaterialDeApoio(MaterialApoioDTO materialApoioDTO) throws Exception {
		Usuario usuario = usuarioContextUtil.getUsuario();
		MaterialApoio materialApoio = materialApoioDTO.toEntityInsert(materialApoioDTO);

		materialApoio.setPais(
				materialApoioDTO.getPais() != null ? paisService.buscarPorId(materialApoioDTO.getPais()) : null);
		materialApoio.setProvinciaEstado(
				materialApoioDTO.getProvinciaEstado() != null ? provinciaEstadoService.buscarPorId(materialApoioDTO.getProvinciaEstado()) : null);
		materialApoio.setCidade(
				materialApoioDTO.getCidade() != null ? cidadeService.buscarPorId(materialApoioDTO.getCidade()) : null);
		
		materialApoio.setPrefeitura(usuario.getPrefeitura());
		
		if(materialApoioDTO.getAreasInteresse() != null && !materialApoioDTO.getAreasInteresse().isEmpty()) {
			materialApoio.setAreasInteresse(areaInteresseService.buscarAreasPorIds(materialApoioDTO.getAreasInteresse()));
		}
		
		if(materialApoioDTO.getEixo() != null && !materialApoioDTO.getEixo().isEmpty()) {
			materialApoio.setEixo(eixoService.buscarEixosPorIds(materialApoioDTO.getEixo()));
		}
		
		if(materialApoioDTO.getIndicador() != null && !materialApoioDTO.getIndicador().isEmpty()) {
			materialApoio.setIndicador(indicadorService.buscarIndicadoresPorIds(materialApoioDTO.getIndicador()));
		}
		
		if(materialApoioDTO.getOds() != null && !materialApoioDTO.getOds().isEmpty()) {
			materialApoio.setOds(odsService.buscarPorIds(materialApoioDTO.getOds()));
		}
		
		if(materialApoioDTO.getMetaOds() != null && !materialApoioDTO.getMetaOds().isEmpty()) {
			materialApoio.setMetaOds(metasOdsService.buscarPorIds(materialApoioDTO.getMetaOds()));
		}
		
		materialApoio = repository.save(materialApoio);
				
		enviarEmailParaInteressados(materialApoio);
		
		return materialApoio;
	}

	public List<MaterialApoioDTO> buscarMateriaisDeApoio() {

		return repository.buscarMateriaisDeApoioToList();
	}

	public void excluirMaterialApoio(Long id) {
		buscarPorId(id);
		try {
			repository.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("O registro está relacionado com outra entidade");
		}
	}

	public MaterialApoioDTO buscarMaterialDeApoioPorId(Long id) {
		MaterialApoio materialApoioRef = buscarPorId(id);
		return new MaterialApoioDTO(materialApoioRef);
	}

	public MaterialApoioDetalhadoDTO buscarMaterialDeApoioDetalhadoPorId(Long id) {
		MaterialApoio materialApoioRef = buscarPorId(id);
		return new MaterialApoioDetalhadoDTO(materialApoioRef);
	}

	public MaterialApoio alterar(MaterialApoioDTO materialApoioDTO) throws Exception {

		if (materialApoioDTO.getId() == null) {
			throw new DataIntegrityException("O registro não pode ser editado pois não existe na base de dados");
		}

		MaterialApoio materialApoioRef = materialApoioDTO.toEntityUpdate(buscarPorId(materialApoioDTO.getId()));
		materialApoioRef.setPais(
				materialApoioDTO.getPais() != null ? paisService.buscarPorId(materialApoioDTO.getPais()) : null);
		materialApoioRef.setProvinciaEstado(
				materialApoioDTO.getProvinciaEstado() != null ? provinciaEstadoService.buscarPorId(materialApoioDTO.getProvinciaEstado()) : null);
		materialApoioRef.setCidade(
				materialApoioDTO.getCidade() != null ? cidadeService.buscarPorId(materialApoioDTO.getCidade()) : null);
		
		if(materialApoioDTO.getAreasInteresse() != null && !materialApoioDTO.getAreasInteresse().isEmpty()) {
			materialApoioRef.setAreasInteresse(areaInteresseService.buscarAreasPorIds(materialApoioDTO.getAreasInteresse()));
		}
		else {
			materialApoioRef.setAreasInteresse(null);
		}
		
		if(materialApoioDTO.getEixo() != null && !materialApoioDTO.getEixo().isEmpty()) {
			materialApoioRef.setEixo(eixoService.buscarEixosPorIds(materialApoioDTO.getEixo()));
		}
		else {
			materialApoioRef.setEixo(null);
		}
		
		if(materialApoioDTO.getIndicador() != null && !materialApoioDTO.getIndicador().isEmpty()) {
			materialApoioRef.setIndicador(indicadorService.buscarIndicadoresPorIds(materialApoioDTO.getIndicador()));
		}
		else {
			materialApoioRef.setIndicador(null);
		}
		
		if(materialApoioDTO.getOds() != null && !materialApoioDTO.getOds().isEmpty()) {
			materialApoioRef.setOds(odsService.buscarPorIds(materialApoioDTO.getOds()));
		}
		else {
			materialApoioRef.setOds(null);
		}
		
		if(materialApoioDTO.getMetaOds() != null && !materialApoioDTO.getMetaOds().isEmpty()) {
			materialApoioRef.setMetaOds(metasOdsService.buscarPorIds(materialApoioDTO.getMetaOds()));
		}
		else {
			materialApoioRef.setMetaOds(null);
		}
		
		if (materialApoioDTO.getImagemCapa() == null) {
			materialApoioRef.setImagemCapa(null);
		} else if (materialApoioDTO.getImagemCapa().getId() != null) {
			materialApoioRef.setImagemCapa(arquivoService.buscarPorId(materialApoioDTO.getImagemCapa().getId()));
		} else {
			materialApoioRef.setImagemCapa(materialApoioDTO.getImagemCapa().toEntityInsert());
		}

		if (materialApoioDTO.getArquivoPublicacao().getId() != null) {
			materialApoioRef
					.setArquivoPublicacao(arquivoService.buscarPorId(materialApoioDTO.getArquivoPublicacao().getId()));
		} else {
			materialApoioRef.setArquivoPublicacao(materialApoioDTO.getArquivoPublicacao().toEntityInsert());
		}

		materialApoioRef = repository.save(materialApoioRef);

		return materialApoioRef;
	}

	public List<ItemComboDTO> buscarParaCombo() {
		return repository.buscarParaCombo();
	}

	public Arquivo buscarArquivoPublicacao(Long idMaterialApoio) {
		return repository.buscarArquivoPublicacao(idMaterialApoio);
	}

	public List<MaterialApoio> listarPorInstituicao(Integer page, Boolean ehPCS, List<Long> idsParaExcluir) {
		List<MaterialApoio> lista = new ArrayList<MaterialApoio>();
		Page<MaterialApoio> pagina = null;
		//se a página for menor que zero, tras tudo.
		int itensPorPagina = (page < 0 ) ? 99999 : 3 ;
		page = (page < 0 ) ? 0 : page ;

		if (ehPCS) {
			if (idsParaExcluir == null || idsParaExcluir.isEmpty()) {
				pagina = repository.findByInstituicaoLikeOrderByIdDesc("PCS", PageRequest.of(page, itensPorPagina));
			} else {
				pagina = repository.findByInstituicaoLikeAndIdNotInOrderByIdDesc("PCS", idsParaExcluir,
						PageRequest.of(page, itensPorPagina));
			}
		} else {
			if (idsParaExcluir == null || idsParaExcluir.isEmpty()) {
				pagina = repository.findByInstituicaoNotLikeOrderByIdDesc("PCS", (PageRequest.of(page, itensPorPagina)));
			} else {
				pagina = repository.findByInstituicaoNotLikeAndIdNotInOrderByIdDesc("PCS", idsParaExcluir,
						PageRequest.of(page, itensPorPagina));
			}
		}
		lista = pagina.getContent();
		return lista;
	}
	
	public List<FiltroMaterialApoioDTO> buscarMaterialApoioFiltrado(Long idAreaInteresse, Long idEixo, Long idOds, Long idMetasOds, Long idIndicador, Long idCidade,
			Long idProvinciaEstado, String regiao, Long idPais, String continente, Long populacaoMin, Long populacaoMax, String palavraChave) {
		
		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<FiltroMaterialApoioDTO> query = cb.createQuery(FiltroMaterialApoioDTO.class);
		
		Root<MaterialApoio> materialApoio = query.from(MaterialApoio.class);
		
		Join<MaterialApoio, AreaInteresse> joinAreasInteresse = materialApoio.join("areasInteresse", JoinType.LEFT);
		Join<MaterialApoio, Eixo> joinEixo = materialApoio.join("eixo", JoinType.LEFT);
		Join<MaterialApoio, Indicador> joinIndicador = materialApoio.join("indicador", JoinType.LEFT);
		Join<MaterialApoio, ObjetivoDesenvolvimentoSustentavel> joinOds = materialApoio.join("ods", JoinType.LEFT);
		Join<MaterialApoio, MetaObjetivoDesenvolvimentoSustentavel> joinMetaOds = materialApoio.join("metaOds", JoinType.LEFT);
		Join<MaterialApoio, Pais> joinPais = materialApoio.join("pais", JoinType.LEFT);
		Join<MaterialApoio, Cidade> joinCidade = materialApoio.join("cidade",JoinType.LEFT);
		Join<MaterialApoio, ProvinciaEstado> joinProvinciaEstado = materialApoio.join("provinciaEstado", JoinType.LEFT);	
		
		
		
		query.multiselect(materialApoio.get("id")).distinct(true);
		
		List<javax.persistence.criteria.Predicate> predicateList = new ArrayList<>();
		
		if (continente != null && !continente.equals("")) {
			Path<String> continenteMaterialApoio = materialApoio.get("continente");
			predicateList.add(cb.equal(continenteMaterialApoio, continente));
		}
		
		if (idPais != null) {
			Path<Long> paisMaterialApoio = materialApoio.get("pais");
			predicateList.add(cb.equal(paisMaterialApoio, idPais));
		}
		
		if (regiao != null && !regiao.equals("")) {
			Path<Long> regiaoMaterialApoio = materialApoio.get("regiao");
			predicateList.add(cb.equal(regiaoMaterialApoio, regiao));
		}
		
		if (idCidade != null) {
			Path<Long> cidadeMaterialApoio = materialApoio.get("cidade");
			predicateList.add(cb.equal(cidadeMaterialApoio, idCidade));
		}
		
		if (idEixo != null) {
			In<Long> inClause = cb.in(joinEixo.get("id"));
			inClause.value(idEixo);
			predicateList.add(inClause);
		}
		
		if (idIndicador != null) {
			In<Long> inClause = cb.in(joinIndicador.get("id"));
			inClause.value(idIndicador);
			predicateList.add(inClause);
		}
		
		if (idOds != null) {
			In<Long> inClause = cb.in(joinOds.get("id"));
			inClause.value(idOds);
			predicateList.add(inClause);
		}
		
		if (idMetasOds != null) {
			In<Long> inClause = cb.in(joinMetaOds.get("id"));
			inClause.value(idMetasOds);
			predicateList.add(inClause);
		}
		
		if (populacaoMin != null) {
			Path<Long> populacaoCidade = joinCidade.get("populacao");
			predicateList.add(cb.greaterThanOrEqualTo(populacaoCidade, populacaoMin));
		}
		
		if (populacaoMax != null) {
			Path<Long> populacaoCidade = joinCidade.get("populacao");
			predicateList.add(cb.lessThanOrEqualTo(populacaoCidade, populacaoMax));
		}
		
		if (idAreaInteresse != null) {
			In<Long> inClause = cb.in(joinAreasInteresse.get("id"));
			inClause.value(idAreaInteresse);
			predicateList.add(inClause);
		}
		
		if(idPais != null) {			
			Path<Long> pais = joinPais.get("id");
			predicateList.add(cb.equal(pais, idPais));
		}
		
		if(idProvinciaEstado != null) {
			Path<Long> provinciaEstado = joinProvinciaEstado.get("id");
			predicateList.add(cb.equal(provinciaEstado, idProvinciaEstado));
		}
		
		if (palavraChave != null && !palavraChave.equals("")) {
			Path<String> tituloMaterialApoio = materialApoio.get("titulo");
			Path<String> subtituloMaterialApoio = materialApoio.get("subtitulo");
			Path<String> palavraChaveMaterialApoio = materialApoio.get("palavraChave");
			Path<String> resumoMaterialApoio = materialApoio.get("resumo");
			Predicate predicateForTitulo = cb.like(cb.lower(tituloMaterialApoio), "%" + palavraChave.toLowerCase() + "%");
			Predicate predicateForSubtitulo = cb.like(cb.lower(subtituloMaterialApoio), "%" + palavraChave.toLowerCase() + "%");
			Predicate predicateForPalavraChave = cb.like(cb.lower(palavraChaveMaterialApoio), "%" + palavraChave.toLowerCase() + "%");
			Predicate predicateForResumo = cb.like(cb.lower(resumoMaterialApoio), "%" + palavraChave.toLowerCase() + "%");
			predicateList.add(cb.or(predicateForTitulo, predicateForSubtitulo, predicateForPalavraChave, predicateForResumo));
		}

		javax.persistence.criteria.Predicate[] predicates = new javax.persistence.criteria.Predicate[predicateList.size()];
		predicateList.toArray(predicates);
		query.where(predicates);

		TypedQuery<FiltroMaterialApoioDTO> typedQuery = em.createQuery(query);
		List<FiltroMaterialApoioDTO> lista = typedQuery.getResultList();

		return lista;
	}
	
	public void enviarEmailParaInteressados(MaterialApoio materialApoio) throws Exception {
			
			List<UsuarioDTO> usuarios = usuarioService.findUsuariosRecebeEmailTrueECredencialNaoExcluida();

			List<String> emailsParaEnviar = new ArrayList<String>();
			
			if(!usuarios.isEmpty()) {
			usuarios.forEach(usuario -> {
				if(materialApoio.getCidade() != null && usuario.getCidadeInteresse() != null && !usuario.getCidadeInteresse().equals("")) {
					if(materialApoio.getCidade().getId() == Long.parseLong(usuario.getCidadeInteresse())) {
						emailsParaEnviar.add(usuario.getEmail());
					}
				}
				else {
					if(materialApoio.getAreasInteresse() != null) {
						materialApoio.getAreasInteresse().forEach(areaInteresseMateriaApoio -> {
							usuario.getAreasInteresse().forEach(areaInteresseUsuario -> {
								if(areaInteresseUsuario.getNome().equals(areaInteresseMateriaApoio.getNome())) {
									emailsParaEnviar.add(usuario.getEmail());
								}
							});
						});
					}
				}
			});

			if(!emailsParaEnviar.isEmpty()) {
					StringBuffer mensagem = new StringBuffer();
					String urlFront = profileUtil.getProperty("profile.frontend");
					
					mensagem.append("<div style='margin-left: 3%; word-break: break-word'>");
						mensagem.append("<p>Foi cadastrado um material de apoio de seu interesse, clique no link abaixo para ver: </p>");
						mensagem.append("<a href='" + urlFront + "/participacao-cidada/material-apoio/detalhe/" + materialApoio.getId() + "' target='_blank'");
							mensagem.append("<p>" + materialApoio.getTitulo() + "</p>");
						mensagem.append("</a>");
					mensagem.append("</div>");		
					
					emailUtil.enviarEmailHTML(emailsParaEnviar.stream().distinct().collect(Collectors.toList()), materialApoio.getTitulo(), mensagem.toString());			
			}
		}
	}
}
