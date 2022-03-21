package br.org.cidadessustentaveis.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.org.cidadessustentaveis.model.participacaoCidada.SecaoFormulario;

@Repository
public interface SecaoFormularioRepository extends JpaRepository<SecaoFormulario, Long>{
	@Query("SELECT s.id from SecaoFormulario s WHERE s.formulario is null")
	public List<Long> secoesSemFormulario();
}
