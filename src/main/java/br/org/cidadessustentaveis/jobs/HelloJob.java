package br.org.cidadessustentaveis.jobs;

import java.time.LocalDateTime;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class HelloJob implements Job {

    public HelloJob() {
    }

    public void execute(JobExecutionContext context) throws JobExecutionException {
    	LocalDateTime agora = LocalDateTime.now();
        System.out.println(agora.toString());
    } 
  }