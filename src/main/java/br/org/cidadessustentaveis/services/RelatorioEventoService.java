package br.org.cidadessustentaveis.services;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.org.cidadessustentaveis.dto.RelatorioEventosDTO;
import br.org.cidadessustentaveis.model.administracao.AreaInteresse;
import br.org.cidadessustentaveis.model.administracao.Cidade;
import br.org.cidadessustentaveis.model.administracao.Eixo;
import br.org.cidadessustentaveis.model.administracao.ObjetivoDesenvolvimentoSustentavel;
import br.org.cidadessustentaveis.model.administracao.Pais;
import br.org.cidadessustentaveis.model.administracao.ProvinciaEstado;
import br.org.cidadessustentaveis.model.eventos.Evento;
import br.org.cidadessustentaveis.repository.EventoRepository;

@Service
public class RelatorioEventoService {
    
    @Autowired
    private EventoRepository eventoRepository;
    
    @Autowired
	private EntityManager em;
    
    public List<RelatorioEventosDTO> buscarRelatorios(String tipo, String dataInicial, String dataFinal,  String endereco, String titulo, Long cidade, Long estado, Long pais) {

		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<RelatorioEventosDTO> query = cb.createQuery(RelatorioEventosDTO.class);

		Root<Evento> evento = query.from(Evento.class);

		Join<Evento, Pais> joinPais = evento.join("pais",JoinType.LEFT);
		Join<Evento, ProvinciaEstado> joinProvinciaEstado = evento.join("provinciaEstado",JoinType.LEFT);
		Join<Evento, Cidade> joinCidade = evento.join("cidade",JoinType.LEFT);
		Join<Evento, ObjetivoDesenvolvimentoSustentavel> joinOds = evento.join("ods",JoinType.LEFT);
		
		query.multiselect(evento.get("id"), evento.get("dataEvento"), evento.get("tipo"), evento.get("endereco"), evento.get("nome"), joinPais.get("nome"),
				joinProvinciaEstado.get("nome"), joinCidade.get("nome"), evento.get("descricao"), evento.get("organizador"), evento.get("nome"), evento.get("nome"),
				joinOds.get("titulo"), evento.get("online"), evento.get("site"), evento.get("latitude"), evento.get("longitude"), evento.get("publicado"),
				evento.get("externo"), evento.get("linkExterno")
				);
		
		List<javax.persistence.criteria.Predicate> predicateList = new ArrayList<>();
		
		DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	
		if (tipo != null && !tipo.equals("")) {
			Path<String> tipoEvento = evento.get("tipo");
			predicateList.add(cb.equal(tipoEvento, tipo));
		}
		
		if(dataInicial != null && !dataInicial.equals("")) {
			Expression<LocalDate> campoDataHora = cb.function("date", LocalDate.class, evento.get("dataEvento"));
			LocalDate dataInicialFormatada = LocalDate.parse(dataInicial, df);
			predicateList.add(cb.greaterThanOrEqualTo(campoDataHora, dataInicialFormatada));
		}
		
		if(dataFinal != null && !dataFinal.equals("")) {
			Expression<LocalDate> campoDataHora = cb.function("date", LocalDate.class, evento.get("dataEvento"));
			LocalDate dataFinalFormatada = LocalDate.parse(dataFinal, df);
			predicateList.add(cb.lessThanOrEqualTo(campoDataHora, dataFinalFormatada));
		}
		
		if (endereco != null && !endereco.equals("")) {
			Path<String> local = evento.get("endereco");
			predicateList.add(cb.like(cb.lower(local), "%" + endereco.toLowerCase() + "%"));
		}
		
		if (titulo != null && !titulo.equals("")) {
			Path<String> tituloEvento = evento.get("nome");
			predicateList.add(cb.like(cb.lower(tituloEvento), "%" + titulo.toLowerCase() + "%"));
		}

		if (pais != null) {
			Path<Long> paisEvento = joinPais.get("id");
			predicateList.add(cb.equal(paisEvento, pais));
		}
		
		if (estado != null) {
			Path<Long> estadoEvento = joinProvinciaEstado.get("id");
			predicateList.add(cb.equal(estadoEvento, estado));
		}
		
		if (cidade != null) {
			Path<Long> cidadeEvento = joinCidade.get("id");
			predicateList.add(cb.equal(cidadeEvento, cidade));
		}

		javax.persistence.criteria.Predicate[] predicates = new javax.persistence.criteria.Predicate[predicateList.size()];
		predicateList.toArray(predicates);
		query.where(predicates);

		TypedQuery<RelatorioEventosDTO> typedQuery = em.createQuery(query);
		List<RelatorioEventosDTO> lista = typedQuery.getResultList();

		return lista;
	}
    
	public List<Eixo> findEixoById(Long idEvento){
		return eventoRepository.findEixoById(idEvento);
	}
	
	public List<AreaInteresse> findAreaInteresseById(Long idEvento){
		return eventoRepository.findAreaInteresseById(idEvento);
	}


}
