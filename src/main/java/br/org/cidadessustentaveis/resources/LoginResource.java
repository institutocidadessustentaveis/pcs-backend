package br.org.cidadessustentaveis.resources;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@RestController
@RequestMapping("/api/v1")
public class LoginResource {

    @PostMapping("/login")
    public ResponseEntity<OAuth2AccessToken> login(@RequestParam("grant_type") String grantType,
                                                   @RequestParam("username") String username,
                                                   @RequestParam("password") String password,
                                                   @RequestHeader("Authorization") String auth) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.set("Authorization", auth);

        HttpEntity<OAuth2AccessToken> requestEntity = new HttpEntity<>(requestHeaders);

        UriComponentsBuilder uriBuilder =
                                UriComponentsBuilder.fromHttpUrl("http://localhost:8080/oauth/token")
                                                                            .queryParam("grant_type", grantType)
                                                                            .queryParam("username", username)
                                                                            .queryParam("password", password);

        return restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.POST, requestEntity,
                                        new ParameterizedTypeReference<OAuth2AccessToken>(){});
    }

}
