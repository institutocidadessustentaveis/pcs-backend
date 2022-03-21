package br.org.cidadessustentaveis.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.org.cidadessustentaveis.dto.ItemComboDTO;
import br.org.cidadessustentaveis.model.administracao.MetaObjetivoDesenvolvimentoSustentavel;
import br.org.cidadessustentaveis.repository.MetaObjetivoDesenvolvimentoSustentavelRepository;

@Service
public class MetaObjetivoDesenvolvimentoSustentavelService {

    @Autowired
    private MetaObjetivoDesenvolvimentoSustentavelRepository dao;

    public MetaObjetivoDesenvolvimentoSustentavel find(Long id) {
        return dao.getOne(id);
    }

	public List<MetaObjetivoDesenvolvimentoSustentavel> listar() {
		  return dao.findAll();
	}
	
	public List<ItemComboDTO> buscarMetasParaCombo() {
		return dao.buscarMetasParaCombo();
	}
	
	public List<MetaObjetivoDesenvolvimentoSustentavel> buscarPorIds(List<Long> ids) {
		return dao.buscarPorIds(ids);
	}
	
	public List<ItemComboDTO> buscarMetaOdsPorIdOdsItemCombo(List<Long> idsOds){
		return dao.buscarMetaOdsPorIdOdsItemCombo(idsOds);
	}
}
