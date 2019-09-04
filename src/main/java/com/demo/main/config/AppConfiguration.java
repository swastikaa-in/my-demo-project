package com.demo.main.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.demo.main.filter.HMACFilter;

@Configuration
@ConditionalOnProperty(
		  name = "hmac.enabled", 
		  havingValue = "true")
public class AppConfiguration {

	@Bean
	public HMACConfig hmacConfig() {
		return new HMACConfig();
	}

	@Bean
	public FilterRegistrationBean<HMACFilter> hmacFilter() {
		FilterRegistrationBean<HMACFilter> registrationBean = new FilterRegistrationBean<>();

		registrationBean
				.setFilter(new HMACFilter(hmacConfig().getDocusignSignatureHeader(), hmacConfig().getDocusignSecret()));
		registrationBean.addUrlPatterns(hmacConfig().getFilterPattern());
		return registrationBean;
	}
}
