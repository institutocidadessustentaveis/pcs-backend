package br.org.cidadessustentaveis.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.org.cidadessustentaveis.dto.PublicacaoDTO;
import br.org.cidadessustentaveis.model.institucional.Publicacao;

@Repository
public interface PublicacaoRepository extends JpaRepository<Publicacao, Long>{

	Page<PublicacaoDTO> findByMaterialApoioPrefeituraIsNotNull(PageRequest of);
	Page<PublicacaoDTO> findByMaterialApoioPrefeituraIsNull(PageRequest of);

}
