package com.niil.nogor.krishi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.convert.Jsr310Converters;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@EnableScheduling
@SpringBootApplication
@EnableCaching
@EnableGlobalMethodSecurity(prePostEnabled=true)
@EntityScan(basePackageClasses={NogorKrishiApplication.class, Jsr310Converters.class})
public class NogorKrishiApplication {

	public static void main(String[] args) {
		SpringApplication.run(NogorKrishiApplication.class, args);
	}
}
