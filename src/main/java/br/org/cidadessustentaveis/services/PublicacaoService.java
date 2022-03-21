package br.org.cidadessustentaveis.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.xmlbeans.impl.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.org.cidadessustentaveis.dto.PublicacaoDTO;
import br.org.cidadessustentaveis.model.institucional.Institucional;
import br.org.cidadessustentaveis.model.institucional.Publicacao;
import br.org.cidadessustentaveis.model.institucional.TemplateInstitucional03;
import br.org.cidadessustentaveis.model.noticias.Imagem;
import br.org.cidadessustentaveis.model.planjementoIntegrado.MaterialApoio;
import br.org.cidadessustentaveis.repository.PublicacaoRepository;
import br.org.cidadessustentaveis.services.exceptions.ObjectNotFoundException;
import br.org.cidadessustentaveis.util.ImageUtils;

@Service
public class PublicacaoService {

	@Autowired
	private PublicacaoRepository publicacaoRepository;

	@Autowired
	private InstitucionalService institucionalService;

	@Autowired
	private MaterialApoioService materialApoioService;

	public Publicacao buscarPorId(final Long id) {
	  Optional<Publicacao> institucional = publicacaoRepository.findById(id);
	  return institucional.orElseThrow(() -> new ObjectNotFoundException("Publicação não encontrada!"));
	}
	
	public PublicacaoDTO buscarPublicacaoPorId(Long id) {
		Publicacao publicacaoRef = buscarPorId(id);
		return new PublicacaoDTO(publicacaoRef);
	}
	
	public void deletar(final Long id) {
		buscarPorId(id);
		publicacaoRepository.deleteById(id);
	}
	
	public Publicacao editar(PublicacaoDTO publicacaoDTO) {
		if (publicacaoDTO.getId() != null) {
			Publicacao publicacaoRef = buscarPorId(publicacaoDTO.getId());
			publicacaoRef.setLink(publicacaoDTO.getLink());
			publicacaoRef.setTexto(publicacaoDTO.getTexto());
			publicacaoRef.setTitulo(publicacaoDTO.getTitulo());
			publicacaoRef.getImagem().setImagePayload(publicacaoDTO.getImagem());
			return publicacaoRepository.save(publicacaoRef);
		}
		return null;
	}
	
	public Publicacao inserir(PublicacaoDTO publicacaoDTO) throws IOException {
		Publicacao publicacao = new Publicacao();
		publicacao.setTitulo(publicacaoDTO.getTitulo());
		publicacao.setTexto(publicacaoDTO.getTexto());
		publicacao.setLink(publicacaoDTO.getLink());
		publicacao.setTemplate(institucionalService.findByTemplate03Id(publicacaoDTO.getIdTemplate03()).getTemplate03());

		if (publicacaoDTO.getImagem() != null) {
			Imagem imagemPublicacao = new Imagem();
			imagemPublicacao.setBytes(
					Base64.decode(ImageUtils.compressBase64Image(publicacaoDTO.getImagem()).getBytes()));
			publicacao.setImagem(imagemPublicacao);
		}
		
		return publicacaoRepository.save(publicacao);
	}

	public List<PublicacaoDTO> listarSemAsPublicacoesInstitucional(Integer page, Boolean ehPCS,
			Long idInstitucional) {
		List<PublicacaoDTO> dtos = new ArrayList<PublicacaoDTO>();
		Institucional institucional = institucionalService.listarById(idInstitucional);
		TemplateInstitucional03 t3 = institucional.getTemplate03();
		List<Long> idsParaExcluir = new ArrayList<>();
		List<MaterialApoio> materiais = materialApoioService.listarPorInstituicao(page, ehPCS, idsParaExcluir);
		for(MaterialApoio materialApoio : materiais){
			Publicacao p = new Publicacao();
			p.setMaterialApoio(materialApoio);
			dtos.add(new PublicacaoDTO(p));
		}
		return dtos;
	}
	
	public Long getMaterialApoioId(Publicacao p) {
		if(p != null && p.getMaterialApoio() != null) {
			return p.getMaterialApoio().getId();
		}
		return 0l;
	}


}
