package br.org.cidadessustentaveis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.org.cidadessustentaveis.model.participacaoCidada.PropostaMunicipio;

@Repository
public interface PropostaMunicipioRepository extends JpaRepository<PropostaMunicipio, Long>{
	
}
