package br.org.cidadessustentaveis.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailConstants;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.stereotype.Service;

import br.org.cidadessustentaveis.dto.BoletimTemplate01DTO;
import br.org.cidadessustentaveis.model.sistema.ParametroGeral;
import br.org.cidadessustentaveis.repository.ParametroGeralRepository;
import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 *
 * @author Leo
 */
@Service
public class EmailUtil {
	
	@Autowired
	private ParametroGeralRepository parametroGeralRepository; 
	
	@Autowired
	private ProfileUtil profileUtil;
	
	@Autowired
	private Configuration freemarkerConfig;
	
	@Async("threadExecutor")
    public void enviarEmailHTML(List<String> destinatarios, String titulo, String mensagem) throws EmailException {
        ParametroGeral parametroGeral = parametroGeralRepository.findById(1L).get();
        HtmlEmail email = new HtmlEmail();
        email.setDebug(false);
        email.setCharset(EmailConstants.UTF_8);
        email.setHostName(parametroGeral.getEnderecoSMTP());
        email.setSmtpPort(parametroGeral.getPortaSMTP());
        email.setAuthenticator(new DefaultAuthenticator(parametroGeral.getUsuarioSMTP(), 
                parametroGeral.getSenhaSMTP()));
        email.setSSLOnConnect(parametroGeral.getConexaoSeguraSMTP());
        email.setFrom(parametroGeral.getUsuarioSMTP(),parametroGeral.getUsuarioApelidoSMTP());
        email.setSubject(titulo);        
        email.setHtmlMsg(mensagem);
        
        for(String dest : destinatarios){
            email.addBcc(dest);            
        }
        email.send();
    }
	
	@Async("threadExecutor")
    public void enviarEmailHTMLPersonalizado(List<String> destinatarios, String titulo, String mensagem) throws EmailException {
        ParametroGeral parametroGeral = parametroGeralRepository.findById(1L).get();
        HtmlEmail email = new HtmlEmail();
        email.setDebug(false);
        email.setCharset(EmailConstants.UTF_8);
        email.setHostName(parametroGeral.getEnderecoSMTP());
        email.setSmtpPort(parametroGeral.getPortaSMTP());
        email.setAuthenticator(new DefaultAuthenticator(parametroGeral.getUsuarioSMTP(), 
                parametroGeral.getSenhaSMTP()));
        email.setSSLOnConnect(parametroGeral.getConexaoSeguraSMTP());
        email.setFrom(parametroGeral.getUsuarioSMTP(),parametroGeral.getUsuarioApelidoSMTP());
        email.setSubject(titulo);     
        email.setHtmlMsg(construirCorpo(mensagem));
        
        for(String dest : destinatarios){
            email.addBcc(dest);            
        }
        email.send();
    }
	
	@Async("threadExecutor")
    public void enviarEmailHTMLPersonalizadoTemplate(List<String> destinatarios, String titulo, BoletimTemplate01DTO template01) throws EmailException, MalformedTemplateNameException, ParseException, IOException {
        ParametroGeral parametroGeral = parametroGeralRepository.findById(1L).get();
        HtmlEmail email = new HtmlEmail();
        email.setDebug(false);
        email.setCharset(EmailConstants.UTF_8);
        email.setHostName(parametroGeral.getEnderecoSMTP());
        email.setSmtpPort(parametroGeral.getPortaSMTP());
        email.setAuthenticator(new DefaultAuthenticator(parametroGeral.getUsuarioSMTP(), 
                parametroGeral.getSenhaSMTP()));
        email.setSSLOnConnect(parametroGeral.getConexaoSeguraSMTP());
        email.setFrom(parametroGeral.getUsuarioSMTP(),parametroGeral.getUsuarioApelidoSMTP());
        email.setSubject(titulo);     
        freemarkerConfig.setClassForTemplateLoading(this.getClass(), "/templates");
        String template = null;
		try {
			Template t = freemarkerConfig.getTemplate("email-template01.ftl");
			String urlAPI = profileUtil.getProperty("profile.api");
			template01.setUrlAPI(urlAPI);
			template = FreeMarkerTemplateUtils.processTemplateIntoString(t, template01);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (TemplateException e) {
			e.printStackTrace();
		}
        email.setHtmlMsg(template);
        
        for(String dest : destinatarios){
            email.addBcc(dest);            
        }
        email.send();
    }
	
	@Async("threadExecutor")
    public void enviarEmailHTMLPersonalizadoComAnexo(List<String> destinatarios, File pdfFile) throws EmailException, IOException {
        ParametroGeral parametroGeral = parametroGeralRepository.findById(1L).get();
        HtmlEmail email = new HtmlEmail();
        email.attach(pdfFile);
        email.setDebug(false);
        email.setCharset(EmailConstants.UTF_8);
        email.setHostName(parametroGeral.getEnderecoSMTP());
        email.setSmtpPort(parametroGeral.getPortaSMTP());
        email.setAuthenticator(new DefaultAuthenticator(parametroGeral.getUsuarioSMTP(), 
                parametroGeral.getSenhaSMTP()));
        email.setSSLOnConnect(parametroGeral.getConexaoSeguraSMTP());
        email.setFrom(parametroGeral.getUsuarioSMTP(),parametroGeral.getUsuarioApelidoSMTP());
        email.setSubject("Certificado");        
        //email.setHtmlMsg(construirCorpo(mensagem));
        email.setBoolHasAttachments(true);
       
        
        for(String dest : destinatarios){
            email.addBcc(dest);            
        }
        email.send();
		Path path = FileSystems.getDefault().getPath("certificado.pdf");
		Files.deleteIfExists(path);
    }

	private String construirCorpo(String mensagem){
		StringBuffer msg = new StringBuffer();
		String urlAPI = profileUtil.getProperty("profile.api");
		
		msg.append("<html> <body style='font-family: sans-serif; background-color: white'>");
		
		msg.append("<div>");
	        msg.append("<div>");
	        	msg.append("<img src='" + urlAPI + "/arquivo/imagem/999978' style='width: 100%'>");	 
	        msg.append("</div>");  
	        
	        msg.append("<br>");	   
	        
	        msg.append("<div style='margin: 30px 30px'>");
	        	msg.append(mensagem);	        
	        msg.append("</div>");   
	        	
	        msg.append("<br>");
	        
	        msg.append("<div>");
        		msg.append("<img src='" + urlAPI + "/arquivo/imagem/999979' style='width: 100%'>");	 
        	msg.append("</div>"); 
        msg.append("</div>");     
	        	        
	    msg.append("</body></html>");
    
    return msg.toString();
	}
}
