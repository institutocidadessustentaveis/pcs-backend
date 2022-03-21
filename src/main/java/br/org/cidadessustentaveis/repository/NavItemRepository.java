package br.org.cidadessustentaveis.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.org.cidadessustentaveis.dto.ItemComboDTO;
import br.org.cidadessustentaveis.model.administracao.Eixo;
import br.org.cidadessustentaveis.model.administracao.NavItem;
import br.org.cidadessustentaveis.model.enums.TipoNavItem;
	
@Repository
public interface NavItemRepository extends JpaRepository<NavItem, Long>{

    @Query("SELECT n " +
            "FROM " +
            "NavItem n " +
            "WHERE " +
            "n.navItemPai is null " +
            "order by n.posicao")
	List<NavItem> buscarTodosOrdemPosicaoAsc();
    
    
    @Query("SELECT new br.org.cidadessustentaveis.dto.ItemComboDTO(n.id, n.displayName) " +
            "FROM " +
            "NavItem n " +
            "order by n.id")
	List<ItemComboDTO> buscarTodosParaCombo();
    
    @Modifying
    @Query("DELETE FROM NavItem nav WHERE nav.menuPagina.id = :id")
    void deleteByIdMenuPagina(Long id);
        
    @Query("SELECT nav.id FROM NavItem nav WHERE nav.navItemPai.id = :id")
	List<Long> buscarNavItemComRelacaoNavPai(Long id);
    
    @Modifying
    void deleteByIdIn(List<Long> ids);
    
    @Query("SELECT new br.org.cidadessustentaveis.dto.ItemComboDTO(n.id, n.displayName) " +
            "FROM " +
            "NavItem n " +
            "WHERE n.tipoItem = ?1")
    List<ItemComboDTO> buscarNavItemPorTipoParaCombo(TipoNavItem tipoItem);
	
}
	