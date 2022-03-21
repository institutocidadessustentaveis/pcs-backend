package br.org.cidadessustentaveis.services;

import br.org.cidadessustentaveis.config.SpringContext;
import br.org.cidadessustentaveis.model.administracao.Usuario;
import br.org.cidadessustentaveis.model.boaspraticas.BoaPratica;
import br.org.cidadessustentaveis.model.sistema.HistoricoAcessoBoaPratica;
import br.org.cidadessustentaveis.repository.HistoricoAcessoBoaPraticaRepository;
import br.org.cidadessustentaveis.services.exceptions.ObjectNotFoundException;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class HistoricoAcessoBoaPraticaService {

    @Autowired
    private HistoricoAcessoBoaPraticaRepository dao;

    @Autowired
    private BoaPraticaService boaPraticaService;

    public HistoricoAcessoBoaPratica registarHistorico(Long idBoaPratica, String referer) {
        BoaPratica boaPratica = boaPraticaService.buscarPorId(idBoaPratica);

        HistoricoAcessoBoaPratica historico = new HistoricoAcessoBoaPratica();
        historico.setPalavraChave(this.extractKeywordFromReferer(referer));
        historico.setBoaPratica(boaPratica);

        Usuario usuarioLogado = getUsuario();

        if(usuarioLogado != null) {
            historico.setUsuario(usuarioLogado);
        }

        historico.setIp(getIP());

        historico.setDataHoraAcesso(LocalDateTime.now());

        return dao.save(historico);
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

    private String getIP() {
        String userIpAddress = getCurrentRequest().getHeader("X-Forwarded-For");

        if(userIpAddress == null) {
            userIpAddress = getCurrentRequest().getRemoteAddr();
        }

        return userIpAddress;
    }

    private static HttpServletRequest getCurrentRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        Assert.state(requestAttributes != null, "Could not find current request via RequestContextHolder");
        Assert.isInstanceOf(ServletRequestAttributes.class, requestAttributes);
        HttpServletRequest servletRequest = ((ServletRequestAttributes) requestAttributes).getRequest();
        Assert.state(servletRequest != null, "Could not find current HttpServletRequest");
        return servletRequest;
    }
    
    public List<Long> buscarHistoricoPorIdBoaPratica(Long id){
    	return dao.findAllByIdBoaPratica(id);
    }
    
    public void deleteById(Long id) {
    	dao.deleteById(id);
    }
}
