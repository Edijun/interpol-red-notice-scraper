package id.edijun.interpolrednoticescraper.controller;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LauncherController {

	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	private Job job;

	@RequestMapping("/launch")
	public String launch() throws Exception {
		JobParameters jobParameters = new JobParameters();
		jobLauncher.run(job, jobParameters);
		return "Job " + job.getName() + "is starting...";
	}
}