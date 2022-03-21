package br.org.cidadessustentaveis.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import br.org.cidadessustentaveis.util.ProfileUtil;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.paths.RelativePathProvider;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
@ComponentScan
public class SwaggerConfig { 
	
	@Autowired
	ProfileUtil profileUtil;
	
    @Bean
    public Docket api() { 
    	final String host = profileUtil.getProperty("profile.swagger");
    	
        return new Docket(DocumentationType.SWAGGER_2)  
          .host(host)
          .select()                                  
          .apis(RequestHandlerSelectors.basePackage("br.org.cidadessustentaveis"))           
          .paths(PathSelectors.any()) 
          .build()
          .apiInfo(apiEndPointsInfo());
    }
    
	private ApiInfo apiEndPointsInfo() {
        return new ApiInfoBuilder().title("Plataforma Cidades Sustentáveis")
            .build();
    }
}