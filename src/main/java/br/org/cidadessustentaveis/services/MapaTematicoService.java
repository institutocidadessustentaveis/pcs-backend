package br.org.cidadessustentaveis.services;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import br.org.cidadessustentaveis.dto.MapaTematicoDTO;
import br.org.cidadessustentaveis.model.administracao.Usuario;
import br.org.cidadessustentaveis.model.planjementoIntegrado.MapaTematico;
import br.org.cidadessustentaveis.repository.MapaTematicoRepository;
import br.org.cidadessustentaveis.services.exceptions.DataIntegrityException;
import br.org.cidadessustentaveis.services.exceptions.ObjectNotFoundException;

@Service
public class MapaTematicoService {
	
	@Autowired
	private MapaTematicoRepository mapaTematicoRepository;	
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	EntityManager em;
	
	
	public MapaTematico buscarPorId(Long id) {
		Optional<MapaTematico> obj = mapaTematicoRepository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException("Mapa Tematico não encontrada!"));
	}
	
	public void editarExibirAuto(Long idMapa, boolean exibirAuto, Long shapeId) {
		mapaTematicoRepository.setExibirAutoToFalseAll(shapeId);
		mapaTematicoRepository.editarExibirAutoById(idMapa, exibirAuto);
	}
	
	public void editarExibirLegenda(Long idMapa, boolean exibirLegenda, Long shapeId) {
		mapaTematicoRepository.editarExibirLegendaById(idMapa, exibirLegenda);
	}
	
	public MapaTematico inserirMapaTematico(MapaTematicoDTO mapaTematicoDTO) {
		
		String user = SecurityContextHolder.getContext().getAuthentication().getName();
		Usuario usuario  = usuarioService.buscarPorEmail(user);

		MapaTematico mapaTematico = new MapaTematico();
		mapaTematico.setNome(mapaTematicoDTO.getNome());
		mapaTematico.setLayerName(mapaTematicoDTO.getLayerName());
		mapaTematico.setAttributeName(mapaTematicoDTO.getAttributeName());	
		mapaTematico.setType(mapaTematicoDTO.getType());
		mapaTematico.setClasses(mapaTematicoDTO.getClasses());
		mapaTematico.setUsuario(usuario);
		mapaTematico.setIdShapeFile(mapaTematicoDTO.getIdShapeFile());
		mapaTematico.setCorMinima(mapaTematicoDTO.getCorMinima());
		mapaTematico.setCorMaxima(mapaTematicoDTO.getCorMaxima());
		mapaTematico.setNumeroClasses(mapaTematicoDTO.getNumeroClasses());
		mapaTematico.setExibirLegenda(mapaTematicoDTO.isExibirLegenda());
			
		mapaTematico = mapaTematicoRepository.save(mapaTematico);

		return mapaTematico;
	}
	
	
	public List<MapaTematico> buscarMapasTematicos(Long idShapeFile) {
		
		String user = SecurityContextHolder.getContext().getAuthentication().getName();
		Usuario usuario  = usuarioService.buscarPorEmail(user);

		return mapaTematicoRepository.findByUsuarioIdAndIdShapeFile(usuario.getId(),idShapeFile);
	}
	
	public MapaTematico mapaExibirAuto(Long idShapeFile) {
		return mapaTematicoRepository.mapaExibirAuto(idShapeFile);
	}
	
	public void deletarMapaTematico(Long id) {
		try {
			mapaTematicoRepository.deleteById(id);
		}catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("O registro está relacionado com outra entidade");
		}	
	}


}
