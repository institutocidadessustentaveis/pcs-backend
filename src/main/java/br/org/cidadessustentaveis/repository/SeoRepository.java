package br.org.cidadessustentaveis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.org.cidadessustentaveis.model.institucional.Seo;

@Repository
public interface SeoRepository extends JpaRepository<Seo, Long>{
}
