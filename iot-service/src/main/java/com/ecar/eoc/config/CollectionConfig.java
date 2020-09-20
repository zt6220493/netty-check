package com.ecar.eoc.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="service.config")
public class CollectionConfig {
	private List<Map<String, String>> gpsCmd = new ArrayList<>();

	public List<Map<String, String>> getGpsCmd() {
		return gpsCmd;
	}
}
