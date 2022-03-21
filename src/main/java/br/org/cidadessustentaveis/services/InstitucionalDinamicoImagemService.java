package br.org.cidadessustentaveis.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.org.cidadessustentaveis.dto.InstitucionalDinamicoImagemDTO;
import br.org.cidadessustentaveis.model.home.HomeImagem;
import br.org.cidadessustentaveis.model.institucional.InstitucionalDinamico;
import br.org.cidadessustentaveis.model.institucional.InstitucionalDinamicoImagem;
import br.org.cidadessustentaveis.repository.InstitucionalDinamicoImagemRepository;
import br.org.cidadessustentaveis.repository.InstitucionalDinamicoRepository;

@Service
public class InstitucionalDinamicoImagemService {	

    @Autowired	
    private InstitucionalDinamicoImagemRepository dao;	
    
    @Autowired	
    private InstitucionalDinamicoRepository daoInstitucionalDinamico;		

    public InstitucionalDinamicoImagem save(InstitucionalDinamicoImagem imagem) {	
        return dao.save(imagem);
    }

    public InstitucionalDinamicoImagem save(byte[] bytes) {
    	InstitucionalDinamicoImagem imagem = new InstitucionalDinamicoImagem();
        imagem.setBytes(bytes);
        return this.save(imagem);
    }

    public InstitucionalDinamicoImagem find(Long id) {
        Optional<InstitucionalDinamicoImagem> optional = dao.findById(id);

        if(optional.isPresent()) {
            return optional.get();
        }

        return null;
    }
    
	public InstitucionalDinamicoImagemDTO editarGaleria(final Long idHome, final  InstitucionalDinamicoImagemDTO imagemDTO) throws Exception {	
		
		InstitucionalDinamicoImagem imagem;
		
		if(imagemDTO.getId() != null) {
			InstitucionalDinamicoImagem imagemAux =  this.dao.getOne(imagemDTO.getId());
			imagemAux.setNomeAutor(imagemDTO.getNomeAutor());
			imagemAux.setImagePayload(imagemDTO.getConteudo());
			imagemAux.setIndice(imagemDTO.getIndice());
			imagemAux.setSubtitulo(imagemDTO.getSubtitulo());
			imagemAux.setTitulo(imagemDTO.getTitulo());
			imagemAux.setLink(imagemDTO.getLink());
			imagem = dao.saveAndFlush(imagemAux);
		}else {
			InstitucionalDinamicoImagem arquivoAux = imagemDTO.toEntityInsert();
			Optional<InstitucionalDinamico> institucionalDinamico = daoInstitucionalDinamico.findById(idHome);
	        if(institucionalDinamico.isPresent()) {
	            arquivoAux.setInstitucional(institucionalDinamico.get());
	        }
			imagem = dao.saveAndFlush(arquivoAux);
		}
		InstitucionalDinamicoImagemDTO institucionalDinamicoImagemDTO = new InstitucionalDinamicoImagemDTO();
		institucionalDinamicoImagemDTO.setId(imagem.getId());
		return institucionalDinamicoImagemDTO;
	}
	
	public void excluirImagem(Long idImagem) {
		InstitucionalDinamicoImagem imagemAux =  this.dao.getOne(idImagem);
		this.dao.delete(imagemAux);
	}

    public void delete(Long id) {
        dao.deleteById(id);
    }

}
