package br.org.cidadessustentaveis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.org.cidadessustentaveis.model.administracao.AtividadeUsuario;

@Repository
public interface AtividadeUsuarioRepository extends JpaRepository<AtividadeUsuario, Long>{

}
