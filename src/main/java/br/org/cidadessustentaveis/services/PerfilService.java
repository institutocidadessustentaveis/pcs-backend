package br.org.cidadessustentaveis.services;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import br.org.cidadessustentaveis.dto.ItemComboDTO;
import br.org.cidadessustentaveis.dto.PerfilDTO;
import br.org.cidadessustentaveis.dto.PermissaoDTO;
import br.org.cidadessustentaveis.model.administracao.Funcionalidade;
import br.org.cidadessustentaveis.model.administracao.Perfil;
import br.org.cidadessustentaveis.model.administracao.Permissao;
import br.org.cidadessustentaveis.model.administracao.Usuario;
import br.org.cidadessustentaveis.repository.PerfilRepository;
import br.org.cidadessustentaveis.services.exceptions.DataIntegrityException;
import br.org.cidadessustentaveis.services.exceptions.ObjectNotFoundException;

@Service
public class PerfilService {
	
	@Autowired
	private PerfilRepository repository;
	
	@Autowired
	private FuncionalidadeService funcionalidadeService;
	
	public List<Perfil> buscar(){
		return repository.findAll(Sort.by(Sort.Direction.ASC, "nome"));
	}
	public List<Perfil> buscarPerfilGestaoPublica() {
		return repository.findByGestaoPublica(Boolean.TRUE);
	}
	
	public Perfil buscarPorId(Long id) {
		Optional<Perfil> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException("Perfil não encontrado!"));
	}
	
	public Perfil inserir(PerfilDTO perfilDTO) {
		Perfil perfilRef = repository.findByNome(perfilDTO.getNome());
		
		
		if(perfilRef != null) {
			throw new DataIntegrityException("Já existe um registro cadastrado com este nome");
		}
		
		validarNome(perfilDTO.getNome());
		
		Perfil perfil = perfilDTO.toEntityInsert();
		for (Permissao permissao : perfil.getPermissoes()) {
			Funcionalidade funcionalidadeRef = funcionalidadeService.buscarPorId(permissao.getFuncionalidade().getId());	
			permissao.setFuncionalidade(funcionalidadeRef);
			permissao.setPerfil(perfil);
		}
		return repository.save(perfil);

	}
	
	private void validarNome(String nome) {
		Pattern regex = Pattern.compile("[$&+, :;=\\\\?@#|/'<>.^*()%!-]");
		
		if (regex.matcher(nome).find()) {
			throw new DataIntegrityException("Nome não pode conter caracteres especiais");
		} 
	}
	
	public void editar(PerfilDTO perfilDto, Long idPerfil) {
		Perfil perfilRef = buscarPorId(idPerfil);
		perfilRef = perfilDto.toEntityUpdate(perfilRef);
		
		for(PermissaoDTO permissaoDTO : perfilDto.getPermissoes()) {
			System.out.println(permissaoDTO.toString());
			if(permissaoDTO.getId() != null) {
				Permissao permissaoRef = perfilRef.getPermissoes().stream().filter(p -> p.getId().equals(permissaoDTO.getId())).findFirst().get();
				permissaoDTO.toEntityUpdate(permissaoRef);
			} else {
				Permissao permissao = permissaoDTO.toEntityInsert();
				permissao.setPerfil(perfilRef);
				perfilRef.getPermissoes().add(permissao);
			}
		}
		repository.save(perfilRef);
	}
	
	public void deletar(Long id) {
		buscarPorId(id);
		try {
			repository.deleteById(id);
		}catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("O registro está relacionado com outra entidade");
		}	
	}
	
	public List<ItemComboDTO> buscarComboBoxPerfil() {
		return repository.findComboBoxPerfil();
	}

}
