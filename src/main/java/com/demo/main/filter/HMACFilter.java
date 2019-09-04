package com.demo.main.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;

import com.demo.main.util.HMAC;
import com.demo.main.util.RequestWrapper;

@Order(Ordered.HIGHEST_PRECEDENCE + 6)
public class HMACFilter extends OncePerRequestFilter {

	private static final Logger LOGGER = LoggerFactory.getLogger(HMACFilter.class);

	private String docusignSignatureHeader;

	private String docusignSecret;

	@Override
	public void destroy() {

	}

	public HMACFilter(String docusignSignatureHeader, String docusignSecret) {
		this.docusignSecret = docusignSecret;
		this.docusignSignatureHeader = docusignSignatureHeader;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		RequestWrapper requestWrapper;
		try {
			requestWrapper = new RequestWrapper(request);

			String payload = requestWrapper.getPayload();
			String docusignSignatureValue = requestWrapper.getHeader(docusignSignatureHeader);
			if (null != docusignSignatureValue) {
				boolean result = HMAC.isHashValid(docusignSecret, payload, docusignSignatureValue);
				if (!result) {
					LOGGER.error("Request is NOT  from Docusign");
					throw new Exception("Request is NOT  from Docusign");
				} else {
					LOGGER.info("Request is VALID from Docusign");
					chain.doFilter(requestWrapper, response);
				}
			} else {
				LOGGER.error("Docusign Signature Header not found in request");
				throw new Exception("Docusign Signature Header not found in request");
			}
		} catch (Exception e) {
			LOGGER.error("Exception:" + e);
		}
	}

}
