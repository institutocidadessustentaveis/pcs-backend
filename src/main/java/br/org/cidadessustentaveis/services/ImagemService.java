package br.org.cidadessustentaveis.services;

import br.org.cidadessustentaveis.model.administracao.Usuario;
import br.org.cidadessustentaveis.model.noticias.Imagem;
import br.org.cidadessustentaveis.repository.ImagemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ImagemService {

    @Autowired
    private ImagemRepository dao;

    public Imagem save(Imagem imagem) {
        return dao.save(imagem);
    }

    public Imagem save(byte[] bytes) {
        Imagem imagem = new Imagem();
        imagem.setBytes(bytes);
        return this.save(imagem);
    }

    public Imagem find(Long id) {
        Optional<Imagem> optional = dao.findById(id);

        if(optional.isPresent()) {
            return optional.get();
        }

        return null;
    }

    public void delete(Long id) {
        dao.deleteById(id);
    }

}
