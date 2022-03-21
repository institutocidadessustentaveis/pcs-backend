package br.org.cidadessustentaveis.repository;

import br.org.cidadessustentaveis.model.administracao.RedeSocialRodape;
import br.org.cidadessustentaveis.model.enums.RedeSocial;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RedeSocialRodapeRepository extends JpaRepository<RedeSocialRodape, Long> {

    @Query("select r from RedeSocialRodape r order by r.ordem")
    public List<RedeSocialRodape> buscarRedesSociais(Pageable pageable);

}
