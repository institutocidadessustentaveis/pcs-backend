package br.org.cidadessustentaveis.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import br.org.cidadessustentaveis.dto.ArquivoDTO;
import br.org.cidadessustentaveis.dto.ItemComboDTO;
import br.org.cidadessustentaveis.dto.MaterialInstitucionalDTO;
import br.org.cidadessustentaveis.model.institucional.Arquivo;
import br.org.cidadessustentaveis.model.institucional.MaterialInstitucional;
import br.org.cidadessustentaveis.repository.MaterialInstitucionalRepository;
import br.org.cidadessustentaveis.services.exceptions.DataIntegrityException;
import br.org.cidadessustentaveis.services.exceptions.ObjectNotFoundException;

@Service
public class MaterialInstitucionalService {
	
	@Autowired
	private MaterialInstitucionalRepository repository;
	
	@Autowired
	private ArquivoService arquivoService;
	
	public List<MaterialInstitucional> buscar() {
		return repository.findAll();
	}
	
	public MaterialInstitucional buscarPorId(Long id) {
		Optional<MaterialInstitucional> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException("Material institucional não encontrado!"));
	}
	
	public MaterialInstitucional inserir(MaterialInstitucionalDTO materialInstitucionalDto){
		MaterialInstitucional materialInstitucional = materialInstitucionalDto.toEntityInsert();
		return repository.save(materialInstitucional);
	}
	
	public MaterialInstitucional alterar(Long id, MaterialInstitucionalDTO materialInstitucionalDTO) {
		MaterialInstitucional materialInstitucionalRef = materialInstitucionalDTO.toEntityUpdate(buscarPorId(id));

		/*Prepara arquivos multimídia*/		
		List<Arquivo> arquivos = new ArrayList<>();
		for (ArquivoDTO arquivo : materialInstitucionalDTO.getArquivos()) {
			try {
				Arquivo arquivoAux = arquivoService.buscarPorId(arquivo.getId());
				arquivos.add(arquivoAux);
			} catch (Exception e) {
				Arquivo arquivoAux = arquivo.toEntityInsert();
				arquivos.add(arquivoAux);
			}
		}
		materialInstitucionalRef.setArquivos(arquivos);
		
		repository.save(materialInstitucionalRef);

		return materialInstitucionalRef;
	}
	
	public void deletar(Long id) {
		buscarPorId(id);
		try {
			repository.deleteById(id);
		}catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("O registro está relacionado com outra entidade");
		}	
	}
	
	public List<ItemComboDTO> buscarParaCombo() {
		return repository.buscarParaCombo();
	}
	
	public Arquivo downloadArquivos(Long idArquivo) {
		return arquivoService.buscarPorId(idArquivo);
	}

	public MaterialInstitucional buscarPorPublicacaoId(Long id) {
		return repository.findByPublicacaoId(id);
	}
}
