package com.demo.s7;

import java.util.Collection;
import java.util.regex.Pattern;

import org.apache.struts2.json.JSONException;
import org.apache.struts2.json.JSONWriter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonWriter implements JSONWriter{

	@Override
	public String write(Object object) throws JSONException {
		try {
			ObjectMapper om = SpringUtil.tryGetBean(ObjectMapper.class);
			return om.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String write(Object object, Collection<Pattern> excludeProperties, Collection<Pattern> includeProperties,
			boolean excludeNullProperties) throws JSONException {
		try {
			ObjectMapper om = SpringUtil.tryGetBean(ObjectMapper.class);
			return om.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void setIgnoreHierarchy(boolean ignoreHierarchy) {
		// 交给jackson ，无需处理任何事
	}

	@Override
	public void setEnumAsBean(boolean enumAsBean) {
		// 交给jackson ，无需处理任何事
	}

	@Override
	public void setDateFormatter(String defaultDateFormat) {
		// 交给jackson ，无需处理任何事
	}

	@Override
	public void setCacheBeanInfo(boolean cacheBeanInfo) {
		// 交给jackson ，无需处理任何事
	}

	@Override
	public void setExcludeProxyProperties(boolean excludeProxyProperties) {
		// 交给jackson ，无需处理任何事
	}

}
