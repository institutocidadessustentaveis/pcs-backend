package br.org.cidadessustentaveis.config.auth.exceptions;

import org.springframework.security.core.AuthenticationException;

public class UserAlreadyLoggedException extends AuthenticationException {

    private static final long serialVersionUID = -3340274122475543422L;

    public UserAlreadyLoggedException(String msg) {
        super(msg);
    }

}
