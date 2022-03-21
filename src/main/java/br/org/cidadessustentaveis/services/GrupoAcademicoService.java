
package br.org.cidadessustentaveis.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.org.cidadessustentaveis.dto.EmpresaDTO;
import br.org.cidadessustentaveis.dto.FiltroGruposAcademicosDTO;
import br.org.cidadessustentaveis.dto.GrupoAcademicoCardDTO;
import br.org.cidadessustentaveis.dto.GrupoAcademicoComboDTO;
import br.org.cidadessustentaveis.dto.GrupoAcademicoDTO;
import br.org.cidadessustentaveis.dto.GrupoAcademicoDetalheDTO;
import br.org.cidadessustentaveis.dto.GrupoAcademicoPainelDTO;
import br.org.cidadessustentaveis.model.administracao.AreaInteresse;
import br.org.cidadessustentaveis.model.administracao.Cidade;
import br.org.cidadessustentaveis.model.administracao.Eixo;
import br.org.cidadessustentaveis.model.administracao.ObjetivoDesenvolvimentoSustentavel;
import br.org.cidadessustentaveis.model.administracao.Pais;
import br.org.cidadessustentaveis.model.administracao.ProvinciaEstado;
import br.org.cidadessustentaveis.model.administracao.Usuario;
import br.org.cidadessustentaveis.model.biblioteca.Biblioteca;
import br.org.cidadessustentaveis.model.contribuicoesAcademicas.GrupoAcademico;
import br.org.cidadessustentaveis.repository.GrupoAcademicoRepository;
import br.org.cidadessustentaveis.services.exceptions.ObjectNotFoundException;
import br.org.cidadessustentaveis.util.UsuarioContextUtil;

@Service
public class GrupoAcademicoService {

	 @Autowired
	 private GrupoAcademicoRepository repository;
	 
	 @Autowired
	 private ObjetivoDesenvolvimentoSustentavelService odsService;
	 
	 @Autowired
	 private AreaInteresseService areaInteresseService;
	 
	 @Autowired
	 private EixoService eixoService;
	 
	 @Autowired
	 private PaisService paisService;
	 
	 @Autowired
	 private ProvinciaEstadoService provinciaEstadoService;
	 
	 @Autowired
	 private CidadeService cidadeService;
	 
	 @Autowired
	 private UsuarioService usuarioService;
	 
	 @Autowired
	 private EntityManager em;
	 
	 @Autowired
	 private BibliotecaService bibliotecaService;
	 
	 @Autowired
	 private UsuarioContextUtil usuarioContextUtil;
	 
	 public GrupoAcademico buscarPorId(Long id) {
		Optional<GrupoAcademico> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException("Grupo Acadmêmico não encontrado!"));
	}
		
	public GrupoAcademicoDTO buscarGrupoAcademicoPorId(Long id) throws Exception {
			GrupoAcademico grupoRef = buscarPorId(id);
			if (grupoAcademicoEhDoUsuario(grupoRef) || usuarioLogadoEhAdmin()) {				
				return new GrupoAcademicoDTO(grupoRef);
			}
			else {				
				throw new Exception("Você não tem permissão para acessar este recurso.");
			}
	}
	
	
	public List<GrupoAcademicoDTO> filtrarGruposAcademicosToList(String nomeGrupo) {
		return repository.findByNomeGrupo(nomeGrupo);
	}
	
	public EmpresaDTO buscarInstituicaoPorId(Long id) {
		GrupoAcademico grupoRef = buscarPorId(id);
	return new EmpresaDTO(grupoRef);
}
	
	public List<GrupoAcademicoDTO> buscarGruposAcademicosToList() throws Exception {
		if (usuarioLogadoEhAdmin()) {			
			return repository.buscarGruposAcademicosToListAdmin();
		}
		else {
			return repository.buscarGruposAcademicosToListByUsuarioId(usuarioLogado().getId());
		}
	}
	
	public boolean grupoAcademicoEhDoUsuario(GrupoAcademico grupoAcademico) throws Exception {
		Usuario usuario = usuarioLogado();
		if (usuario.getId().equals(grupoAcademico.getUsuario().getId())) {
			return true;
		}
		else {
			return false;
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
	
	public GrupoAcademico inserir(GrupoAcademicoDTO grupoAcademicoDTO) {
		GrupoAcademico grupoAcademico = grupoAcademicoDTO.toEntityInsert(grupoAcademicoDTO);
		
		grupoAcademico.setDataCadastro(LocalDate.now());
		grupoAcademico.setPais(grupoAcademicoDTO.getPais() != null ? paisService.buscarPorId(grupoAcademicoDTO.getPais()) : null);
		grupoAcademico.setCidade(grupoAcademicoDTO.getCidade() != null ? cidadeService.buscarPorId(grupoAcademicoDTO.getCidade()) : null);
		grupoAcademico.setUsuario(grupoAcademicoDTO.getUsuario() != null ? usuarioService.buscarPorId(grupoAcademicoDTO.getUsuario()) : null);
		grupoAcademico.setEstado(grupoAcademicoDTO.getEstado() != null ? provinciaEstadoService.buscarPorId(grupoAcademicoDTO.getEstado()) : null);
		grupoAcademico.setOds(grupoAcademicoDTO.getOds() != null && !grupoAcademicoDTO.getOds().isEmpty() ? odsService.buscarPorIds(grupoAcademicoDTO.getOds()) : null);
		grupoAcademico.setEixos(grupoAcademicoDTO.getEixos() != null && !grupoAcademicoDTO.getEixos().isEmpty()? eixoService.buscarEixosPorIds(grupoAcademicoDTO.getEixos()) : null);
		grupoAcademico.setAreasInteresse(grupoAcademicoDTO.getAreasInteresse() != null && !grupoAcademicoDTO.getAreasInteresse().isEmpty()? areaInteresseService.buscarAreasPorIds(grupoAcademicoDTO.getAreasInteresse()) : null);
		grupoAcademico.setPaisApl(grupoAcademicoDTO.getPaisApl() != null ? paisService.buscarPorId(grupoAcademicoDTO.getPaisApl()) : null);
		grupoAcademico.setEstadoApl(grupoAcademicoDTO.getEstadoApl() != null ? provinciaEstadoService.buscarPorId(grupoAcademicoDTO.getEstadoApl()) : null);
		if (grupoAcademicoDTO.getCidadesApl() != null) {
			List<Long> idsCidades = grupoAcademicoDTO.getCidadesApl();
			List<Cidade> cidadesApl = new ArrayList<Cidade>();
			for (int i = 0; i < idsCidades.size(); i++) {
				cidadesApl.add(cidadeService.buscarPorId(idsCidades.get(i)));
			}
			grupoAcademico.setCidadesApl(cidadesApl);
		}
		grupoAcademico.setBibliotecas(grupoAcademicoDTO.getBibliotecas() != null && !grupoAcademicoDTO.getBibliotecas().isEmpty() ? bibliotecaService.buscarBibliotecasPorIds(grupoAcademicoDTO.getBibliotecas()): null);
		
		repository.save(grupoAcademico);
		return grupoAcademico;
	}
	
	public GrupoAcademico alterar(GrupoAcademicoDTO grupoAcademicoDTO) throws Exception {
		if(grupoAcademicoDTO.getId() == null) {
			throw new Exception("Campo id divergente.");
		}
		GrupoAcademico grupoAcademico = grupoAcademicoDTO.toEntityUpdate(buscarPorId(grupoAcademicoDTO.getId()));
		
		grupoAcademico.setPais(grupoAcademicoDTO.getPais() != null ? paisService.buscarPorId(grupoAcademicoDTO.getPais()) : null);
		grupoAcademico.setCidade(grupoAcademicoDTO.getCidade() != null ? cidadeService.buscarPorId(grupoAcademicoDTO.getCidade()) : null);
		grupoAcademico.setEstado(grupoAcademicoDTO.getEstado() != null ? provinciaEstadoService.buscarPorId(grupoAcademicoDTO.getEstado()) : null);
		grupoAcademico.setOds(grupoAcademicoDTO.getOds() != null && !grupoAcademicoDTO.getOds().isEmpty() ? odsService.buscarPorIds(grupoAcademicoDTO.getOds()) : null);
		grupoAcademico.setEixos(grupoAcademicoDTO.getEixos() != null && !grupoAcademicoDTO.getEixos().isEmpty()? eixoService.buscarEixosPorIds(grupoAcademicoDTO.getEixos()) : null);
		grupoAcademico.setAreasInteresse(grupoAcademicoDTO.getAreasInteresse() != null && !grupoAcademicoDTO.getAreasInteresse().isEmpty()? areaInteresseService.buscarAreasPorIds(grupoAcademicoDTO.getAreasInteresse()) : null);
		grupoAcademico.setPaisApl(grupoAcademicoDTO.getPaisApl() != null ? paisService.buscarPorId(grupoAcademicoDTO.getPaisApl()) : null);
		grupoAcademico.setEstadoApl(grupoAcademicoDTO.getEstadoApl() != null ? provinciaEstadoService.buscarPorId(grupoAcademicoDTO.getEstadoApl()) : null);
		if (grupoAcademicoDTO.getCidadesApl() != null) {
			List<Long> idsCidades = grupoAcademicoDTO.getCidadesApl();
			List<Cidade> cidadesApl = new ArrayList<Cidade>();
			for (int i = 0; i < idsCidades.size(); i++) {
				cidadesApl.add(cidadeService.buscarPorId(idsCidades.get(i)));
			}
			grupoAcademico.setCidadesApl(cidadesApl);
		}
		grupoAcademico.setBibliotecas(grupoAcademicoDTO.getBibliotecas() != null && !grupoAcademicoDTO.getBibliotecas().isEmpty() ? bibliotecaService.buscarBibliotecasPorIds(grupoAcademicoDTO.getBibliotecas()): null);
		repository.save(grupoAcademico);
		return grupoAcademico;
	}
	
	public void deletar(Long id) {
		repository.deleteById(id);
	}
	
	public GrupoAcademicoCardDTO buscarGrupoAcademicoPorIdCard(Long id) {
		return repository.buscarGrupoAcademicoPorIdCard(id);
	}
	
	public GrupoAcademicoPainelDTO buscarGrupoAcademicoPorIdPainel(Long id) {
		return repository.buscarGrupoAcademicoPorIdPainel(id);
	}
	
	
	public List<FiltroGruposAcademicosDTO> buscarGruposAcademicosFiltrados(
			String tipoCadastro,
			Long idAreaInteresse,
			Long idEixo,
			Long idOds,
			Long idCidade,
			Long idProvinciaEstado,
			Long idPais,
			String palavraChave,
			String vinculo,
			String nomeGrupo,
			List<String> setoresApl,
			Long cidadesApl,
			Long receitaAnual,
			boolean participaApl,
			boolean associadaEthos, 
			String setorEconomico,
			Long quantidadeFuncionarios,
			boolean atuaProjetoSustentabilidade,
			Long quantidadeAlunosMin,
			Long quantidadeAlunosMax,
			boolean possuiExperiencias,
			String tipoInstituicaoAcademia) {

		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<FiltroGruposAcademicosDTO> query = cb.createQuery(FiltroGruposAcademicosDTO.class);
		
		Root<GrupoAcademico> grupoAcademico = query.from(GrupoAcademico.class);
		
		Join<GrupoAcademico, AreaInteresse> joinAreasInteresse = grupoAcademico.join("areasInteresse", JoinType.LEFT);
		Join<GrupoAcademico, Eixo> joinEixo = grupoAcademico.join("eixos", JoinType.LEFT);
		Join<GrupoAcademico, ObjetivoDesenvolvimentoSustentavel> joinOds = grupoAcademico.join("ods", JoinType.LEFT);
		Join<GrupoAcademico, Pais> joinPais = grupoAcademico.join("pais", JoinType.LEFT);
		Join<GrupoAcademico, Cidade> joinCidade = grupoAcademico.join("cidade",JoinType.LEFT);
		Join<GrupoAcademico, Long> joinCidadesApl = grupoAcademico.join("cidadesApl", JoinType.LEFT);
		Join<GrupoAcademico, ProvinciaEstado> joinProvinciaEstado = grupoAcademico.join("estado", JoinType.LEFT);

		query.multiselect(grupoAcademico.get("id"), grupoAcademico.get("latitude"), grupoAcademico.get("longitude"), grupoAcademico.get("nomeGrupo")).distinct(true);
		
		List<javax.persistence.criteria.Predicate> predicateList = new ArrayList<>();
		
		if (idAreaInteresse != null) {
			Path<Long> area = joinAreasInteresse.get("id");
			predicateList.add(cb.equal(area, idAreaInteresse));
		}
		
		if (tipoCadastro != null && !tipoCadastro.trim().equals("")) {
			Path<String> ng = grupoAcademico.get("tipoCadastro");
			if (tipoCadastro.contains("Grupo Acadêmico")) {
				Predicate predicateForTipoCadastro = cb.like(cb.lower(ng), "%grupo acadêmico%");
				predicateList.add(predicateForTipoCadastro);
			}
			else {
				Predicate predicateEmpresa = cb.like(cb.lower(ng), "%empresa%");
				Predicate predicateFundacao = cb.like(cb.lower(ng), "%fundação empresarial%");
				Predicate predicateForTipoCadastro = cb.or(predicateEmpresa,predicateFundacao);
				predicateList.add(predicateForTipoCadastro);
			}
		}
		
		if (nomeGrupo != null && !nomeGrupo.trim().equals("")) {
			Path<String> ng = grupoAcademico.get("nomeGrupo");
			String nome = nomeGrupo.substring(0, nomeGrupo.length() - 4).toLowerCase();
			Predicate predicateForNomeGrupo = cb.like(cb.lower(ng), "%" + nome + "%");
			predicateList.add(predicateForNomeGrupo);
		}
		if (vinculo != null && !vinculo.trim().equals("")) {
			Path<String> vinc = grupoAcademico.get("vinculo");
			Predicate predicateForVinculo = cb.like(cb.lower(vinc), "%" + vinculo.substring(0, vinculo.length() - 4).toLowerCase() + "%");
			predicateList.add(predicateForVinculo);
		}
		
		if (receitaAnual != null) {
			Path<String> receita = grupoAcademico.get("receitaAnual");
			Predicate predicateForReceitaAnual = cb.equal(receita, receitaAnual);
			predicateList.add(predicateForReceitaAnual);
		}
		if (participaApl == true) {
			Path<String> apl = grupoAcademico.get("participaApl");
			Predicate predicateForParticipaApl = cb.equal(apl, participaApl);
			predicateList.add(predicateForParticipaApl);
		}
		
		if (possuiExperiencias == true) {
			Path<String> possuiExperienciasPrefeitura = grupoAcademico.get("possuiExperiencias");
			Predicate predicateForPossuiExperiencias = cb.equal(possuiExperienciasPrefeitura, possuiExperiencias);
			predicateList.add(predicateForPossuiExperiencias);
		}
		
		if (associadaEthos == true) {			
			Path<String> ethos = grupoAcademico.get("associadaEthos");
			Predicate predicateForAssociadaEthos = cb.equal(ethos, associadaEthos);
			predicateList.add(predicateForAssociadaEthos);
		}
		
		if (setorEconomico != null && !setorEconomico.trim().equals("")) {
			Path<String> se = grupoAcademico.get("setorEconomico");
			String set = setorEconomico.substring(0, setorEconomico.length() - 4).toLowerCase();
			Predicate predicateForNomeGrupo = cb.like(cb.lower(se), "%" + set + "%");
			predicateList.add(predicateForNomeGrupo);
		}
		if (quantidadeFuncionarios != null) {
			Path<Long> qntdFuncionarios = grupoAcademico.get("quantidadeFuncionarios");
			Predicate predicateForQuantidadeFuncionarios = cb.equal(qntdFuncionarios, quantidadeFuncionarios);
			predicateList.add(predicateForQuantidadeFuncionarios);
		}
		
		if (quantidadeAlunosMin != null) {
			Path<Long> quantidadeAlunosMinPath = grupoAcademico.get("quantidadeAlunos");
			Predicate predicateForQuantidadeAlunosMin = cb.greaterThanOrEqualTo(quantidadeAlunosMinPath, quantidadeAlunosMin);
			predicateList.add(predicateForQuantidadeAlunosMin);
		}
		
		if (quantidadeAlunosMax != null) {
			Path<Long> quantidadeAlunosMaxPath = grupoAcademico.get("quantidadeAlunos");
			Predicate predicateForQuantidadeAlunosMax = cb.lessThanOrEqualTo(quantidadeAlunosMaxPath, quantidadeAlunosMax);
			predicateList.add(predicateForQuantidadeAlunosMax);
		}
		
		if(atuaProjetoSustentabilidade == true) {
			Path<String> atuaPS = grupoAcademico.get("atuaProjetoSustentabilidade");
			Predicate predicateForAtuaProjetoSustentabilidade = cb.equal(atuaPS, atuaProjetoSustentabilidade);
			predicateList.add(predicateForAtuaProjetoSustentabilidade);
		}

		if (cidadesApl != null) {
			Path<Long> cidadeApl = joinCidadesApl.get("id");
			predicateList.add(cb.equal(cidadeApl, cidadesApl));
		}
		
		if (!setoresApl.get(0).isEmpty()) { 
			Path<String> se = grupoAcademico.join("setoresApl");
			for (int i = 0; i < setoresApl.size(); i++) {
				String set = setoresApl.get(i).toLowerCase();
				predicateList.add(cb.like(cb.lower(se), "%" + set + "%"));
			}
		}
		
		if (idEixo != null) {
			Path<Long> eixo = joinEixo.get("id");
			predicateList.add(cb.equal(eixo, idEixo));
		}
		
		if (idOds != null) {
			Path<Long> ods = joinOds.get("id");
			predicateList.add(cb.equal(ods, idOds));
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
		if (tipoInstituicaoAcademia != null && !tipoInstituicaoAcademia.trim().equals("")) {
			Path<String> tipo = grupoAcademico.get("tipo");
			String tipoAcademia = tipoInstituicaoAcademia.substring(0, tipoInstituicaoAcademia.length() - 4).toLowerCase();
			Predicate predicateTipoInstituicaoAcademia = cb.like(cb.lower(tipo), "%" + tipoAcademia + "%");
			predicateList.add(predicateTipoInstituicaoAcademia);
		}
		
		if (palavraChave != null && palavraChave.length() > 4) {
			String palavra = palavraChave.substring(0, palavraChave.length() - 4).toLowerCase();
			Path<String> continente = grupoAcademico.get("continente");
			Path<String> tipo = grupoAcademico.get("tipo");
			Path<String> nomeG = grupoAcademico.get("nomeGrupo");
			Path<String> paginaOnline = grupoAcademico.get("paginaOnline");
			Path<String> nomeContato = grupoAcademico.get("nomeContato");
			Path<String> emailContato = grupoAcademico.get("emailContato");
			Path<String> telefoneContato = grupoAcademico.get("telefoneContato");
			Path<String> emailInstitucional = grupoAcademico.get("emailInstitucional");
			Path<String> telefoneInstitucional = grupoAcademico.get("telefoneInstitucional");
			Path<String> linkBaseDados = grupoAcademico.get("linkBaseDados");
			Path<String> observacoes = grupoAcademico.get("observacoes");
			Path<String> descricaoInstituicao = grupoAcademico.get("descricaoInstituicao");
			Path<String> experienciasDesenvolvidas = grupoAcademico.get("experienciasDesenvolvidas");
			
			Predicate predicateForContinente = cb.like(cb.lower(continente), "%" + palavra.toLowerCase() + "%");
			Predicate predicateForTipo = cb.like(cb.lower(tipo), "%" + palavra.toLowerCase() + "%");
			Predicate predicateForNomeGrupo = cb.like(cb.lower(nomeG), "%" + palavra.toLowerCase() + "%");
			Predicate predicateForPaginaOnline = cb.like(cb.lower(paginaOnline), "%" + palavra.toLowerCase() + "%");
			Predicate predicateForNomeContato = cb.like(cb.lower(nomeContato), "%" + palavra.toLowerCase() + "%");
			Predicate predicateForEmailContato = cb.like(cb.lower(emailContato), "%" + palavra.toLowerCase() + "%");
			Predicate predicateForTelefoneContato= cb.like(cb.lower(telefoneContato), "%" + palavra.toLowerCase() + "%");
			Predicate predicateForEmailInstitucional = cb.like(cb.lower(emailInstitucional), "%" + palavra.toLowerCase() + "%");
			Predicate predicateForTelefoneInstitucional = cb.like(cb.lower(telefoneInstitucional), "%" + palavra.toLowerCase() + "%");
			Predicate predicateForLinkBaseDados = cb.like(cb.lower(linkBaseDados), "%" + palavra.toLowerCase() + "%");
			Predicate predicateForObservacoes = cb.like(cb.lower(observacoes), "%" + palavra.toLowerCase() + "%");
			Predicate predicateForDescricaoInstituicao = cb.like(cb.lower(descricaoInstituicao), "%" + palavra.toLowerCase() + "%");
			Predicate predicateForExperienciasDesenvolvidas = cb.like(cb.lower(experienciasDesenvolvidas), "%" + palavra.toLowerCase() + "%");

			predicateList.add(cb.or(predicateForContinente, predicateForTipo, predicateForNomeGrupo, predicateForPaginaOnline, predicateForNomeContato,
					predicateForEmailContato, predicateForTelefoneContato, predicateForEmailInstitucional, predicateForTelefoneInstitucional,
					predicateForLinkBaseDados, predicateForObservacoes, predicateForDescricaoInstituicao, predicateForExperienciasDesenvolvidas));
		}
		
		javax.persistence.criteria.Predicate[] predicates = new javax.persistence.criteria.Predicate[predicateList.size()];
		predicateList.toArray(predicates);
		query.where(predicates);
		
		TypedQuery<FiltroGruposAcademicosDTO> typedQuery = em.createQuery(query);
		List<FiltroGruposAcademicosDTO> lista = typedQuery.getResultList();

		return lista;
	}
	
	public List<GrupoAcademicoDTO> buscarGruposAcademicosMapa() {
		return repository.buscarGruposAcademicosMapa();
	}
	
	public List<Eixo> findEixoById(Long idGrupo){
		return repository.findEixoById(idGrupo);
	}
	
	public List<AreaInteresse> findAreaInteresseById(Long idGrupo){
		return repository.findAreaInteresseById(idGrupo);
	}
	
	public List<ObjetivoDesenvolvimentoSustentavel> findODSById(Long idGrupo){
		return repository.findODSById(idGrupo);
	}
	
	public List<Biblioteca> findBibliotecaById(Long idGrupo){
		return repository.findBibliotecaById(idGrupo);
	}
	
	public List<GrupoAcademicoComboDTO> buscarComboGruposAcademicos() {
		return repository.buscarComboGrupoAcademico();
	}
	
	public GrupoAcademicoDetalheDTO buscarGrupoAcademicoPorIdDetalhesDTO(Long id) {
		return repository.buscarGrupoAcademicoPorIdDetalhesDTO(id);
	}
	
	public List<ObjetivoDesenvolvimentoSustentavel> buscarOdsDoGrupoAcademicoPorId(Long id){
		return repository.buscarOdsDoGrupoAcademicoPorId(id);
	}
	
	public GrupoAcademico inserirLinhaPlanilhaExcell(GrupoAcademico grupoAcademico) {
		repository.save(grupoAcademico);
		return grupoAcademico;
	}
	

}
