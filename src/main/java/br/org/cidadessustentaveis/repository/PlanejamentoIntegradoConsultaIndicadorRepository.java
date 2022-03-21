package br.org.cidadessustentaveis.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.org.cidadessustentaveis.model.planjementoIntegrado.ConsultaIndicador;

@Repository
public interface PlanejamentoIntegradoConsultaIndicadorRepository extends JpaRepository<ConsultaIndicador, Long>{

	Optional<ConsultaIndicador> findById(Long id);

	List<ConsultaIndicador	> findByUsuarioId(Long idUsuario);
}
