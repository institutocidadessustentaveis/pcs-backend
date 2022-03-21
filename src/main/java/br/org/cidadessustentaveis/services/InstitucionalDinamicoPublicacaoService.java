package br.org.cidadessustentaveis.services;

import java.io.IOException;
import java.util.Optional;

import org.apache.xmlbeans.impl.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.org.cidadessustentaveis.dto.InstitucionalDinamicoPublicacaoDTO;
import br.org.cidadessustentaveis.model.institucional.InstitucionalDinamicoImagem;
import br.org.cidadessustentaveis.model.institucional.InstitucionalDinamicoPublicacao;
import br.org.cidadessustentaveis.repository.InstitucionalDinamicoPublicacaoRepository;
import br.org.cidadessustentaveis.services.exceptions.ObjectNotFoundException;
import br.org.cidadessustentaveis.util.ImageUtils;

@Service	
public class InstitucionalDinamicoPublicacaoService {

	@Autowired
	private InstitucionalDinamicoPublicacaoRepository publicacaoRepository;	

	@Autowired
	private InstitucionalDinamicoService institucionalService;


	public InstitucionalDinamicoPublicacao buscarPorId(final Long id) {	
	  Optional<InstitucionalDinamicoPublicacao> institucional = publicacaoRepository.findById(id);
	  return institucional.orElseThrow(() -> new ObjectNotFoundException("Publicação não encontrada!"));
	}
	
	public InstitucionalDinamicoPublicacaoDTO buscarPublicacaoPorId(Long id) {	
		InstitucionalDinamicoPublicacao publicacaoRef = buscarPorId(id);
		return new InstitucionalDinamicoPublicacaoDTO(publicacaoRef);
	}
	
	public void deletar(final Long id) {
		buscarPorId(id);
		publicacaoRepository.deleteById(id);
	}
	
	public InstitucionalDinamicoPublicacao editarOrdemExibicao(Long id, Integer ordem) {
		if (id != null) {
			InstitucionalDinamicoPublicacao publicacaoRef = buscarPorId(id);
			publicacaoRef.setOrdemExibicao(ordem);
			return publicacaoRepository.save(publicacaoRef);
		}
		return null;
	}
	
	public InstitucionalDinamicoPublicacao editar(InstitucionalDinamicoPublicacaoDTO publicacaoDTO) throws IOException {
		if (publicacaoDTO.getId() != null) {
			InstitucionalDinamicoPublicacao publicacaoRef = buscarPorId(publicacaoDTO.getId());
			publicacaoRef.setLink(publicacaoDTO.getLink());
			publicacaoRef.setTexto(publicacaoDTO.getTexto());
			publicacaoRef.setTitulo(publicacaoDTO.getTitulo());
			publicacaoRef.setTooltipTitulo(publicacaoDTO.getTooltipTitulo());
			publicacaoRef.setTooltipTexto(publicacaoDTO.getTooltipTexto());
			if(publicacaoDTO.getImagem() != null && !publicacaoDTO.getImagem().isEmpty()) {
				if(publicacaoRef.getImagem() == null) {
					InstitucionalDinamicoImagem imagemPublicacao = new InstitucionalDinamicoImagem();	
					imagemPublicacao.setBytes(
							Base64.decode(ImageUtils.compressBase64Image(publicacaoDTO.getImagem()).getBytes()));
					
					publicacaoRef.setImagem(imagemPublicacao);
				} else if(publicacaoRef.getImagem() != null) {
					publicacaoRef.getImagem().setImagePayload(publicacaoDTO.getImagem());
				}
				publicacaoRef.getImagem().setImagePayload(publicacaoDTO.getImagem());
			} else {
				publicacaoRef.setImagem(null);
			}
			
			return publicacaoRepository.save(publicacaoRef);
		}
		return null;
	}
	
	public InstitucionalDinamicoPublicacao inserir(InstitucionalDinamicoPublicacaoDTO publicacaoDTO) throws IOException {
		InstitucionalDinamicoPublicacao publicacao = new InstitucionalDinamicoPublicacao();
		publicacao.setTitulo(publicacaoDTO.getTitulo());
		publicacao.setTexto(publicacaoDTO.getTexto());
		publicacao.setLink(publicacaoDTO.getLink());
		publicacao.setOrdemExibicao(publicacaoDTO.getOrdemExibicao());
		publicacao.setTooltipTitulo(publicacaoDTO.getTooltipTitulo());
		publicacao.setTooltipTexto(publicacaoDTO.getTooltipTexto());
		publicacao.setInstitucionalDinamicoSecao03(institucionalService.findByIdInstitucionalDinamicoSecao03(publicacaoDTO.getIdInstitucionalDinamicoSecao03()));

		if (publicacaoDTO.getImagem() != null && !publicacaoDTO.getImagem().isEmpty()) {
			InstitucionalDinamicoImagem imagemPublicacao = new InstitucionalDinamicoImagem();	
			imagemPublicacao.setBytes(
					Base64.decode(ImageUtils.compressBase64Image(publicacaoDTO.getImagem()).getBytes()));
			publicacao.setImagem(imagemPublicacao);
		}
		
		return publicacaoRepository.save(publicacao);
	}


}
