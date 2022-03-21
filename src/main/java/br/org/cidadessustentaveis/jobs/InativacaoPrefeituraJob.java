package br.org.cidadessustentaveis.jobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import br.org.cidadessustentaveis.config.SpringContext;
import br.org.cidadessustentaveis.util.ProfileUtil;
import br.org.cidadessustentaveis.util.RestClient;

@Component
public class InativacaoPrefeituraJob implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        ApplicationContext appContext = SpringContext.getAppContext();
    	ProfileUtil profileUtil = (ProfileUtil)appContext.getBean("profileUtil");
        String urlAPI = profileUtil.getProperty("profile.api");
    	
        // System.out.println("Iniciei o JOB de Inativacao Prefeitura"+urlAPI);
        RestClient restClient = new RestClient();
        restClient.post(urlAPI+"/prefeitura/inativarPrefeituras", "");
    }
    
}
