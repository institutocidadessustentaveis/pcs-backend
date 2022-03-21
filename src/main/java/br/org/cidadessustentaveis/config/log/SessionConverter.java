package br.org.cidadessustentaveis.config.log;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

public class SessionConverter extends ClassicConverter {
    @Override
    public String convert(ILoggingEvent event) {
        RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
        if (attrs != null) {
            return attrs.getSessionId();
        }
        return "NO_SESSION";
    }
}