package com.ecar.eoc.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/log")
public class LogRest {
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	
}
