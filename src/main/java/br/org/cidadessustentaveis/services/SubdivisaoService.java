package br.org.cidadessustentaveis.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.org.cidadessustentaveis.dto.SubdivisaoDTO;
import br.org.cidadessustentaveis.dto.SubdivisaoFilhosDTO;
import br.org.cidadessustentaveis.model.administracao.SubdivisaoCidade;
import br.org.cidadessustentaveis.model.planjementoIntegrado.ShapeFile;
import br.org.cidadessustentaveis.repository.SubdivisaoCidadeRepository;
import br.org.cidadessustentaveis.services.exceptions.ObjectNotFoundException;

@Service
public class SubdivisaoService {
	
    @Autowired
    private SubdivisaoCidadeRepository subdivisaoCidadeRepository;
    
    @Autowired
    private CidadeService cidadeService;
    
    @Autowired
    private TipoSubdivisaoService tipoSubdivisaoService;
    
	@Autowired
	private ShapeFileService shapeFileService;

    
	public SubdivisaoCidade buscarPorId(final Long id) {
		  Optional<SubdivisaoCidade> subdivisaoCidade = subdivisaoCidadeRepository.findById(id);
		  return subdivisaoCidade.orElseThrow(() -> new ObjectNotFoundException("Subdivisao não encontrada!"));
	}
	
	public SubdivisaoCidade inserir(SubdivisaoDTO subdivisaoDTO) throws Exception {	
		
		SubdivisaoCidade subdivisao = subdivisaoDTO.toEntityInsert();
		subdivisao.setNome(subdivisaoDTO.getNome());
		subdivisao.setCidade(cidadeService.buscarPorId(subdivisaoDTO.getCidade()));
		if(subdivisaoDTO.getSubdivisaoPai() != null && subdivisaoDTO.getSubdivisaoPai().getId() != null) {
			subdivisao.setSubdivisaoPai(buscarPorId(subdivisaoDTO.getSubdivisaoPai().getId()));		
		}
		subdivisao.setTipoSubdivisao(tipoSubdivisaoService.buscarPorId(subdivisaoDTO.getTipoSubdivisao().getId()));
		SubdivisaoCidade ref = subdivisaoCidadeRepository.save(subdivisao);
		
		return ref;
	} 
	

	public List<SubdivisaoDTO> buscarTodosPorCidadeId(Long idCidade) throws Exception {

		return subdivisaoCidadeRepository.buscarTodosPorCidadeIdOrderByNivelAndSubdivisaoNome(idCidade);
	}
	
	public void deletar(Long id) {	
		
		Long idShapeFile = shapeFileService.buscarShapeFileIdPorSubdivisaoId(id);
		if(idShapeFile != null) {
			shapeFileService.excluirShapeFilePorId(idShapeFile);
		}
		
		subdivisaoCidadeRepository.deleteById(id);
	}
	
	public SubdivisaoDTO alterar(SubdivisaoDTO subdivisaoDTO) throws Exception {
		if (subdivisaoDTO.getId() == null) {
			throw new Exception("Campo id divergente.");
		}
	    
		SubdivisaoCidade subdivisao = subdivisaoDTO.toEntityUpdate(buscarPorId(subdivisaoDTO.getId()));
		

		subdivisaoCidadeRepository.save(subdivisao);
		
		return new SubdivisaoDTO(subdivisao);
	}
	
	public SubdivisaoCidade update(SubdivisaoDTO subdivisaoDTO) throws Exception {
		if (subdivisaoDTO.getId() == null) {
			throw new Exception("Campo id divergente.");
		}
	    
		SubdivisaoCidade subdivisao = subdivisaoDTO.toEntityUpdate(buscarPorId(subdivisaoDTO.getId()));
		
		if(subdivisaoDTO.getCidade() != null) {
			subdivisao.setCidade(cidadeService.buscarPorId(subdivisaoDTO.getCidade()));	
		}
		
		if(subdivisaoDTO.getSubdivisaoPai() != null && subdivisaoDTO.getSubdivisaoPai().getId() != null) {
			subdivisao.setSubdivisaoPai(buscarPorId(subdivisaoDTO.getSubdivisaoPai().getId()));		
		}
		
		if(subdivisaoDTO.getTipoSubdivisao() != null && subdivisaoDTO.getTipoSubdivisao().getId() != null ) {
			subdivisao.setTipoSubdivisao(tipoSubdivisaoService.buscarPorId(subdivisaoDTO.getTipoSubdivisao().getId()));
		}

		SubdivisaoCidade ref = subdivisaoCidadeRepository.save(subdivisao);
		
		return ref;
	}
	
	public List<SubdivisaoDTO> buscarTodasSubdivisaoRelacionadasComSubdivisaoPai(Long idSubdivisao) throws Exception {

		return subdivisaoCidadeRepository.buscarTodasSubdivisaoRelacionadasComSubdivisaoPai(idSubdivisao);
	}

	public List<SubdivisaoCidade> buscarPorNivelECidade(long nivelTipo, Long idCidade) {
		List<SubdivisaoCidade> lista = this.subdivisaoCidadeRepository.findByCidadeIdAndTipoSubdivisaoNivel(idCidade, nivelTipo);
		return lista;
	}

	public void preencherFilhosDTO(SubdivisaoFilhosDTO dto) {
		if(dto.getId() != null) {
			List<SubdivisaoCidade> subdivisoes = this.subdivisaoCidadeRepository.findBySubdivisaoPaiId(dto.getId());
			for(SubdivisaoCidade subdivisao : subdivisoes ){
				SubdivisaoFilhosDTO filhoDTO = new SubdivisaoFilhosDTO(subdivisao);
				preencherFilhosDTO(filhoDTO);
				dto.getFilhos().add(filhoDTO);
			}
		}
	}

	public SubdivisaoCidade buscarUfCidadeSubdivisao(String uf, String cidade, String nomeSubdivisao) {
		SubdivisaoCidade subdivisao = this.subdivisaoCidadeRepository.findByUfCidadeSubdivisao(uf, cidade, nomeSubdivisao);
		return subdivisao;
	}

	public SubdivisaoCidade findBySheetName(String sheetName) {
		SubdivisaoCidade subdivisao = this.subdivisaoCidadeRepository.findBySheetName(sheetName);
		return subdivisao;
	}
	
	
	
}
