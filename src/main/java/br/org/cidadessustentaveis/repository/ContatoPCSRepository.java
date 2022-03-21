package br.org.cidadessustentaveis.repository;

import br.org.cidadessustentaveis.model.administracao.ContatoPCS;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContatoPCSRepository extends JpaRepository<ContatoPCS, Long> {

    @Query("select c from ContatoPCS c order by data desc")
    public List<ContatoPCS> buscarContatoMaisRecente(Pageable pageable);

}
