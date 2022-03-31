package com.manelon.config;

import com.manelon.config.ConfigApplication.ManelonProperties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import lombok.Data;
import lombok.extern.log4j.Log4j2;

@Log4j2
@EnableConfigurationProperties(ManelonProperties.class)
@SpringBootApplication
public class ConfigApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConfigApplication.class, args);
	}

	@Bean 
	ApplicationRunner applicationRunner(Environment environment,
										ManelonProperties manelon,
										@Value("${HOME}") String userHome, 
										@Value("${greetings-message:Default Hello }") String defaultValue) {
		return args -> {
			// TODO Auto-generated method stub
			log.info("message from application.properties " + environment.getProperty("message-from-application-properties"));
			log.info("default value from application.properties " + defaultValue);
			log.info("user home from environment variables" + userHome);
			log.info("message from @ConfigurationProperties message " + manelon.getMessage());
			log.info("message from @ConfigurationProperties fromEnv " + manelon.getFromEnv());
			
		};

	}

	@Data
	@ConfigurationProperties("manelon")
	class ManelonProperties {
		private String message;
		private String fromEnv;
	}

}
