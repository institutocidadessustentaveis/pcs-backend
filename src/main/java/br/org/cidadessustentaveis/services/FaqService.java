package br.org.cidadessustentaveis.services;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.org.cidadessustentaveis.dto.FaqDTO;
import br.org.cidadessustentaveis.dto.FiltroFaqDTO;
import br.org.cidadessustentaveis.model.noticias.Noticia;
import br.org.cidadessustentaveis.model.participacaoCidada.Faq;
import br.org.cidadessustentaveis.repository.FaqRepository;
import br.org.cidadessustentaveis.services.exceptions.ObjectNotFoundException;

@Service
public class FaqService {
	
	@Autowired
	private FaqRepository faqRepository;
	
	@Autowired
	private EntityManager em;
	
	private final AtomicBoolean initialized = new AtomicBoolean(false);
	
	@Autowired
	private EntityManager entityManager;
	
	public Faq buscarPorId(Long id) {
		Optional<Faq> obj = faqRepository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException("nada encontrado!"));
	}
	
	public FaqDTO buscarFaqPorId(Long id) {
		Faq faqRef = buscarPorId(id);
		return new FaqDTO(faqRef);
	}

	public Faq inserir(FaqDTO faqDTO) {
	
		Faq faq = faqDTO.toEntityInsert(faqDTO);
		faqRepository.save(faq);
		
		return faq;
	}
	
	public void deletar(Long id) {
		faqRepository.deleteById(id);
	}
	
	public Faq alterar(FaqDTO faqDTO) throws Exception {
		if (faqDTO.getId() == null) {
			throw new Exception("Campo id divergente.");
		}
		Faq faq = faqDTO.toEntityUpdate(buscarPorId(faqDTO.getId()));
		faqRepository.save(faq);
		return faq;
	}
	
	public List<Faq> Listar() {
		return faqRepository.findAll();
	}
	
	public Long count() {
		return faqRepository.count();
	}

	public List<Faq> buscarFaqFiltrado(String palavraChave) {
		FullTextEntityManager fullTextEntityManager = getFullTextEntityManager();

		QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory()
															.buildQueryBuilder()
															.forEntity(Faq.class).get();

		try {
			org.apache.lucene.search.Query query = queryBuilder
													.keyword()
														.fuzzy()
														.withEditDistanceUpTo(1)
														.withPrefixLength(0)
														.onFields("pergunta", "resposta")
														.matching(palavraChave)
													.createQuery();

			org.hibernate.search.jpa.FullTextQuery jpaQuery =
													fullTextEntityManager.createFullTextQuery(query, Faq.class);

			return jpaQuery.setMaxResults(15).getResultList();
		} catch(org.hibernate.search.exception.EmptyQueryException e) {
			return Listar();
		}
	}
	
	private FullTextEntityManager getFullTextEntityManager() {
		final FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);

		if (initialized.get()) {
			return fullTextEntityManager;
		} else {
			synchronized (initialized) {
				if (!initialized.getAndSet(true)) {
					try {
						fullTextEntityManager.createIndexer().startAndWait();
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
					}
				}
				return fullTextEntityManager;
			}
		}
	}
}
