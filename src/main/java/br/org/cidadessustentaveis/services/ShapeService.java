package br.org.cidadessustentaveis.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.org.cidadessustentaveis.model.administracao.Shape;
import br.org.cidadessustentaveis.repository.ShapeRepository;
import br.org.cidadessustentaveis.services.exceptions.ObjectNotFoundException;

@Service
public class ShapeService {

	@Autowired
	private ShapeRepository shapeRepository;
	
	public List<Shape> listar() {
		return shapeRepository.findAll();
	}
	
	public Shape listarById(final Long id) {
	  Optional<Shape> shape = shapeRepository.findById(id);
	  return shape.orElseThrow(() -> new ObjectNotFoundException("Shape não encontrado!"));
	}
	
	public Shape inserir(Shape shape) {
		return shapeRepository.save(shape);
	}
	
	/*
	public Shape alterar(final Long id, final ShapeDTO shapeDTO) throws Exception {
		if (!(id == shapeDTO.getId())) {
			throw new Exception("Campo id divergente.");
		}
		Shape shape = listarById(id);
		return shapeRepository.saveAndFlush(shapeDTO.toEntityUpdate(shape));
	}
	*/
	
	public void deletar(final Long id) {
		listarById(id);
		shapeRepository.deleteById(id);
	}
	
	
}
