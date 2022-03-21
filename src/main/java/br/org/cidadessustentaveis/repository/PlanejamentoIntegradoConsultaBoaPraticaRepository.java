package br.org.cidadessustentaveis.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.org.cidadessustentaveis.model.planjementoIntegrado.ConsultaBoaPratica;

@Repository
public interface PlanejamentoIntegradoConsultaBoaPraticaRepository extends JpaRepository<ConsultaBoaPratica, Long>{

	Optional<ConsultaBoaPratica> findById(Long id);

	List<ConsultaBoaPratica> findByUsuarioId(Long idUsuario);
}
