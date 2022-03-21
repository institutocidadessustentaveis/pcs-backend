package br.org.cidadessustentaveis.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.org.cidadessustentaveis.dto.ItemComboDTO;
import br.org.cidadessustentaveis.model.administracao.Perfil;
import br.org.cidadessustentaveis.model.administracao.Usuario;

@Repository
public interface PerfilRepository extends JpaRepository<Perfil, Long>{
	public List<Perfil> findByGestaoPublica(Boolean gestaoPublica);
	
	public Perfil findByNome(String nome);
	
	@Query("select new br.org.cidadessustentaveis.dto.ItemComboDTO(p.id, p.nome) from Perfil p order by p.nome")
	List<ItemComboDTO> findComboBoxPerfil();

}
