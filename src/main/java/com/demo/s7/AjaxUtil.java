package com.demo.s7;

import java.io.PrintWriter;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletResponse;

public class AjaxUtil {

	private AjaxUtil() {
	}

	public static void writeJSON(String errMsg) {
		if (StringUtils.isNotBlank(errMsg)) {
			HttpServletResponse res = SpringUtil.tryGetResponse();
			if (res != null) {
				try {
					res.setContentType("application/json");
					res.setCharacterEncoding("utf-8");
					PrintWriter w = res.getWriter();
					w.write(errMsg);
					w.flush();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void writeJSON(Object result) {
		if (result != null) {
			if (result instanceof String s) {
				writeJSON(s);
			} else {
				try {
					ObjectMapper mapper = SpringUtil.tryGetBean(ObjectMapper.class);
					String res = mapper.writeValueAsString(result);
					writeJSON(res);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
