package br.org.cidadessustentaveis.repository;

import br.org.cidadessustentaveis.model.sistema.IPLookup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IPLookupRepository extends JpaRepository<IPLookup, Long> {

    public List<IPLookup> findByIp(String ip);

}
