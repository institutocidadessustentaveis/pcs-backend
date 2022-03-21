package br.org.cidadessustentaveis.services;

import br.org.cidadessustentaveis.dto.ContatoPcsDTO;
import br.org.cidadessustentaveis.model.administracao.ContatoPCS;
import br.org.cidadessustentaveis.repository.ContatoPCSRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ContatoPCSService {

    @Autowired
    private ContatoPCSRepository dao;

    public ContatoPCS save(ContatoPcsDTO dto) {
        if(dto == null) throw new IllegalArgumentException("Contato do PCS não pode ser nulo");

        if(dto.getId() == null) {
            ContatoPCS contato = dto.toEntityInsert();
            contato.setData(new Date());
            return dao.save(contato);
        } else {
             Optional<ContatoPCS> optional = dao.findById(dto.getId());

             if(optional.isPresent()) {
                return dao.save(dto.toEntityUpdate(optional.get()));
             } else {
                 throw new IllegalStateException("Contato não encontrado. Não foi possível editar.");
             }
        }
    }

    public ContatoPCS buscarContatoMaisRecente() {
        List<ContatoPCS> contatos = dao.buscarContatoMaisRecente(PageRequest.of(0, 1));

        if(contatos.size() > 0) {
            return contatos.get(0);
        }

        return null;
    }

    @Cacheable("contatoPcs")
    public ContatoPCS buscarContatoMaisRecenteComCache() {
        List<ContatoPCS> contatos = dao.buscarContatoMaisRecente(PageRequest.of(0, 1));

        if(contatos.size() > 0) {
            return contatos.get(0);
        }

        return null;
    }

    public Optional<ContatoPCS> findById(Long id) {
        return dao.findById(id);
    }

}