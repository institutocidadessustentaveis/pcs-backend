package br.org.cidadessustentaveis.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import br.org.cidadessustentaveis.model.planjementoIntegrado.ConsultaVariavel;

@Repository
public interface PlanejamentoIntegradoConsultaVariavelRepository extends JpaRepository<ConsultaVariavel, Long>{

	Optional<ConsultaVariavel> findById(Long id);

	List<ConsultaVariavel> findByUsuarioId(Long idUsuario);
}
