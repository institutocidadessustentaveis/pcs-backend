package br.org.cidadessustentaveis.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.org.cidadessustentaveis.dto.BoletimTemplate01ToListDTO;
import br.org.cidadessustentaveis.model.noticias.BoletimInformativo;
import br.org.cidadessustentaveis.model.noticias.BoletimTemplate01;

@Repository
public interface BoletimInformativoRepository extends JpaRepository<BoletimInformativo, Long>{

	@Query("Select new br.org.cidadessustentaveis.dto.BoletimTemplate01ToListDTO(bf.id, bt1.titulo, bf.data_hora_enviado, bf.usuario.nome)"
			+ " from BoletimInformativo bf"
			+ " inner join bf.boletimTemplate01 bt1")
	List<BoletimTemplate01ToListDTO> buscarBoletinsTemplate01();
	
	@Query("Select bt1"
			+ " from BoletimInformativo bf"
			+ " inner join bf.boletimTemplate01 bt1"
			+ " where bf.id = :id")
	BoletimTemplate01 buscarBoletimTemplate01PorId(Long id);
	
	@Query("Select bi"
			+ " from BoletimInformativo bi"
			+ " inner join bi.boletimTemplate01 bt1"
			+ " where bt1.id = :id")
	Optional<BoletimInformativo> buscarPorIdBoletimTemplate01(Long id);
	
}
