package br.org.cidadessustentaveis.services;

import java.io.IOException;
import java.text.Normalizer;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.wololo.geojson.Feature;

import br.org.cidadessustentaveis.dto.BoaPraticaDTO;
import br.org.cidadessustentaveis.dto.BoaPraticaDetalheDTO;
import br.org.cidadessustentaveis.dto.BoaPraticaFiltradaIntegracaoDTO;
import br.org.cidadessustentaveis.dto.BoaPraticaItemDTO;
import br.org.cidadessustentaveis.dto.BoaPraticaResumidoToListDTO;
import br.org.cidadessustentaveis.dto.BoasPraticasFiltradasDTO;
import br.org.cidadessustentaveis.dto.CidadeComBoasPraticasDTO;
import br.org.cidadessustentaveis.dto.CidadeDTO;
import br.org.cidadessustentaveis.dto.CombosCidadesComBoasPraticasDTO;
import br.org.cidadessustentaveis.dto.FiltroCidadesComBoasPraticas;
import br.org.cidadessustentaveis.dto.ImagemBoaPraticaDTO;
import br.org.cidadessustentaveis.dto.ItemComboDTO;
import br.org.cidadessustentaveis.model.administracao.Cidade;
import br.org.cidadessustentaveis.model.administracao.Eixo;
import br.org.cidadessustentaveis.model.administracao.MetaObjetivoDesenvolvimentoSustentavel;
import br.org.cidadessustentaveis.model.administracao.ObjetivoDesenvolvimentoSustentavel;
import br.org.cidadessustentaveis.model.administracao.Pais;
import br.org.cidadessustentaveis.model.administracao.Prefeitura;
import br.org.cidadessustentaveis.model.administracao.ProvinciaEstado;
import br.org.cidadessustentaveis.model.administracao.Usuario;
import br.org.cidadessustentaveis.model.boaspraticas.BoaPratica;
import br.org.cidadessustentaveis.model.boaspraticas.FonteReferenciaBoaPratica;
import br.org.cidadessustentaveis.model.boaspraticas.ImagemBoaPratica;
import br.org.cidadessustentaveis.model.boaspraticas.SolucaoBoaPratica;
import br.org.cidadessustentaveis.model.enums.TipoBoaPratica;
import br.org.cidadessustentaveis.model.enums.TipoImagemBoaPratica;
import br.org.cidadessustentaveis.model.indicadores.Indicador;
import br.org.cidadessustentaveis.repository.BoaPraticaRepository;
import br.org.cidadessustentaveis.repository.FonteReferenciaBoaPraticaRepository;
import br.org.cidadessustentaveis.repository.ImagemBoaPraticaRepository;
import br.org.cidadessustentaveis.services.exceptions.BusinessLogicErrorException;
import br.org.cidadessustentaveis.services.exceptions.DataIntegrityException;
import br.org.cidadessustentaveis.services.exceptions.ObjectNotFoundException;
import br.org.cidadessustentaveis.util.ImageUtils;

@Service
public class BoaPraticaService {
	
	@Autowired
	private BoaPraticaRepository repository;
	
	@Autowired
	private FonteReferenciaBoaPraticaRepository fonteReferenciaBoaPraticaRepository;
	
	@Autowired
	private ImagemBoaPraticaRepository imagemBoaPraticaRepository;
	
	@Autowired
	private ProvinciaEstadoService provinciaEstadoService;
	
	@Autowired
	private CidadeService cidadeService;
	
	@Autowired
	private ObjetivoDesenvolvimentoSustentavelService odsService;
	
	@Autowired
	private EixoService eixoService;
	
	@Autowired
	private MetaObjetivoDesenvolvimentoSustentavelService metaObjetivoDesenvolvimentoSustentavelService;
	
	@Autowired
	private IndicadorService indicadorService;
	
	@Autowired
	private PrefeituraService prefeituraService;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private MetaObjetivoDesenvolvimentoSustentavelService metaOdsService;
	
	@Autowired
	private ShapeFileService shapeFileService;
	
	@Autowired
	EntityManager em;
	
	@Autowired
	private HistoricoAcessoBoaPraticaService historicoService;
	
	@Autowired
	private SolucaoBoaPraticaService solucaoBoaPraticaService;
	
	public List<BoaPratica> buscar(){
		return repository.findAllByOrderByIdDesc();
	}
	
	public BoaPratica buscarPorId(Long id) {
		Optional<BoaPratica> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException("Boa Prática não encontrada!"));
	}
	
	public FonteReferenciaBoaPratica buscarFonteReferenciaBoaPraticaPorId(Long id) {
		Optional<FonteReferenciaBoaPratica> obj = fonteReferenciaBoaPraticaRepository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException("Fonte de Referência não encontrada!"));
	}
	
	public ImagemBoaPratica buscarImagemBoaPraticaPorId(Long id) {
		Optional<ImagemBoaPratica> obj = imagemBoaPraticaRepository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException("Imagem não encontrada!"));
	}
	
	public List<BoaPratica> buscarBoasPraticasPCS(){
		return repository.buscarBoasPraticasPorTipo(TipoBoaPratica.PCS.getTipo());
	}

	public List<BoaPraticaResumidoToListDTO> buscarBoasPraticasGeral(){
		return repository.buscarBoasPraticasGeral();
	}

	public List<BoaPraticaResumidoToListDTO> buscarBoasPraticasPorPrefeitura(Long idPrefeitura){
		return repository.findByPrefeituraId(idPrefeitura);
	}
	
	public BoaPraticaDTO buscarBoaPrticaParaEditar(Long id) {
		BoaPratica boaPraticaRef = buscarPorId(id);
		BoaPraticaDTO boaPraticaDto = new BoaPraticaDTO(boaPraticaRef);

		List<ImagemBoaPraticaDTO> imagensGaleria = new ArrayList<>();
		for (ImagemBoaPratica imagem : boaPraticaRef.getImagens()) {
			if (TipoImagemBoaPratica.PRINCIPAL.getTipo().equals(imagem.getTipo())) {
				boaPraticaDto.setImagemPrincipal(new ImagemBoaPraticaDTO(imagem));
			}else if (TipoImagemBoaPratica.GALERIA.getTipo().equals(imagem.getTipo())) {
				imagensGaleria.add(new ImagemBoaPraticaDTO(imagem));
			}
		}
		boaPraticaDto.setGaleriaDeImagens(imagensGaleria.isEmpty()?null:imagensGaleria);
		
		return boaPraticaDto;
	}
	
	public BoaPratica inserir(BoaPraticaDTO boaPraticaDTO) throws IOException {
		
		String user = SecurityContextHolder.getContext().getAuthentication().getName();
		Usuario usuario  = usuarioService.buscarPorEmailCredencial(user);
		boaPraticaDTO.setNomeResponsavel(usuario.getNome());
		boaPraticaDTO.setDataPublicacao(LocalDate.now());

		BoaPratica boaPraticaToInsert = boaPraticaDTO.toEntityInsert();

		boaPraticaDTO.getImagemPrincipal().setConteudo(
												ImageUtils.compressBase64Image(boaPraticaDTO.getImagemPrincipal()
																									.getConteudo()));
		
		if(!boaPraticaDTO.getSolucoes().isEmpty()) {
			List<SolucaoBoaPratica> solucoes = new ArrayList<>();
			boaPraticaDTO.getSolucoes().forEach(solucao -> {
				solucoes.add(solucao.toEntityInsert(boaPraticaToInsert));
			});
			boaPraticaToInsert.setSolucoes(solucoes);
		}

		for(ImagemBoaPraticaDTO imagem : boaPraticaDTO.getGaleriaDeImagens()) {
		    imagem.setConteudo(ImageUtils.compressBase64Image(imagem.getConteudo()));
        }
		if(boaPraticaDTO.getIdPrefeituraCadastro() != null) {
			Prefeitura prefeituraCadastro = prefeituraService.buscarPorId(boaPraticaDTO.getIdPrefeituraCadastro());
			boaPraticaToInsert.setPrefeitura(prefeituraCadastro);
			boaPraticaToInsert.setTipo(TipoBoaPratica.PREFEITURA.getTipo());
			return inserirBoaPraticaPrefeitura(boaPraticaToInsert, boaPraticaDTO);
		} else {
			boaPraticaToInsert.setTipo(TipoBoaPratica.PCS.getTipo());
			return inserirBoaPraticaPCS(boaPraticaToInsert, boaPraticaDTO);
		}
	}
	
	public BoaPratica inserirBoaPraticaPCS(BoaPratica boaPratica, BoaPraticaDTO boaPraticaDTO) {

		/*Recupera referência de ProvinciaEstado, Cidade e Eixo*/
		ProvinciaEstado provinciaEstadoRef = provinciaEstadoService.buscarPorId(boaPraticaDTO.getIdEstado());
		Cidade cidadeRef = cidadeService.buscarPorId(boaPraticaDTO.getIdMunicipio());
		Eixo eixoRef = eixoService.listarById(boaPraticaDTO.getIdEixo());
		boaPratica.setEstado(provinciaEstadoRef);
		boaPratica.setMunicipio(cidadeRef);
		boaPratica.setEixo(eixoRef);
		
		/*Recupera referência dos ODS*/
		List<ObjetivoDesenvolvimentoSustentavel> listaOdsRef = new ArrayList<>();
		for (Long idOds : boaPraticaDTO.getIdsOds()) {
			listaOdsRef.add(odsService.listarPorId(idOds));
		}
		boaPratica.setOds(listaOdsRef);
		
		/*Recupera referência das Metas ODS*/
		List<MetaObjetivoDesenvolvimentoSustentavel> listaMetasRef = new ArrayList<>();
		for (Long idMetaDto : boaPraticaDTO.getIdsMetasOds()) {
			listaMetasRef.add(metaObjetivoDesenvolvimentoSustentavelService.find(idMetaDto));
		}
		boaPratica.setMetasOds(listaMetasRef);
		
		/*Recupera referência dos Indicadores*/
		List<Indicador> listaIndicadoresRef = new ArrayList<>();
		for (Long idIndicador : boaPraticaDTO.getIdsIndicadores()) {
			listaIndicadoresRef.add(indicadorService.listarById(idIndicador));
		}
		boaPratica.setIndicadores(listaIndicadoresRef);
		
//		boaPratica.getFontesReferencia().forEach(obj -> obj.setBoaPratica(boaPratica));
		
		/*Prepara imagens*/
		List<ImagemBoaPratica> listaImagens = new ArrayList<>();
		if(boaPraticaDTO.getGaleriaDeImagens() != null) {
			for (ImagemBoaPraticaDTO imagem : boaPraticaDTO.getGaleriaDeImagens()) {
				ImagemBoaPratica img = imagem.toEntityInsert();
				img.setBoaPratica(boaPratica);
				listaImagens.add(img);
			}
		}
		
		if(boaPraticaDTO.getImagemPrincipal() != null) {
			ImagemBoaPratica img = boaPraticaDTO.getImagemPrincipal().toEntityInsert();
			img.setBoaPratica(boaPratica);
			listaImagens.add(img);
		}
		boaPratica.setImagens(listaImagens);
		
		/* Flag para exibir ou não o conteúdo na página inicial*/
		boaPratica.setPaginaInicial(boaPraticaDTO.getPaginaInicial());
		while(boaPraticaDTO.getIdPrefeituraCadastro() != null) {
			boaPratica.setPaginaInicial(false);
		}

		return repository.save(boaPratica);
	}
	
	public BoaPratica inserirBoaPraticaPrefeitura(BoaPratica boaPratica, BoaPraticaDTO boaPraticaDTO) {
		
		String user = SecurityContextHolder.getContext().getAuthentication().getName();
		Usuario usuario  = usuarioService.buscarPorEmailCredencial(user);
		
		if (usuario.getPrefeitura() == null) {
			throw new BusinessLogicErrorException("Usuário logado não está associado a nenhuma prefeitura");
		}
		
		/*Recupera referência de ProvinciaEstado, Cidade e Eixo*/
		Eixo eixoRef = eixoService.listarById(boaPraticaDTO.getIdEixo());
		boaPratica.setEstado(usuario.getPrefeitura().getCidade().getProvinciaEstado());
		boaPratica.setMunicipio(usuario.getPrefeitura().getCidade());
		boaPratica.setEixo(eixoRef);
		
		/*Recupera referência dos ODS*/
		List<ObjetivoDesenvolvimentoSustentavel> listaOdsRef = new ArrayList<>();
		for (Long idOds : boaPraticaDTO.getIdsOds()) {
			listaOdsRef.add(odsService.listarPorId(idOds));
		}
		boaPratica.setOds(listaOdsRef);
		
		/*Recupera referência das Metas ODS*/
		List<MetaObjetivoDesenvolvimentoSustentavel> listaMetasRef = new ArrayList<>();
		for (Long idMetaDto : boaPraticaDTO.getIdsMetasOds()) {
			listaMetasRef.add(metaObjetivoDesenvolvimentoSustentavelService.find(idMetaDto));
		}
		boaPratica.setMetasOds(listaMetasRef);
		
		/*Recupera referência dos Indicadores*/
		List<Indicador> listaIndicadoresRef = new ArrayList<>();
		for (Long idIndicador : boaPraticaDTO.getIdsIndicadores()) {
			listaIndicadoresRef.add(indicadorService.listarById(idIndicador));
		}
		boaPratica.setIndicadores(listaIndicadoresRef);
		
//		boaPratica.getFontesReferencia().forEach(obj -> obj.setBoaPratica(boaPratica));
		
		/*Prepara imagens*/
		List<ImagemBoaPratica> listaImagens = new ArrayList<>();
		for (ImagemBoaPraticaDTO imagem : boaPraticaDTO.getGaleriaDeImagens()) {
			ImagemBoaPratica img = imagem.toEntityInsert();
			img.setBoaPratica(boaPratica);
			listaImagens.add(img);
		}
		if(boaPraticaDTO.getImagemPrincipal() != null) {
			ImagemBoaPratica img = boaPraticaDTO.getImagemPrincipal().toEntityInsert();
			img.setBoaPratica(boaPratica);
			listaImagens.add(img);
		}
		boaPratica.setImagens(listaImagens);

		return repository.save(boaPratica);
	}

	public void deletar(Long id) {
		BoaPratica boaPraticaRef = buscarPorId(id);
		String user = SecurityContextHolder.getContext().getAuthentication().getName();
		Usuario usuario  = usuarioService.buscarPorEmailCredencial(user);
		
		if(TipoBoaPratica.PREFEITURA.getTipo().equals(boaPraticaRef.getTipo()) && (usuario.getPrefeitura() == null || !usuario.getPrefeitura().getId().equals(boaPraticaRef.getPrefeitura().getId()))) {

			throw new BusinessLogicErrorException("Usuário logado não possui permissão para excluir a Boa Prática");

		}else if (TipoBoaPratica.PCS.getTipo().equals(boaPraticaRef.getTipo()) && usuario.getPrefeitura() != null) {

			throw new BusinessLogicErrorException("Usuário logado não possui permissão para excluir a Boa Prática");

		}
		try {
			repository.deleteById(id);
		}catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("O registro está relacionado com outra entidade");
		}	
	}

	public BoaPratica alterar(Long id, BoaPraticaDTO boaPraticaDTO) throws IOException {
		BoaPratica boaPraticaRef = boaPraticaDTO.toEntityUpdate(buscarPorId(id));

		String user = SecurityContextHolder.getContext().getAuthentication().getName();
		Usuario usuario  = usuarioService.buscarPorEmailCredencial(user);
		boaPraticaDTO.setNomeResponsavel(usuario.getNome());
		
		/*Validações de acordo com tipo da boa prática*/
		if(TipoBoaPratica.PREFEITURA.getTipo().equals(boaPraticaRef.getTipo()) && (usuario.getPrefeitura() == null || !usuario.getPrefeitura().getCidade().getId().equals(boaPraticaDTO.getIdMunicipio()))) {

			throw new BusinessLogicErrorException("Usuário logado não possui permissão para editar a Boa Prática");

		}else if (TipoBoaPratica.PCS.getTipo().equals(boaPraticaRef.getTipo()) && usuario.getPrefeitura() != null) {

			throw new BusinessLogicErrorException("Usuário logado não possui permissão para editar a Boa Prática");

		}else if (TipoBoaPratica.PCS.getTipo().equals(boaPraticaRef.getTipo())) {
			ProvinciaEstado provinciaEstadoRef = provinciaEstadoService.buscarPorId(boaPraticaDTO.getIdEstado());
			Cidade cidadeRef = cidadeService.buscarPorId(boaPraticaDTO.getIdMunicipio());
			boaPraticaRef.setEstado(provinciaEstadoRef);
			boaPraticaRef.setMunicipio(cidadeRef);
			boaPraticaRef.setPaginaInicial(boaPraticaDTO.getPaginaInicial());
		}

		/*Recupera referência de Eixo*/
		Eixo eixoRef = eixoService.listarById(boaPraticaDTO.getIdEixo());
		boaPraticaRef.setEixo(eixoRef);

		/*Recupera referência dos ODS*/
		List<ObjetivoDesenvolvimentoSustentavel> listaOdsRef = new ArrayList<>();
		for (Long idOds : boaPraticaDTO.getIdsOds()) {
			listaOdsRef.add(odsService.listarPorId(idOds));
		}
		boaPraticaRef.setOds(listaOdsRef);

		/*Recupera referência das Metas ODS*/
		List<MetaObjetivoDesenvolvimentoSustentavel> listaMetasRef = new ArrayList<>();
		for (Long idMetaDto : boaPraticaDTO.getIdsMetasOds()) {
			listaMetasRef.add(metaObjetivoDesenvolvimentoSustentavelService.find(idMetaDto));
		}
		boaPraticaRef.setMetasOds(listaMetasRef);

		/*Recupera referência dos Indicadores*/
		List<Indicador> listaIndicadoresRef = new ArrayList<>();
		for (Long idIndicador : boaPraticaDTO.getIdsIndicadores()) {
			listaIndicadoresRef.add(indicadorService.listarById(idIndicador));
		}
		boaPraticaRef.setIndicadores(listaIndicadoresRef);
		
		/*Exclui lista de soluções boas praticas e adiciona a nova*/
		
		if(!boaPraticaDTO.getSolucoes().isEmpty()) {
			solucaoBoaPraticaService.deleteByIdBoaPratica(boaPraticaDTO.getId());
			List<SolucaoBoaPratica> solucoes = new ArrayList<>();
			boaPraticaDTO.getSolucoes().forEach(solucao -> {
				solucoes.add(solucao.toEntityInsert(boaPraticaRef));
			});
			boaPraticaRef.setSolucoes(solucoes);
		}

		/*Prepara imagens*/
		List<ImagemBoaPratica> listaImagens = new ArrayList<>();
		for (ImagemBoaPraticaDTO imagem : boaPraticaDTO.getGaleriaDeImagens()) {
			try {
				ImagemBoaPratica imagemAux =  buscarImagemBoaPraticaPorId(imagem.getId());
				imagemAux.setBoaPratica(boaPraticaRef);
				imagemAux.setNomeAutor(imagem.getNomeAutor());
				listaImagens.add(imagemAux);
			} catch (Exception e) {
				ImagemBoaPratica arquivoAux = imagem.toEntityInsert();
				
				arquivoAux.setBoaPratica(boaPraticaRef);
				listaImagens.add(arquivoAux);
			}
		}
		if(boaPraticaDTO.getImagemPrincipal() != null && boaPraticaDTO.getImagemPrincipal().getId() != null) {
			ImagemBoaPratica imagemAux =  buscarImagemBoaPraticaPorId(boaPraticaDTO.getImagemPrincipal().getId());
			imagemAux.setNomeAutor(boaPraticaDTO.getImagemPrincipal().getNomeAutor());
			imagemAux.setBoaPratica(boaPraticaRef);
			listaImagens.add(imagemAux);
		} else if (boaPraticaDTO.getImagemPrincipal() != null) {
			ImagemBoaPratica arquivoAux = boaPraticaDTO.getImagemPrincipal().toEntityInsert();
			arquivoAux.setBoaPratica(boaPraticaRef);
			listaImagens.add(arquivoAux);

			ImagemBoaPratica imagemBoaPratica = imagemBoaPraticaRepository
													.buscarImagemPrincipalPorIdBoaPratica(boaPraticaRef.getId(),
																			TipoImagemBoaPratica.PRINCIPAL.getTipo());
			if(imagemBoaPratica != null) {
				Long idImagemParaDeletar = imagemBoaPratica.getId();
				excluirImagemBoaPratica(idImagemParaDeletar);
			}
		}

		boaPraticaRef.setImagens(listaImagens);

		if(boaPraticaDTO.getImagemPrincipal() != null) {
			boaPraticaDTO.getImagemPrincipal().setConteudo(
												ImageUtils.compressBase64Image(boaPraticaDTO.getImagemPrincipal()
																									.getConteudo()));
		}

		repository.save(boaPraticaRef);

		return boaPraticaRef;
	}
	
	public void excluirFonteReferencia(Long idFonteReferrencia) {
		FonteReferenciaBoaPratica fonteReferenciaRef = buscarFonteReferenciaBoaPraticaPorId(idFonteReferrencia);
		fonteReferenciaBoaPraticaRepository.delete(fonteReferenciaRef);
	}
	
	public void excluirImagemBoaPratica(Long idImagemBoaPratica) {
		ImagemBoaPratica imagemBoaPratica = buscarImagemBoaPraticaPorId(idImagemBoaPratica);
		imagemBoaPraticaRepository.delete(imagemBoaPratica);
	}

	public List<BoaPratica> buscarBoasPraticasRelacionadasAoIndicador(Long idIndicador) {
		Indicador indicador = indicadorService.listarById(idIndicador);
		List<BoaPratica> boasPraticasDoIndicador = indicador.getBoasPraticasRelacionadas().stream().parallel().filter(o -> TipoBoaPratica.PCS.getTipo().equals(o.getTipo())).collect(Collectors.toList());
		return boasPraticasDoIndicador;
	}
	
	public List<CidadeComBoasPraticasDTO> buscarCidadescomBoasPraticas(){
		return repository.buscarCidadescomBoasPraticas();
	}
	
	public CombosCidadesComBoasPraticasDTO buscarCombosCidadesComBoasPraticas(){
		
		List<ItemComboDTO> listaCidadesComboAux = new ArrayList<ItemComboDTO>();
		List<ItemComboDTO> listaProvinciaEstadoComboAux = new ArrayList<ItemComboDTO>();
		List<ItemComboDTO> listaPaisComboAux = new ArrayList<ItemComboDTO>();
		List<ItemComboDTO> listaContinenteComboAux = new ArrayList<ItemComboDTO>();
		
		for(CidadeDTO cidade : repository.buscarCombosCidadesComBoasPraticas()) {
			listaCidadesComboAux.add(new ItemComboDTO(cidade.getId(),cidade.getNome()));
			listaProvinciaEstadoComboAux.add(new ItemComboDTO(cidade.getProvinciaEstado().getId(),
												cidade.getProvinciaEstado().getNome()));
			listaPaisComboAux.add(new ItemComboDTO(cidade.getProvinciaEstado().getPais().getId(),
									cidade.getProvinciaEstado().getPais().getNome()));
			listaContinenteComboAux.add(new ItemComboDTO(null,
														cidade.getProvinciaEstado().getPais().getContinente()));
		}
		
		List<ItemComboDTO> listaCidadesCombo = null;
		if(listaCidadesComboAux != null && !listaCidadesComboAux.isEmpty()) {
			listaCidadesCombo = listaCidadesComboAux.stream()
														.filter(distinctByKey(ItemComboDTO::getId))
														.sorted((o1, o2) -> Normalizer.normalize(o1.getLabel(), Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "").compareToIgnoreCase(Normalizer.normalize(o2.getLabel(), Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "")))
													.collect(Collectors.toList());
		}
		
		List<ItemComboDTO> listaProvinciaEstadoCombo = null;
		if(listaProvinciaEstadoComboAux != null && !listaProvinciaEstadoComboAux.isEmpty()) {

			listaProvinciaEstadoCombo = listaProvinciaEstadoComboAux.stream()
																		.filter(distinctByKey(ItemComboDTO::getId))
																		.sorted((o1, o2) -> Normalizer.normalize(o1.getLabel(), Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "").compareToIgnoreCase(Normalizer.normalize(o2.getLabel(), Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "")))
																	.collect(Collectors.toList());
		}

		List<ItemComboDTO> listaPaisCombo = null;		
		if(listaPaisComboAux != null && !listaPaisComboAux.isEmpty()) {
			listaPaisCombo = listaPaisComboAux.stream()
												.filter(distinctByKey(ItemComboDTO::getId))
												.sorted((o1, o2) -> Normalizer.normalize(o1.getLabel(), Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "").compareToIgnoreCase(Normalizer.normalize(o2.getLabel(), Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "")))
											.collect(Collectors.toList());
		}
		 
		List<ItemComboDTO> listaContinenteCombo = null;
		if(listaContinenteComboAux != null && !listaContinenteComboAux.isEmpty()) {
			listaContinenteComboAux.removeIf(n -> n.getLabel() == null);
			listaContinenteCombo = listaContinenteComboAux.stream()
															.filter(distinctByKey(ItemComboDTO::getLabel))
															.sorted((o1, o2) -> Normalizer.normalize(o1.getLabel(), Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "").compareToIgnoreCase(Normalizer.normalize(o2.getLabel(), Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "")))
														.collect(Collectors.toList());
		}
		
		
		List<ItemComboDTO> listaItensEixos =  new ArrayList<ItemComboDTO>();
		List<Eixo> listaEixos = eixoService.listar();
		for(Eixo eixo : listaEixos) {
			listaItensEixos.add(new ItemComboDTO(eixo.getId(),eixo.getNome()));
		}
		
		List<ItemComboDTO> listaItensOds =  new ArrayList<ItemComboDTO>();
		List<ObjetivoDesenvolvimentoSustentavel> listaOds = odsService.listar();
		for(ObjetivoDesenvolvimentoSustentavel ods : listaOds) {
			listaItensOds.add(new ItemComboDTO(ods.getId(),ods.getNumero(),ods.getTitulo()));
		}
		
		List<ItemComboDTO> listaItensMetaOds =  new ArrayList<ItemComboDTO>();
		List<MetaObjetivoDesenvolvimentoSustentavel> listaMetaOds = metaOdsService.listar();
		for(MetaObjetivoDesenvolvimentoSustentavel metaOds : listaMetaOds) {
			listaItensMetaOds.add(new ItemComboDTO(metaOds.getId(), metaOds.getNumero(), metaOds.getDescricao()));
		}
		
		
		CombosCidadesComBoasPraticasDTO comboCidadesDTO = new CombosCidadesComBoasPraticasDTO();
		comboCidadesDTO.setListaProvinciasEstados(listaProvinciaEstadoCombo);
		comboCidadesDTO.setListaCidades(listaCidadesCombo);
		comboCidadesDTO.setListaPaises(listaPaisCombo);
		comboCidadesDTO.setListaContinentes(listaContinenteCombo);
		comboCidadesDTO.setListaEixos(listaItensEixos);
		comboCidadesDTO.setListaOds(listaItensOds);
		comboCidadesDTO.setListaMetaOds(listaItensMetaOds);
		
		return comboCidadesDTO;
	}
	
	public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
		
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        
        return t -> seen.add(keyExtractor.apply(t));
    }
	
	public BoasPraticasFiltradasDTO buscarBoasPraticasFiltradasPaginaInicial(FiltroCidadesComBoasPraticas filtro){

		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<BoaPraticaItemDTO> query = cb.createQuery(BoaPraticaItemDTO.class);

		Root<BoaPratica> boaPratica = query.from(BoaPratica.class);
		
		Join<BoaPratica, Cidade> joinCidade = boaPratica.join("municipio",JoinType.LEFT);
		Join<BoaPratica, ProvinciaEstado> joinProvinciaEstado = boaPratica.join("estado",JoinType.LEFT);
		Join<BoaPratica, Pais> joinPais = joinProvinciaEstado.join("pais",JoinType.LEFT);
		
		query.multiselect(boaPratica.get("id"),boaPratica.get("titulo"),boaPratica.get("subtitulo"),boaPratica.get("objetivoGeral"),joinCidade.get("nome"),
				joinProvinciaEstado.get("nome"),joinPais.get("nome")).distinct(true);
		
		List<javax.persistence.criteria.Predicate> predicateList = new ArrayList<>();
		
		predicateList.add(boaPratica.get("prefeitura").isNull());
		predicateList.add(cb.equal(boaPratica.get("paginaInicial"), true));
		
		List<Order> orderList = new ArrayList<Order>();
		orderList.add(cb.desc(boaPratica.get("id")));
		query.orderBy(orderList);
		
		javax.persistence.criteria.Predicate[] predicates = new javax.persistence.criteria.Predicate[predicateList.size()];
		predicateList.toArray(predicates);
		query.where(predicates);

		TypedQuery<BoaPraticaItemDTO> typedQuery = em.createQuery(query);
		
		if(filtro.getPage() == 0) {
			typedQuery.setFirstResult(filtro.getPage());
			typedQuery.setMaxResults(filtro.getLinesPerPage());
		} else {
			typedQuery.setFirstResult(filtro.getPage() * filtro.getLinesPerPage());
			typedQuery.setMaxResults(filtro.getLinesPerPage());
		}

		List<BoaPraticaItemDTO> listBoasPraticas = typedQuery.getResultList(); 

		BoasPraticasFiltradasDTO boasPraticasFiltradasDTO =  new BoasPraticasFiltradasDTO();
		
		boasPraticasFiltradasDTO.setListBoasPraticas(listBoasPraticas);
		
		boasPraticasFiltradasDTO.setCountTotalBoasPraticas(countBoasPraticasFiltradas(filtro));
		
		return boasPraticasFiltradasDTO;
	}

	public BoasPraticasFiltradasDTO buscarBoasPraticasFiltradas(FiltroCidadesComBoasPraticas filtro){

		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<BoaPraticaItemDTO> query = cb.createQuery(BoaPraticaItemDTO.class);

		Root<BoaPratica> boaPratica = query.from(BoaPratica.class);
		
		Join<BoaPratica, Cidade> joinCidade = boaPratica.join("municipio",JoinType.LEFT);
		Join<BoaPratica, ProvinciaEstado> joinProvinciaEstado = boaPratica.join("estado",JoinType.LEFT);
		Join<BoaPratica, Pais> joinPais = joinProvinciaEstado.join("pais",JoinType.LEFT);
		Join<BoaPratica, Eixo> joinEixo = boaPratica.join("eixo",JoinType.LEFT);
		Join<BoaPratica, ObjetivoDesenvolvimentoSustentavel> joinOds = boaPratica.join("ods",JoinType.LEFT);
		Join<BoaPratica, MetaObjetivoDesenvolvimentoSustentavel> joinMetasOds = boaPratica.join("metasOds",JoinType.LEFT);
		
		query.multiselect(boaPratica.get("id"),boaPratica.get("titulo"),boaPratica.get("subtitulo"),boaPratica.get("objetivoGeral"),joinCidade.get("nome"),
				joinProvinciaEstado.get("nome"),joinPais.get("nome"), boaPratica.get("autorImagemPrincipal")).distinct(true);
		
		
		
		List<javax.persistence.criteria.Predicate> predicateList = new ArrayList<>();
		
		predicateList.add(boaPratica.get("prefeitura").isNull());
		
		if(filtro.getCidade() != null && filtro.getCidade().getId() != null) {
			Path<Long> idCidade = joinCidade.get("id");
			predicateList.add(cb.equal(idCidade, filtro.getCidade().getId()));
		}
		
		if(filtro.getEstado() != null && filtro.getEstado().getId() != null) {
			Path<Long> idProvinciaEstado = joinProvinciaEstado.get("id");
			predicateList.add(cb.equal(idProvinciaEstado, filtro.getEstado().getId()));
		}
		
		if(filtro.getPais()!= null && filtro.getPais().getId() != null) {
			Path<Long> idPais = joinPais.get("id");
			predicateList.add(cb.equal(idPais, filtro.getPais().getId()));
		}
		
		if(filtro.getContinente()!= null && !filtro.getContinente().isEmpty()) {
			Path<String> continente = joinPais.get("continente");
			predicateList.add(cb.like(cb.lower(continente), "%" + filtro.getContinente().toLowerCase() + "%"));
		}
		
		if(filtro.getIdEixo()!= null) {
			Path<Long> idEixo = joinEixo.get("id");
			predicateList.add(cb.equal(idEixo, filtro.getIdEixo()));
		}
		
	
		if(filtro.getIdOds()!= null) {
			Path<Long> idOds = joinOds.get("id");
			predicateList.add(cb.equal(idOds,filtro.getIdOds()));
		}
		
		if(filtro.getMetaOds()!= null && filtro.getMetaOds().getId() != null) {
			Path<Long> idMetaOds = joinMetasOds.get("id");
			predicateList.add(cb.equal(idMetaOds, filtro.getMetaOds().getId()));
		}
		
		if(filtro.getPopuMin()!= null) {
			Path<Long> populacao = joinCidade.get("populacao");
			predicateList.add(cb.greaterThanOrEqualTo(populacao,filtro.getPopuMin()));
		}
		
		if(filtro.getPopuMax()!= null) {
			Path<Long> populacao = joinCidade.get("populacao");
			predicateList.add(cb.lessThanOrEqualTo(populacao,filtro.getPopuMax()));
		}
		
		if (filtro.getPalavraChave() != null && !filtro.getPalavraChave().equals("")) {
			Path<String> titulo = boaPratica.get("titulo");
			Path<String> subtitulo = boaPratica.get("subtitulo");
			javax.persistence.criteria.Predicate predicateForTitulo = cb.like(cb.lower(titulo), "%" + filtro.getPalavraChave().toLowerCase() + "%");
			javax.persistence.criteria.Predicate predicateForEvento = cb.like(cb.lower(subtitulo), "%" + filtro.getPalavraChave().toLowerCase() + "%");
			predicateList.add(cb.or(predicateForTitulo, predicateForEvento));
		}		
		
		List<Order> orderList = new ArrayList<Order>();
		orderList.add(cb.desc(boaPratica.get("id")));
		query.orderBy(orderList);
		
		javax.persistence.criteria.Predicate[] predicates = new javax.persistence.criteria.Predicate[predicateList.size()];
		predicateList.toArray(predicates);
		query.where(predicates);

		TypedQuery<BoaPraticaItemDTO> typedQuery = em.createQuery(query);
		
		if(filtro.getPage() == 0) {
			typedQuery.setFirstResult(filtro.getPage());
			typedQuery.setMaxResults(filtro.getLinesPerPage());
		} else {
			typedQuery.setFirstResult(filtro.getPage() * filtro.getLinesPerPage());
			typedQuery.setMaxResults(filtro.getLinesPerPage());
		}

		List<BoaPraticaItemDTO> listBoasPraticas = typedQuery.getResultList(); 

		BoasPraticasFiltradasDTO boasPraticasFiltradasDTO =  new BoasPraticasFiltradasDTO();
		
		boasPraticasFiltradasDTO.setListBoasPraticas(listBoasPraticas);
		
		boasPraticasFiltradasDTO.setCountTotalBoasPraticas(countBoasPraticasFiltradas(filtro));
		
		return boasPraticasFiltradasDTO;
	}
	
	
	public 	List<CidadeComBoasPraticasDTO> buscarCidadesComBoasPraticasFiltradas(FiltroCidadesComBoasPraticas filtro){
		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<CidadeDTO> query = cb.createQuery(CidadeDTO.class);

		Root<BoaPratica> boaPratica = query.from(BoaPratica.class);
		
		Join<BoaPratica, Cidade> joinCidade = boaPratica.join("municipio",JoinType.LEFT);
		Join<BoaPratica, ProvinciaEstado> joinProvinciaEstado = joinCidade.join("provinciaEstado",JoinType.LEFT);
		Join<BoaPratica, Pais> joinPais = joinProvinciaEstado.join("pais",JoinType.LEFT);
		Join<BoaPratica, Eixo> joinEixo = boaPratica.join("eixo",JoinType.LEFT);
		Join<BoaPratica, ObjetivoDesenvolvimentoSustentavel> joinOds = boaPratica.join("ods",JoinType.LEFT);
		Join<BoaPratica, MetaObjetivoDesenvolvimentoSustentavel> joinMetasOds = boaPratica.join("metasOds",JoinType.LEFT);
		
		query.multiselect(joinCidade.get("id")).distinct(true);
		
		List<javax.persistence.criteria.Predicate> predicateList = new ArrayList<>();
		
		predicateList.add(boaPratica.get("prefeitura").isNull());
		
		if(filtro.getCidade() != null) {
			Path<Long> idCidade = joinCidade.get("id");
			predicateList.add(cb.equal(idCidade, filtro.getCidade().getId()));
		}
		
		if(filtro.getIdCidade() != null) {
			Path<Long> idCidade = joinCidade.get("id");
			predicateList.add(cb.equal(idCidade, filtro.getIdCidade()));
		}
		
		if(filtro.getEstado() != null) {
			Path<Long> idProvinciaEstado = joinProvinciaEstado.get("id");
			predicateList.add(cb.equal(idProvinciaEstado, filtro.getEstado().getId()));
		}
		
		if(filtro.getIdEstado() != null) {
			Path<Long> idProvinciaEstado = joinProvinciaEstado.get("id");
			predicateList.add(cb.equal(idProvinciaEstado, filtro.getIdEstado()));
		}
		
		if(filtro.getPais()!= null) {
			Path<Long> idPais = joinPais.get("id");
			predicateList.add(cb.equal(idPais, filtro.getPais().getId()));
		}
		
		if(filtro.getIdPais() != null) {
			Path<Long> idPais = joinPais.get("id");
			predicateList.add(cb.equal(idPais, filtro.getIdPais()));
		}
		
		if(filtro.getContinente()!= null && !filtro.getContinente().isEmpty()) {
			Path<String> continente = joinPais.get("continente");
			predicateList.add(cb.like(cb.lower(continente), "%" + filtro.getContinente().toLowerCase() + "%"));
		}
		
		if(filtro.getIdEixo()!= null) {
			Path<Long> idEixo = joinEixo.get("id");
			predicateList.add(cb.equal(idEixo, filtro.getIdEixo()));
		}
		
		if(filtro.getIdOds()!= null) {
			Path<Long> idOds = joinOds.get("id");
			predicateList.add(cb.equal(idOds,filtro.getIdOds()));
		}
		
		if(filtro.getMetaOds()!= null && filtro.getMetaOds().getId() != null) {
			Path<Long> idMetaOds = joinMetasOds.get("id");
			predicateList.add(cb.equal(idMetaOds, filtro.getMetaOds().getId()));
		}

		if(filtro.getPopuMin()!= null) {
			Path<Long> populacao = joinCidade.get("populacao");
			predicateList.add(cb.greaterThanOrEqualTo(populacao,filtro.getPopuMin()));
		}
		
		if(filtro.getPopuMax()!= null) {
			Path<Long> populacao = joinCidade.get("populacao");
			predicateList.add(cb.lessThanOrEqualTo(populacao,filtro.getPopuMax()));
		}
		
		if (filtro.getPalavraChave() != null && !filtro.getPalavraChave().equals("")) {
			Path<String> titulo = boaPratica.get("titulo");
			Path<String> subtitulo = boaPratica.get("subtitulo");
			javax.persistence.criteria.Predicate predicateForTitulo = cb.like(cb.lower(titulo), "%" + filtro.getPalavraChave().toLowerCase() + "%");
			javax.persistence.criteria.Predicate predicateForEvento = cb.like(cb.lower(subtitulo), "%" + filtro.getPalavraChave().toLowerCase() + "%");
			predicateList.add(cb.or(predicateForTitulo, predicateForEvento));
		}	
		
		javax.persistence.criteria.Predicate[] predicates = new javax.persistence.criteria.Predicate[predicateList.size()];
		predicateList.toArray(predicates);
		query.where(predicates);

		TypedQuery<CidadeDTO> typedQuery = em.createQuery(query);

		List<CidadeDTO> listCidades = typedQuery.getResultList(); 
		
		List<CidadeComBoasPraticasDTO> listCidadesAux = new ArrayList<CidadeComBoasPraticasDTO>();
		
		List<Long> idsCidades = listCidades.stream().map(CidadeDTO:: getId).collect(Collectors.toList());
		
		if(idsCidades != null && !idsCidades.isEmpty()) {
			listCidadesAux = cidadeService.findCidadesByIds(idsCidades);
		}
		
		if (!filtro.isVisualizarComoPontos()) {
			for (CidadeComBoasPraticasDTO cidadeComBoaPraticaDTO : listCidadesAux) {
				List<Feature> shapeZoneamento = shapeFileService.buscarShapeZoneamento(cidadeComBoaPraticaDTO.getIdCidade());
				cidadeComBoaPraticaDTO.setShapeZoneamento(shapeZoneamento);
				cidadeComBoaPraticaDTO.setVisualizarComoPontos(false);
			}
		}

		return listCidadesAux;
	}
	
	private Long countBoasPraticasFiltradas(FiltroCidadesComBoasPraticas filtro){

		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<Long> query = cb.createQuery(Long.class);
		
		Root<BoaPratica> boaPratica = query.from(BoaPratica.class);
		
		Join<BoaPratica, Cidade> joinCidade = boaPratica.join("municipio",JoinType.LEFT);
		Join<BoaPratica, ProvinciaEstado> joinProvinciaEstado = boaPratica.join("estado",JoinType.LEFT);
		Join<BoaPratica, Pais> joinPais = joinProvinciaEstado.join("pais",JoinType.LEFT);
		Join<BoaPratica, Eixo> joinEixo = boaPratica.join("eixo",JoinType.LEFT);
		Join<BoaPratica, ObjetivoDesenvolvimentoSustentavel> joinOds = boaPratica.join("ods",JoinType.LEFT);
		Join<BoaPratica, MetaObjetivoDesenvolvimentoSustentavel> joinMetasOds = boaPratica.join("metasOds",JoinType.LEFT);
		
		query.select(cb.countDistinct(boaPratica));
		
		List<javax.persistence.criteria.Predicate> predicateList = new ArrayList<>();

		predicateList.add(boaPratica.get("prefeitura").isNull());
		
		if(filtro.getCidade() != null) {
			Path<Long> idCidade = joinCidade.get("id");
			predicateList.add(cb.equal(idCidade, filtro.getCidade().getId()));
		}
		
		if(filtro.getEstado() != null) {
			Path<Long> idProvinciaEstado = joinProvinciaEstado.get("id");
			predicateList.add(cb.equal(idProvinciaEstado, filtro.getEstado().getId()));
		}
		
		if(filtro.getPais()!= null) {
			Path<Long> idPais = joinPais.get("id");
			predicateList.add(cb.equal(idPais, filtro.getPais().getId()));
		}
		
		if(filtro.getContinente()!= null && !filtro.getContinente().isEmpty()) {
			Path<String> continente = joinPais.get("continente");
			predicateList.add(cb.like(cb.lower(continente), "%" + filtro.getContinente().toLowerCase() + "%"));
		}
		
		if(filtro.getIdEixo()!= null) {
			Path<Long> idEixo = joinEixo.get("id");
			predicateList.add(cb.equal(idEixo, filtro.getIdEixo()));
		}
		
		if(filtro.getIdOds()!= null) {
			Path<Long> idOds = joinOds.get("id");
			predicateList.add(cb.equal(idOds,filtro.getIdOds()));
		}
		
		if(filtro.getIdMetaOds()!= null) {
			Path<Long> idMetaOds = joinMetasOds.get("id");
			predicateList.add(cb.equal(idMetaOds,filtro.getIdMetaOds()));
		}
		
		if(filtro.getPopuMin()!= null) {
			Path<Long> populacao = joinCidade.get("populacao");
			predicateList.add(cb.greaterThanOrEqualTo(populacao,filtro.getPopuMin()));
		}
		
		if(filtro.getPopuMax()!= null) {
			Path<Long> populacao = joinCidade.get("populacao");
			predicateList.add(cb.lessThanOrEqualTo(populacao,filtro.getPopuMax()));
		}
		
		if (filtro.getPalavraChave() != null && !filtro.getPalavraChave().equals("")) {
			Path<String> titulo = boaPratica.get("titulo");
			Path<String> subtitulo = boaPratica.get("subtitulo");
			javax.persistence.criteria.Predicate predicateForTitulo = cb.like(cb.lower(titulo), "%" + filtro.getPalavraChave().toLowerCase() + "%");
			javax.persistence.criteria.Predicate predicateForEvento = cb.like(cb.lower(subtitulo), "%" + filtro.getPalavraChave().toLowerCase() + "%");
			predicateList.add(cb.or(predicateForTitulo, predicateForEvento));
		}	
		
		javax.persistence.criteria.Predicate[] predicates = new javax.persistence.criteria.Predicate[predicateList.size()];
		predicateList.toArray(predicates);
		query.where(predicates);
		
		TypedQuery<Long> typedQuery = em.createQuery(query);
		Long lista = typedQuery.getSingleResult();
		
		return lista;
	}

	
	private List<String> getImagePrincipalBoaPratica(Long idBoaPratica, String tipo) {
		List<String> imagemPrincipalComAutor = new ArrayList<>();
		ImagemBoaPratica imagemBoaPratica = imagemBoaPraticaRepository.buscarImagemPrincipalPorIdBoaPratica(idBoaPratica, tipo);
		
		if(imagemBoaPratica != null) {
			imagemPrincipalComAutor.add(imagemBoaPratica.getConteudo());
			imagemPrincipalComAutor.add(imagemBoaPratica.getNomeAutor());
			return imagemPrincipalComAutor;
		} else {
			imagemPrincipalComAutor.add("");
			imagemPrincipalComAutor.add("");			
		}
		return imagemPrincipalComAutor;
	}
	
	public String removeTextoEntre(String texto, String textoInicial, String textoFinal) {
		if(texto == null || textoInicial == null || textoFinal == null) return "";

		while(texto.contains(textoInicial)) {
			int inicio = texto.indexOf(textoInicial);
			int inicioComLength = inicio+textoInicial.length();
			String subApartiInicio = texto.substring(inicioComLength , texto.length());
			int fim = subApartiInicio.indexOf(textoFinal)+inicioComLength;
			String textoParaRemover = texto.substring(inicio, fim+1);
			texto = texto.replace(textoParaRemover , "")		;	
		}
		return texto;
	}
	public BoaPraticaDetalheDTO buscarBoaPrticaDetalhe(Long id) {
		BoaPratica boaPraticaRef = buscarPorId(id);
		boaPraticaRef.setObjetivoGeral(removeTextoEntre(boaPraticaRef.getObjetivoGeral(),"style=\"","\""));
		BoaPraticaDetalheDTO boaPraticaDto = new BoaPraticaDetalheDTO(boaPraticaRef);

		List<ImagemBoaPraticaDTO> imagensGaleria = new ArrayList<>();
		for (ImagemBoaPratica imagem : boaPraticaRef.getImagens()) {
			if (TipoImagemBoaPratica.GALERIA.getTipo().equals(imagem.getTipo())) {
				imagensGaleria.add(new ImagemBoaPraticaDTO(imagem));
			}
		}
		boaPraticaDto.setGaleriaDeImagens(imagensGaleria.isEmpty()?null:imagensGaleria);
		
		return boaPraticaDto;
	}
	
	public List<BoaPratica> ultimasBoasPraticas(int qtd) {
		PageRequest pageRequest = PageRequest.of(0, qtd, Direction.valueOf("DESC"), "id");
		return repository.carregarUltimasBoasPraticas(pageRequest);
	}
	
	public BoasPraticasFiltradasDTO buscarBoasPraticasDaCidade(Integer page, Integer linesPerPage, Long idCidade){

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<BoaPraticaItemDTO> query = cb.createQuery(BoaPraticaItemDTO.class);
		Root<BoaPratica> boaPratica = query.from(BoaPratica.class);
		
		Join<BoaPratica, Prefeitura> joinPrefeitura = boaPratica.join("prefeitura",JoinType.LEFT);
		Join<BoaPratica, ProvinciaEstado> joinCidade = joinPrefeitura.join("cidade",JoinType.LEFT);
		Join<BoaPratica, ProvinciaEstado> joinProvinciaEstado = boaPratica.join("estado",JoinType.LEFT);
		Join<BoaPratica, Pais> joinPais = joinProvinciaEstado.join("pais",JoinType.LEFT);
		
		query.multiselect(boaPratica.get("id"),boaPratica.get("titulo"),boaPratica.get("subtitulo"),boaPratica.get("objetivoGeral"),joinCidade.get("nome"),
				joinProvinciaEstado.get("nome"),joinPais.get("nome")).distinct(true);
		
		List<javax.persistence.criteria.Predicate> predicateList = new ArrayList<>();
		
		Path<Long> tipoAux = boaPratica.get("tipo");
		predicateList.add(cb.equal(tipoAux, TipoBoaPratica.PREFEITURA.getTipo()));
		
		if(idCidade != null) {
			Path<Long> idCidadeAux = joinCidade.get("id");
			predicateList.add(cb.equal(idCidadeAux, idCidade));
		}
		
		List<Order> orderList = new ArrayList<Order>();
		orderList.add(cb.desc(boaPratica.get("id")));
		query.orderBy(orderList);
		
		javax.persistence.criteria.Predicate[] predicates = new javax.persistence.criteria.Predicate[predicateList.size()];
		predicateList.toArray(predicates);
		query.where(predicates);
		
		TypedQuery<BoaPraticaItemDTO> typedQuery = em.createQuery(query);
		
		if(page == 0) {
			typedQuery.setFirstResult(page);
			typedQuery.setMaxResults(linesPerPage);
		}else {
			typedQuery.setFirstResult(page * linesPerPage);
			typedQuery.setMaxResults(linesPerPage);
		}

		
		List<BoaPraticaItemDTO> listBoasPraticas = typedQuery.getResultList(); 
		if(listBoasPraticas != null) {
			listBoasPraticas.forEach(boaPraticaItemDTO -> boaPraticaItemDTO.setImagemPrincipal(getImagePrincipalBoaPratica(boaPraticaItemDTO.getIdBoaPratica(),"principal").get(0)));
		}
		
		BoasPraticasFiltradasDTO boasPraticasFiltradasDTO =  new BoasPraticasFiltradasDTO();
		boasPraticasFiltradasDTO.setListBoasPraticas(listBoasPraticas);
		boasPraticasFiltradasDTO.setCountTotalBoasPraticas(countBoasPraticasDaCidade(idCidade));
		
		return boasPraticasFiltradasDTO;
	}
	
	private Long countBoasPraticasDaCidade(Long idCidade){

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> query = cb.createQuery(Long.class);
		Root<BoaPratica> boaPratica = query.from(BoaPratica.class);
		
		Join<BoaPratica, Prefeitura> joinPrefeitura = boaPratica.join("prefeitura",JoinType.LEFT);
		Join<BoaPratica, ProvinciaEstado> joinCidade = joinPrefeitura.join("cidade",JoinType.LEFT);
		
		query.select(cb.countDistinct(boaPratica));
		
		List<javax.persistence.criteria.Predicate> predicateList = new ArrayList<>();
		
		Path<Long> tipoAux = boaPratica.get("tipo");
		predicateList.add(cb.equal(tipoAux, TipoBoaPratica.PREFEITURA.getTipo()));

		if(idCidade != null) {
			Path<Long> idCidadeAux = joinCidade.get("id");
			predicateList.add(cb.equal(idCidadeAux, idCidade));
		}
		
		javax.persistence.criteria.Predicate[] predicates = new javax.persistence.criteria.Predicate[predicateList.size()];
		predicateList.toArray(predicates);
		query.where(predicates);
		TypedQuery<Long> typedQuery = em.createQuery(query);
		Long lista = typedQuery.getSingleResult();
		
		return lista;
	}
	
	public List<ObjetivoDesenvolvimentoSustentavel> buscarOdsDaBoaPratica(Long idBoaPratica){
		return repository.buscarOdsDaBoaPratica(idBoaPratica);
	}

	public void removerEstiloBoasPraticas() {
		List<BoaPratica> boasPraticas = repository.findAll();
		for(BoaPratica bp : boasPraticas) {
			if(bp.getPrefeitura() != null ) {	
				if (bp.getObjetivoGeral() != null) bp.setObjetivoGeral(removeTextoEntre(bp.getObjetivoGeral(),"style=\"","\""));
				if (bp.getObjetivoEspecifico() != null) bp.setObjetivoEspecifico(removeTextoEntre(bp.getObjetivoEspecifico(),"style=\"","\""));
				if (bp.getPrincipaisResultados() != null) bp.setPrincipaisResultados(removeTextoEntre(bp.getPrincipaisResultados(),"style=\"","\""));
				if (bp.getParceirosEnvolvidos() != null) bp.setParceirosEnvolvidos(removeTextoEntre(bp.getParceirosEnvolvidos(),"style=\"","\""));
				if (bp.getInformacoesComplementares() != null) bp.setInformacoesComplementares(removeTextoEntre(bp.getInformacoesComplementares(),"style=\"","\""));
				if (bp.getFontesReferencia() != null) bp.setFontesReferencia(removeTextoEntre(bp.getFontesReferencia(),"style=\"","\""));
				

				if (bp.getObjetivoGeral() != null) bp.setObjetivoGeral(removeTextoEntre(bp.getObjetivoGeral(),"style='","'"));
				if (bp.getObjetivoEspecifico() != null) bp.setObjetivoEspecifico(removeTextoEntre(bp.getObjetivoEspecifico(),"style='","'"));
				if (bp.getPrincipaisResultados() != null) bp.setPrincipaisResultados(removeTextoEntre(bp.getPrincipaisResultados(),"style='","'"));
				if (bp.getParceirosEnvolvidos() != null) bp.setParceirosEnvolvidos(removeTextoEntre(bp.getParceirosEnvolvidos(),"style='","'"));
				if (bp.getInformacoesComplementares() != null) bp.setInformacoesComplementares(removeTextoEntre(bp.getInformacoesComplementares(),"style='","'"));
				if (bp.getFontesReferencia() != null) bp.setFontesReferencia(removeTextoEntre(bp.getFontesReferencia(),"style='","'"));
				
			}
		}
		repository.saveAll(boasPraticas);
		
	}
	
	public List<BoaPraticaFiltradaIntegracaoDTO> buscarBoasPraticasFiltradasIntegracao(String nomeBoaPratica, String nomeCidade){

		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<BoaPraticaFiltradaIntegracaoDTO> query = cb.createQuery(BoaPraticaFiltradaIntegracaoDTO.class);

		Root<BoaPratica> boaPratica = query.from(BoaPratica.class);
		
		Join<BoaPratica, Cidade> joinCidade = boaPratica.join("municipio",JoinType.LEFT);
		Join<BoaPratica, ProvinciaEstado> joinProvinciaEstado = boaPratica.join("estado",JoinType.LEFT);
		Join<BoaPratica, Pais> joinPais = joinProvinciaEstado.join("pais",JoinType.LEFT);
		
		query.multiselect(boaPratica.get("id"),boaPratica.get("titulo"),boaPratica.get("subtitulo"),boaPratica.get("objetivoGeral"),joinCidade.get("nome"),
				joinProvinciaEstado.get("nome"),joinPais.get("nome")).distinct(true);
		
		List<javax.persistence.criteria.Predicate> predicateList = new ArrayList<>();
		
		if (nomeBoaPratica != null && !nomeBoaPratica.equals("")) {
			Path<String> tituloBoaPratica = boaPratica.get("titulo");
			javax.persistence.criteria.Predicate predicateForTitulo = cb.like(cb.lower(tituloBoaPratica), "%" + nomeBoaPratica.toLowerCase() + "%");
			predicateList.add(predicateForTitulo);
		}
		
		if (nomeCidade != null && !nomeCidade.equals("")) {
			Path<String> nomeCidadePath = joinCidade.get("nome");
			javax.persistence.criteria.Predicate predicateForNomeCidade = cb.like(cb.lower(nomeCidadePath), "%" + nomeCidade.toLowerCase() + "%");
			predicateList.add(predicateForNomeCidade);
		}
		
		List<Order> orderList = new ArrayList<Order>();
		orderList.add(cb.desc(boaPratica.get("id")));
		query.orderBy(orderList);
		
		javax.persistence.criteria.Predicate[] predicates = new javax.persistence.criteria.Predicate[predicateList.size()];
		predicateList.toArray(predicates);
		query.where(predicates);

		TypedQuery<BoaPraticaFiltradaIntegracaoDTO> typedQuery = em.createQuery(query);

		List<BoaPraticaFiltradaIntegracaoDTO> listBoasPraticas = typedQuery.getResultList();
		
		return listBoasPraticas;
	}

}
