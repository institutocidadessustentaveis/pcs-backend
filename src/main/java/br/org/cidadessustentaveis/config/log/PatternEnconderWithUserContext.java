package br.org.cidadessustentaveis.config.log;

import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.pattern.PatternLayoutEncoderBase;

public class PatternEnconderWithUserContext extends PatternLayoutEncoderBase<ILoggingEvent> {
    @Override
    public void start() {
        PatternLayout patternLayout = new PatternLayoutWithUserContext();
        patternLayout.setContext(context);
        patternLayout.setPattern(getPattern());
        patternLayout.setOutputPatternAsHeader(outputPatternAsHeader);
        patternLayout.start();
        this.layout = patternLayout;
        super.start();
    }

}
