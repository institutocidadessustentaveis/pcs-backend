package br.org.cidadessustentaveis.repository;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.org.cidadessustentaveis.model.administracao.Credencial;
@Configurable
public interface CredencialRepository extends JpaRepository<Credencial, Long>{
	/*
	 * CAROS AMIGXS PARA EVITAR QUALQUER TIPO DE DUPLICAÇÃO DE EMAILS, NÃO SE ESQUEÇA 
	 * AO CRIAR UMA QUERY DE USUARIOS, NÃO BUSCAR AQUELES COM A PROPRIEDADE SNEXCLUIDO = TRUE
	 * FORTE ABRAÇO 
	 * */
	@Query("select c from Credencial c where LOWER(c.login) like LOWER(?1) and c.snExcluido = ?2 and c.snBloqueado = ?2")
	public Credencial findByLoginIgnoreCaseAndSnExcluido(String login,Boolean excluido);

}
