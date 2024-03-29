package com.niil.nogor.krishi.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.niil.nogor.krishi.entity.Settings;
import com.niil.nogor.krishi.repo.SettingsRepo;

import krishi.gov.api.KrishiAPI;
import krishi.gov.api.KrishiAPIv2;

/**
 * @author Noor
 * @email niildu@gmail.com
 * @since May 31, 2018
 *
 */
@Configuration
public class AppConfig {
	private final Logger log = LoggerFactory.getLogger(getClass());
	public static boolean reloadSettings = false;
	private Settings siteSettings;

	@Value("${pages.image.path:/var/nogorkrishi/images}")
	private String imagePathStr;

	@Value("${pages.file.path:/var/nogorkrishi/files}")
	private String filePathStr;

	@Autowired SettingsRepo settingsRepo;

	@Bean("imagePath")
	Path imagePath() {
		return getPath(Paths.get(imagePathStr));
	}

	@Bean("filePath")
	Path filePath() {
		return getPath(Paths.get(filePathStr));
	}

	@Scope(value="request", proxyMode=ScopedProxyMode.TARGET_CLASS)
	@Bean("siteSettings")
	Settings siteSettings() {
		if (reloadSettings || siteSettings == null) {
			siteSettings = settingsRepo.findAll().stream().findFirst().orElse(new Settings());
			reloadSettings = false;
		}
		return siteSettings;
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	KrishiAPI krishiAPI(@Value("${krishi.api.url:http://api.krishi.gov.bd/api}") String url) {
		return KrishiAPI.connect(url);
	}
	
	@Bean
	KrishiAPIv2 krishiAPIv2(@Value("${krishi.api.url:http://api.krishi.gov.bd/api/external}") String url) {
		return KrishiAPIv2.connect(url);
	}

	private Path getPath(Path path) {
		try {
			Files.createDirectories(path);
		} catch (IOException e) {
			log.error("Failed to create path directories of path {}", path.toString());
		}
		return path;
	}
}
