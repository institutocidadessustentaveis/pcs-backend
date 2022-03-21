package br.org.cidadessustentaveis.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.org.cidadessustentaveis.dto.FormularioAtendimentoDTO;
import br.org.cidadessustentaveis.model.administracao.FormularioAtendimento;
import br.org.cidadessustentaveis.repository.FormularioAtendimentoRepository;
import br.org.cidadessustentaveis.services.exceptions.ObjectNotFoundException;

@Service
public class FormularioAtendimentoService {
	
	@Autowired
	private FormularioAtendimentoRepository repository;
	
	
	public FormularioAtendimento salvar(FormularioAtendimentoDTO formularioDTO) {
		FormularioAtendimento formulario = formularioDTO.toEntityInsert();
		return repository.save(formulario);
	}
	
	public FormularioAtendimento buscarPorId(Long id) {
		Optional<FormularioAtendimento> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException("Formulário não encontrado!"));
	}
	
	public void changeFormularioParaRespondido(Long id) {
		repository.changeFormularioParaRespondido(id);
	}
	
	public List<FormularioAtendimentoDTO> buscarTodos() {
		return repository.buscarTodos();
	}

}
