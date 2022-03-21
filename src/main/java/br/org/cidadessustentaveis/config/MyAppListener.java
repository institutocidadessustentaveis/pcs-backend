//package br.org.cidadessustentaveis.config;
//
//import org.springframework.context.ApplicationEvent;
//import org.springframework.context.ApplicationListener;
//import org.springframework.context.event.ContextRefreshedEvent;
//import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
//import org.springframework.stereotype.Component;
//import org.springframework.web.context.support.ServletRequestHandledEvent;
//
//@Component
//public class MyAppListener implements ApplicationListener<ApplicationEvent> {
// 
//    @Override
//    public void onApplicationEvent(ApplicationEvent applicationEvent) {
//    	System.out.println(applicationEvent.toString());
//        // process event
//    	if(applicationEvent instanceof AuthenticationSuccessEvent)
//        {
//
////        	System.out.println("AuthenticationSuccessEvent");
//        }
//        if(applicationEvent instanceof ContextRefreshedEvent)
//        {
//            // fire ping to statsd server
//
////        	System.out.println("ContextRefreshedEvent");
//        } 
//        else if (applicationEvent instanceof ServletRequestHandledEvent)
//        {
////        	System.out.println("ServletRequestHandledEvent");
//            // fire ping to statsd server    
//        }
//    }
//}