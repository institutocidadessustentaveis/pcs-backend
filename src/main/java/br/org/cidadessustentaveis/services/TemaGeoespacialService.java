package br.org.cidadessustentaveis.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import br.org.cidadessustentaveis.dto.TemaGeoespacialDTO;
import br.org.cidadessustentaveis.dto.TemaGeoespacialExibicaoDTO;
import br.org.cidadessustentaveis.model.planjementoIntegrado.TemaGeoespacial;
import br.org.cidadessustentaveis.repository.TemaGeoespacialRepository;

@Service
public class TemaGeoespacialService {

	@Autowired
	private TemaGeoespacialRepository geoespacialRepository;
	@Autowired
	private AreaInteresseService areaInteresseService;
	@Autowired
	private EixoService eixoService;
	@Autowired
	private ObjetivoDesenvolvimentoSustentavelService odsService;
	@Autowired
	private MetaObjetivoDesenvolvimentoSustentavelService metaService;
	
	
	public List<TemaGeoespacialDTO> buscarTodos() {
		List<TemaGeoespacial> temas = this.geoespacialRepository.findAll(new Sort(Sort.Direction.ASC, "nome"));
		List<TemaGeoespacialDTO> dtos = new ArrayList<>();
		temas.forEach(item ->{
			TemaGeoespacialDTO  dto = new TemaGeoespacialDTO(item);
			dtos.add(dto);
		});

		return dtos;
	}
	public List<TemaGeoespacialExibicaoDTO> buscarTodosSimples() {
		List<TemaGeoespacial> temas = this.geoespacialRepository.findAll(new Sort(Sort.Direction.ASC, "nome"));
		List<TemaGeoespacialExibicaoDTO> dtos = new ArrayList<>();
		temas.forEach(item ->{
			TemaGeoespacialExibicaoDTO  dto = new TemaGeoespacialExibicaoDTO(item);
			dtos.add(dto);
		});

		return dtos;
	}

	public void remover(Long id) {
		Optional<TemaGeoespacial> temaOptional = geoespacialRepository.findById(id);
		TemaGeoespacial tema = temaOptional.get();
		this.geoespacialRepository.delete(tema);
		
	}

	public void atualizar(TemaGeoespacialDTO dto) {
		Optional<TemaGeoespacial> temaOptional = geoespacialRepository.findById(dto.getId());
		TemaGeoespacial tema = temaOptional.get();
		tema.setNome(dto.getNome());
		tema.setDescricao(dto.getDescricao());
		tema.setAreasInteresse(new ArrayList<>());
		tema.setEixos(new ArrayList<>());
		tema.setOds(new ArrayList<>());
		tema.setMetas(new ArrayList<>());
		if(dto.getAreasInteresse() != null) {
			dto.getAreasInteresse().forEach(item -> {
				tema.getAreasInteresse().add(areaInteresseService.buscarPorId(item));
			});
		}
		if(dto.getEixos() != null) {
			dto.getEixos().forEach(item -> {
				tema.getEixos().add(eixoService.listarById(item));
			});
		}
		if(dto.getOds() != null) {
			dto.getOds().forEach(item -> {
				tema.getOds().add(odsService.listarPorId(item));
			});
		}
		if(dto.getMetas() != null) {
			dto.getMetas().forEach(item -> {
				tema.getMetas().add(metaService.find(item));
			});
		}
		this.geoespacialRepository.save(tema);
		
		
	}

	public void salvar(TemaGeoespacialDTO dto) {
		TemaGeoespacial tema = new TemaGeoespacial();
		tema.setNome(dto.getNome());
		tema.setDescricao(dto.getDescricao());
		if(dto.getAreasInteresse() != null) {
			dto.getAreasInteresse().forEach(item -> {
				tema.getAreasInteresse().add(areaInteresseService.buscarPorId(item));
			});
		}
		if(dto.getEixos() != null) {
			dto.getEixos().forEach(item -> {
				tema.getEixos().add(eixoService.listarById(item));
			});
		}
		if(dto.getOds() != null) {
			dto.getOds().forEach(item -> {
				tema.getOds().add(odsService.listarPorId(item));
			});
		}
		if(dto.getMetas() != null) {
			dto.getMetas().forEach(item -> {
				tema.getMetas().add(metaService.find(item));
			});
		}
		this.geoespacialRepository.save(tema);
	}

	public TemaGeoespacialDTO buscarPorId(Long id) {
		Optional<TemaGeoespacial> tema = this.geoespacialRepository.findById(id);
		if(!tema.isPresent()) {
			throw new EntityNotFoundException("Tema não encontrado.");
		}
		TemaGeoespacialDTO dto = new TemaGeoespacialDTO(tema.get());
		return dto;
	}
	public TemaGeoespacial buscar(Long temaGeoespacial) {
		Optional<TemaGeoespacial> tema = this.geoespacialRepository.findById(temaGeoespacial);
		if(!tema.isPresent()) {
			throw new EntityNotFoundException("Tema não encontrado.");
		}
		return tema.get();
	}
	
	
}
