package br.org.cidadessustentaveis.services;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.org.cidadessustentaveis.dto.BoletimInformativoDTO;
import br.org.cidadessustentaveis.dto.BoletimTemplate01DTO;
import br.org.cidadessustentaveis.model.institucional.Newsletter;
import br.org.cidadessustentaveis.model.noticias.BoletimInformativo;
import br.org.cidadessustentaveis.repository.NewsletterRepository;
import br.org.cidadessustentaveis.util.EmailUtil;
import br.org.cidadessustentaveis.util.ProfileUtil;

@Service
public class NewsletterService {

    @Autowired
    private NewsletterRepository dao;

    @Autowired
    private UsuarioService usuarioService;


    @Autowired
    private EmailUtil emailUtil;
    
    @Autowired
    private BoletimTemplate01Service boletimTemplate01Service;
    
    @Autowired
    private BoletimInformativoService boletimInformativoService;
    
    @Autowired
    private ProfileUtil profileUtil;
    
    public void save(Newsletter newsletter) {
        dao.save(newsletter);
    }

    public void delete(Newsletter newsletter) {
        dao.delete(newsletter);
    }

    public void delete(String email) {
        dao.deleteById(email);
    }

    public boolean exists(String email) {
        return dao.existsById(email);
    }

    public Newsletter find(String email) {
        Optional<Newsletter> optional = dao.findById(email);

        if(optional.isPresent()) {
            return optional.get();
        }

        return null;

    }
    
    public Long salvarTemplate01(BoletimTemplate01DTO template01) throws Exception {
        if (template01.getTextoIntroducao() != null) {
        	return boletimTemplate01Service.salvarTemplate01(template01);
        }
        return null;
    }
    
    public void editarBoletimTemplate01(BoletimTemplate01DTO template01) throws Exception {
        if (template01.getTextoIntroducao() != null) {
        	boletimTemplate01Service.editarBoletimTemplate01(template01);
        }
    }
    
    public void salvarDataHoraEnviado(BoletimTemplate01DTO template01, Long idBoletim) throws Exception {
    	
    	BoletimInformativo boletim = idBoletim != null ? boletimInformativoService.buscarPorId(idBoletim) : boletimInformativoService.buscarPorIdBoletimTemplate01(template01.getId());
    	boletim.setData_hora_enviado(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
    	boletimInformativoService.saveDadosBoletim(boletim);
    }
    
    public void enviarTemplate01(BoletimTemplate01DTO template01, Long idBoletim) throws Exception {
    	List<String> emails = this.buscarEmailsEnvioNewsletter();
    	salvarDataHoraEnviado(template01, idBoletim);
        if (!emails.isEmpty() && boletimTemplate01Valido(template01) == true) {
       	emailUtil.enviarEmailHTMLPersonalizado(emails, "Cidades sustentáveis - Boletim de notícias", construirEmailBoletimTemplate01(template01).toString());
       }
    }
    
    public void enviarTesteTemplate01(BoletimTemplate01DTO template01, Long idBoletim, String emailTeste) throws Exception {
    	List<String> emailTesteList = new ArrayList<String>();
    	emailTesteList.add(emailTeste);
        if (!emailTesteList.isEmpty() && boletimTemplate01Valido(template01) == true) {
       	emailUtil.enviarEmailHTMLPersonalizado(emailTesteList, "Cidades sustentáveis - Boletim de notícias", construirEmailBoletimTemplate01(template01).toString());
       }
    }
    
    public void enviarTesteTemplateHtml01(BoletimTemplate01DTO template01, Long idBoletim, String emailTeste) throws Exception {
    	List<String> emailTesteList = new ArrayList<String>();
    	emailTesteList.add(emailTeste);
        if (!emailTesteList.isEmpty() && boletimTemplate01Valido(template01) == true) {
       	emailUtil.enviarEmailHTMLPersonalizadoTemplate(emailTesteList, "Cidades sustentáveis - Boletim de notícias", template01);
       }
    }
   
    public void enviarTemplate01PorId(Long idBoletim) throws Exception {
    	List<String> emails = this.buscarEmailsEnvioNewsletter();
    	BoletimInformativo boletim = boletimInformativoService.buscarPorId(idBoletim);
    	BoletimTemplate01DTO boletimTemplate01DTO = new BoletimTemplate01DTO(boletim.getBoletimTemplate01());
    	salvarDataHoraEnviado(boletimTemplate01DTO, (Long) null);
        if (!emails.isEmpty() && boletimTemplate01Valido(boletimTemplate01DTO) == true) {
       	emailUtil.enviarEmailHTMLPersonalizado(emails, "Cidades sustentáveis - Boletim de notícias", construirEmailBoletimTemplate01(boletimTemplate01DTO).toString());
       }
    	
    }

    public List<String> buscarEmailsEnvioNewsletter() {
        List<Newsletter> newsletters = dao.findByAtivo(true);
        List<Newsletter> newsletterUsuarios = usuarioService.buscarNewsletter();

        newsletters.addAll(newsletterUsuarios);

        return newsletters.stream().map((n) -> n.getEmail()).distinct().collect(Collectors.toList());
    }
    
    public boolean boletimTemplate01Valido(BoletimTemplate01DTO boletim) {
    	if (boletim.getTitulo() != null && boletim.getTextoIntroducao() != null) {
    		return true;
    	}	
    	return false;
    }

    public void salvarEnviarBoletimTemplate01(BoletimTemplate01DTO boletim) throws Exception {
       Long idBoletim = salvarTemplate01(boletim);
       enviarTemplate01(boletim, idBoletim);
    }
    
    public void salvarEnviarTesteBoletimTemplate01(BoletimTemplate01DTO boletimTemplate01, String emailTeste) throws Exception {

    	enviarTesteTemplateHtml01(boletimTemplate01, (Long)null, emailTeste);
     }
    
    public StringBuffer construirEmailBoletimTemplate01(BoletimTemplate01DTO boletim) {
    	StringBuffer msg = new StringBuffer ();
    	String urlPCS = profileUtil.getProperty("profile.api");
    	msg.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\" \"http://www.w3.org/TR/REC-html40/loose.dtd\">"
    			+ "	<html>"
    			+ "		<head>"
    			+ 			"<link rel=\"preconnect\" href=\"https://fonts.gstatic.com\">"
    			+ 			"<style>"
    			+ 				"@font-face{font-family:'Roboto';font-style:normal;font-weight:300;font-display:swap;src:url('https://fonts.gstatic.com/s/roboto/v27/KFOlCnqEu92Fr1MmSU5vAw.ttf') format('truetype')}@font-face{font-family:'Roboto Slab';font-style:normal;font-weight:300;font-display:swap;src:url('https://fonts.gstatic.com/s/robotoslab/v13/BngbUXZYTXPIvIBgJJSb6s3BzlRRfKOFbvjo0oSWaA.ttf') format('truetype')}"
    			+ 				"body{margin:0;padding:0;min-width:100% !important;font-family:'Helvetica',sans-serif}"
    			+ 				"@media only screen and (min-device-width: 901px){.content{width:900px !important}}"
    			+ 			"</style>"
    			+ 		"</head>"
    			+ 		"<body yahoo style=\"min-width: 100% !important; font-family: 'Roboto', sans-serif; margin: 0; padding: 0;\">"
    			+ 		"<table class=\"content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width: min-content; max-width: 900px; margin-top: 32px;\">"
    			+ 			"<tr>"
    			+ 				"<td>"
    			+ 					"<table align=\"center\" class=\"h1\" style=\"font-size: 24px; font-weight: bold; color: #00C300; font-family: sans-serif; padding: 0 0 32px;\">"
    			+ 						"<tr>"
    			+ 							"<td>"+ boletim.getTitulo() +"</td>"
    					+ 				"</tr>"
    					+ 			"</table>"
    					+ 		"</td>"
    					+ "	</tr>"
    					+ "	<tr>"
    					+ 		"<td>"
    					+ 			"<table width=\"850\" class=\"text\" style=\"font-size: 14px; color: black; line-height: 21px; font-family: Roboto Slab, serif; padding: 0 0 24px;\">"
    					+ 				"<tr>"
    					+ 					"<td> " + boletim.getTextoIntroducao() + " </td>"
    							+ 		"</tr>"
    							+ 	"</table>"
    						+  "</td>"
    					+ "</tr>"
    					+ "<tr>"
    					+ 		"<td class=\"header\">"
    					+ 			"<table width=\"425\" align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">"
    					+ 				"<tr>"
						+ 					"<td height=\"425px\">");
										    	if(boletim.getImagemPrimeiroBanner() != null) {
										    		
										    		msg.append("<div width=\"425\" height=\"100%\" alt=\"\" border=\"0\" style=\"color: #fff; background: url('"+urlPCS +"/newsletter/imagem/"+boletim.getImagemPrimeiroBanner().getId()+"') no-repeat center center / cover; padding: 0px 0; min-height: 425px;\" aling=\"left\">");
										    	}
    	msg.append(							"</td>"
    			+ 						"</tr>"
    			+ 					"</table>"
    			+ 					"<table class=\"col425\" width=\"425\" align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"max-width: 425px; width: 425 !important; height: 425px;\" bgcolor=\"#e3dbd2\">"
    			+ 						"<tr>"
    			+ 							"<td height=\"425px\">"
    			+ 								"<table height=\"425px\" style=\"padding: 50px;\" width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">"
    			+ 									"<tr>"
    			+ 										"<td>"
    			+ 											"<p class=\"h2\" style=\"font-size: 18px; font-weight: bold; margin-bottom: -16px; text-transform: uppercase; color: black; font-family: sans-serif;\"> "+boletim.getTituloPrimeiroBanner()+"</p>"
    					+ 									"<p class=\"text\" style=\"margin-top: 32; font-size: 14px; color: black; line-height: 21px; font-family: Roboto Slab, serif;\"> "+boletim.getTextoPrimeiroBanner() +"</p>"
    							+ 							"<p style=\"margin-top: 32; text-decoration: none; display: block; -webkit-border-radius: 12px; font-size: 12px; color: black; text-transform: uppercase; padding: 16px; border: 2px solid black;\" class=\"botao\" align=\"center\">"); 		
    	msg.append(												" <a style=\"text-decoration: none; color: black;\" href='"+boletim.getTextoBotao01()+"'>CONFIRA</a>");		 
    	msg.append(											"</p>"
    			+ 										"</td>"
    			+ 									"</tr>"
    			+ 								"</table>"
    			+ 							"</td>"
    			+ 						"</tr>"
    			+ 					"</table>"
    			+ 				"</td>"
    			+ 		"</tr>"
    			+ 		"<tr>"
    			+ 			"<td class=\"header\">"
    			+ 				"<table width=\"405\" style=\"margin-top: 64px; margin-right: 20px;\" align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">"
    			+ 					"<tr>"
    			+ 						"<td height=\"300px\" class=\"caixa-chamada\" style=\"border-top-width: 4px; border-top-color: #de7921; border-top-style: solid; border-bottom-width: 4px; border-bottom-color: #de7921; border-bottom-style: solid; padding: 20px;\">"
    			+ 							"<p class=\"subtitulo\" width=\"425\" height=\"425px\" style=\"font-size: 14px; color: black; line-height: 21px; text-transform: uppercase; font-weight: bold;\"> "+boletim.getTituloChamada01()+"</p>"
    			+ 							"<p class=\"h2\" width=\"425\" height=\"425px\" style=\"font-size: 18px; font-weight: bold; text-transform: uppercase; color: black; font-family: sans-serif;\"> "+boletim.getSubtituloChamada01()+"</p>"
    			+ 							"<p class=\"text\" width=\"425\" height=\"425px\" style=\"font-size: 14px; line-height: 21px; color: black; font-family: Roboto Slab, serif;\"> "+boletim.getTextoChamada01()+"</p>"
    							+ 		"</td>"
    							+ 	"</tr>"
    						+ 	"</table>"
    						+ 	"<table width=\"405\" style=\"margin-top: 64px; margin-left: 20px;\" align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">"
    						+ 		"<tr>"
    						+ 			"<td height=\"300\" class=\"caixa-chamada\" style=\"border-top-width: 4px; border-top-color: #de7921; border-top-style: solid; border-bottom-width: 4px; border-bottom-color: #de7921; border-bottom-style: solid; padding: 20px;\">"
    						+ 				"<p class=\"subtitulo\" width=\"425\" height=\"425px\" style=\"font-size: 14px; line-height: 21px; text-transform: uppercase; color: black; font-weight: bold;\"> "+boletim.getTituloChamada02()+"</p>"
    								+ 		"<p class=\"h2\" width=\"425px\" height=\"425\" style=\"font-size: 18px; font-weight: bold; text-transform: uppercase; color: black; font-family: sans-serif;\"> "+boletim.getSubtituloChamada02()+"</p>"
    								+ 		"<p class=\"text\" width=\"425px\" height=\"425\" style=\"font-size: 14px; color: black; line-height: 21px; font-family: Roboto Slab, serif;\"> "+boletim.getTextoChamada02()+"</p>"
    						+ 			"</td>"
    						+ 		"</tr>"
    						+ 	"</table>"
    			+ 			"</td>"
    			+ 		"</tr>"
    			+ 		"<tr>"
    			+ 			"<td class=\"header\">"
    			+ 				"<table style=\"margin-top: 64px; margin-right: 20px;\" width=\"850px\" align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">"
    			+ 					"<tr>"
    			+ 						"<td style=\"padding: 0 10px 0 0;\" height=\"425px\">");
									    	if (boletim.getImagemPrincipal() != null) {    		
									    		msg.append("<div class=\"imagem-principal\" width=\"850\" height=\"500px\" border=\"0\" alt=\"\" style=\"color: #fff; background: url('"+urlPCS +"/newsletter/imagem/"+boletim.getImagemPrincipal().getId()+"') no-repeat center center / cover; padding: 150px 0;\" align=\"center\">");
									    		System.out.println(boletim.getImagemPrincipal().getId());
									    		System.out.println("");
									    	}
									    		msg.append("<div class=\"texto-imagem-principal\" style=\"font-size: 18px; font-weight: bold; text-transform: uppercase; color: white;\"> "+boletim.getTituloImagemPrincipal()+"</div></div><div style=\"padding: 15px;\"> "+boletim.getLegendaImagemPrincipal()+"</div></td></tr></table></td></tr><tr><td class=\"header\"><table width=\"405\" style=\"margin-top: 64px; margin-right: 20px;\" align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\"><tr><td height=\"300px\" class=\"caixa-chamada\" style=\"border-top-width: 4px; border-top-color: #de7921; border-top-style: solid; border-bottom-width: 4px; border-bottom-color: #de7921; border-bottom-style: solid; padding: 20px;\"><p class=\"subtitulo\" width=\"425px\" height=\"425px\" style=\"font-size: 14px; color: black; line-height: 21px; text-transform: uppercase; font-weight: bold;\"> "+boletim.getTituloChamada03()+"</p><p class=\"h2\" width=\"425px\" height=\"425px\" style=\"font-size: 18px; font-weight: bold; text-transform: uppercase; color: black; font-family: sans-serif;\"> "+boletim.getSubtituloChamada03()+"</p><p class=\"text\" width=\"425px\" height=\"425px\" style=\"font-size: 14px; color: black; line-height: 21px; font-family: Roboto Slab, serif;\"> "+boletim.getTextoChamada03()+"</p></td></tr></table><table width=\"405\" style=\"margin-top: 64px; margin-left: 20px;\" align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\"><tr><td height=\"300px\" class=\"caixa-chamada\" style=\"border-top-width: 4px; border-top-color: #de7921; border-top-style: solid; border-bottom-width: 4px; border-bottom-color: #de7921; border-bottom-style: solid; padding: 20px;\"><p class=\"subtitulo\" width=\"425\" height=\"425px\" style=\"font-size: 14px; color: black; line-height: 21px; text-transform: uppercase; font-weight: bold;\"> "+boletim.getTituloChamada04()+"</p><p class=\"h2\" width=\"425px\" height=\"425px\" style=\"font-size: 18px; color: black; font-weight: bold; text-transform: uppercase; color: #153643; font-family: sans-serif;\"> "+boletim.getSubtituloChamada04()+"</p><p class=\"text\" width=\"425px\" height=\"425px\" style=\"font-size: 14px; line-height: 21px; color: black; font-family: Roboto Slab, serif;\"> "+boletim.getTextoChamada04()+"</p></td></tr></table></td></tr><tr><td class=\"header\" style=\"padding-top: 32px;\"><table width=\"425\" align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\"><tr><td height=\"425px\">");
									    	if(boletim.getImagemSegundoBanner() != null) {    		
									    		msg.append("<div width=\"425\" height=\"100%\" alt=\"\" border=\"0\" style=\"color: #fff; background: url('"+urlPCS +"/newsletter/imagem/"+boletim.getImagemSegundoBanner().getId()+"') no-repeat center center / cover; padding: 0px 0; min-height: 425px;\" aling=\"left\">");
									    	}
							msg.append( "</td>"
				+ 					"</tr>"
				+ 				"</table>"
				+ 				"<table class=\"col425\" align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"max-width: 425px; width: 100% !important; height: 425px;\" bgcolor=\"#e3dbd2\">"
				+ 					"<tr>"
				+ 						"<td height=\"425px\">"
				+ 							"<table height=\"425px\" style=\"padding: 50px;\" width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">"
				+ 								"<tr>"
				+ 									"<td>"
				+ 										"<p class=\"h2\" style=\"font-size: 18px; font-weight: bold; text-transform: uppercase; color: black; font-family: sans-serif;\"> "+boletim.getTituloSegundoBanner()+"</p>"
				+ 											"<p class=\"text\" style=\"margin-top: 32; font-size: 14px; color: black; line-height: 21px; font-family: Roboto Slab, serif;\"> "+boletim.getTextoSegundoBanner()+"</p>"
				+ 											"<p style=\"margin-top: 32; text-decoration: none; display: block; -webkit-border-radius: 12px; color: black; text-transform: uppercase; padding: 16px; font-size: 12px; border: 2px solid black;\" class=\"botao\" align=\"center\"> <a style=\"text-decoration: none; color: black;\" href='"+boletim.getTextoBotao02()+"'>CONFIRA</a></p>"
				+ 									"</td>"
				+ 								"</tr>"
				+ 							"</table>"
				+ 						"</td>"
				+ 					"</tr>"
				+ 				"</table>"
				+ 			"</td>"
				+ 		"</tr>"
				+ 		"<tr>"
				+ 			"<td>"
				+ 				"<table width=\"850\" align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"footer text\" style=\"font-size: 14px; line-height: 21px; font-family: Roboto Slab, serif; margin-top: 32px; color: white; padding: 30px;\" bgcolor=\"#408559\">"
				+ 					"<tr>"
				+ 						"<td> "+boletim.getTextoFinal()+"</td>"
				+ 					"</tr>"
				+ 				"</table>"
				+ 			"</td>"
				+ 		"</tr>"
				+ "</table>"
				+ "<br>"
			+ "</body>"
		+ "</html>");
    	
    	return msg;
    }
//        public StringBuffer construirEmailPersonalizadoBoletimNewsLetter(BoletimInformativoDTO boletim) {
//		StringBuffer msg = new StringBuffer ();
//		
//		msg.append("<html> <body style='font-family: sans-serif'>");
//        
//			msg.append("<div style='margin-left: 7%; margin-right: 7%; word-break: break-word'>");
//				msg.append("<br>");	
//				if(boletim.getTitulo() != null) {
//					msg.append("<h2 style='text-align: center'>" + boletim.getTitulo() + "</h2>");
//					msg.append("<br>");
//				}
//	        	msg.append("<h4>Olá,</h4>");
//	        	msg.append("<h4>Na nossa newsletter trataremos dos principais temas dessa semana. Confira! </h4>");
//       	        	
//	        	
//	        		msg.append("<div style='width: 90%; margin-top: 50px'>");
//			        	boletim.getNoticiasInformacoesLivres().forEach(informacaoLivre -> {
//			        		if(informacaoLivre.getLinkNoticia() != null && informacaoLivre.getTituloNoticia() != null) {
//				        		msg.append("<h4 style='margin-bottom: 1px'>" + informacaoLivre.getTituloNoticia() + "</h4>");
//				        		msg.append("<a href='" +  informacaoLivre.getLinkNoticia() + "'><h5 style='margin-top: 0px'>" + informacaoLivre.getLinkNoticia() +"</h5></a>");
//			        		}
//			        		else {
//			        			msg.append("<h4 style='margin-bottom: 1px'>" + informacaoLivre.getTituloInformacaoLivre() + "</h4>");
//				        		msg.append("<a href='" +  informacaoLivre.getLinkInformacaoLivre() + "'><h5 style='margin-top: 0px'>" + informacaoLivre.getLinkInformacaoLivre() +"</h5></a>");
//			        		}
//			        	});
//	        		msg.append("</div>");      	
//	        	
//			msg.append("</div>");
//    
//        msg.append("<br>");
//
//        msg.append("</body></html>");
//		
//		return msg;
//		
//    }
    
}
