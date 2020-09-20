package com.ecar.eoc.config.datasource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class DataSourceConfig {
	
	@Primary
    @Bean(name ="eocDSProperties")
    @Qualifier("eocDSProperties")
    @ConfigurationProperties(prefix = "spring.datasource.eocdb")
	public DataSourceProperties eocDSProperties() {
		return new DataSourceProperties();
	}
	
	@Bean(name="simDSProperties")
	@Qualifier("simDSProperties")
	@ConfigurationProperties(prefix="spring.datasource.simdb")
	public DataSourceProperties simDSProperties() {
		return new DataSourceProperties();
	}
	
	@Bean(name="iotDSProperties")
	@Qualifier("iotDSProperties")
	@ConfigurationProperties(prefix="spring.datasource.iotdb")
	public DataSourceProperties iotDSProperties() {
		return new DataSourceProperties();
	}
	
	@Primary
	@Bean(name="eocDS")
	@ConfigurationProperties(prefix="spring.datasource.eocdb")
	public HikariDataSource dataSourceEoc() {
		 return eocDSProperties().initializeDataSourceBuilder().type(HikariDataSource.class).build(); 
	}
	
	@Bean(name="simDS")
	@ConfigurationProperties(prefix="spring.datasource.simdb")
	public HikariDataSource dataSourceSim() {
		 return simDSProperties().initializeDataSourceBuilder().type(HikariDataSource.class).build(); 
	}
	
	@Bean(name="iotDS")
	@ConfigurationProperties(prefix="spring.datasource.iotdb")
	public HikariDataSource dataSourceIot() {
		 return iotDSProperties().initializeDataSourceBuilder().type(HikariDataSource.class).build(); 
	}
}
