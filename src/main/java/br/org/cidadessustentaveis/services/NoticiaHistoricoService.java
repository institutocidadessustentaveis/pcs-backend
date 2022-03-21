package br.org.cidadessustentaveis.services;

import java.util.List;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.org.cidadessustentaveis.dto.NoticiaHistoricoDTO;
import br.org.cidadessustentaveis.model.administracao.Usuario;
import br.org.cidadessustentaveis.model.noticias.Noticia;
import br.org.cidadessustentaveis.model.noticias.NoticiaHistorico;
import br.org.cidadessustentaveis.repository.NoticiaHistoricoRepository;

@Service
public class NoticiaHistoricoService {
	@Autowired
	private NoticiaHistoricoRepository repository;
	@Autowired
	EntityManager em;

	public NoticiaHistorico salvar(NoticiaHistoricoDTO noticiaHistoricoDto) throws Exception {
		NoticiaHistorico noticiaHistorico = noticiaHistoricoDto.toEntity();
		
		Usuario usuario = new Usuario();
		usuario.setId(noticiaHistoricoDto.getUsuario().getId());
		noticiaHistorico.setUsuario(usuario);
		
		Noticia noticia = new Noticia();
		noticia.setId(noticiaHistoricoDto.getId());
		noticiaHistorico.setNoticia(noticia);
		
		noticiaHistorico = repository.save(noticiaHistorico);
		
		return noticiaHistorico;
	}
	
	public NoticiaHistorico salvar(NoticiaHistorico noticiaHistorico) throws Exception {		
		noticiaHistorico = repository.save(noticiaHistorico);		
		return noticiaHistorico;
	}

	public List<Noticia> listar() {
		return repository.findAllByOrderByDataHoraDesc();
	}
	
	@Transactional
	public void deletarPorIdNoticia(Long id) {
		repository.deletarPorIdNoticia(id);
	}
}
