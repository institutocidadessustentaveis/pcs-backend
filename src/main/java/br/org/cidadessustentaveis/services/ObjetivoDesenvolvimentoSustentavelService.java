package br.org.cidadessustentaveis.services;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import br.org.cidadessustentaveis.dto.ItemComboDTO;
import br.org.cidadessustentaveis.dto.MetaObjetivoDesenvolvimentoSustentavelDTO;
import br.org.cidadessustentaveis.dto.ObjetivoDesenvolvimentoSustentavelDTO;
import br.org.cidadessustentaveis.dto.OdsParaComboDTO;
import br.org.cidadessustentaveis.model.administracao.MetaObjetivoDesenvolvimentoSustentavel;
import br.org.cidadessustentaveis.model.administracao.ObjetivoDesenvolvimentoSustentavel;
import br.org.cidadessustentaveis.repository.EixoRepository;
import br.org.cidadessustentaveis.repository.MetaObjetivoDesenvolvimentoSustentavelRepository;
import br.org.cidadessustentaveis.repository.ObjetivoDesenvolvimentoSustentavelRepository;
import br.org.cidadessustentaveis.services.exceptions.ObjectNotFoundException;

@Service
public class ObjetivoDesenvolvimentoSustentavelService {

	@Autowired
	private ObjetivoDesenvolvimentoSustentavelRepository odsRepository;
	
	@Autowired
	private  EixoRepository eixoRepository;

	@Autowired
	private MetaObjetivoDesenvolvimentoSustentavelRepository metaRepository;
	
	public List<ObjetivoDesenvolvimentoSustentavel> listar() {
	  return odsRepository.findAll();
	}
	
	public List<ObjetivoDesenvolvimentoSustentavel> listarTodosOrdenadoPorNumero() {
	  return odsRepository.findAllByOrderByNumeroAsc();
	}

	public ObjetivoDesenvolvimentoSustentavel listarPorId(final Long id) {
	  Optional<ObjetivoDesenvolvimentoSustentavel> ods = odsRepository.findById(id);
	  return ods.orElseThrow(() -> new ObjectNotFoundException("ODS não encontrado!"));
	}

	public ObjetivoDesenvolvimentoSustentavel inserir(final ObjetivoDesenvolvimentoSustentavelDTO odsDTO) {
	  ObjetivoDesenvolvimentoSustentavel ods = odsDTO.toEntityInsert();
	  if (!ods.getMetas().isEmpty()) {
		for (MetaObjetivoDesenvolvimentoSustentavel meta: ods.getMetas()) {
		  meta.setOds(ods);
		}
	  }	
	  return odsRepository.save(ods);
	}

	public ObjetivoDesenvolvimentoSustentavel alterar(final Long id, final ObjetivoDesenvolvimentoSustentavelDTO odsDTO) throws Exception {
		if (!(id == odsDTO.getId())) {
			throw new Exception("Campo id divergente.");
		}
		ObjetivoDesenvolvimentoSustentavel ods = odsDTO.toEntityUpdate(listarPorId(id));
		  for (MetaObjetivoDesenvolvimentoSustentavel meta: ods.getMetas()) {
		    if (meta.getOds() == null) {
		      meta.setOds(ods);
		    }
		  }
		return odsRepository.saveAndFlush(odsDTO.toEntityUpdate(ods));
	}

	public void deletar(final Long id) {
		ObjetivoDesenvolvimentoSustentavel ods = listarPorId(id);
		odsRepository.delete(ods);
	}

	public void deletarMeta(final Long idOds, final Long idMeta) {
		listarPorId(idOds);
		MetaObjetivoDesenvolvimentoSustentavel meta = listarMetaPorId(idMeta);
		meta.getOds().getMetas().remove(meta);
		metaRepository.delete(meta);
	}
	
	private MetaObjetivoDesenvolvimentoSustentavel listarMetaPorId(final Long idMeta) {
	  Optional<MetaObjetivoDesenvolvimentoSustentavel> meta = metaRepository.findById(idMeta);
	  return meta.orElseThrow(() -> new ObjectNotFoundException("Meta não encontrada!"));
	}

	public MetaObjetivoDesenvolvimentoSustentavel alterarMeta(final Long idOds, final Long idMeta, final MetaObjetivoDesenvolvimentoSustentavelDTO metaDTO) throws Exception {
		if (!(idMeta == metaDTO.getId())) {
			throw new Exception("Campo id divergente.");
		}
		ObjetivoDesenvolvimentoSustentavel ods = listarPorId(idOds);
		MetaObjetivoDesenvolvimentoSustentavel meta = metaDTO.toEntityUpdate(listarMetaPorId(idMeta));
		if (meta.getOds() == null) {
		  meta.setOds(ods);
		}
		return metaRepository.saveAndFlush(meta);
	}
	

	public List<ItemComboDTO> buscarPorIdEixo(final Long id) {
	  return odsRepository.buscarOdsPorIdEixo(id);
	}
	

	public List<ItemComboDTO> buscarOdsParaCombo() {
		return odsRepository.buscarOdsParaCombo();
	}
	
	public List< OdsParaComboDTO > buscarOdsParaComboComMetas() {
		List<OdsParaComboDTO> dtos = new ArrayList<>();
		List<ObjetivoDesenvolvimentoSustentavel> listaOds = odsRepository.findAll(new Sort(Sort.Direction.ASC, "id"));
		listaOds.forEach(item ->{
			dtos.add(new OdsParaComboDTO(item,false));
		});
		return  dtos;
	}

	public List<ObjetivoDesenvolvimentoSustentavelDTO> buscarPorListaIds(List<Long> ids) {
		  return odsRepository.buscarPorListaIds(ids);
	}

	public List<ObjetivoDesenvolvimentoSustentavel> buscarPorIds(List<Long> ids) {
		  return odsRepository.buscarPorIds(ids);
	}


}
