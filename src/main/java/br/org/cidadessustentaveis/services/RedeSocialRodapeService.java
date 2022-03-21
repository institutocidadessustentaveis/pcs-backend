package br.org.cidadessustentaveis.services;

import br.org.cidadessustentaveis.dto.RedeSocialRodapeDTO;
import br.org.cidadessustentaveis.model.administracao.RedeSocialRodape;
import br.org.cidadessustentaveis.model.enums.RedeSocial;
import br.org.cidadessustentaveis.repository.RedeSocialRodapeRepository;
import org.hibernate.annotations.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RedeSocialRodapeService {

    @Autowired
    private RedeSocialRodapeRepository dao;

    public RedeSocialRodape save(RedeSocialRodape redeSocialRodape) {
        return dao.save(redeSocialRodape);
    }

    public void salvarRedesSociais(List<RedeSocialRodapeDTO> redes) {
        if(redes == null) throw new IllegalArgumentException("Lista de redes sociais não pode ser nula");
        if(redes.size() > 6) throw new IllegalArgumentException("Não é possível cadastrar mais de cinco redes sociais");

        for(RedeSocialRodapeDTO dto : redes) {
            RedeSocialRodape redeSocial = dto.toEntityInsert();

            if(redeSocial.getId() != null) {
                Optional<RedeSocialRodape> optional = this.findById(redeSocial.getId());

                if(optional.isPresent()) {
                    redeSocial = optional.get();
                }
            }

            redeSocial.setOrdem(redes.indexOf(dto) + 1);

            if(this.countRedesSociais() <= 6) {
                this.save(redeSocial);
            } else {
                throw new IllegalStateException("Não é possível adicionar mais de cinco redes sociais no rodapé");
            }
        }

    }

    public List<RedeSocialRodape> buscarRedesSociais() {
        return dao.buscarRedesSociais(PageRequest.of(0, 6));
    }

    @Cacheable("linksRedesSociais")
    public List<RedeSocialRodape> buscarRedesSociaisComCache() {
        return dao.buscarRedesSociais(PageRequest.of(0, 6));
    }

    public Optional<RedeSocialRodape> findById(Long id) {
        return dao.findById(id);
    }

    public void excluirRedeSocial(Long id) {
        dao.deleteById(id);
    }

    public Long countRedesSociais() {
        return dao.count();
    }

}
