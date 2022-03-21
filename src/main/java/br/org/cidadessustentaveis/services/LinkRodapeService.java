package br.org.cidadessustentaveis.services;

import br.org.cidadessustentaveis.dto.LinkRodapeDTO;
import br.org.cidadessustentaveis.model.administracao.LinkRodape;
import br.org.cidadessustentaveis.repository.LinkRodapeRepository;
import org.hibernate.annotations.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LinkRodapeService {

    @Autowired
    private LinkRodapeRepository dao;

    public LinkRodape save(LinkRodape link) {
        return dao.save(link);
    }

    public Optional<LinkRodape> findById(Long id) {
        return dao.findById(id);
    }

    public void atualizarLinks(List<LinkRodapeDTO> links) {
        if(links == null) throw new IllegalArgumentException("Lista de links não pode ser nula");
        if(links.size() > 4) throw new IllegalArgumentException("Lista de links não pode ter mais de quatro elementos");

        for(LinkRodapeDTO dto : links) {
            LinkRodape linkRodape = dto.createEntity();

            if(linkRodape.getId() != null) {
                Optional<LinkRodape> optional = this.findById(linkRodape.getId());

                if(optional.isPresent()) {
                    linkRodape = optional.get();
                }
            }

            linkRodape.setOrdem(links.indexOf(dto) + 1);

            if(linkRodape.getAbrirNovaJanela() == null) {
                linkRodape.setAbrirNovaJanela(false);
            }

            if(this.countLinks() <= 4) {
                this.save(linkRodape);
            } else {
                throw new IllegalStateException("Não é possível adicionar mais de quatro links no rodapé");
            }
        }
    }

    public List<LinkRodape> buscarLinksOdernados() {
        return dao.buscarLinksOdernados(PageRequest.of(0, 4));
    }

    @Cacheable("linksRodape")
    public List<LinkRodape> buscarLinksOdernadosComCache() {
        return dao.buscarLinksOdernados(PageRequest.of(0, 4));
    }

    public void removerLink(Long id) {
        dao.deleteById(id);
    }

    public Long countLinks() {
        return dao.count();
    }
}
