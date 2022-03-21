package br.org.cidadessustentaveis.services;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import br.org.cidadessustentaveis.config.SpringContext;
import br.org.cidadessustentaveis.model.administracao.Usuario;
import br.org.cidadessustentaveis.model.noticias.Noticia;
import br.org.cidadessustentaveis.model.sistema.HistoricoAcessoNoticia;
import br.org.cidadessustentaveis.repository.HistoricoAcessoNoticiaRepository;
import br.org.cidadessustentaveis.services.exceptions.ObjectNotFoundException;
import br.org.cidadessustentaveis.util.IPUtil;

@Service
public class HistoricoAcessoNoticiaService {

    @Autowired
    private HistoricoAcessoNoticiaRepository historicoAcessoNoticiaRepository;

    public HistoricoAcessoNoticia registrarAcesso(Noticia noticia, String referer) {
        HistoricoAcessoNoticia historico = new HistoricoAcessoNoticia();

        historico.setIp(IPUtil.getIP());

        historico.setPalavraChave(this.extractKeywordFromReferer(referer));

        historico.setNoticia(noticia);

        Usuario usuario = this.getUsuario();

        if(usuario != null) {
            historico.setUsuario(usuario);
        }

        historico.setDataHoraAcesso(LocalDateTime.now());

        return historicoAcessoNoticiaRepository.save(historico);
    }

    private String extractKeywordFromReferer(String referer) {
        if(referer == null) return null;
        if(referer.isEmpty()) return null;

        String keywords = null;

        try {
            List<NameValuePair> params = URLEncodedUtils.parse(new URI(referer), Charset.forName("UTF-8"));
            Optional<NameValuePair> opt = params.stream()
                                                    .filter((p) -> p.getName().equals("palavra-chave"))
                                                .findFirst();
            keywords = opt.isPresent() ? opt.get().getValue() : null;
        } catch (URISyntaxException e) {
            return null;
        }

        return keywords;
    }

    private Usuario getUsuario() {
        ApplicationContext context = SpringContext.getAppContext();
        UsuarioService usuarioService = (UsuarioService)context.getBean("usuarioService");
        String user = SecurityContextHolder.getContext().getAuthentication().getName();

        try {
            return usuarioService.buscarPorEmailCredencial(user);
        } catch(ObjectNotFoundException ex) {
            return null;
        }
    }
    
    @Transactional
    public void deletarPorIdNoticia(Long id) {
    	historicoAcessoNoticiaRepository.deletarPorIdNoticia(id);
	}
}
