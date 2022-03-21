package br.org.cidadessustentaveis.repository;

import br.org.cidadessustentaveis.model.administracao.LinkRodape;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LinkRodapeRepository extends JpaRepository<LinkRodape, Long> {

    @Query("select l from LinkRodape l order by l.ordem asc")
    public List<LinkRodape> buscarLinksOdernados(Pageable pageable);

}
