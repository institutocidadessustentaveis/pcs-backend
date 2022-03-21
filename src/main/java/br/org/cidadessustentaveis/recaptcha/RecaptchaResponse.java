package br.org.cidadessustentaveis.recaptcha;

import java.util.Date;

public class RecaptchaResponse {

    private boolean success;

    private Date challenge_ts;

    private String hostname;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Date getChallenge_ts() {
        return challenge_ts;
    }

    public void setChallenge_ts(Date challenge_ts) {
        this.challenge_ts = challenge_ts;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }


}
