package br.org.cidadessustentaveis.recaptcha;

public class InvalidRecaptchaException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public InvalidRecaptchaException(String msg) {
        super(msg);
    }

    public InvalidRecaptchaException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
