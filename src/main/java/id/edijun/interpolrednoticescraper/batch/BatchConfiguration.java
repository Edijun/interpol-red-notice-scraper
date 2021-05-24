package id.edijun.interpolrednoticescraper.batch;

import java.util.Arrays;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.integration.async.AsyncItemProcessor;
import org.springframework.batch.integration.async.AsyncItemWriter;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import id.edijun.interpolrednoticescraper.batch.processor.NoticeItemProcessor;
import id.edijun.interpolrednoticescraper.batch.reader.EntityItemReader;
import id.edijun.interpolrednoticescraper.batch.writer.ArrestWarrantItemWriter;
import id.edijun.interpolrednoticescraper.batch.writer.ImageItemWriter;
import id.edijun.interpolrednoticescraper.batch.writer.LinkItemWriter;
import id.edijun.interpolrednoticescraper.batch.writer.NoticeItemWriter;
import id.edijun.interpolrednoticescraper.model.Link;
import id.edijun.interpolrednoticescraper.model.Notice;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableBatchProcessing
@EnableScheduling
public class BatchConfiguration extends DefaultBatchConfigurer {
	
	@Value("${async.thread.max.pool}")
    private Integer maxPoolSize;

    @Value("${async.thread.core.pool}")
    private Integer corePoolSize;

    @Value("${async.thread.queue}")
    private Integer queueSize;

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Autowired
	public DataSource dataSource;
	
	@Autowired
	private JobLauncher jobLauncher;
	
	@Autowired
	private Job job;

	@Bean
	public ItemReader<Link> itemReader() {
		return new EntityItemReader(dataSource);
	}

	@Bean
	public ItemProcessor<Link, Notice> noticeProcessor() {
		log.info("Initializing processor...");
		return new NoticeItemProcessor(dataSource);
	}
	
	@Bean
	public ItemProcessor<Link, Future<Notice>> asyncItemProcessor() {
		AsyncItemProcessor<Link, Notice> asyncItemProcessor = new AsyncItemProcessor<>();
		asyncItemProcessor.setDelegate(noticeProcessor());
		asyncItemProcessor.setTaskExecutor(getAsyncExecutor());
		return asyncItemProcessor;
	}

	@Bean
	public ItemWriter<Notice> noticeItemWriter() {
		log.info("Initializing notice item writer...");
		return new NoticeItemWriter(dataSource);
	}

	@Bean
	public ItemWriter<Notice> arrestWarrantItemWriter() {
		log.info("Initializing arrest warrant item writer...");
		return new ArrestWarrantItemWriter(dataSource);
	}

	@Bean
	public ItemWriter<Notice> imageItemWriter() {
		log.info("Initializing image item writer...");
		return new ImageItemWriter(dataSource);
	}

	@Bean
	public ItemWriter<Notice> linkItemWriter() {
		log.info("Initializing link item writer...");
		return new LinkItemWriter(dataSource);
	}
	
	@Bean
	public ItemWriter<Notice> itemWriter() {
		CompositeItemWriter<Notice> compositeItemWriter = new CompositeItemWriter<>();
		compositeItemWriter
			.setDelegates(Arrays.asList(
				noticeItemWriter(), 
				arrestWarrantItemWriter(), 
				imageItemWriter(),
				linkItemWriter()));
		return compositeItemWriter;
	}
	
	@Bean
    public ItemWriter<Future<Notice>> asyncItemWriter(){
        AsyncItemWriter<Notice> asyncItemWriter = new AsyncItemWriter<>();
        asyncItemWriter.setDelegate(itemWriter());
        return asyncItemWriter;
    }
	
	@Bean(name = "asyncExecutor")
	public TaskExecutor getAsyncExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(corePoolSize);
		executor.setMaxPoolSize(corePoolSize);
		executor.setQueueCapacity(queueSize);
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		executor.setThreadNamePrefix("AsyncExecutor-");
		return executor;
	}

	@Bean
	public Step noticeStep() throws Exception {
		log.info("Initializing step...");
		return this.stepBuilderFactory.get("noticeStep")
				.<Link, Future<Notice>>chunk(corePoolSize)
				.reader(itemReader())
				.processor(asyncItemProcessor())
				.writer(asyncItemWriter())
				.build();
	}

	@Bean
	public Job noticeJob(JobCompletionNotificationListener listener) throws Exception {
		log.info("Initializing job...");
		return this.jobBuilderFactory.get("noticeJob")
				.incrementer(new RunIdIncrementer())
				.listener(listener)
				.start(noticeStep())
				.build();
	}
	
	@Override
	public void setDataSource(DataSource dataSource) {
		// override to do not set datasource even if a datasource exist.
		// initialize will use a Map based JobRepository (instead of database)
	}
	
	@Scheduled(fixedDelay = 30000)
	public void schedule() throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {

		JobParameters jobParameters = new JobParameters();
		jobLauncher.run(job, jobParameters);
	}

}
