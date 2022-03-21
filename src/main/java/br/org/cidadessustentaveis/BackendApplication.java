package br.org.cidadessustentaveis;

import java.util.concurrent.Executor;

import org.quartz.DailyTimeIntervalScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TimeOfDay;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.github.slugify.Slugify;

import br.org.cidadessustentaveis.jobs.EnvioEventosJob;
import br.org.cidadessustentaveis.jobs.InativacaoPrefeituraJob;
import br.org.cidadessustentaveis.services.dev.VersaoBuildService;
import br.org.cidadessustentaveis.util.ProfileUtil;
import redis.embedded.RedisServer;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@SpringBootApplication
@EnableSwagger2
@EnableAsync
@EnableCaching
public class BackendApplication implements CommandLineRunner {

	@Autowired
	ProfileUtil profileUtil;
	@Autowired
	VersaoBuildService versaoBuildService;

	@Override
	public void run(String... args) throws Exception {
		System.out.println("Profile: "+profileUtil.getProperty("spring.profiles.active"));
		System.out.println("Frontend URL: "+profileUtil.getProperty("profile.frontend"));
		System.out.println("API URL: "+profileUtil.getProperty("profile.api"));
		versaoBuildService.gerarVersao(args);

		try {
			RedisServer redisServer = new RedisServer(6379);
			redisServer.start();
		} catch(RuntimeException e) {

		}
		try {
			// Grab the Scheduler instance from the Factory
			Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
			
			scheduler.start();
			JobDetail job = JobBuilder.newJob(EnvioEventosJob.class)
					.withIdentity("emailEvento", "eventos")
					.build();
			TimeOfDay inicio = TimeOfDay.hourAndMinuteOfDay(8, 00);
			TimeOfDay fim = TimeOfDay.hourAndMinuteOfDay(18, 00);
			Trigger trigger = TriggerBuilder.newTrigger()
					.withIdentity("envioEventos", "emails")
					.startNow()
					.withSchedule(
							DailyTimeIntervalScheduleBuilder.dailyTimeIntervalSchedule()
							.startingDailyAt(inicio)
							.endingDailyAt(fim)
							.withIntervalInMinutes(1))
					.build();
			// Tell quartz to schedule the job using our trigger
			JobKey jobKey = new JobKey("emailEvento", "eventos");
			if(scheduler.checkExists(jobKey)) {
				scheduler.deleteJob(jobKey);
			}
			scheduler.scheduleJob(job, trigger );
 
		} catch (SchedulerException se) {
			se.printStackTrace();
		}



		try {
			// Grab the Scheduler instance from the Factory
			Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
			
			scheduler.start();
			JobDetail job = JobBuilder.newJob(InativacaoPrefeituraJob.class)
					.withIdentity("inativacaoPrefeitura", "prefeituras")
					.build();
			TimeOfDay inicio = TimeOfDay.hourAndMinuteOfDay(00, 00);
			TimeOfDay fim = TimeOfDay.hourAndMinuteOfDay(23, 59);
			Trigger trigger = TriggerBuilder.newTrigger()
					.withIdentity("inativacaoPrefeitura", "prefeituras")
					.startNow()
					.withSchedule(
							DailyTimeIntervalScheduleBuilder.dailyTimeIntervalSchedule()
							.startingDailyAt(inicio)
							.endingDailyAt(fim)
							.withIntervalInMinutes(1))
					.build();
			// Tell quartz to schedule the job using our trigger
			JobKey jobKey = new JobKey("inativacaoPrefeitura", "prefeituras");
			if(scheduler.checkExists(jobKey)) {
				scheduler.deleteJob(jobKey);
			}
			scheduler.scheduleJob(job, trigger );
 
		} catch (SchedulerException se) {
			se.printStackTrace();
		}

	}

	@Bean(name = "threadExecutor")
	public Executor asyncExecutor() {
		final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(5);
		executor.setMaxPoolSize(100);
		executor.setWaitForTasksToCompleteOnShutdown(true);
		executor.initialize();
		return executor;
	}

	@Bean(name = "threadRecalculo")
	public Executor threadRecalculo() {
		final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(20);
		executor.setMaxPoolSize(100);
		executor.setWaitForTasksToCompleteOnShutdown(true);
		executor.initialize();
		return executor;
	}

	@Bean
	public CacheManager cacheManager() {
		return new ConcurrentMapCacheManager("linksRodape", "contatoPcs", "linksRedesSociais",
				"versao", "shapes_estados", "cidades", "prefeituras",
				"comboPartido");
	}

	@Bean
	public Slugify slugify() {
		Slugify slg = new Slugify();
		slg.withLowerCase(true);
		return slg;
	}
	
	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}
}
