package br.org.cidadessustentaveis.repository;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.org.cidadessustentaveis.model.administracao.AprovacaoPrefeitura;
import br.org.cidadessustentaveis.model.administracao.EmailToken;
import br.org.cidadessustentaveis.model.administracao.Prefeitura;
import br.org.cidadessustentaveis.model.administracao.Usuario;
import br.org.cidadessustentaveis.model.enums.FuncionalidadeToken;
@Repository

public interface EmailTokenRepository extends JpaRepository<EmailToken, Long>{
	public Optional<EmailToken> findByHash(String hash);

	public EmailToken findByAprovacaoPrefeituraPrefeitura(Prefeitura prefeitura);
	public EmailToken findByUsuarioPrefeituraAndUsuario(Prefeitura prefeitura, Usuario usuario);
	public List<EmailToken> findByUsuarioAndFuncionalidadeTokenAndAtivo(Usuario usuario, FuncionalidadeToken recuperacaoSenha, Boolean ativo);
	public EmailToken findByAprovacaoPrefeitura(AprovacaoPrefeitura aprovacaoPrefeitura);
}
