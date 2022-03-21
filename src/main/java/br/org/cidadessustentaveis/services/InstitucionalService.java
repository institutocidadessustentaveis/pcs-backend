package br.org.cidadessustentaveis.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.apache.xmlbeans.impl.util.Base64;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import br.org.cidadessustentaveis.dto.InstitucionalDTO;
import br.org.cidadessustentaveis.dto.PublicacaoDTO;
import br.org.cidadessustentaveis.model.administracao.ArquivoInstitucional;
import br.org.cidadessustentaveis.model.administracao.MenuPagina;
import br.org.cidadessustentaveis.model.administracao.Usuario;
import br.org.cidadessustentaveis.model.institucional.Institucional;
import br.org.cidadessustentaveis.model.institucional.Publicacao;
import br.org.cidadessustentaveis.model.institucional.TemplateInstitucional03;
import br.org.cidadessustentaveis.model.noticias.Imagem;
import br.org.cidadessustentaveis.repository.ArquivoInstitucionalRepository;
import br.org.cidadessustentaveis.repository.InstitucionalRepository;
import br.org.cidadessustentaveis.repository.MenuPaginaRepository;
import br.org.cidadessustentaveis.services.exceptions.DataIntegrityException;
import br.org.cidadessustentaveis.services.exceptions.ObjectNotFoundException;
import br.org.cidadessustentaveis.util.ImageUtils;
import br.org.cidadessustentaveis.util.ProfileUtil;


@Service	
public class InstitucionalService {


	@Autowired
	private ProfileUtil profileUtil;

	@Autowired
	private InstitucionalRepository institucionalRepository;

	@Autowired
	private ArquivoInstitucionalRepository arquivoInstitucionalRepository;

	@Autowired
	private MenuPaginaRepository menuPaginaRepository;
	
	@Autowired
	private NavItemService navItemService;

	@Autowired
	private MaterialInstitucionalService materialInstitucionalService;

	@Autowired
	private MaterialApoioService materialApoioService;

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private ImagemService imagemService;
	
	@Autowired
	private PublicacaoService publicacaoService;
	
	@Autowired
	private HttpServletRequest request;
	
	public List<Institucional> listar() {
		return institucionalRepository.findAllByOrderByTituloAsc();
	}

	public Institucional listarById(final Long id) {
		Optional<Institucional> institucional = institucionalRepository.findById(id);
		return institucional.orElseThrow(() -> new ObjectNotFoundException("Institucional não encontrado!"));
	}

	public Institucional inserir(InstitucionalDTO institucionalDTO) throws Exception {
		institucionalDTO.setImagemPrincipal(ImageUtils.compressImage(institucionalDTO.getImagemPrincipal()));

		if(institucionalDTO.getTemplate02() != null) {

			if(institucionalDTO.getTemplate02().getImagemPrimeiraSecao() != null) {
				String base64ImagemSecao1Comprimida = ImageUtils.compressImage(
						institucionalDTO.getTemplate02().getImagemPrimeiraSecao());
				institucionalDTO.getTemplate02().setImagemPrimeiraSecao(base64ImagemSecao1Comprimida);
			}

			if(institucionalDTO.getTemplate02().getImagemTerceiraSecao() != null) {
				String base64ImagemSecao3Comprimida = ImageUtils.compressImage(
						institucionalDTO.getTemplate02().getImagemTerceiraSecao());
				institucionalDTO.getTemplate02().setImagemPrimeiraSecao(base64ImagemSecao3Comprimida);
			}
		}

		Institucional institucionalToInsert = institucionalDTO.toEntityInsert();

		if (institucionalDTO.getTemplate03() != null) {
			if (institucionalDTO.getTemplate03().getImagemPrimeiraSecao() != null) {
				Imagem imagem = new Imagem();
				imagem.setBytes(Base64.decode(ImageUtils.compressBase64Image(institucionalDTO.getTemplate03().getImagemPrimeiraSecao()).getBytes()));
				institucionalToInsert.getTemplate03().setImagemPrimeiraSecao(imagem);	
			}
		}
		
	
		if (institucionalDTO.getTemplate01() != null && institucionalToInsert.getTemplate01().getTextoPrimeiraSecao() != null) {
			String textoPrimeiraSecao = this.transformBase64ImagesToBinaryImage(institucionalToInsert.getTemplate01().getTextoPrimeiraSecao());
			institucionalToInsert.getTemplate01().setTextoPrimeiraSecao(textoPrimeiraSecao);
		}
		
		if (institucionalDTO.getTemplate01() != null && institucionalToInsert.getTemplate01().getTxtSegundaSecao() != null) {
			String textoSegundaSecao = this.transformBase64ImagesToBinaryImage(institucionalToInsert.getTemplate01().getTxtSegundaSecao());
			institucionalToInsert.getTemplate01().setTxtSegundaSecao(textoSegundaSecao);
		}
		
		if (institucionalDTO.getTemplate02() != null && institucionalToInsert.getTemplate02().getTextoPrimeiraSecao() != null) {
			String textoPrimeiraSecao = this.transformBase64ImagesToBinaryImage(institucionalToInsert.getTemplate02().getTextoPrimeiraSecao());
			institucionalToInsert.getTemplate02().setTextoPrimeiraSecao(textoPrimeiraSecao);
		}
		
		if (institucionalDTO.getTemplate02() != null && institucionalToInsert.getTemplate02().getTxtSegundaSecao() != null) {
			String textoSegundaSecao = this.transformBase64ImagesToBinaryImage(institucionalToInsert.getTemplate02().getTxtSegundaSecao());
			institucionalToInsert.getTemplate02().setTxtSegundaSecao(textoSegundaSecao);
		}
		
		if (institucionalDTO.getTemplate02() != null && institucionalToInsert.getTemplate02().getTxtTerceiraSecao() != null) {
			String textoTerceiraSecao = this.transformBase64ImagesToBinaryImage(institucionalToInsert.getTemplate02().getTxtTerceiraSecao());
			institucionalToInsert.getTemplate02().setTxtTerceiraSecao(textoTerceiraSecao);
		}
		
		if (institucionalDTO.getTemplate02() != null && institucionalToInsert.getTemplate02().getTxtQuartaSecao() != null) {
			String textoQuartaSecao = this.transformBase64ImagesToBinaryImage(institucionalToInsert.getTemplate02().getTxtQuartaSecao());
			institucionalToInsert.getTemplate02().setTxtQuartaSecao(textoQuartaSecao);
		}
		
		if(institucionalDTO.getTemplate04() != null && institucionalDTO.getTemplate04().getShapeFiles() != null && !institucionalDTO.getTemplate04().getShapeFiles().isEmpty()) {
			 institucionalToInsert.getTemplate04().setShapesRelacionados(institucionalDTO.getTemplate04().getShapeFiles()); 
		}
		
		if(institucionalDTO.getTemplate04() != null && institucionalDTO.getTemplate04().getPrimeiroTexto() != null) {
			institucionalToInsert.getTemplate04().setPrimeiroTexto(institucionalDTO.getTemplate04().getPrimeiroTexto());
		}
		
		if(institucionalDTO.getTemplate04() != null && institucionalDTO.getTemplate04().getSegundoTexto() != null) {
			institucionalToInsert.getTemplate04().setSegundoTexto(institucionalDTO.getTemplate04().getSegundoTexto());
		}
		
		
		institucionalToInsert.setMenuPagina(inserirMenuPagina(institucionalDTO));		

		return institucionalRepository.save(institucionalToInsert);
	}
	
	private String transformBase64ImagesToBinaryImage(String html) throws IOException {
		Usuario usuario = usuarioService.buscarPorEmail(
											SecurityContextHolder.getContext().getAuthentication().getName());

		Document doc = Jsoup.parse(html);
		Elements imgs = doc.select("img");

		for(Element img : imgs) {
			String src = img.attr("src");

			if(src != null && src.contains(";base64")) {
				String[] split = src.split(",");

				if(split.length > 1) {
					String base64Image = split[1];

					byte[] imageBytes = ImageUtils.compressImage(org.apache.commons.codec.binary.Base64.decodeBase64(base64Image));

					Imagem imagem = new Imagem(imageBytes);
					imagem = imagemService.save(imagem);

					String imageUrl = profileUtil.getProperty("profile.api") +  "/imagens/" + imagem.getId();

					img.attr("src", imageUrl);
				}
			}
		}

		return doc.html();
	}

	public Publicacao configurarPublicacao(PublicacaoDTO publicacaoDTO, TemplateInstitucional03 template) throws Exception {
		if( null == publicacaoDTO ) {
			return null;
		}
		Publicacao publicacao = null;
		if(null != publicacaoDTO.getId()) {
			publicacao = publicacaoService.buscarPorId(publicacaoDTO.getId());
			publicacao.setMaterialApoio(null);
			publicacao.setMaterialInstitucional(null);
			publicacao.setTexto(publicacaoDTO.getTexto());
			publicacao.setTitulo(publicacaoDTO.getTitulo());
			publicacao.setLink(publicacaoDTO.getLink());
			publicacao.setTooltipTexto(publicacaoDTO.getTooltipTexto());
			publicacao.setTooltipTitulo(publicacao.getTooltipTitulo());
			publicacao.setOrdemExibicao(publicacao.getOrdemExibicao());
		} else {
			publicacao = new Publicacao(null, null, publicacaoDTO.getTitulo(), publicacaoDTO.getTexto(), publicacaoDTO.getLink(), null, null, null , publicacaoDTO.getOrdemExibicao(), publicacaoDTO.getTooltipTitulo(), publicacaoDTO.getTooltipTexto());
			
		}
		if( null != publicacaoDTO.getIdMaterialApoio() ) {
			publicacao.setMaterialApoio(materialApoioService.buscarPorId(publicacaoDTO.getIdMaterialApoio()));
		}

		if( null != publicacaoDTO.getIdMaterialInstitucional() ) {
			publicacao.setMaterialInstitucional(materialInstitucionalService.buscarPorId(publicacaoDTO.getIdMaterialInstitucional()));
		}
		if(null != publicacaoDTO.getImagem() && !publicacaoDTO.getImagem().isEmpty()) {
			try {
				Imagem imagem = new Imagem(ImageUtils.compressBase64Image(publicacaoDTO.getImagem()));
				publicacao.setImagem(imagem);
			} catch (IOException e) {
				throw new Exception("Erro ao converter Imagem");
			}

		}
		publicacao.setOrdemExibicao(publicacaoDTO.getOrdemExibicao());
		publicacao.setTemplate(template);
		return publicacao;
	}

	public Institucional alterar(final Long id, final InstitucionalDTO institucionalDTO) throws Exception {
		Institucional institucional = listarById(id); 

		MenuPagina novoMenuPagina = atualizarMenuPagina(institucional, institucionalDTO);

		Institucional institucionalToUpdate = institucionalDTO.toEntityUpdate(institucional);

		if (!(id == institucionalDTO.getId())) {
			throw new Exception("Campo id divergente.");
		}

		/*Prepara template 01*/
		if (institucionalDTO.getTemplate01() == null && institucional.getTemplate01() != null) {
			//delete template
			institucionalToUpdate.setTemplate01(null);
		}else if (institucionalDTO.getTemplate01() != null && institucional.getTemplate01() == null) {
			institucionalToUpdate.setTemplate01(institucionalDTO.getTemplate01().toEntityInsert());
		}else if (institucionalDTO.getTemplate01() != null && institucional.getTemplate01() != null) {
			institucionalToUpdate.setTemplate01(institucionalDTO.getTemplate01().toEntityUpdate(institucionalToUpdate.getTemplate01()));
		}

		/*Prepara template 02*/
		if (institucionalDTO.getTemplate02() == null && institucional.getTemplate02() != null) {
			//delete template
			institucionalToUpdate.setTemplate02(null);
		}else if (institucionalDTO.getTemplate02() != null && institucional.getTemplate02() == null) {
			institucionalToUpdate.setTemplate02(institucionalDTO.getTemplate02().toEntityInsert());
		}else if (institucionalDTO.getTemplate02() != null && institucional.getTemplate02() != null) {
			institucionalToUpdate.setTemplate02(institucionalDTO.getTemplate02().toEntityUpdate(institucionalToUpdate.getTemplate02()));
		}

		/*Prepara template 03*/
		if (institucionalDTO.getTemplate03() == null && institucional.getTemplate03() != null) {
			//delete template
			institucionalToUpdate.setTemplate03(null);
		}else if (institucionalDTO.getTemplate03() != null && institucional.getTemplate03() == null) {
			institucionalToUpdate.setTemplate03(institucionalDTO.getTemplate03().toEntityInsert());
			institucionalToUpdate.getTemplate03().setPublicacoes(new ArrayList<>());
			institucionalToUpdate.getTemplate03().setPublicacoes2(new ArrayList<>());
			

			if(null != institucionalDTO.getTemplate03().getPublicacoes()) {
				for(PublicacaoDTO p : institucionalDTO.getTemplate03().getPublicacoes()) {
					Publicacao _p = configurarPublicacao(p, institucionalToUpdate.getTemplate03());
					institucionalToUpdate.getTemplate03().getPublicacoes().add(_p);
				}
			}
			if(null != institucionalDTO.getTemplate03().getPublicacoes2()) {
				for(PublicacaoDTO p : institucionalDTO.getTemplate03().getPublicacoes2()) {
					Publicacao _p = configurarPublicacao(p, institucionalToUpdate.getTemplate03());
					institucionalToUpdate.getTemplate03().getPublicacoes2().add(_p);
				}
			}

		}else if (institucionalDTO.getTemplate03() != null && institucional.getTemplate03() != null) {
			institucionalToUpdate.getTemplate03().setTituloSecaoTexto(institucionalDTO.getTemplate03().getTituloSecaoTexto());
			institucionalToUpdate.getTemplate03().setSecaoTexto(institucionalDTO.getTemplate03().getSecaoTexto());
			institucionalToUpdate.getTemplate03().setTituloPrimeiraSecao(institucionalDTO.getTemplate03().getTituloPrimeiraSecao());
			institucionalToUpdate.getTemplate03().setTextoPrimeiraSecao(institucionalDTO.getTemplate03().getTextoPrimeiraSecao());
			institucionalToUpdate.getTemplate03().setTituloSegundaSecao(institucionalDTO.getTemplate03().getTituloSegundaSecao());
			institucionalToUpdate.getTemplate03().setVerMaisInstituicao(institucionalDTO.getTemplate03().getVerMaisInstituicao());
			institucionalToUpdate.getTemplate03().setVerMaisPCS(institucionalDTO.getTemplate03().getVerMaisPCS());
			institucionalToUpdate.getTemplate03().setTituloCatalogo1(institucionalDTO.getTemplate03().getTituloCatalogo1());
			institucionalToUpdate.getTemplate03().setTituloCatalogo2(institucionalDTO.getTemplate03().getTituloCatalogo2());
			institucionalToUpdate.getTemplate03().setPublicacoes(new ArrayList<>());
			institucionalToUpdate.getTemplate03().setPublicacoes2(new ArrayList<>());
			

			if(null != institucionalDTO.getTemplate03().getPublicacoes()) {
				for(PublicacaoDTO p : institucionalDTO.getTemplate03().getPublicacoes()) {
					Publicacao _p = configurarPublicacao(p, institucionalToUpdate.getTemplate03());
					institucionalToUpdate.getTemplate03().getPublicacoes().add(_p);
				}
			}
			if(null != institucionalDTO.getTemplate03().getPublicacoes2()) {
				for(PublicacaoDTO p : institucionalDTO.getTemplate03().getPublicacoes2()) {
					Publicacao _p = configurarPublicacao(p, institucionalToUpdate.getTemplate03());
					institucionalToUpdate.getTemplate03().getPublicacoes2().add(_p);
				}
			}
		}
		
		
		/*Prepara template 04*/
		if (institucionalDTO.getTemplate04() == null && institucional.getTemplate04() != null) {
			//delete template
			institucionalToUpdate.setTemplate04(null);
		}else if (institucionalDTO.getTemplate04() != null && institucional.getTemplate04() == null) {
			institucionalToUpdate.setTemplate04(institucionalDTO.getTemplate04().toEntityInsert());
		}else if (institucionalDTO.getTemplate04() != null && institucional.getTemplate04() != null) {
			if(institucionalDTO.getTemplate04().getShapeFiles() != null && !institucionalDTO.getTemplate04().getShapeFiles().isEmpty()) {
				 institucionalToUpdate.getTemplate04().setShapesRelacionados(institucionalDTO.getTemplate04().getShapeFiles()); 
			}else if (institucionalDTO.getTemplate04().getShapeFiles() != null && institucionalDTO.getTemplate04().getShapeFiles().isEmpty()) {
				institucionalToUpdate.getTemplate04().setShapesRelacionados(new ArrayList<>()); 
			}
			if(institucionalDTO.getTemplate04().getPrimeiroTexto() != null) {
				institucionalToUpdate.getTemplate04().setPrimeiroTexto(institucionalDTO.getTemplate04().getPrimeiroTexto());
			}
			if(institucionalDTO.getTemplate04().getSegundoTexto() != null) {
				institucionalToUpdate.getTemplate04().setSegundoTexto(institucionalDTO.getTemplate04().getSegundoTexto());
			}
		}
		
		
		if (institucionalDTO.getTemplate01() != null && institucionalToUpdate.getTemplate01().getTextoPrimeiraSecao() != null) {
			String textoPrimeiraSecao = this.transformBase64ImagesToBinaryImage(institucionalToUpdate.getTemplate01().getTextoPrimeiraSecao());
			institucionalToUpdate.getTemplate01().setTextoPrimeiraSecao(textoPrimeiraSecao);
		}
		
		if (institucionalDTO.getTemplate01() != null && institucionalToUpdate.getTemplate01().getTxtSegundaSecao() != null) {
			String textoSegundaSecao = this.transformBase64ImagesToBinaryImage(institucionalToUpdate.getTemplate01().getTxtSegundaSecao());
			institucionalToUpdate.getTemplate01().setTxtSegundaSecao(textoSegundaSecao);
		}
		
		
		if (institucionalDTO.getTemplate02() != null && institucionalToUpdate.getTemplate02().getTextoPrimeiraSecao() != null) {
			String textoPrimeiraSecao = this.transformBase64ImagesToBinaryImage(institucionalToUpdate.getTemplate02().getTextoPrimeiraSecao());
			institucionalToUpdate.getTemplate02().setTextoPrimeiraSecao(textoPrimeiraSecao);
		}
		
		if (institucionalDTO.getTemplate02() != null && institucionalToUpdate.getTemplate02().getTxtSegundaSecao() != null) {
			String textoSegundaSecao = this.transformBase64ImagesToBinaryImage(institucionalToUpdate.getTemplate02().getTxtSegundaSecao());
			institucionalToUpdate.getTemplate02().setTxtSegundaSecao(textoSegundaSecao);
		}
		
		if (institucionalDTO.getTemplate02() != null && institucionalToUpdate.getTemplate02().getTxtTerceiraSecao() != null) {
			String textoTerceiraSecao = this.transformBase64ImagesToBinaryImage(institucionalToUpdate.getTemplate02().getTxtTerceiraSecao());
			institucionalToUpdate.getTemplate02().setTxtTerceiraSecao(textoTerceiraSecao);
		}
		
		if (institucionalDTO.getTemplate02() != null && institucionalToUpdate.getTemplate02().getTxtQuartaSecao() != null) {
			String textoQuartaSecao = this.transformBase64ImagesToBinaryImage(institucionalToUpdate.getTemplate02().getTxtQuartaSecao());
			institucionalToUpdate.getTemplate02().setTxtQuartaSecao(textoQuartaSecao);
		}
		
		institucionalToUpdate.setMenuPagina(novoMenuPagina);

		return institucionalRepository.saveAndFlush(institucionalToUpdate);
	}

	public void deletar(final Long id) {
		Institucional pagina = listarById(id);
		
		if(pagina.getMenuPagina() != null) {
			navItemService.deletarPorIdMenuPagina(pagina.getMenuPagina().getId());
		}
		else {
			MenuPagina menuPagina = menuPaginaRepository.findByNome(pagina.getTitulo());
			if(menuPagina != null) {
				navItemService.deletarPorIdMenuPagina(menuPagina.getId());
			}
		}
		
		institucionalRepository.deleteById(id);
	}

	public ArquivoInstitucional listarArquivoPorId(final Long idArquivo) {
		Optional<ArquivoInstitucional> arquivo = arquivoInstitucionalRepository.findById(idArquivo);
		return arquivo.orElseThrow(() -> new ObjectNotFoundException("Arquivo não encontrado!"));
	}

	public InstitucionalDTO buscarPaginaInstitucionalPorLink(String link) {		
		Institucional paginaRef = institucionalRepository.findByLink(link);

		if (paginaRef == null) {
			throw new ObjectNotFoundException("Página não encontrada!");
		}

		InstitucionalDTO paginaDto = new InstitucionalDTO(paginaRef);
		if (paginaRef.getTemplate03() != null) {
			preencherTemplate03(paginaRef, paginaDto);
		}

		return paginaDto;
	}

	public InstitucionalDTO existePaginaInstitucionalComLink(String link) {
		Long id = institucionalRepository.findByLinkOnlyId(link);
		Institucional paginaRef = new Institucional();
		paginaRef.setId(id);		
		return new InstitucionalDTO(paginaRef);
	}

	public List<Institucional> ultimasInstitucional(int qtd) {
		PageRequest pageRequest = PageRequest.of(0, qtd, Direction.valueOf("DESC"), "id");
		return institucionalRepository.carregarUltimasInstitucional(pageRequest);
	}

	public MenuPagina inserirMenuPagina(InstitucionalDTO institucionalDTO) {

		StringBuilder link = new StringBuilder();
		link.append("/pagina/");
		link.append(institucionalDTO.getLink_pagina());

		MenuPagina novaPagina = new MenuPagina(institucionalDTO.getTitulo(), link.toString(), "Institucional");
		return menuPaginaRepository.save(novaPagina);
	}

	public MenuPagina atualizarMenuPagina(Institucional institucional, InstitucionalDTO institucionalDTO) {
		MenuPagina menuPagina = menuPaginaRepository.findByNome(institucional.getTitulo());
		menuPagina.setNome(institucionalDTO.getTitulo());
		menuPagina.setRota("/pagina/" + institucionalDTO.getLink_pagina());
		return menuPaginaRepository.save(menuPagina);
	}

	public void deletarMenuPagina(String nome) {
		try {
			MenuPagina menuPagina = menuPaginaRepository.findByNome(nome);
			menuPaginaRepository.delete(menuPagina);			
		}catch (Exception e) {
			throw new DataIntegrityException("A página deve ser removida do menu antes de ser excluída");
		}
	}

	public void criarPaginasMenu() {
		List<Institucional> institucionais = institucionalRepository.findAllByOrderByTituloAsc();

		for(Institucional institucional: institucionais) {
			MenuPagina menuPagina = null;

			try {
				menuPagina = menuPaginaRepository.findByNome(institucional.getTitulo());
			} catch(Exception e) {
				menuPagina = menuPaginaRepository.findByNomeLista(institucional.getTitulo()).get(0);
			}

			if(menuPagina == null) {
				menuPagina = new MenuPagina();
				menuPagina.setNome(institucional.getTitulo());
				menuPagina.setRota("/institucional/pagina/" + institucional.getLink_pagina());
				menuPagina.setModulo("Institucional");
				menuPagina = menuPaginaRepository.save(menuPagina);
			}

			institucional.setMenuPagina(menuPagina);
		}

		institucionalRepository.saveAll(institucionais);
	}

	public void preencherTemplate03(Institucional paginaRef, InstitucionalDTO paginaDto) {
		/*List<PublicacaoDTO> publicacoes = new ArrayList<>();

		if ("livre".equals(paginaRef.getTemplate03().getTipoPublicacao())) {
			for (Publicacao publicacao : paginaRef.getTemplate03().getPublicacoes()) {
				PublicacaoDTO publicacaoDTO = new PublicacaoDTO(publicacao);
				publicacoes.add(publicacaoDTO);
			}

		} else if ("material_institucional".equals(paginaRef.getTemplate03().getTipoPublicacao())) {

			List<MaterialInstitucional> materiais = materialInstitucionalService.buscar();
			for (MaterialInstitucional materialInstitucional : materiais) {
				int count = 5;
				if (materialInstitucional.getId().equals(paginaRef.getTemplate03().getIdPrimeiroDestaque())) {
					publicacoes.add(new PublicacaoDTO(materialInstitucional, 1));
				} else if ((materialInstitucional.getId().equals(paginaRef.getTemplate03().getIdSegundoDestaque()))) {
					publicacoes.add(new PublicacaoDTO(materialInstitucional, 2));
				} else if ((materialInstitucional.getId().equals(paginaRef.getTemplate03().getIdTerceiroDestaque()))) {
					publicacoes.add(new PublicacaoDTO(materialInstitucional, 3));
				} else if ((materialInstitucional.getId().equals(paginaRef.getTemplate03().getIdQuartoDestaque()))) {
					publicacoes.add(new PublicacaoDTO(materialInstitucional, 4));
				} else {
					publicacoes.add(new PublicacaoDTO(materialInstitucional, count));
					count++;
				}
			}

		}  else if ("material_apoio".equals(paginaRef.getTemplate03().getTipoPublicacao())) {

			List<MaterialApoio> materiais = materialApoioService.buscar();
			for (MaterialApoio materialApoio : materiais) {
				int count = 5;
				if (materialApoio.getId().equals(paginaRef.getTemplate03().getIdPrimeiroDestaque())) {
					publicacoes.add(new PublicacaoDTO(materialApoio, 1));
				} else if ((materialApoio.getId().equals(paginaRef.getTemplate03().getIdSegundoDestaque()))) {
					publicacoes.add(new PublicacaoDTO(materialApoio, 2));
				} else if ((materialApoio.getId().equals(paginaRef.getTemplate03().getIdTerceiroDestaque()))) {
					publicacoes.add(new PublicacaoDTO(materialApoio, 3));
				} else if ((materialApoio.getId().equals(paginaRef.getTemplate03().getIdQuartoDestaque()))) {
					publicacoes.add(new PublicacaoDTO(materialApoio, 4));
				} else {
					publicacoes.add(new PublicacaoDTO(materialApoio, count));
					count++;
				}
			}

		} */
	}

	public InstitucionalDTO buscarParaEdicao(Long id) {
		Institucional institucionalRef = listarById(id);
		InstitucionalDTO institucionalDTO = new InstitucionalDTO(institucionalRef);

		return institucionalDTO;

	}

	public Institucional findByTemplate03Id(Long idTemplate03) {
		return institucionalRepository.findByTemplate03Id(idTemplate03);
	}

}
