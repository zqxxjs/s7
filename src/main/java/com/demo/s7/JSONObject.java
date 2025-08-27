package com.demo.s7;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONObject {

	private JSONObject() {
	}

	private static final Logger logger = LoggerFactory.getLogger(JSONObject.class);

	public static String toJSONString(Object obj) {
		ObjectMapper om = SpringUtil.tryGetBean(ObjectMapper.class);
		try {
			if (om == null) {
				om = new ObjectMapper();
			}
			return om.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static <T> T parseObject(String json, Class<T> clazz) {
		ObjectMapper om = SpringUtil.tryGetBean(ObjectMapper.class);
		try {
			return om.readValue(json, clazz);
		} catch (JsonProcessingException e) {
			logger.error("parseObject error: {}", e.getMessage());
		}
		return null;
	}

	public static <T> T convertObject(Map<String, Object> map, Class<T> clazz) {
		ObjectMapper om = SpringUtil.tryGetBean(ObjectMapper.class);
		return om.convertValue(map, clazz);
	}

	@SuppressWarnings("unchecked")
	public static <T> Map<String, Object> convertMap(T bean) {
		ObjectMapper om = SpringUtil.tryGetBean(ObjectMapper.class);
		return om.convertValue(bean, Map.class);
	}
}
