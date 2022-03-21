package br.org.cidadessustentaveis.util;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.TrustStrategy;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;


public class RestClient {

    private RestTemplate rest;
    private HttpHeaders headers;
    private HttpStatus status;

    public RestClient() {
        this.rest = new RestTemplate();
        this.headers = new HttpHeaders();
        headers.add("Content-Type", "*/*");
        headers.add("Accept", "*/*");
        headers.add("Origin", "https://developer.mozilla.org");
    }
    
    public RestClient(Boolean withoutSSL) {
        this.rest = this.restTemplate();
        this.headers = new HttpHeaders();
        headers.add("Content-Type", "*/*");
        headers.add("Accept", "*/*");
        headers.add("Origin", "https://developer.mozilla.org");
    }


    public String get(String uri) {
        HttpEntity<String> requestEntity = new HttpEntity<String>("", headers);
        ResponseEntity<String> responseEntity = rest.exchange(uri, HttpMethod.GET, requestEntity, String.class);
        this.setStatus(responseEntity.getStatusCode());
        return responseEntity.getBody();
    }

    public byte[] getBytes(String uri) {
        HttpEntity<String> requestEntity = new HttpEntity<String>("", headers);
        ResponseEntity<byte[]> responseEntity = rest.exchange(uri, HttpMethod.GET, requestEntity, byte[].class);
        this.setStatus(responseEntity.getStatusCode());
        return responseEntity.getBody();
    }

    public String post(String uri, String json) {
        HttpEntity<String> requestEntity = new HttpEntity<String>(json, headers);
        ResponseEntity<String> responseEntity = rest.exchange(uri, HttpMethod.POST, requestEntity, String.class);
        this.setStatus(responseEntity.getStatusCode());
        return responseEntity.getBody();
    }

    public void addHeader(String name, String value) {
        headers.add(name, value);
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }
    
    public String getJSON(String uri) {
    	RestTemplate restTemplate = new RestTemplate();

    	 HttpHeaders headers = new HttpHeaders();
         headers.add("user-agent", "Mozilla/5.0 Firefox/26.0");
         HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

         ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
         
         return result.getBody();
    }
    
    public RestTemplate restTemplate(){
		TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;
		
		SSLContext sslContext;
		RestTemplate restTemplate = null;
		try {
			sslContext = org.apache.http.ssl.SSLContexts.custom()
			                .loadTrustMaterial(null, acceptingTrustStrategy)
			                .build();
			SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);
			
			CloseableHttpClient httpClient = HttpClients.custom()
			                .setSSLSocketFactory(csf)
			                .build();
			
			HttpComponentsClientHttpRequestFactory requestFactory =
			                new HttpComponentsClientHttpRequestFactory();
			
			requestFactory.setHttpClient(httpClient);
			restTemplate = new RestTemplate(requestFactory);
		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		return restTemplate;
    }
}