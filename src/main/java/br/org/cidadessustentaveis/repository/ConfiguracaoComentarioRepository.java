package br.org.cidadessustentaveis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.org.cidadessustentaveis.model.participacaoCidada.ConfiguracaoComentario;

public interface ConfiguracaoComentarioRepository extends JpaRepository<ConfiguracaoComentario, Long>{
	@Query(value="select new ConfiguracaoComentario(c.id, c.tamanhoComentario) from ConfiguracaoComentario c where c.tamanhoComentario != null")
	ConfiguracaoComentario findTamanhoComentario();
}
