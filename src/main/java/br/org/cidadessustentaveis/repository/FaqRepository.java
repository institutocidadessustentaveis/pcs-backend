package br.org.cidadessustentaveis.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import br.org.cidadessustentaveis.model.participacaoCidada.Faq;

public interface FaqRepository extends JpaRepository<Faq, Long>{
	
	
}
