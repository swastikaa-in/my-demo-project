package com.demo.main.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "hmac")
@ConditionalOnProperty(
		  name = "hmac.enabled", 
		  havingValue = "true")
public class HMACConfig {

	private String docusignSignatureHeader;

	private String docusignSecret;
	

	@Override
	public String toString() {
		return "HMACConfig [docusignSignatureHeader=" + docusignSignatureHeader + ", docusignSecret=" + docusignSecret
				+ ", filterPattern=" + filterPattern + "]";
	}

	public String getDocusignSignatureHeader() {
		return docusignSignatureHeader;
	}

	public void setDocusignSignatureHeader(String docusignSignatureHeader) {
		this.docusignSignatureHeader = docusignSignatureHeader;
	}

	public String getDocusignSecret() {
		return docusignSecret;
	}

	public void setDocusignSecret(String docusignSecret) {
		this.docusignSecret = docusignSecret;
	}

	public String getFilterPattern() {
		return filterPattern;
	}

	public void setFilterPattern(String filterPattern) {
		this.filterPattern = filterPattern;
	}

	private String filterPattern;
}
