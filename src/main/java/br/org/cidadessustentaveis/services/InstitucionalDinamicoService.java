package br.org.cidadessustentaveis.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.xmlbeans.impl.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.org.cidadessustentaveis.dto.HomeImagemDTO;
import br.org.cidadessustentaveis.dto.InstitucionalDinamicoDTO;
import br.org.cidadessustentaveis.dto.InstitucionalDinamicoImagemDTO;
import br.org.cidadessustentaveis.dto.InstitucionalDinamicoPublicacaoDTO;
import br.org.cidadessustentaveis.dto.InstitucionalDinamicoSecao01DTO;
import br.org.cidadessustentaveis.dto.InstitucionalDinamicoSecao02DTO;
import br.org.cidadessustentaveis.dto.InstitucionalDinamicoSecao03DTO;
import br.org.cidadessustentaveis.dto.InstitucionalDinamicoSecao04DTO;
import br.org.cidadessustentaveis.model.administracao.MenuPagina;
import br.org.cidadessustentaveis.model.home.HomeImagem;
import br.org.cidadessustentaveis.model.institucional.InstitucionalDinamico;
import br.org.cidadessustentaveis.model.institucional.InstitucionalDinamicoImagem;
import br.org.cidadessustentaveis.model.institucional.InstitucionalDinamicoPublicacao;
import br.org.cidadessustentaveis.model.institucional.InstitucionalDinamicoSecao01;
import br.org.cidadessustentaveis.model.institucional.InstitucionalDinamicoSecao02;
import br.org.cidadessustentaveis.model.institucional.InstitucionalDinamicoSecao03;
import br.org.cidadessustentaveis.model.institucional.InstitucionalDinamicoSecao04;
import br.org.cidadessustentaveis.repository.InstitucionalDinamicoImagemRepository;
import br.org.cidadessustentaveis.repository.InstitucionalDinamicoRepository;
import br.org.cidadessustentaveis.repository.InstitucionalDinamicoSecao01Repository;
import br.org.cidadessustentaveis.repository.InstitucionalDinamicoSecao02Repository;
import br.org.cidadessustentaveis.repository.InstitucionalDinamicoSecao03Repository;
import br.org.cidadessustentaveis.repository.InstitucionalDinamicoSecao04Repository;
import br.org.cidadessustentaveis.repository.MenuPaginaRepository;
import br.org.cidadessustentaveis.services.exceptions.ObjectNotFoundException;
import br.org.cidadessustentaveis.util.ImageUtils;

@Service
public class InstitucionalDinamicoService {

		
	@Autowired
	private MenuPaginaRepository menuPaginaRepository;
	
	@Autowired
	private InstitucionalDinamicoRepository institucionalDinamicoRepository;
	
	@Autowired
	private InstitucionalDinamicoImagemRepository institucionalDinamicoImagemRepository;	
	
	@Autowired	
	private InstitucionalDinamicoSecao01Repository institucionalDinamicoSecao01Repository;
	
	@Autowired	
	private InstitucionalDinamicoSecao02Repository institucionalDinamicoSecao02Repository;
	
	@Autowired	
	private InstitucionalDinamicoSecao03Repository institucionalDinamicoSecao03Repository;
	
	@Autowired	
	private InstitucionalDinamicoSecao04Repository institucionalDinamicoSecao04Repository;
	
	@Autowired
	private InstitucionalDinamicoPublicacaoService institucionalDinamicoPublicacaoService;
	
	
	//MENU PÁGINA
	
	public MenuPagina findByIdMenuPagina(final Long id) {
		  Optional<MenuPagina> institucional = menuPaginaRepository.findById(id);
		  return institucional.orElseThrow(() -> new ObjectNotFoundException("Menu Página não encontrado!"));
	}
	
	public void atualizarMenuPagina(InstitucionalDinamico institucionalDinamico, InstitucionalDinamicoDTO institucionalDinamicoDTO) {
		MenuPagina menuPagina = this.findByIdMenuPagina(institucionalDinamico.getMenuPagina().getId());
		menuPagina.setNome(institucionalDinamicoDTO.getTitulo());
		menuPagina.setRota("/paginas/" + institucionalDinamicoDTO.getLink_pagina());
		menuPaginaRepository.save(menuPagina);
	}
	
	public MenuPagina inserirMenuPagina(InstitucionalDinamicoDTO institucionalDinamicoDTO) {

		StringBuilder link = new StringBuilder();
		link.append("/paginas/");
		link.append(institucionalDinamicoDTO.getLink_pagina());
		
		MenuPagina novaPagina = new MenuPagina(institucionalDinamicoDTO.getTitulo(), link.toString(), "Sistema");
		return menuPaginaRepository.save(novaPagina);
	}
	

	//INSTITUCIONAL DINÂMICO
	
	public InstitucionalDinamico findById(final Long id) {
		  Optional<InstitucionalDinamico> institucional = institucionalDinamicoRepository.findById(id);
		  return institucional.orElseThrow(() -> new ObjectNotFoundException("Home não encontrada!"));
	}
	
	public InstitucionalDinamico listarById(final Long id) {
		Optional<InstitucionalDinamico> institucional = institucionalDinamicoRepository.findById(id);
		return institucional.orElseThrow(() -> new ObjectNotFoundException("Institucional não encontrado!"));
	}
	
	public List<InstitucionalDinamicoDTO> buscarTodas() {
		List<InstitucionalDinamicoDTO> listaInstitucional = institucionalDinamicoRepository.findAllByOrderByTituloAsc();
		return listaInstitucional;
	}
	

	public InstitucionalDinamicoDTO buscarParaEdicao(Long id) {
		InstitucionalDinamico institucionalDinamicoRef = findById(id);
		InstitucionalDinamicoDTO institucionalDinamicoDTO = new InstitucionalDinamicoDTO(institucionalDinamicoRef);
		return institucionalDinamicoDTO;
	}
	
	
	public InstitucionalDinamicoDTO buscarInstitucionalDinamicoPorLink(String link) {		
		InstitucionalDinamico paginaRef = institucionalDinamicoRepository.findByLink(link);

		if (paginaRef == null) {
			throw new ObjectNotFoundException("Página não encontrada!");
		}
		
		InstitucionalDinamicoDTO paginaDto = new InstitucionalDinamicoDTO(paginaRef);

		return paginaDto;
	}
	
	public InstitucionalDinamicoDTO buscarIdsInstitucionalDinamicoPorLink(String link) {		
		InstitucionalDinamicoDTO paginaRef = institucionalDinamicoRepository.findAllIdsByLink(link);

		if (paginaRef == null) {
			throw new ObjectNotFoundException("Página não encontrada!");
		}
		
		return paginaRef;
	}
	
	public InstitucionalDinamicoDTO buscarIdsPaginaInstitucionalDinamicoPorId(Long idInstitucionalDinamico) {		
		InstitucionalDinamicoDTO paginaRef = institucionalDinamicoRepository.findAllIdsByIdInstitucionalDinamico(idInstitucionalDinamico);
		if (paginaRef == null) {
			throw new ObjectNotFoundException("Página não encontrada!");
		}
		
		return paginaRef;
	}
	
	public InstitucionalDinamicoDTO existePaginaInstitucionalComLink(String link) {
		Long id = institucionalDinamicoRepository.findByLinkOnlyId(link);
		InstitucionalDinamico paginaRef = new InstitucionalDinamico();
		paginaRef.setId(id);		
		return new InstitucionalDinamicoDTO(paginaRef);
	}
	
	public InstitucionalDinamico inserir(InstitucionalDinamicoDTO institucionalDinamicoDTO) throws IOException {

		InstitucionalDinamico institucionalDinamicoToInsert = institucionalDinamicoDTO.toEntityInsert();
		
		MenuPagina menuPagina = inserirMenuPagina(institucionalDinamicoDTO);
		
		
		institucionalDinamicoToInsert.setMenuPagina(menuPagina);
		

		/*Prepara imagens*/
		List<InstitucionalDinamicoImagem> listaImagens = new ArrayList<>();
		for (InstitucionalDinamicoImagemDTO imagem : institucionalDinamicoDTO.getImagens()) {
			InstitucionalDinamicoImagem imagemAux =  new InstitucionalDinamicoImagem();
			imagemAux.setBytes(Base64.decode(imagem.getConteudo().getBytes()));
			imagemAux.setNomeAutor(imagem.getNomeAutor());
			imagemAux.setIndice(imagem.getIndice());
			imagemAux.setInstitucional(institucionalDinamicoToInsert);
			imagemAux.setSubtitulo(imagem.getSubtitulo());
			listaImagens.add(imagemAux);
		}
		
		institucionalDinamicoToInsert.setImagens(listaImagens);
		
		
		return institucionalDinamicoRepository.save(institucionalDinamicoToInsert);
	}

	public InstitucionalDinamico alterar(final Long id, final InstitucionalDinamicoDTO institucionalDinamicoDTO) throws Exception {
		InstitucionalDinamico institucionalDinamico = findById(id);

		atualizarMenuPagina(institucionalDinamico, institucionalDinamicoDTO);

		InstitucionalDinamico institucionalDinamicoToUpdate = institucionalDinamicoDTO.toEntityUpdate(institucionalDinamico);

		if (!(id == institucionalDinamicoDTO.getId())) {
			throw new Exception("Campo id divergente.");
		}

		return institucionalDinamicoRepository.saveAndFlush(institucionalDinamicoToUpdate);
	}
	
	
	public void excluirInstitucionalDinamico(Long id) {
		InstitucionalDinamico institucionalDinamico =  this.institucionalDinamicoRepository.getOne(id);
		this.institucionalDinamicoRepository.delete(institucionalDinamico);
	}
	
	
	
	//INDICES
	public Long editarIndiceSecao(final Long idSecao, final String tipo, Long indice) throws Exception {
		
		if(tipo.equals("primeiraSecao")) {
			InstitucionalDinamicoSecao01 primeiraSecaoRef = findByIdInstitucionalDinamicoSecao01(idSecao);
			primeiraSecaoRef.setIndice(indice);
			primeiraSecaoRef = institucionalDinamicoSecao01Repository.saveAndFlush(primeiraSecaoRef);
			return primeiraSecaoRef.getId();
		}else if(tipo.equals("segundaSecao")) {
			InstitucionalDinamicoSecao02 segundaSecaoRef = findByIdInstitucionalDinamicoSecao02(idSecao);
			segundaSecaoRef.setIndice(indice);
			segundaSecaoRef = institucionalDinamicoSecao02Repository.saveAndFlush(segundaSecaoRef);
			return segundaSecaoRef.getId();
		}else if(tipo.equals("terceiraSecao")) {
			InstitucionalDinamicoSecao03 terceiraSecaoRef = findByIdInstitucionalDinamicoSecao03(idSecao);
			terceiraSecaoRef.setIndice(indice);
			terceiraSecaoRef = institucionalDinamicoSecao03Repository.saveAndFlush(terceiraSecaoRef);
			return terceiraSecaoRef.getId();
		}else if(tipo.equals("quartaSecao")) {
			InstitucionalDinamicoSecao04 quartaSecaoRef = findByIdInstitucionalDinamicoSecao04(idSecao);
			quartaSecaoRef.setIndice(indice);
			quartaSecaoRef = institucionalDinamicoSecao04Repository.saveAndFlush(quartaSecaoRef);
			return quartaSecaoRef.getId();
		}
	
		
		return null;
	}
	
	
	
	
	// INSTITUCIONAL DINÂMICO SEÇÃO 01
	public List<InstitucionalDinamicoSecao01DTO> buscarInstitucionalDinamicoSecao01PorId(Long id) {	
		
		List<InstitucionalDinamicoSecao01> listaPrimeiraSecao = institucionalDinamicoSecao01Repository.findByIdInstitucionalDinamico(id);
		List<InstitucionalDinamicoSecao01DTO> listaPrimeiraSecaoDTO = null;
		if(listaPrimeiraSecao != null && !listaPrimeiraSecao.isEmpty()){
			listaPrimeiraSecaoDTO = listaPrimeiraSecao.stream().map(InstitucionalDinamicoSecao01DTO::create).collect(Collectors.toList());
			return listaPrimeiraSecaoDTO;
		}
		
		return null;
	}
		
	public List<InstitucionalDinamicoSecao01DTO> buscarListaInstitucionalDinamicoSecao01ResumidaPorId(Long id) {
		
		List<InstitucionalDinamicoSecao01DTO> listaPrimeiraSecaoDTO = institucionalDinamicoSecao01Repository.buscarListaInstitucionalDinamicoSecao01ResumidaPorId(id);

		return listaPrimeiraSecaoDTO;
	}
	
	public InstitucionalDinamicoSecao01DTO buscarInstitucionalDinamicoSecao01Detalhe(Long id) {
		
		InstitucionalDinamicoSecao01DTO primeiraSecaoDTO = institucionalDinamicoSecao01Repository.buscarInstitucionalDinamicoSecao01Detalhe(id);

		return primeiraSecaoDTO;
	}
	
	
	public InstitucionalDinamicoSecao01 findByIdInstitucionalDinamicoSecao01(final Long id) {
		  Optional<InstitucionalDinamicoSecao01> h = institucionalDinamicoSecao01Repository.findById(id);
		  return h.orElseThrow(() -> new ObjectNotFoundException("Primeira Seção não encontrada!"));
	}
	
	public InstitucionalDinamicoSecao01 editarInstitucionalDinamicoSecao01(final Long idInstitucionalDinamico, final InstitucionalDinamicoSecao01DTO institucionalDinamicoSecao01DTO) throws Exception {
		
		InstitucionalDinamicoSecao01 primeiraSecaoRef = null;
		if(institucionalDinamicoSecao01DTO.getId() != null) {
			primeiraSecaoRef = findByIdInstitucionalDinamicoSecao01(institucionalDinamicoSecao01DTO.getId());
		}
		
		InstitucionalDinamico institucionalDinamicoRef = null;
		if(idInstitucionalDinamico != null) {
			institucionalDinamicoRef = findById(idInstitucionalDinamico);
		}
	
		
		InstitucionalDinamicoSecao01 primeiraSecaoToUpdate = null;
		if(primeiraSecaoRef != null && institucionalDinamicoSecao01DTO != null) {
			primeiraSecaoToUpdate = institucionalDinamicoSecao01DTO.toEntityUpdate(primeiraSecaoRef);
			primeiraSecaoToUpdate.setInstitucionalDinamico(institucionalDinamicoRef);
			primeiraSecaoToUpdate.setTipo("primeiraSecao");
			return institucionalDinamicoSecao01Repository.saveAndFlush(primeiraSecaoToUpdate);
		}else if (primeiraSecaoRef == null && institucionalDinamicoSecao01DTO != null)  {
			primeiraSecaoToUpdate = institucionalDinamicoSecao01DTO.toEntityInsert();
			primeiraSecaoToUpdate.setInstitucionalDinamico(institucionalDinamicoRef);
			primeiraSecaoToUpdate.setTipo("primeiraSecao");
			return institucionalDinamicoSecao01Repository.saveAndFlush(primeiraSecaoToUpdate);
		}
	
		return null;
	}
	
	public void excluirInstitucionalDinamicoSecao01(Long id) {
		InstitucionalDinamicoSecao01 primeiraSecao =  this.institucionalDinamicoSecao01Repository.getOne(id);
		this.institucionalDinamicoSecao01Repository.delete(primeiraSecao);
	}
	
	
	
	
	// INSTITUCIONAL DINÂMICO SEÇÃO 02
	public List<InstitucionalDinamicoSecao02DTO> buscarInstitucionalDinamicoSecao02PorId(Long id) {	
		
		List<InstitucionalDinamicoSecao02> listaSegundaSecao = institucionalDinamicoSecao02Repository.findByIdInstitucionalDinamico(id);
		List<InstitucionalDinamicoSecao02DTO> listaSegundaSecaoDTO = null;
		if(listaSegundaSecao != null && !listaSegundaSecao.isEmpty()){
			listaSegundaSecaoDTO = listaSegundaSecao.stream().map(InstitucionalDinamicoSecao02DTO::create).collect(Collectors.toList());
			return listaSegundaSecaoDTO;
		}
		
		return null;
	}
		
	public List<InstitucionalDinamicoSecao02DTO> buscarListaInstitucionalDinamicoSecao02ResumidaPorId(Long id) {
		
		List<InstitucionalDinamicoSecao02DTO> listaSegundaSecaoDTO = institucionalDinamicoSecao02Repository.buscarListaInstitucionalDinamicoSecao02ResumidaPorId(id);

		return listaSegundaSecaoDTO;
	}
	
	public InstitucionalDinamicoSecao02DTO buscarInstitucionalDinamicoSecao02Detalhe(Long id) {
		
		InstitucionalDinamicoSecao02DTO segundaSecaoDTO = institucionalDinamicoSecao02Repository.buscarInstitucionalDinamicoSecao02Detalhe(id);

		return segundaSecaoDTO;
	}
	
	
	public InstitucionalDinamicoSecao02 findByIdInstitucionalDinamicoSecao02(final Long id) {
		  Optional<InstitucionalDinamicoSecao02> h = institucionalDinamicoSecao02Repository.findById(id);
		  return h.orElseThrow(() -> new ObjectNotFoundException("Segunda Seção não encontrada!"));
	}
	
	public InstitucionalDinamicoSecao02 editarInstitucionalDinamicoSecao02(final Long idInstitucionalDinamico, final InstitucionalDinamicoSecao02DTO institucionalDinamicoSecao02DTO) throws Exception {
		
		InstitucionalDinamicoSecao02 segundaSecaoRef = null;
		if(institucionalDinamicoSecao02DTO.getId() != null) {
			segundaSecaoRef = findByIdInstitucionalDinamicoSecao02(institucionalDinamicoSecao02DTO.getId());
		}
		
		InstitucionalDinamico institucionalDinamicoRef = null;
		if(idInstitucionalDinamico != null) {
			institucionalDinamicoRef = findById(idInstitucionalDinamico);
		}
	
		
		InstitucionalDinamicoSecao02 segundaSecaoToUpdate = null;
		if(segundaSecaoRef != null && institucionalDinamicoSecao02DTO != null) {
			segundaSecaoToUpdate = institucionalDinamicoSecao02DTO.toEntityUpdate(segundaSecaoRef);
			segundaSecaoToUpdate.setInstitucionalDinamico(institucionalDinamicoRef);
			segundaSecaoToUpdate.setTipo("segundaSecao");
			
			if(null != institucionalDinamicoSecao02DTO.getImagem() && !institucionalDinamicoSecao02DTO.getImagem().isEmpty()) {
				try {
					InstitucionalDinamicoImagem imagem = new InstitucionalDinamicoImagem(ImageUtils.compressBase64Image(institucionalDinamicoSecao02DTO.getImagem()));
					segundaSecaoToUpdate.setImagemPrincipal(imagem);
				} catch (IOException e) {
					throw new Exception("Erro ao converter Imagem");
				}

			}else if( segundaSecaoRef.getImagemPrincipal() != null && institucionalDinamicoSecao02DTO.getIdImagem() == null) {
				segundaSecaoToUpdate.setImagemPrincipal(null);
			}
	
			return institucionalDinamicoSecao02Repository.saveAndFlush(segundaSecaoToUpdate);
		}else if (segundaSecaoRef == null && institucionalDinamicoSecao02DTO != null)  {
			segundaSecaoToUpdate = institucionalDinamicoSecao02DTO.toEntityInsert();
			segundaSecaoToUpdate.setInstitucionalDinamico(institucionalDinamicoRef);
			segundaSecaoToUpdate.setTipo("segundaSecao");
			
			if(null != institucionalDinamicoSecao02DTO.getImagem() && !institucionalDinamicoSecao02DTO.getImagem().isEmpty()) {
				try {
					InstitucionalDinamicoImagem imagem = new InstitucionalDinamicoImagem(ImageUtils.compressBase64Image(institucionalDinamicoSecao02DTO.getImagem()));
					segundaSecaoToUpdate.setImagemPrincipal(imagem);
				} catch (IOException e) {
					throw new Exception("Erro ao converter Imagem");
				}
			}
			
			
			return institucionalDinamicoSecao02Repository.saveAndFlush(segundaSecaoToUpdate);
		}
	
		return null;
	}
	
	public void excluirInstitucionalDinamicoSecao02(Long id) {
		InstitucionalDinamicoSecao02 segundaSecao =  this.institucionalDinamicoSecao02Repository.getOne(id);
		this.institucionalDinamicoSecao02Repository.delete(segundaSecao);
	}
	
	
	
	// INSTITUCIONAL DINÂMICO SEÇÃO 03
	public List<InstitucionalDinamicoSecao03DTO> buscarInstitucionalDinamicoSecao03PorId(Long id) {	
		
		List<InstitucionalDinamicoSecao03> listaTerceiraSecao = institucionalDinamicoSecao03Repository.findByIdInstitucionalDinamico(id);
		List<InstitucionalDinamicoSecao03DTO> listaTerceiraSecaoDTO = null;
		if(listaTerceiraSecao != null && !listaTerceiraSecao.isEmpty()){
			listaTerceiraSecaoDTO = listaTerceiraSecao.stream().map(InstitucionalDinamicoSecao03DTO::create).collect(Collectors.toList());
			return listaTerceiraSecaoDTO;
		}
		
		return null;
	}
		
	public List<InstitucionalDinamicoSecao03DTO> buscarListaInstitucionalDinamicoSecao03ResumidaPorId(Long id) {
		
		List<InstitucionalDinamicoSecao03DTO> listaTerceiraSecaoDTO = institucionalDinamicoSecao03Repository.buscarListaInstitucionalDinamicoSecao03ResumidaPorId(id);

		return listaTerceiraSecaoDTO;
	}
	
	public InstitucionalDinamicoSecao03DTO buscarInstitucionalDinamicoSecao03Detalhe(Long id) {
		
		InstitucionalDinamicoSecao03 terceiraSecao = this.institucionalDinamicoSecao03listarById(id);
		InstitucionalDinamicoSecao03DTO terceiraSecaoDTO = new InstitucionalDinamicoSecao03DTO(terceiraSecao);

		return terceiraSecaoDTO;
	}
	
	public InstitucionalDinamicoSecao03 institucionalDinamicoSecao03listarById(final Long id) {
		Optional<InstitucionalDinamicoSecao03> institucionalDinamicoSecao03 = institucionalDinamicoSecao03Repository.findById(id);
		return institucionalDinamicoSecao03.orElseThrow(() -> new ObjectNotFoundException("Seção 03 não encontrada!"));
	}
	
	public InstitucionalDinamicoSecao03 findByIdInstitucionalDinamicoSecao03(final Long id) {
		  Optional<InstitucionalDinamicoSecao03> h = institucionalDinamicoSecao03Repository.findById(id);
		  return h.orElseThrow(() -> new ObjectNotFoundException("Segunda Seção não encontrada!"));
	}
	
	public InstitucionalDinamicoSecao03 editarInstitucionalDinamicoSecao03(final Long idInstitucionalDinamico, final InstitucionalDinamicoSecao03DTO institucionalDinamicoSecao03DTO) throws Exception {
		
		InstitucionalDinamicoSecao03 terceiraSecaoRef = null;
		if(institucionalDinamicoSecao03DTO.getId() != null) {
			terceiraSecaoRef = findByIdInstitucionalDinamicoSecao03(institucionalDinamicoSecao03DTO.getId());
		}
		
		InstitucionalDinamico institucionalDinamicoRef = null;
		if(idInstitucionalDinamico != null) {
			institucionalDinamicoRef = findById(idInstitucionalDinamico);
		}
	
		
		InstitucionalDinamicoSecao03 terceiraSecaoToUpdate = null;
		if(terceiraSecaoRef != null && institucionalDinamicoSecao03DTO != null) {
			terceiraSecaoToUpdate = institucionalDinamicoSecao03DTO.toEntityUpdate(terceiraSecaoRef);
			terceiraSecaoToUpdate.setInstitucionalDinamico(institucionalDinamicoRef);
			
			terceiraSecaoToUpdate.setTipo("terceiraSecao");
			
			return institucionalDinamicoSecao03Repository.saveAndFlush(terceiraSecaoToUpdate);
		}else if (terceiraSecaoRef == null && institucionalDinamicoSecao03DTO != null)  {
			terceiraSecaoToUpdate = institucionalDinamicoSecao03DTO.toEntityInsert();
			terceiraSecaoToUpdate.setInstitucionalDinamico(institucionalDinamicoRef);
			terceiraSecaoToUpdate.setPublicacoes(new ArrayList<InstitucionalDinamicoPublicacao>());
			terceiraSecaoToUpdate.setTipo("terceiraSecao");
			
			if(null != institucionalDinamicoSecao03DTO.getPublicacoes()) {
				for(InstitucionalDinamicoPublicacaoDTO p : institucionalDinamicoSecao03DTO.getPublicacoes()) {	
					InstitucionalDinamicoPublicacao _p = configurarPublicacao(p, terceiraSecaoToUpdate);
					terceiraSecaoToUpdate.getPublicacoes().add(_p);
				}
			}
			
			
			
			return institucionalDinamicoSecao03Repository.saveAndFlush(terceiraSecaoToUpdate);
		}
	
		return null;
	}
	
	public InstitucionalDinamicoPublicacao configurarPublicacao(InstitucionalDinamicoPublicacaoDTO publicacaoDTO, InstitucionalDinamicoSecao03 template) throws Exception {
		if( null == publicacaoDTO ) {
			return null;
		}
		InstitucionalDinamicoPublicacao publicacao = null;
		if(null != publicacaoDTO.getId()) {
			publicacao = institucionalDinamicoPublicacaoService.buscarPorId(publicacaoDTO.getId());
			publicacao.setTexto(publicacaoDTO.getTexto());
			publicacao.setTitulo(publicacaoDTO.getTitulo());
			publicacao.setLink(publicacaoDTO.getLink());
			publicacao.setTooltipTexto(publicacaoDTO.getTooltipTexto());
			publicacao.setTooltipTitulo(publicacao.getTooltipTitulo());
			publicacao.setOrdemExibicao(publicacao.getOrdemExibicao());
		} else {
			publicacao = new InstitucionalDinamicoPublicacao(null, null, publicacaoDTO.getTitulo(), publicacaoDTO.getTexto(), publicacaoDTO.getLink(), null, publicacaoDTO.getOrdemExibicao(), publicacaoDTO.getTooltipTitulo(), publicacaoDTO.getTooltipTexto());
			
		}
		if(null != publicacaoDTO.getImagem() && !publicacaoDTO.getImagem().isEmpty()) {
			try {
				InstitucionalDinamicoImagem imagem = new InstitucionalDinamicoImagem(ImageUtils.compressBase64Image(publicacaoDTO.getImagem()));
				publicacao.setImagem(imagem);
			} catch (IOException e) {
				throw new Exception("Erro ao converter Imagem");
			}

		}
		publicacao.setOrdemExibicao(publicacaoDTO.getOrdemExibicao());
		publicacao.setInstitucionalDinamicoSecao03(template);
		return publicacao;
	}
	
	public void excluirInstitucionalDinamicoSecao03(Long id) {
		InstitucionalDinamicoSecao03 terceiraSecao =  this.institucionalDinamicoSecao03Repository.getOne(id);
		this.institucionalDinamicoSecao03Repository.delete(terceiraSecao);
	}
		
		
	
	// INSTITUCIONAL DINÂMICO SEÇÃO 04
	public List<InstitucionalDinamicoSecao04DTO> buscarInstitucionalDinamicoSecao04PorId(Long id) {	
		
		List<InstitucionalDinamicoSecao04> listaQuartaSecao = institucionalDinamicoSecao04Repository.findByIdInstitucionalDinamico(id);
		List<InstitucionalDinamicoSecao04DTO> listaQuartaSecaoDTO = null;
		if(listaQuartaSecao != null && !listaQuartaSecao.isEmpty()){
			listaQuartaSecaoDTO = listaQuartaSecao.stream().map(InstitucionalDinamicoSecao04DTO::create).collect(Collectors.toList());
			return listaQuartaSecaoDTO;
		}
		
		return null;
	}
		
	public List<InstitucionalDinamicoSecao04DTO> buscarListaInstitucionalDinamicoSecao04ResumidaPorId(Long id) {
		
		List<InstitucionalDinamicoSecao04DTO> listaQuartaSecaoDTO = institucionalDinamicoSecao04Repository.buscarListaInstitucionalDinamicoSecao04ResumidaPorId(id);

		return listaQuartaSecaoDTO;
	}
	
	public InstitucionalDinamicoSecao04DTO buscarInstitucionalDinamicoSecao04Detalhe(Long id) {
		
		InstitucionalDinamicoSecao04DTO quartaSecaoDTO = institucionalDinamicoSecao04Repository.buscarInstitucionalDinamicoSecao04Detalhe(id);

		return quartaSecaoDTO;
	}
	
	
	public InstitucionalDinamicoSecao04 findByIdInstitucionalDinamicoSecao04(final Long id) {
		  Optional<InstitucionalDinamicoSecao04> h = institucionalDinamicoSecao04Repository.findById(id);
		  return h.orElseThrow(() -> new ObjectNotFoundException("Segunda Seção não encontrada!"));
	}
	
	public InstitucionalDinamicoSecao04 editarInstitucionalDinamicoSecao04(final Long idInstitucionalDinamico, final InstitucionalDinamicoSecao04DTO institucionalDinamicoSecao04DTO) throws Exception {
		
		InstitucionalDinamicoSecao04 quartaSecaoRef = null;
		if(institucionalDinamicoSecao04DTO.getId() != null) {
			quartaSecaoRef = findByIdInstitucionalDinamicoSecao04(institucionalDinamicoSecao04DTO.getId());
		}
		
		InstitucionalDinamico institucionalDinamicoRef = null;
		if(idInstitucionalDinamico != null) {
			institucionalDinamicoRef = findById(idInstitucionalDinamico);
		}
	
		
		InstitucionalDinamicoSecao04 quartaSecaoToUpdate = null;
		if(quartaSecaoRef != null && institucionalDinamicoSecao04DTO != null) {
			quartaSecaoToUpdate = institucionalDinamicoSecao04DTO.toEntityUpdate(quartaSecaoRef);
			quartaSecaoToUpdate.setInstitucionalDinamico(institucionalDinamicoRef);
			quartaSecaoToUpdate.setTipo("quartaSecao");
			
	
			return institucionalDinamicoSecao04Repository.saveAndFlush(quartaSecaoToUpdate);
		}else if (quartaSecaoRef == null && institucionalDinamicoSecao04DTO != null)  {
			quartaSecaoToUpdate = institucionalDinamicoSecao04DTO.toEntityInsert();
			quartaSecaoToUpdate.setInstitucionalDinamico(institucionalDinamicoRef);
			quartaSecaoToUpdate.setTipo("quartaSecao");
			
			
			return institucionalDinamicoSecao04Repository.saveAndFlush(quartaSecaoToUpdate);
		}
	
		return null;
	}
	
	public void excluirInstitucionalDinamicoSecao04(Long id) {
		InstitucionalDinamicoSecao04 quartaSecao =  this.institucionalDinamicoSecao04Repository.getOne(id);
		this.institucionalDinamicoSecao04Repository.delete(quartaSecao);
	}


}
