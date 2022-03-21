package br.org.cidadessustentaveis.recaptcha;

public class RecaptchaRequest {

    private String secret;

    private String response;

    private String remoteIp;

    public RecaptchaRequest(String secret, String response) {
        this.secret = secret;
        this.response = response;
    }

    public RecaptchaRequest(String secret, String response, String remoteIp) {
        this.secret = secret;
        this.response = response;
        this.remoteIp = remoteIp;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getRemoteIp() {
        return remoteIp;
    }

    public void setRemoteIp(String remoteIp) {
        this.remoteIp = remoteIp;
    }


}
