package com.demo.main.filter;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.google.gson.Gson;
import com.demo.main.util.RequestWrapper;
import com.demo.main.util.HMAC;
import org.springframework.beans.factory.annotation.Value;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 5)
public class RequestLoggingFilter extends OncePerRequestFilter {

	private static final Logger LOGGER = LoggerFactory.getLogger(RequestLoggingFilter.class);


	@Override
	public void destroy() {

	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		RequestWrapper requestWrapper;
		try {
			requestWrapper = new RequestWrapper(request);
			Gson gson = new Gson();

			LOGGER.info("Header Map");
                        Map<String,String> headerMap=new HashMap<>();
                        headerMap=getHeaderMap(requestWrapper);
			LOGGER.info("***HEader Map:" + gson.toJson(headerMap));

			chain.doFilter(requestWrapper, response);

		} catch (Exception e) {
			LOGGER.error("Exception:" + e);
		}
	}

	private Map<String, String> getHeaderMap(RequestWrapper request) {
		Map<String, String> map = new HashMap<String, String>();
		Enumeration<String> headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String key = (String) headerNames.nextElement();
			String value = request.getHeader(key);
			map.put(key, value);
		}
		return map;
	}

}

