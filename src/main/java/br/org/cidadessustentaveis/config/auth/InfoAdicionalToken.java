package br.org.cidadessustentaveis.config.auth;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import br.org.cidadessustentaveis.model.administracao.Credencial;

@Component
public class InfoAdicionalToken implements TokenEnhancer{
	
	@Autowired
	private DetailsService detailsService;

	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
		
		Credencial credencial = detailsService.findByLogin(authentication.getName());
		
		Map<String, Object> info = new HashMap<>();
		info.put("isOnline: ", credencial.getSnOnline());
		info.put("login: ", credencial.getLogin());
		info.put("nome", credencial.getUsuario().getNome());
		info.put("id", credencial.getUsuario().getId());
		
		((DefaultOAuth2AccessToken)accessToken).setAdditionalInformation(info);
		return accessToken;
	}

}
