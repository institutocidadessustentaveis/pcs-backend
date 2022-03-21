package br.org.cidadessustentaveis.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
import org.springframework.stereotype.Service;

import br.org.cidadessustentaveis.dto.ArquivoDTO;
import br.org.cidadessustentaveis.dto.BibliotecaDTO;
import br.org.cidadessustentaveis.dto.BibliotecaDetalhesDTO;
import br.org.cidadessustentaveis.dto.CombosBibliotecaDTO;
import br.org.cidadessustentaveis.dto.FiltroBibliotecaPlanoLeisRegulamentacaoDTO;
import br.org.cidadessustentaveis.dto.FiltroBibliotecasDTO;
import br.org.cidadessustentaveis.model.administracao.AreaInteresse;
import br.org.cidadessustentaveis.model.administracao.Cidade;
import br.org.cidadessustentaveis.model.administracao.Eixo;
import br.org.cidadessustentaveis.model.administracao.MetaObjetivoDesenvolvimentoSustentavel;
import br.org.cidadessustentaveis.model.administracao.ObjetivoDesenvolvimentoSustentavel;
import br.org.cidadessustentaveis.model.administracao.Pais;
import br.org.cidadessustentaveis.model.administracao.ProvinciaEstado;
import br.org.cidadessustentaveis.model.administracao.Usuario;
import br.org.cidadessustentaveis.model.biblioteca.Biblioteca;
import br.org.cidadessustentaveis.model.indicadores.Indicador;
import br.org.cidadessustentaveis.model.institucional.Arquivo;
import br.org.cidadessustentaveis.repository.BibliotecaRepository;
import br.org.cidadessustentaveis.services.exceptions.ObjectNotFoundException;
import br.org.cidadessustentaveis.util.UsuarioContextUtil;

@Service
public class BibliotecaService {

	@Autowired
	private BibliotecaRepository repository;
	
	@Autowired 
	private EixoService eixoService;
	
	@Autowired
	ObjetivoDesenvolvimentoSustentavelService odsService;
	
	@Autowired
	private PaisService paisService;
	
	@Autowired
	private CidadeService cidadeService;
	
	@Autowired
	AreaInteresseService areaInteresseService;
	
	@Autowired
	private ArquivoService arquivoService;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private IndicadorService indicadorService;
	
	@Autowired
	private MetaObjetivoDesenvolvimentoSustentavelService metaOdsService;
	
	@Autowired
	ProvinciaEstadoService provinciaEstadoService;
	
	@Autowired
	UsuarioContextUtil usuarioContextUtil;
	
	@Autowired
	private EntityManager em;
	
	@Autowired
	GrupoAcademicoService grupoAcademicoService;
	
	
	public Biblioteca buscarPorId(Long id) {

		Optional<Biblioteca> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException("Biblioteca não encontrada!"));
	}
	
	public Biblioteca buscarPorIdEUsuarioId(Long idBiblioteca, long idUsuario) throws Exception {
		Usuario usuario = usuarioLogado();
		Optional<Biblioteca> obj = repository.buscarBibliotecaPorIdEUsuario(idBiblioteca, usuario.getId());
		return obj.orElseThrow(() -> new ObjectNotFoundException("Biblioteca não encontrada!"));

	}
	
	public BibliotecaDTO buscarBibliotecaPorId(Long id) throws Exception {
		Biblioteca bibliotecaRef = buscarPorId(id);
		if (bibliotecaEhDoUsuario(bibliotecaRef) || usuarioLogadoEhAdmin()) {			
			return new BibliotecaDTO(bibliotecaRef);
		}
		else {
			throw new Exception("Você não tem permissão para acessar este recurso");
		}
	}
	
	public boolean bibliotecaEhDoUsuario(Biblioteca biblioteca) throws Exception {
		Usuario usuario = usuarioLogado();
		if (usuario.getId().equals(biblioteca.getUsuario().getId())) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public BibliotecaDetalhesDTO buscarBibliotecaDetalhesPorId(Long id) {
		Biblioteca bibliotecaRef = buscarPorId(id);
		List<Arquivo> arquivos = this.buscarArquivoPublicacao(id);
		return new BibliotecaDetalhesDTO(bibliotecaRef, (arquivos.size() != 0 ? true : false));
	}	
	
	
	public List<BibliotecaDTO> buscarBibliotecasToList(Long idUsuario) {
		return repository.buscarBibliotecasToList(idUsuario);
	}
	
	public List<BibliotecaDTO> buscarBibliotecasParaComboBox() {
		return repository.buscarBibliotecasParaComboBox();
	}
	
	public List<BibliotecaDTO> buscarBibliotecasToListAdmin() throws Exception {
		if (usuarioLogadoEhAdmin()) {			
			return repository.buscarBibliotecasToListAdmin();
		} 
		else {
			throw new Exception("Você não tem permissão para acessar este recurso");
		}	
	}
	
	private boolean usuarioLogadoEhAdmin() throws Exception {
		Usuario usuarioLogado = usuarioLogado();
		for (int i = 0; i < usuarioLogado.getCredencial().getListaPerfil().size(); i++) {
			if (usuarioLogado.getCredencial().getListaPerfil().get(i).getNome().equals("Administrador")) {
				return true;
			}
		}
		return false;
	}
	
	private Usuario usuarioLogado() throws Exception {
		return usuarioContextUtil.getUsuario();
	}
	
	public Biblioteca inserir(BibliotecaDTO	 bibliotecaDTO) {
		Biblioteca biblioteca = fazerBiblioteca(bibliotecaDTO);
		repository.save(biblioteca);
		return biblioteca;
	}
	
	public Biblioteca fazerBiblioteca(BibliotecaDTO bibliotecaDTO) {
		Biblioteca biblioteca = bibliotecaDTO.toEntityInsert(bibliotecaDTO);
		
		biblioteca.setOds(bibliotecaDTO.getOds() != null ? odsService.buscarPorIds(bibliotecaDTO.getOds()) : null);
		biblioteca.setMeta(bibliotecaDTO.getMeta() != null ? metaOdsService.buscarPorIds(bibliotecaDTO.getMeta()) : null);
		biblioteca.setCidade(bibliotecaDTO.getCidade() != null ? cidadeService.buscarPorId(bibliotecaDTO.getCidade()) : null);
		biblioteca.setUsuario(bibliotecaDTO.getUsuario() != null ? usuarioService.buscarPorId(bibliotecaDTO.getUsuario()) : null);
		biblioteca.setEstado(bibliotecaDTO.getEstado() != null ? provinciaEstadoService.buscarPorId(bibliotecaDTO.getEstado()) : null);
		biblioteca.setPaisPublicacao(bibliotecaDTO.getPaisPublicacao() != null ? paisService.buscarPorId(bibliotecaDTO.getPaisPublicacao()) : null);
		biblioteca.setIndicadores(bibliotecaDTO.getIndicadores() != null ? indicadorService.buscarIndicadoresPorIds(bibliotecaDTO.getIndicadores()) : null );
		biblioteca.setEixos(bibliotecaDTO.getEixos() != null && !bibliotecaDTO.getEixos().isEmpty()? eixoService.buscarEixosPorIds(bibliotecaDTO.getEixos()) : null);
		biblioteca.setAreasInteresse(bibliotecaDTO.getAreasInteresse() != null && !bibliotecaDTO.getAreasInteresse().isEmpty()? areaInteresseService.buscarAreasPorIds(bibliotecaDTO.getAreasInteresse()) : null);
		biblioteca.setDataPublicacao(LocalDate.now());
		biblioteca.setGrupoAcademico(bibliotecaDTO.getGrupoAcademico() != null ? grupoAcademicoService.buscarPorId(bibliotecaDTO.getGrupoAcademico()) : null);
		return biblioteca;
	}
	
	public Biblioteca alterar( BibliotecaDTO bibliotecaDTO) throws Exception {
		if (bibliotecaDTO.getId() == null) {
			throw new Exception("Campo id divergente.");
		}
	    
		Biblioteca biblioteca = bibliotecaDTO.toEntityUpdate(buscarPorId(bibliotecaDTO.getId()));
		
		biblioteca.setCidade(bibliotecaDTO.getCidade() != null ? cidadeService.buscarPorId(bibliotecaDTO.getCidade()) : null);
		biblioteca.setEstado(bibliotecaDTO.getEstado() != null ? provinciaEstadoService.buscarPorId(bibliotecaDTO.getEstado()) : null);
		biblioteca.setPaisPublicacao(bibliotecaDTO.getPaisPublicacao() != null ? paisService.buscarPorId(bibliotecaDTO.getPaisPublicacao()) : null);
		biblioteca.setEixos(bibliotecaDTO.getEixos() != null && !bibliotecaDTO.getEixos().isEmpty()? eixoService.buscarEixosPorIds(bibliotecaDTO.getEixos()) : null);
		biblioteca.setAreasInteresse(bibliotecaDTO.getAreasInteresse() != null && !bibliotecaDTO.getAreasInteresse().isEmpty()? areaInteresseService.buscarAreasPorIds(bibliotecaDTO.getAreasInteresse()) : null);
		biblioteca.setGrupoAcademico(bibliotecaDTO.getGrupoAcademico() != null ? grupoAcademicoService.buscarPorId(bibliotecaDTO.getGrupoAcademico()) : null);
		biblioteca.setOds(bibliotecaDTO.getOds().isEmpty() != true ? odsService.buscarPorIds(bibliotecaDTO.getOds()) : null);
		biblioteca.setMeta(!bibliotecaDTO.getMeta().isEmpty() ? metaOdsService.buscarPorIds(bibliotecaDTO.getMeta()) : null);
		
		if (bibliotecaDTO.getImagemCapa() == null) {
			bibliotecaDTO.setImagemCapa(null);
		} else if (bibliotecaDTO.getImagemCapa().getId() != null) {
			biblioteca.setImagemCapa(arquivoService.buscarPorId(bibliotecaDTO.getImagemCapa().getId()));
		} else {
			biblioteca.setImagemCapa(bibliotecaDTO.getImagemCapa().toEntityInsert());
		}
		
		/*Prepara arquivos multimídia*/		
		List<Arquivo> arquivos = new ArrayList<>();
		for (ArquivoDTO arquivo : bibliotecaDTO.getArquivoMultimidia()) {
			try {
				Arquivo arquivoAux = arquivoService.buscarPorId(arquivo.getId());
				arquivos.add(arquivoAux);
			} catch (Exception e) {
				Arquivo arquivoAux = arquivo.toEntityInsert();
				arquivos.add(arquivoAux);
			}
		}
		biblioteca.setArquivoMultimidia(arquivos);
				
		repository.save(biblioteca);
		return biblioteca;
	}
	
	public CombosBibliotecaDTO carregarCombosBiblioteca() {
		CombosBibliotecaDTO combosMaterialApoio = new CombosBibliotecaDTO();

		combosMaterialApoio.setListaPaises(paisService.buscarPaisesCombo());
		combosMaterialApoio.setListaAreasInteresse(areaInteresseService.buscaAreaInteresses());
		combosMaterialApoio.setListaEixos(eixoService.buscarEixosParaComboBox());
		combosMaterialApoio.setListaOds(odsService.buscarOdsParaCombo());

		return combosMaterialApoio;
	}
	
	public void deletar(Long id) {
		repository.deleteById(id);
	}
	
	public List<FiltroBibliotecaPlanoLeisRegulamentacaoDTO> buscarBibliotecaPlanoLeisRegulamentacaoFiltrida(Long idCidade, Long idProvinciaEstado, Long idPais, Long idTema, String palavraChave) {
		
		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<FiltroBibliotecaPlanoLeisRegulamentacaoDTO> query = cb.createQuery(FiltroBibliotecaPlanoLeisRegulamentacaoDTO.class);
		
		Root<Biblioteca> biblioteca = query.from(Biblioteca.class);
		
		Join<Biblioteca, Cidade> joinCidade = biblioteca.join("cidade",JoinType.LEFT);
		Join<Biblioteca, ProvinciaEstado> joinProvinciaEstado = biblioteca.join("estado",JoinType.LEFT);
		Join<Biblioteca, Pais> joinPais = biblioteca.join("paisPublicacao",JoinType.LEFT);
		Join<Biblioteca, AreaInteresse> joinAreasInteresse = biblioteca.join("areasInteresse", JoinType.LEFT);
		
		query.multiselect(biblioteca.get("id"), joinCidade.get("latitude"), joinCidade.get("longitude"), joinCidade.get("id"),
				joinCidade.get("nome"), biblioteca.get("tituloPublicacao"), biblioteca.get("descricao"), joinProvinciaEstado.get("id"), joinPais.get("id")).distinct(true);
		
		List<javax.persistence.criteria.Predicate> predicateList = new ArrayList<>();
		
		Boolean verificado = false;
		Path<String> pathModulo = biblioteca.join("modulo");

		Predicate predicateForModulo = cb.equal(pathModulo, "Plano, Leis e Regulamentações");
		Predicate predicateForPossuiCidade= joinCidade.get("id").isNotNull();
		
		if (idPais != null) {
			Path<Long> pais = biblioteca.get("paisPublicacao");
			Predicate predicateForPais = cb.equal(pais, idPais);
			predicateList.add(cb.and(predicateForPais, predicateForModulo, predicateForPossuiCidade));
			verificado = true;
		}
		
		if (idCidade != null) {
			Path<Long> cidade = biblioteca.get("cidade");
			Predicate predicateForCidade = cb.equal(cidade, idCidade);
			predicateList.add(cb.and(predicateForCidade, predicateForModulo, predicateForPossuiCidade));
			verificado = true;
		}
		
		if(idProvinciaEstado != null) {
			Path<Long> provinciaEstado = biblioteca.get("estado");
			Predicate predicateForEstado = cb.equal(provinciaEstado, idProvinciaEstado);
			predicateList.add(cb.and(predicateForEstado, predicateForModulo, predicateForPossuiCidade));
			verificado = true;
		}
		
		if(palavraChave != null && !palavraChave.equals("")) {
			Path<String> descricaoBiblioteca = biblioteca.get("descricao");
			Predicate predicateForBiblioteca = cb.like(cb.lower(descricaoBiblioteca), "%" + palavraChave.toLowerCase() + "%");
			predicateList.add(cb.and(predicateForBiblioteca, predicateForModulo, predicateForPossuiCidade));
			verificado = true;
		}
		
		if (idTema != null) {
			In<Long> inClause = cb.in(joinAreasInteresse.get("id"));
			inClause.value(idTema);
			predicateList.add(cb.and(inClause, predicateForModulo, predicateForPossuiCidade));
			verificado = true;
		}
			
		if(!verificado) {
			predicateList.add(cb.and(predicateForModulo, predicateForPossuiCidade));
		}

		javax.persistence.criteria.Predicate[] predicates = new javax.persistence.criteria.Predicate[predicateList.size()];
		predicateList.toArray(predicates);
		query.where(predicates);

		TypedQuery<FiltroBibliotecaPlanoLeisRegulamentacaoDTO> typedQuery = em.createQuery(query);
		List<FiltroBibliotecaPlanoLeisRegulamentacaoDTO> lista = typedQuery.getResultList();

		return lista;
	}
	public List<Biblioteca> carregarIdsOrdenados() { 
		return repository.carregarIdBibliotecas();
	}
	
	public List<FiltroBibliotecasDTO> buscarBibliotecasFiltrado(Long idAreaInteresse, List<String> nomeModulo, Long idEixo, Long idOds, Long idMetasOds, Long idIndicador, Long idCidade,
			Long idProvinciaEstado, Long idPais, String palavraChave) {
		
		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<FiltroBibliotecasDTO> query = cb.createQuery(FiltroBibliotecasDTO.class);
		
		Root<Biblioteca> biblioteca = query.from(Biblioteca.class);
		
		Join<Biblioteca, AreaInteresse> joinAreasInteresse = biblioteca.join("areasInteresse", JoinType.LEFT);
		Join<Biblioteca, Eixo> joinEixo = biblioteca.join("eixos", JoinType.LEFT);
		Join<Biblioteca, Indicador> joinIndicador = biblioteca.join("indicadores", JoinType.LEFT);
		Join<Biblioteca, ObjetivoDesenvolvimentoSustentavel> joinOds = biblioteca.join("ods", JoinType.LEFT);
		Join<Biblioteca, MetaObjetivoDesenvolvimentoSustentavel> joinMetaOds = biblioteca.join("meta", JoinType.LEFT);
		Join<Biblioteca, Pais> joinPais = biblioteca.join("paisPublicacao", JoinType.LEFT);
		Join<Biblioteca, Cidade> joinCidade = biblioteca.join("cidade",JoinType.LEFT);
		Join<Biblioteca, ProvinciaEstado> joinProvinciaEstado = biblioteca.join("estado", JoinType.LEFT);
		
		query.multiselect(biblioteca.get("id")).distinct(true);
		
		List<javax.persistence.criteria.Predicate> predicateList = new ArrayList<>();
		
		if (idAreaInteresse != null) {
			In<Long> inClause = cb.in(joinAreasInteresse.get("id"));
			inClause.value(idAreaInteresse);
			predicateList.add(inClause);
		}
		
		if (nomeModulo.size() > 0) { 
			Path<String> se = biblioteca.join("modulo");
			for (int i = 0; i < nomeModulo.size(); i++) {
				String set = nomeModulo.get(i).toLowerCase();
				predicateList.add(cb.like(cb.lower(se), "%" + set + "%"));
			}
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
		
		if (idPais != null) {
			Path<Long> paisBiblioteca = joinPais.get("id");
			predicateList.add(cb.equal(paisBiblioteca, idPais));
		}
		
		if (idProvinciaEstado != null) {
			Path<Long> estadoBiblioteca = joinProvinciaEstado.get("id");
			predicateList.add(cb.equal(estadoBiblioteca, idProvinciaEstado));
		}
		
		if (idCidade != null) {
			Path<Long> cidadeBiblioteca = joinCidade.get("id");
			predicateList.add(cb.equal(cidadeBiblioteca, idCidade));
		}
		
		if (palavraChave != null && !palavraChave.equals("")) {
			Path<String> autor = biblioteca.get("autor");
			Path<String> idioma = biblioteca.get("idioma");
			Path<String> instituicao = biblioteca.get("instituicao");
			Path<String> palavraChaveModel = biblioteca.get("palavraChave");
			Path<String> descricao = biblioteca.get("descricao");
			Path<String> subtitulo = biblioteca.get("subtitulo");
			Path<String> tipoMaterial = biblioteca.get("tipoMaterial");
			Path<String> tituloPublicacao = biblioteca.get("tituloPublicacao");
			Path<String> localExibicao = biblioteca.get("localExibicao");
			
			Predicate predicateForAutor = cb.like(cb.lower(autor), "%" + palavraChave.toLowerCase() + "%");
			Predicate predicateForIdioma = cb.like(cb.lower(idioma), "%" + palavraChave.toLowerCase() + "%");
			Predicate predicateForInstituicao = cb.like(cb.lower(instituicao), "%" + palavraChave.toLowerCase() + "%");
			Predicate predicateForPalavraChaveModel = cb.like(cb.lower(palavraChaveModel), "%" + palavraChave.toLowerCase() + "%");
			Predicate predicateForDescricao = cb.like(cb.lower(descricao), "%" + palavraChave.toLowerCase() + "%");
			Predicate predicateForSubtitulo = cb.like(cb.lower(subtitulo), "%" + palavraChave.toLowerCase() + "%");
			Predicate predicateForTipoMaterial = cb.like(cb.lower(tipoMaterial), "%" + palavraChave.toLowerCase() + "%");
			Predicate predicateForTituloPublicacao = cb.like(cb.lower(tituloPublicacao), "%" + palavraChave.toLowerCase() + "%");
			Predicate predicateForLocalExibicao = cb.like(cb.lower(localExibicao), "%" + palavraChave.toLowerCase() + "%");
			predicateList.add(cb.or(predicateForAutor,  predicateForIdioma, predicateForInstituicao, predicateForPalavraChaveModel,
					predicateForDescricao, predicateForSubtitulo, predicateForTipoMaterial, predicateForTituloPublicacao,
					predicateForLocalExibicao));
		}
		
		javax.persistence.criteria.Predicate[] predicates = new javax.persistence.criteria.Predicate[predicateList.size()];
		predicateList.toArray(predicates);
		query.where(predicates);
		
		TypedQuery<FiltroBibliotecasDTO> typedQuery = em.createQuery(query);
		List<FiltroBibliotecasDTO> lista = typedQuery.getResultList();

		return lista;
	}

	public List<Arquivo> buscarArquivoPublicacao(Long idBiblioteca) {
		return repository.buscarArquivoPublicacao(idBiblioteca);
	}
	
	public List<Biblioteca> buscarBibliotecasPorIds(List<Long> ids) {
		return repository.buscarBibliotecasPorIds(ids);
	}

}
