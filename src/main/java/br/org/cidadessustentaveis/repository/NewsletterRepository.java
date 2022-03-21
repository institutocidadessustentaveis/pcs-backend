package br.org.cidadessustentaveis.repository;

import br.org.cidadessustentaveis.model.institucional.Newsletter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsletterRepository extends JpaRepository<Newsletter, String> {

    public List<Newsletter> findByAtivo(boolean ativo);

}
