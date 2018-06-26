package it.tim.dashboard.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;



@ConfigurationProperties(prefix = "application.config")
@Getter
@Setter
@ToString
@Component
public class ApplicationConfiguration {

	private String promotionsConfigFile;
	private String profilesConfigFile;
	
	
}
