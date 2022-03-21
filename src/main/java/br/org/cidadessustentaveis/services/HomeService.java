package br.org.cidadessustentaveis.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.org.cidadessustentaveis.dto.HomeBarraDTO;
import br.org.cidadessustentaveis.dto.HomeDTO;
import br.org.cidadessustentaveis.dto.HomeImagemDTO;
import br.org.cidadessustentaveis.dto.InstitucionalDinamicoDTO;
import br.org.cidadessustentaveis.dto.PrimeiraSecaoDTO;
import br.org.cidadessustentaveis.dto.QuartaSecaoDTO;
import br.org.cidadessustentaveis.dto.QuintaSecaoDTO;
import br.org.cidadessustentaveis.dto.SecaoLateralDTO;
import br.org.cidadessustentaveis.dto.SegundaSecaoDTO;
import br.org.cidadessustentaveis.dto.SetimaSecaoDTO;
import br.org.cidadessustentaveis.dto.SextaSecaoDTO;
import br.org.cidadessustentaveis.dto.TerceiraSecaoDTO;
import br.org.cidadessustentaveis.model.administracao.MenuPagina;
import br.org.cidadessustentaveis.model.home.Home;
import br.org.cidadessustentaveis.model.home.HomeBarra;
import br.org.cidadessustentaveis.model.home.HomeImagem;
import br.org.cidadessustentaveis.model.home.PrimeiraSecao;
import br.org.cidadessustentaveis.model.home.QuartaSecao;
import br.org.cidadessustentaveis.model.home.QuintaSecao;
import br.org.cidadessustentaveis.model.home.SecaoLateral;
import br.org.cidadessustentaveis.model.home.SegundaSecao;
import br.org.cidadessustentaveis.model.home.SetimaSecao;
import br.org.cidadessustentaveis.model.home.SextaSecao;
import br.org.cidadessustentaveis.model.home.TerceiraSecao;
import br.org.cidadessustentaveis.model.institucional.InstitucionalDinamico;
import br.org.cidadessustentaveis.repository.HomeBarraRepository;
import br.org.cidadessustentaveis.repository.HomeImagemRepository;
import br.org.cidadessustentaveis.repository.HomeRepository;
import br.org.cidadessustentaveis.repository.MenuPaginaRepository;
import br.org.cidadessustentaveis.repository.PrimeiraSecaoRepository;
import br.org.cidadessustentaveis.repository.QuartaSecaoRepository;
import br.org.cidadessustentaveis.repository.QuintaSecaoRepository;
import br.org.cidadessustentaveis.repository.SecaoLateralRepository;
import br.org.cidadessustentaveis.repository.SegundaSecaoRepository;
import br.org.cidadessustentaveis.repository.SetimaSecaoRepository;
import br.org.cidadessustentaveis.repository.SextaSecaoRepository;
import br.org.cidadessustentaveis.repository.TerceiraSecaoRepository;
import br.org.cidadessustentaveis.services.exceptions.ObjectNotFoundException;
import br.org.cidadessustentaveis.util.ImageUtils;

@Service
public class HomeService {

	
	@Autowired
	private MenuPaginaRepository menuPaginaRepository;
	
	@Autowired
	private HomeRepository homeRepository;
	
	@Autowired
	private HomeImagemRepository homeImagemRepository;
	
	@Autowired
	private HomeBarraRepository homeBarraRepository;
	
	@Autowired
	private PrimeiraSecaoRepository primeiraSecaoRepository;
	
	@Autowired
	private SegundaSecaoRepository segundaSecaoRepository;
	
	@Autowired
	private TerceiraSecaoRepository terceiraSecaoRepository;
	
	@Autowired
	private QuartaSecaoRepository quartaSecaoRepository;
	
	@Autowired
	private QuintaSecaoRepository quintaSecaoRepository;	
	
	@Autowired
	private SecaoLateralRepository secaoLateralRepository;
	
	@Autowired
	private SextaSecaoRepository sextaSecaoRepository;	
	
	@Autowired
	private SetimaSecaoRepository setimaSecaoRepository;
	

	
	public Home findById(final Long id) {
		  Optional<Home> institucional = homeRepository.findById(id);
		  return institucional.orElseThrow(() -> new ObjectNotFoundException("Home não encontrada!"));
	}
	

	public HomeDTO buscarParaEdicao(Long id) {
		Home homeRef = findById(id);
		HomeDTO homeDTO = new HomeDTO(homeRef);
		return homeDTO;
	}
	
	public Home inserir(HomeDTO homeDTO) throws IOException {
		for(HomeImagemDTO imagem : homeDTO.getGaleriaDeImagens()) {
		    imagem.setConteudo(ImageUtils.compressBase64Image(imagem.getConteudo()));
        }

		Home homeToInsert = homeDTO.toEntityInsert();
		
		MenuPagina menuPagina = inserirMenuPagina(homeDTO);
		
		homeToInsert.setMenuPagina(menuPagina);
		
		return homeRepository.save(homeToInsert);
	}

	public Home alterar(final Long id, final HomeDTO homeDTO) throws Exception {
		Home home = findById(id);

		atualizarMenuPagina(home, homeDTO);

		Home homeToUpdate = homeDTO.toEntityUpdate(home);
		
		/*Prepara imagens*/
//		List<HomeImagem> listaImagens = new ArrayList<>();
//		for (HomeImagemDTO imagem : homeDTO.getGaleriaDeImagens()) {
//			if(imagem.getId() != null) {
//				HomeImagem imagemAux =  this.homeImagemRepository.getOne(imagem.getId());
//				imagemAux.setNomeAutor(imagem.getNomeAutor());
//				imagemAux.setConteudo(imagem.getConteudo());
//				imagemAux.setTitulo(imagem.getTitulo());
//				imagemAux.setSubtitulo(imagem.getSubtitulo());
//				imagemAux.setLink(imagem.getLink());
//				imagemAux.setIndice(imagem.getIndice());
//				listaImagens.add(imagemAux);
//			}else {
//				HomeImagem arquivoAux = imagem.toEntityInsert();
//				arquivoAux.setHome(homeToUpdate);
//				listaImagens.add(arquivoAux);
//			}
//		}
//		homeToUpdate.setImagens(listaImagens);

		if (!(id == homeDTO.getId())) {
			throw new Exception("Campo id divergente.");
		}

		return homeRepository.saveAndFlush(homeToUpdate);
	}

	public void atualizarMenuPagina(Home home, HomeDTO homeDTO) {
		//MenuPagina menuPagina = menuPaginaRepository.findByNome(home.getTitulo());
		if(home.getMenuPagina()!= null) {
			MenuPagina menuPagina = null;
			Optional<MenuPagina> menuPaginaOpt = menuPaginaRepository.findById(home.getMenuPagina().getId());
			if (menuPaginaOpt.isPresent()) {
				menuPagina = menuPaginaOpt.get();
				menuPagina.setNome(homeDTO.getTitulo());
				menuPagina.setRota("/inicial/" + homeDTO.getLink_pagina());
				menuPaginaRepository.save(menuPagina);
			} 
		} else {
			MenuPagina menuPagina = inserirMenuPagina(homeDTO);
			home.setMenuPagina(menuPagina);
		}

	}
	
	public HomeDTO buscarPaginaHomePorLink(String link) {		
		Home paginaRef = homeRepository.findByLink(link);

		if (paginaRef == null) {
			throw new ObjectNotFoundException("Página não encontrada!");
		}
		
		HomeDTO paginaDto = new HomeDTO(paginaRef);
		
		if(paginaDto != null && paginaDto.getGaleriaDeImagens() != null) {
			for (HomeImagemDTO imagem : paginaDto.getGaleriaDeImagens()) {
				imagem.setConteudo(null);
			}
		}

		return paginaDto;
	}
	
	public MenuPagina inserirMenuPagina(HomeDTO homeDTO) {

		StringBuilder link = new StringBuilder();
		link.append("/inicial/");
		link.append(homeDTO.getLink_pagina());
		
		MenuPagina novaPagina = new MenuPagina(homeDTO.getTitulo(), link.toString(), "Sistema");
		return menuPaginaRepository.save(novaPagina);
	}
	
	public HomeImagem buscarHomeImagemById(Long id) {
		return homeImagemRepository.getOne(id);
	}
	
	public void excluirHomeImagem(Long idImagem) {
		HomeImagem imagemAux =  this.homeImagemRepository.getOne(idImagem);
		this.homeImagemRepository.delete(imagemAux);
	}
	
	public HomeDTO buscarIdsPaginaHomePorLink(String link) {		
		HomeDTO paginaRef = homeRepository.findAllIdsByLink(link);

		if (paginaRef == null) {
			throw new ObjectNotFoundException("Página não encontrada!");
		}
		
		return paginaRef;
	}
	
	
	public HomeBarraDTO buscarHomeBarraPorId(Long id) {
		
		HomeBarra homeBarra = homeBarraRepository.findByIdHomeBarra(id);
		if(homeBarra != null) {
			return new HomeBarraDTO(homeBarra); 
		}
		return null;
	}
	
	public List<PrimeiraSecaoDTO> buscarPrimeiraSecaoPorId(Long id) {
		
		List<PrimeiraSecao> listaPrimeiraSecao = primeiraSecaoRepository.findByIdHome(id);
		List<PrimeiraSecaoDTO> listaPrimeiraSecaoDTO = null;
		if(listaPrimeiraSecao != null && !listaPrimeiraSecao.isEmpty()){
			listaPrimeiraSecaoDTO = listaPrimeiraSecao.stream().map(PrimeiraSecaoDTO::create).collect(Collectors.toList());
			return listaPrimeiraSecaoDTO;
		}
		
		return null;
	}
		
	public List<PrimeiraSecaoDTO> buscarListaPrimeiraSecaoResumidaPorId(Long id) {
		
		List<PrimeiraSecaoDTO> listaPrimeiraSecaoDTO = primeiraSecaoRepository.buscarListaPrimeiraSecaoResumidaPorId(id);

		return listaPrimeiraSecaoDTO;
	}
	
	public PrimeiraSecaoDTO buscarPrimeiraSecaoDetalhe(Long id) {
		
		PrimeiraSecaoDTO primeiraSecaoDTO = primeiraSecaoRepository.buscarPrimeiraSecaoDetalhe(id);

		return primeiraSecaoDTO;
	}
	
	public SegundaSecaoDTO buscarSegundaSecaoDetalhe(Long id) {
		
		SegundaSecaoDTO segundaSecaoDTO = segundaSecaoRepository.buscarSegundaSecaoDetalhe(id);

		return segundaSecaoDTO;
	}
	
	public TerceiraSecaoDTO buscarTerceiraSecaoDetalhe(Long id) {
		
		TerceiraSecaoDTO terceiraSecaoDTO = terceiraSecaoRepository.buscarTerceiraSecaoDetalhe(id);

		return terceiraSecaoDTO;
	}
	
	public QuartaSecaoDTO buscarQuartaSecaoDetalhe(Long id) {
		
		QuartaSecaoDTO quartaSecaoDTO = quartaSecaoRepository.buscarQuartaSecaoDetalhe(id);

		return quartaSecaoDTO;
	}
	
	public QuintaSecaoDTO buscarQuintaSecaoDetalhe(Long id) {
		
		QuintaSecaoDTO quintaSecaoDTO = quintaSecaoRepository.buscarQuintaSecaoDetalhe(id);

		return quintaSecaoDTO;
	}
	
	public SecaoLateralDTO buscarSecaoLateralDetalhe(Long id) {
		
		SecaoLateralDTO secaoLateralDTO = secaoLateralRepository.buscarSecaoLateralDetalhe(id);

		return secaoLateralDTO;
	}
	
	public SextaSecaoDTO buscarSextaSecaoDetalhe(Long id) {
		
		SextaSecaoDTO sextaSecaoDTO = sextaSecaoRepository.buscarSextaSecaoDetalhe(id);

		return sextaSecaoDTO;
	}
	
	public SetimaSecaoDTO buscarSetimaSecaoDetalhe(Long id) {
		
		SetimaSecaoDTO setimaSecaoDTO = setimaSecaoRepository.buscarSetimaSecaoDetalhe(id);

		return setimaSecaoDTO;
	}
	
	public List<SegundaSecaoDTO> buscarSegundaSecaoPorId(Long id) {
		
		List<SegundaSecao> listaSegundaSecao = segundaSecaoRepository.findByIdHome(id);
		List<SegundaSecaoDTO> listaSegundaSecaoDTO = null;
		if(listaSegundaSecao != null && !listaSegundaSecao.isEmpty()) {
			listaSegundaSecaoDTO = listaSegundaSecao.stream().map(SegundaSecaoDTO::create).collect(Collectors.toList());
			return listaSegundaSecaoDTO;
		}
		
		return null;
	}
	
	public List<SegundaSecaoDTO> buscarListaSegundaSecaoResumidaPorId(Long id) {
		
		List<SegundaSecaoDTO> listaSegundaSecaoDTO = segundaSecaoRepository.buscarListaSegundaSecaoResumidaPorId(id);

		return listaSegundaSecaoDTO;
	}
	
	
	public List<TerceiraSecaoDTO> buscarTerceiraSecaoPorId(Long id) {
		
		List<TerceiraSecao> listaTerceiraSecao = terceiraSecaoRepository.findByIdHome(id);
		List<TerceiraSecaoDTO> listaTerceiraSecaoDTO = null;
		if(listaTerceiraSecao != null && !listaTerceiraSecao.isEmpty()) {
			listaTerceiraSecaoDTO = listaTerceiraSecao.stream().map(TerceiraSecaoDTO::create).collect(Collectors.toList());
			return listaTerceiraSecaoDTO;
		}
		
		return null;
	}
	
	public List<TerceiraSecaoDTO> buscarListaTerceiraSecaoResumidaPorId(Long id) {
		
		List<TerceiraSecaoDTO> listaTerceiraSecaoDTO = terceiraSecaoRepository.buscarListaTerceiraSecaoResumidaPorId(id);

		return listaTerceiraSecaoDTO;
	}
	
	public List<QuartaSecaoDTO> buscarQuartaSecaoPorId(Long id) {
		
		List<QuartaSecao> ListaQuartaSecao = quartaSecaoRepository.findByIdHome(id);
		List<QuartaSecaoDTO> listaQuartaSecaoDTO = null;
		if(ListaQuartaSecao != null && !ListaQuartaSecao.isEmpty()) {
			listaQuartaSecaoDTO = ListaQuartaSecao.stream().map(QuartaSecaoDTO::create).collect(Collectors.toList());
			return listaQuartaSecaoDTO;
		}
		
		return null;
	}
	
	public List<QuartaSecaoDTO> buscarListaQuartaSecaoResumidaPorId(Long id) {
		
		List<QuartaSecaoDTO> listaQuartaSecaoDTO = quartaSecaoRepository.buscarListaQuartaSecaoResumidaPorId(id);

		return listaQuartaSecaoDTO;
	}
	
	public List<QuintaSecaoDTO> buscarQuintaSecaoPorId(Long id) {
		
		List<QuintaSecao> listaQuintaSecao = quintaSecaoRepository.findByIdHome(id);
		List<QuintaSecaoDTO> listaQuintaSecaoDTO = null;
		if(listaQuintaSecao != null && !listaQuintaSecao.isEmpty()) {
			listaQuintaSecaoDTO = listaQuintaSecao.stream().map(QuintaSecaoDTO::create).collect(Collectors.toList());
			return listaQuintaSecaoDTO;
		}
		return null;
	}
	
	public List<QuintaSecaoDTO> buscarListaQuintaSecaoResumidaPorId(Long id) {
		
		List<QuintaSecaoDTO> listaQuintaSecaoDTO = quintaSecaoRepository.buscarListaQuintaSecaoResumidaPorId(id);

		return listaQuintaSecaoDTO;
	}
	
	public List<SecaoLateralDTO> buscarSecaoLateralPorId(Long id) {
		
		List<SecaoLateral> listaSecaoLateral = secaoLateralRepository.findByIdHome(id);
		List<SecaoLateralDTO> secaoLateralDTO = null;
		if(listaSecaoLateral != null && !listaSecaoLateral.isEmpty()) {
			secaoLateralDTO = listaSecaoLateral.stream().map(SecaoLateralDTO::create).collect(Collectors.toList());
			return secaoLateralDTO;
		}
		return null;	
	}
	
	public List<SecaoLateralDTO> buscarListaSecaoLateralResumidaPorId(Long id) {
		
		List<SecaoLateralDTO> listaSecaoLateralDTO = secaoLateralRepository.buscarListaSecaoLateralResumidaPorId(id);

		return listaSecaoLateralDTO;
	}
	
	public List<SextaSecaoDTO> buscarSextaSecaoPorId(Long id) {	
		
		List<SextaSecao> listaSextaSecao = sextaSecaoRepository.findByIdHome(id);
		List<SextaSecaoDTO> listaSextaSecaoDTO = null;
		if(listaSextaSecao != null && !listaSextaSecao.isEmpty()) {
			listaSextaSecaoDTO = listaSextaSecao.stream().map(SextaSecaoDTO::create).collect(Collectors.toList());
			return listaSextaSecaoDTO;
		}
		
		return null;
	}
	
	public List<SextaSecaoDTO> buscarListaSextaSecaoResumidaPorId(Long id) {
		
		List<SextaSecaoDTO> listaSextaSecaoDTO = sextaSecaoRepository.buscarListaSextaSecaoResumidaPorId(id);

		return listaSextaSecaoDTO;
	}
	
	public List<SetimaSecaoDTO> buscarSetimaSecaoPorId(Long id) {
		
		List<SetimaSecao> listaSetimaSecao = setimaSecaoRepository.findByIdHome(id);
		List<SetimaSecaoDTO> listaSetimaSecaoDTO = null;
		if(listaSetimaSecao != null && !listaSetimaSecao.isEmpty()) {
			listaSetimaSecaoDTO = listaSetimaSecao.stream().map(SetimaSecaoDTO::create).collect(Collectors.toList());
			return listaSetimaSecaoDTO;
		}
		
		return null;
	}
	
	public List<SetimaSecaoDTO> buscarListaSetimaSecaoResumidaPorId(Long id) {
		
		List<SetimaSecaoDTO> listaSetimaSecaoDTO = setimaSecaoRepository.buscarListaSetimaSecaoResumidaPorId(id);

		return listaSetimaSecaoDTO;
	}
	
	
	public HomeDTO buscarIdsPaginaHomePorIdHome(Long idHome) {		
		HomeDTO paginaRef = homeRepository.findAllIdsByIdHome(idHome);

		if (paginaRef == null) {
			throw new ObjectNotFoundException("Página não encontrada!");
		}
		
		return paginaRef;
	}
	
	public List<HomeImagemDTO> buscarListaImagensGaleriaPorId(Long idHome) {		
		
		List<HomeImagem> listHomeImagens = homeImagemRepository.findAllByIdHome(idHome);
		
		if (listHomeImagens == null) {
			throw new ObjectNotFoundException("Lista de imagens não encontrada!");
		}
		
		List<HomeImagemDTO> imagensGaleria = new ArrayList<>();
		for (HomeImagem imagem : listHomeImagens) {
			imagensGaleria.add(new HomeImagemDTO(imagem));
		}
		
		return imagensGaleria.isEmpty()?null:imagensGaleria;

	}
	
	public List<HomeImagemDTO> buscarTodasSemConteudoPorIdHome(Long idHome) {		
		
		List<HomeImagemDTO> listHomeImagens = homeImagemRepository.buscarTodasSemConteudoPorIdHome(idHome);
		
		if (listHomeImagens == null) {
			throw new ObjectNotFoundException("Lista de imagens não encontrada!");
		}
		
		return listHomeImagens.isEmpty()?null:listHomeImagens;

	}
	
	public HomeBarra findByIdHomeBarra(final Long id) {
		  Optional<HomeBarra> h = homeBarraRepository.findById(id);
		  return h.orElseThrow(() -> new ObjectNotFoundException("Home barra não encontrada!"));
	}
	
	public HomeBarra editarHomeBarra(final Long idHome, final HomeBarraDTO homeBarraDTO) throws Exception {
		HomeBarra homeBarraToUpdate = null;
		if(homeBarraDTO.getId() == null) {
			homeBarraToUpdate = homeBarraDTO.toEntityInsert();
			Home homeToUpdate = findById(idHome);
			homeToUpdate.setHomeBarra(homeBarraToUpdate);
			return homeRepository.save(homeToUpdate).getHomeBarra();
		}else {
			HomeBarra homeBarra = findByIdHomeBarra(homeBarraDTO.getId());
			homeBarraToUpdate = homeBarraDTO.toEntityUpdate(homeBarra);
			return homeBarraRepository.saveAndFlush(homeBarraToUpdate);
		}
	}
	
	public HomeImagemDTO editarHomeGaleria(final Long idHome, final  HomeImagemDTO imagem) throws Exception {
		
		HomeImagem homeImagem;
		
		if(imagem.getId() != null) {
			HomeImagem imagemAux =  this.homeImagemRepository.getOne(imagem.getId());
			imagemAux.setNomeAutor(imagem.getNomeAutor());
			imagemAux.setConteudo(imagem.getConteudo());
			imagemAux.setTitulo(imagem.getTitulo());
			imagemAux.setSubtitulo(imagem.getSubtitulo());
			imagemAux.setLink(imagem.getLink());
			imagemAux.setIndice(imagem.getIndice());
			imagemAux.setExibirBusca(imagem.getExibirBusca());
			homeImagem = homeImagemRepository.saveAndFlush(imagemAux);
		}else {
			HomeImagem arquivoAux = imagem.toEntityInsert();
			Home homeToUpdate = findById(idHome);
			arquivoAux.setHome(homeToUpdate);
			homeImagem = homeImagemRepository.saveAndFlush(arquivoAux);
		}
		HomeImagemDTO homeImagemDTO = new HomeImagemDTO();
		homeImagemDTO.setId(homeImagem.getId());
		return homeImagemDTO;
	}
	
	public PrimeiraSecao findByIdPrimeiraSecao(final Long id) {
		  Optional<PrimeiraSecao> h = primeiraSecaoRepository.findById(id);
		  return h.orElseThrow(() -> new ObjectNotFoundException("Primeira Seção não encontrada!"));
	}
	
	public PrimeiraSecao editarPrimeiraSecao(final Long idHome, final PrimeiraSecaoDTO primeiraSecaoDTO) throws Exception {
		
		PrimeiraSecao primeiraSecaoRef = null;
		if(primeiraSecaoDTO.getId() != null) {
			primeiraSecaoRef = findByIdPrimeiraSecao(primeiraSecaoDTO.getId());
		}
		
		Home homeRef = null;
		if(idHome != null) {
			homeRef = findById(idHome);
		}
	
		
		PrimeiraSecao primeiraSecaoToUpdate = null;
		if(primeiraSecaoRef != null && primeiraSecaoDTO != null) {
			primeiraSecaoToUpdate = primeiraSecaoDTO.toEntityUpdate(primeiraSecaoRef);
			primeiraSecaoToUpdate.setHome(homeRef);
			primeiraSecaoToUpdate.setTipo("primeiraSecao");
			return primeiraSecaoRepository.saveAndFlush(primeiraSecaoToUpdate);
		}else if (primeiraSecaoRef == null && primeiraSecaoDTO != null)  {
			primeiraSecaoToUpdate = primeiraSecaoDTO.toEntityInsert();
			primeiraSecaoToUpdate.setHome(homeRef);
			primeiraSecaoToUpdate.setTipo("primeiraSecao");
			return primeiraSecaoRepository.saveAndFlush(primeiraSecaoToUpdate);
		}
	
		return null;
	}
	
	public void excluirPrimeiraSecao(Long id) {
		PrimeiraSecao primeiraSecao =  this.primeiraSecaoRepository.getOne(id);
		this.primeiraSecaoRepository.delete(primeiraSecao);
	}
	
	public SegundaSecao findByIdSegundaSecao(final Long id) {
		  Optional<SegundaSecao> h = segundaSecaoRepository.findById(id);
		  return h.orElseThrow(() -> new ObjectNotFoundException("Segunda Seção não encontrada!"));
	}
	
	public SegundaSecao editarSegundaSecao(final Long idHome, final SegundaSecaoDTO segundaSecaoDTO) throws Exception {
		
		
		SegundaSecao segundaSecaoRef = null;
		if(segundaSecaoDTO.getId() != null) {
			segundaSecaoRef = findByIdSegundaSecao(segundaSecaoDTO.getId());
		}
		
		Home homeRef = null;
		if(idHome != null) {
			homeRef = findById(idHome);
		}
	
		
		SegundaSecao segundaSecaoToUpdate = null;
		if(segundaSecaoRef != null && segundaSecaoDTO != null) {
			segundaSecaoToUpdate = segundaSecaoDTO.toEntityUpdate(segundaSecaoRef);
			segundaSecaoToUpdate.setHome(homeRef);
			segundaSecaoToUpdate.setTipo("segundaSecao");
			return segundaSecaoRepository.saveAndFlush(segundaSecaoToUpdate);
		}else if (segundaSecaoRef == null && segundaSecaoDTO != null)  {
			segundaSecaoToUpdate = segundaSecaoDTO.toEntityInsert();
			segundaSecaoToUpdate.setHome(homeRef);
			segundaSecaoToUpdate.setTipo("segundaSecao");
			return segundaSecaoRepository.saveAndFlush(segundaSecaoToUpdate);
		}
	
		return null;
	
	}
	
	public void excluirSegundaSecao(Long id) {
		SegundaSecao segundaSecao =  this.segundaSecaoRepository.getOne(id);
		this.segundaSecaoRepository.delete(segundaSecao);
	}
	
	
	public TerceiraSecao findByIdTerceiraSecao(final Long id) {
		  Optional<TerceiraSecao> h = terceiraSecaoRepository.findById(id);
		  return h.orElseThrow(() -> new ObjectNotFoundException("Terceira Seção não encontrada!"));
	}
	
	public TerceiraSecao editarTerceiraSecao(final Long idHome, final TerceiraSecaoDTO terceiraSecaoDTO) throws Exception {
		
		
		TerceiraSecao terceiraSecaoRef = null;
		if(terceiraSecaoDTO.getId() != null) {
			terceiraSecaoRef = findByIdTerceiraSecao(terceiraSecaoDTO.getId());
		}
		
		Home homeRef = null;
		if(idHome != null) {
			homeRef = findById(idHome);
		}
	
		
		TerceiraSecao terceiraSecaoToUpdate = null;
		if(terceiraSecaoRef != null && terceiraSecaoDTO != null) {
			terceiraSecaoToUpdate = terceiraSecaoDTO.toEntityUpdate(terceiraSecaoRef);
			terceiraSecaoToUpdate.setHome(homeRef);
			terceiraSecaoToUpdate.setTipo("terceiraSecao");
			return terceiraSecaoRepository.saveAndFlush(terceiraSecaoToUpdate);
		}else if (terceiraSecaoRef == null && terceiraSecaoDTO != null)  {
			terceiraSecaoToUpdate = terceiraSecaoDTO.toEntityInsert();
			terceiraSecaoToUpdate.setHome(homeRef);
			terceiraSecaoToUpdate.setTipo("terceiraSecao");
			return terceiraSecaoRepository.saveAndFlush(terceiraSecaoToUpdate);
		}
	
		return null;
	}
	
	public void excluirTerceiraSecao(Long id) {
		TerceiraSecao terceiraSecao =  this.terceiraSecaoRepository.getOne(id);
		this.terceiraSecaoRepository.delete(terceiraSecao);
	}
	
	
	public QuartaSecao findByIdQuartaSecao(final Long id) {
		  Optional<QuartaSecao> h = quartaSecaoRepository.findById(id);
		  return h.orElseThrow(() -> new ObjectNotFoundException("Quarta Seção não encontrada!"));
	}
	
	public QuartaSecao editarQuartaSecao(final Long idHome, final QuartaSecaoDTO quartaSecaoDTO) throws Exception {

		QuartaSecao quartaSecaoRef = null;
		if(quartaSecaoDTO.getId() != null) {
			quartaSecaoRef = findByIdQuartaSecao(quartaSecaoDTO.getId());
		}
		
		Home homeRef = null;
		if(idHome != null) {
			homeRef = findById(idHome);
		}
		
		QuartaSecao quartaSecaoToUpdate = null;
		if(quartaSecaoRef != null && quartaSecaoDTO != null) {
			quartaSecaoToUpdate = quartaSecaoDTO.toEntityUpdate(quartaSecaoRef);
			quartaSecaoToUpdate.setHome(homeRef);
			quartaSecaoToUpdate.setTipo("quartaSecao");
			return quartaSecaoRepository.saveAndFlush(quartaSecaoToUpdate);
		}else if (quartaSecaoRef == null && quartaSecaoDTO != null)  {
			quartaSecaoToUpdate = quartaSecaoDTO.toEntityInsert();
			quartaSecaoToUpdate.setHome(homeRef);
			quartaSecaoToUpdate.setTipo("quartaSecao");
			return quartaSecaoRepository.saveAndFlush(quartaSecaoToUpdate);
		}
	
		return null;
	
	}
	
	public void excluirQuartaSecao(Long id) {
		QuartaSecao quartaSecao =  this.quartaSecaoRepository.getOne(id);
		this.quartaSecaoRepository.delete(quartaSecao);
	}
	
	
	public QuintaSecao findByIdQuintaSecao(final Long id) {
		  Optional<QuintaSecao> h = quintaSecaoRepository.findById(id);
		  return h.orElseThrow(() -> new ObjectNotFoundException("Quinta Seção não encontrada!"));
	}
	
	public QuintaSecao editarQuintaSecao(final Long idHome, final QuintaSecaoDTO quintaSecaoDTO) throws Exception {

		QuintaSecao quintaSecaoRef = null;
		if(quintaSecaoDTO.getId() != null) {
			quintaSecaoRef = findByIdQuintaSecao(quintaSecaoDTO.getId());
		}
		
		Home homeRef = null;
		if(idHome != null) {
			homeRef = findById(idHome);
		}
		
		QuintaSecao quintaSecaoToUpdate = null;
		if(quintaSecaoRef != null && quintaSecaoDTO != null) {
			quintaSecaoToUpdate = quintaSecaoDTO.toEntityUpdate(quintaSecaoRef);
			quintaSecaoToUpdate.setHome(homeRef);
			quintaSecaoToUpdate.setTipo("quintaSecao");
			return quintaSecaoRepository.saveAndFlush(quintaSecaoToUpdate);
		}else if (quintaSecaoRef == null && quintaSecaoDTO != null)  {
			quintaSecaoToUpdate = quintaSecaoDTO.toEntityInsert();
			quintaSecaoToUpdate.setHome(homeRef);
			quintaSecaoToUpdate.setTipo("quintaSecao");
			return quintaSecaoRepository.saveAndFlush(quintaSecaoToUpdate);
		}
	
		return null;
	}
	
	public void excluirQuintaSecao(Long id) {
		QuintaSecao quintaSecao =  this.quintaSecaoRepository.getOne(id);
		this.quintaSecaoRepository.delete(quintaSecao);
	}
	
	public SecaoLateral findByIdSecaoLateral(final Long id) {
		  Optional<SecaoLateral> h = secaoLateralRepository.findById(id);
		  return h.orElseThrow(() -> new ObjectNotFoundException("Quinta Seção não encontrada!"));
	}
	
	public SecaoLateral editarSecaoLateral(final Long idHome, final SecaoLateralDTO secaoLateralDTO) throws Exception {

		SecaoLateral secaoLateralRef = null;
		if(secaoLateralDTO.getId() != null) {
			secaoLateralRef = findByIdSecaoLateral(secaoLateralDTO.getId());
		}
		
		Home homeRef = null;
		if(idHome != null) {
			homeRef = findById(idHome);
		}
		
		SecaoLateral secaoLateralToUpdate = null;
		if(secaoLateralRef != null && secaoLateralDTO != null) {
			secaoLateralToUpdate = secaoLateralDTO.toEntityUpdate(secaoLateralRef);
			secaoLateralToUpdate.setHome(homeRef);
			secaoLateralToUpdate.setTipo("secaoLateral");
			return secaoLateralRepository.saveAndFlush(secaoLateralToUpdate);
		}else if (secaoLateralRef == null && secaoLateralDTO != null)  {
			secaoLateralToUpdate = secaoLateralDTO.toEntityInsert();
			secaoLateralToUpdate.setHome(homeRef);
			secaoLateralToUpdate.setTipo("secaoLateral");
			return secaoLateralRepository.saveAndFlush(secaoLateralToUpdate);
		}
	
		return null;
	}
	
	public void excluirSecaoLateral(Long id) {
		SecaoLateral secaoLateral =  this.secaoLateralRepository.getOne(id);
		this.secaoLateralRepository.delete(secaoLateral);
	}
	
	public SextaSecao findByIdSextaSecao(final Long id) {
		  Optional<SextaSecao> h = sextaSecaoRepository.findById(id);
		  return h.orElseThrow(() -> new ObjectNotFoundException("Sexta Seção não encontrada!"));
	}
	
	public SextaSecao editarSextaSecao(final Long idHome, final SextaSecaoDTO sextaSecaoDTO) throws Exception {
		
		
		SextaSecao sextaSecaoRef = null;
		if(sextaSecaoDTO.getId() != null) {
			sextaSecaoRef = findByIdSextaSecao(sextaSecaoDTO.getId());
		}
		
		Home homeRef = null;
		if(idHome != null) {
			homeRef = findById(idHome);
		}
	
		
		SextaSecao sextaSecaoToUpdate = null;
		if(sextaSecaoRef != null && sextaSecaoDTO != null) {
			sextaSecaoToUpdate = sextaSecaoDTO.toEntityUpdate(sextaSecaoRef);
			sextaSecaoToUpdate.setHome(homeRef);
			sextaSecaoToUpdate.setTipo("sextaSecao");
			return sextaSecaoRepository.saveAndFlush(sextaSecaoToUpdate);
		}else if (sextaSecaoRef == null && sextaSecaoDTO != null)  {
			sextaSecaoToUpdate = sextaSecaoDTO.toEntityInsert();
			sextaSecaoToUpdate.setHome(homeRef);
			sextaSecaoToUpdate.setTipo("sextaSecao");
			return sextaSecaoRepository.saveAndFlush(sextaSecaoToUpdate);
		}
	
		return null;
	}
	
	public void excluirSextaSecao(Long id) {
		SextaSecao sextaSecao =  this.sextaSecaoRepository.getOne(id);
		this.sextaSecaoRepository.delete(sextaSecao);
	}
	
	public SetimaSecao findByIdSetimaSecao(final Long id) {
		  Optional<SetimaSecao> h = setimaSecaoRepository.findById(id);
		  return h.orElseThrow(() -> new ObjectNotFoundException("Setima Seção não encontrada!"));
	}
	
	public SetimaSecao editarSetimaSecao(final Long idHome, final SetimaSecaoDTO setimaSecaoDTO) throws Exception {
		
		
		SetimaSecao setimaSecaoRef = null;
		if(setimaSecaoDTO.getId() != null) {
			setimaSecaoRef = findByIdSetimaSecao(setimaSecaoDTO.getId());
		}
		
		Home homeRef = null;
		if(idHome != null) {
			homeRef = findById(idHome);
		}
	
		
		SetimaSecao setimaSecaoToUpdate = null;
		if(setimaSecaoRef != null && setimaSecaoDTO != null) {
			setimaSecaoToUpdate = setimaSecaoDTO.toEntityUpdate(setimaSecaoRef);
			setimaSecaoToUpdate.setHome(homeRef);
			setimaSecaoToUpdate.setTipo("setimaSecao");
			return setimaSecaoRepository.saveAndFlush(setimaSecaoToUpdate);
		}else if (setimaSecaoRef == null && setimaSecaoDTO != null)  {
			setimaSecaoToUpdate = setimaSecaoDTO.toEntityInsert();
			setimaSecaoToUpdate.setHome(homeRef);
			setimaSecaoToUpdate.setTipo("setimaSecao");
			return setimaSecaoRepository.saveAndFlush(setimaSecaoToUpdate);
		}
	
		return null;
	
	}
	
	public void excluirSetimaSecao(Long id) {
		SetimaSecao setimaSecao =  this.setimaSecaoRepository.getOne(id);
		this.setimaSecaoRepository.delete(setimaSecao);
	}
	
	public Long editarIndiceSecao(final Long idSecao, final String tipo, Long indice) throws Exception {
		
		if(tipo.equals("primeiraSecao")) {
			PrimeiraSecao primeiraSecaoRef = findByIdPrimeiraSecao(idSecao);
			primeiraSecaoRef.setIndice(indice);
			primeiraSecaoRef = primeiraSecaoRepository.saveAndFlush(primeiraSecaoRef);
			return primeiraSecaoRef.getId();
		}
		
		if(tipo.equals("segundaSecao")) {
			SegundaSecao segundaSecaoRef = findByIdSegundaSecao(idSecao);
			segundaSecaoRef.setIndice(indice);
			segundaSecaoRef = segundaSecaoRepository.saveAndFlush(segundaSecaoRef);
			return segundaSecaoRef.getId();
		}
		
		if(tipo.equals("terceiraSecao")) {
			TerceiraSecao terceiraSecaoRef = findByIdTerceiraSecao(idSecao);
			terceiraSecaoRef.setIndice(indice);
			terceiraSecaoRef = terceiraSecaoRepository.saveAndFlush(terceiraSecaoRef);
			return terceiraSecaoRef.getId();
		}
		
		if(tipo.equals("quartaSecao")) {
			QuartaSecao quartaSecaoRef = findByIdQuartaSecao(idSecao);
			quartaSecaoRef.setIndice(indice);
			quartaSecaoRef = quartaSecaoRepository.saveAndFlush(quartaSecaoRef);
			return quartaSecaoRef.getId();
		}
		
		if(tipo.equals("quintaSecao")) {
			QuintaSecao quintaSecaoRef = findByIdQuintaSecao(idSecao);
			quintaSecaoRef.setIndice(indice);
			quintaSecaoRef = quintaSecaoRepository.saveAndFlush(quintaSecaoRef);
			return quintaSecaoRef.getId();
		}
		
		if(tipo.equals("secaoLateral")) {
			SecaoLateral secaoLateralRef = findByIdSecaoLateral(idSecao);
			secaoLateralRef.setIndice(indice);
			secaoLateralRef = secaoLateralRepository.saveAndFlush(secaoLateralRef);
			return secaoLateralRef.getId();
		}
		
		if(tipo.equals("sextaSecao")) {
			SextaSecao sextaSecaoRef = findByIdSextaSecao(idSecao);
			sextaSecaoRef.setIndice(indice);
			sextaSecaoRef = sextaSecaoRepository.saveAndFlush(sextaSecaoRef);
			return sextaSecaoRef.getId();
		}
		
		if(tipo.equals("setimaSecao")) {
			SetimaSecao setimaSecaoRef = findByIdSetimaSecao(idSecao);
			setimaSecaoRef.setIndice(indice);
			setimaSecaoRef = setimaSecaoRepository.saveAndFlush(setimaSecaoRef);
			return setimaSecaoRef.getId();
		}
	
		
		return null;
	}
	
	public List<HomeDTO> buscarTodas() {
		List<HomeDTO> listaHome = homeRepository.findAllByOrderByTituloAsc();
		return listaHome;
	}
	
	public HomeDTO existePaginaHomeComLink(String link) {
		Long id = homeRepository.findByLinkOnlyId(link);
		HomeDTO homeDTO = new HomeDTO();
		homeDTO.setId(id);
		return homeDTO;
	}
	
	public void excluirPaginaHome(Long id) {
		Home home =  this.homeRepository.getOne(id);
		this.homeRepository.delete(home);
	}

	
}
