package com.demo.main.util;



import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RequestWrapper extends HttpServletRequestWrapper {

	private static final Logger log = LoggerFactory.getLogger(RequestWrapper.class);

	public static final String FORM_URLENCODED = "application/x-www-form-urlencoded";
	public static final String MULTIPART_FORMDATA = "multipart/form-data";

	private final String payload;

	public String getPayload() {
		return payload;
	}

	public RequestWrapper(HttpServletRequest request) throws Exception {
		super(request);
		super.getParameterMap();

		String contentType = request.getContentType();

		if (null != contentType
				&& (contentType.equalsIgnoreCase(FORM_URLENCODED) || contentType.indexOf(MULTIPART_FORMDATA) >= 0)) {
			payload = getPayloadAsJSONString(request);
		} else {

			// read the original payload into the payload variable
			payload = getPayloadStr(request);
		}
	}

	public static String getPayloadAsJSONString(HttpServletRequest request) {
		Map<?, ?> paramMap = request.getParameterMap();

		Map<Object, Object> newMap = paramMap.entrySet().stream().collect(
				Collectors.toMap(entry -> entry.getKey(), entry -> getParamValue((String[]) entry.getValue())));

		return getJSONStringFromMap(newMap);
	}

	public static Object getParamValue(String[] value) {
		if (null != value && value.length == 1) {
			return value[0];
		} else {
			return value;
		}

	}

	public static String getJSONStringFromMap(Map<Object, Object> map) {
		ObjectMapper objMap = new ObjectMapper();
		String jsonResponse = null;
		try {
			jsonResponse = objMap.writerWithDefaultPrettyPrinter().writeValueAsString(map);
		} catch (JsonProcessingException e) {
			log.error("Exception converting to JSON:" + e.getMessage());
		}
		return jsonResponse;
	}

	public String getPayloadStr(HttpServletRequest request) throws Exception {
		// read the original payload into the payload variable
		StringBuilder stringBuilder = new StringBuilder();
		BufferedReader bufferedReader = null;
		try {
			// read the payload into the StringBuilder
			InputStream inputStream = request.getInputStream();
			if (inputStream != null) {
				bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
				char[] charBuffer = new char[128];
				int bytesRead = -1;
				while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
					stringBuilder.append(charBuffer, 0, bytesRead);
				}
			} else {
				// make an empty string since there is no payload
				stringBuilder.append("");
			}
		} catch (IOException ex) {
			log.error("Error reading the request payload", ex);
			throw new Exception("Error reading the request payload", ex);
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException iox) {
					// ignore
				}
			}
		}
		return stringBuilder.toString();
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(payload.getBytes());
		ServletInputStream inputStream = new ServletInputStream() {

			@Override
			public int read() throws IOException {
				return byteArrayInputStream.read();
			}

			public boolean isFinished() {
				return false;
			}

			public boolean isReady() {
				return false;
			}

			public void setReadListener(ReadListener listener) {
				throw new RuntimeException("Not implemented");
			}
		};
		return inputStream;
	}
}
