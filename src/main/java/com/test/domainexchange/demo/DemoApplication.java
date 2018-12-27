package com.test.domainexchange.demo;

import com.test.domainexchange.demo.config.ProcessMsgThread;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class DemoApplication {
	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(DemoApplication.class);
		ConfigurableApplicationContext context = app.run(args);
		ProcessMsgThread processMsgThread = (ProcessMsgThread)context.getBean("processMsgThread");
		processMsgThread.start();
		//SpringApplication.run(DemoApplication.class, args);
	}
}

