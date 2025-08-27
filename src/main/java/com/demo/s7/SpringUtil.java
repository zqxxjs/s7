package com.demo.s7;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.util.ResourceUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.support.WebApplicationContextUtils;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ognl.OgnlRuntime;

public class SpringUtil implements ServletContextListener {

	private static final Map<String, Object> ctx = new ConcurrentHashMap<>();
	private static final String SPRING_CONTEXT = "SPRING_CONTEXT";

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		OgnlRuntime.setSecurityManager(null);
		// 1. 获取ServletContext（Web应用全局上下文）
		ServletContext servletContext = sce.getServletContext();

		// 2. 通过Spring提供的工具类获取ApplicationContext
		WebApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		ctx.put(SPRING_CONTEXT, applicationContext);
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		ctx.clear();
	}

	public ApplicationContext getContext() {
		return ctx.get(SPRING_CONTEXT) instanceof ApplicationContext c ? c : null;
	}

	public static String getClassPath() {
		return ResourceUtils.CLASSPATH_URL_PREFIX;
	}

	public static <T> T tryGetBean(Class<T> clazz) {
		return getBean(clazz);
	}

	public static <T> Map<String, T> tryGetBeanMap(Class<T> clazz) {
		return getBeanMap(clazz);
	}

	public static <T> T tryGetBean(String string, Class<T> clazz) {
		return getBean(string, clazz);
	}

	public static HttpServletRequest tryGetRequest() {
		RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
		if (attrs instanceof ServletRequestAttributes attrs1) {
			return attrs1.getRequest();
		}
		return null;
	}

	public static HttpServletResponse tryGetResponse() {
		RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
		if (attrs instanceof ServletRequestAttributes attrs1) {
			return attrs1.getResponse();
		}
		return null;
	}

	public static void trySetAttribute(String key, Object value) {
		HttpServletRequest req = tryGetRequest();
		if (req != null && key != null && value != null) {
			req.setAttribute(key, value);
		}
	}

	public static void trySetSessionAttribute(String key, Object value) {
		HttpServletRequest req = tryGetRequest();
		if (req != null && key != null && value != null) {
			req.getSession().setAttribute(key, value);
		}
	}

	public static Object tryGetAttribute(String key) {
		HttpServletRequest req = tryGetRequest();
		if (req != null && key != null) {
			return req.getAttribute(key);
		}
		return null;
	}

	public static <T> T tryGetAttribute(String key, Class<T> clazz) {
		HttpServletRequest req = tryGetRequest();
		if (req != null && key != null && clazz != null) {
			Object obj = req.getAttribute(key);
			if (clazz.isInstance(obj)) {
				return clazz.cast(obj);
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public static <T> T tryGetParameters(String key, Class<T> clazz) {
		HttpServletRequest req = tryGetRequest();
		if (req != null && key != null && clazz != null) {
			String[] values = req.getParameterValues(key);

			Object arrs = ConvertUtils.convert(values, clazz);
			if (arrs != null && arrs instanceof Object[] o && o.length > 0 && clazz.isInstance(o[0])) {
				return (T) o[0];
			}
		}
		return null;
	}

	public static String tryGetHeader(String key) {
		HttpServletRequest req = tryGetRequest();
		if (req != null && key != null) {
			return req.getHeader(key);
		}
		return "";
	}

	/**
	 * return cookie.getSecure() && cookie.isHttpOnly() &&
	 * cookie.getName().equals(key)
	 * 
	 * @param key
	 * @return
	 */
	public static void tryAddHeader(String key, String value) {
		HttpServletResponse res = tryGetResponse();
		if (res != null && StringUtils.isNotBlank(key) && StringUtils.isNotBlank(value)) {
			res.setHeader(key, value);
		}
	}

	/**
	 * return cookie.getSecure() && cookie.isHttpOnly() &&
	 * cookie.getName().equals(key)
	 * 
	 * @param key
	 * @return
	 */
	public static Cookie tryAddCookie(String key, String value) {
		HttpServletResponse res = tryGetResponse();
		if (res != null && StringUtils.isNotBlank(key) && StringUtils.isNotBlank(value)) {
			Cookie cookie = new Cookie(key, value);
			// cookie.setHttpOnly(true)可以屏蔽js读取
			cookie.setPath("/");
			cookie.setHttpOnly(true);
			cookie.setMaxAge(24 * 3600);
			// https协议可以考虑setSecure(true)为true
			cookie.setSecure(false);
			res.addCookie(cookie);
		}
		return null;
	}

	/**
	 * return cookie.getSecure() && cookie.isHttpOnly() &&
	 * cookie.getName().equals(key)
	 * 
	 * @param key
	 * @return
	 */
	public static Cookie tryGetCookie(String key, boolean secure, boolean httpOnly) {
		HttpServletRequest req = tryGetRequest();
		if (req != null && key != null) {
			Cookie[] cookies = req.getCookies();
			if (cookies != null) {
				for (Cookie cookie : cookies) {
					if (cookie != null && secure == cookie.getSecure() && httpOnly == cookie.isHttpOnly()
							&& key.equals(cookie.getName())) {
						return cookie;
					}
				}
			}
		}
		return null;
	}

	/**
	 * return cookie.getSecure() && cookie.isHttpOnly() &&
	 * cookie.getName().equals(key)
	 * 
	 * @param key
	 * @return
	 */
	public static void tryDeleteCookie(String key) {
		HttpServletResponse res = tryGetResponse();
		if (res != null && key != null) {
			Cookie cookie = new Cookie(key, null);
			res.addCookie(cookie);
		}
	}

	/**
	 * return cookie.getSecure() && cookie.isHttpOnly() &&
	 * cookie.getName().equals(key)
	 * 
	 * @param key
	 * @return
	 */
	public static void tryDeleteHeader(String key) {
		HttpServletResponse res = tryGetResponse();
		if (res != null && key != null) {
			res.addHeader(key, null);
		}
	}

	/**
	 * return cookie.getSecure() && cookie.isHttpOnly() &&
	 * cookie.getName().equals(key)
	 * 
	 * @param key
	 * @return
	 */
	public static void tryDeleteAttribute(String key) {
		HttpServletRequest req = tryGetRequest();
		if (req != null && key != null) {
			req.setAttribute(key, null);
		}
	}

	private static <T> T getBean(Class<T> clazz) {
		Object applicationContext = ctx.get(SPRING_CONTEXT);
		if (clazz != null && applicationContext instanceof ApplicationContext ctx) {
			return ctx.getBean(clazz);
		}
		return null;
	}

	private static <T> Map<String, T> getBeanMap(Class<T> clazz) {
		Object applicationContext = ctx.get(SPRING_CONTEXT);
		if (clazz != null && applicationContext instanceof ApplicationContext ctx) {
			return ctx.getBeansOfType(clazz);
		}
		return Collections.emptyMap();
	}

	private static <T> T getBean(String name, Class<T> clazz) {
		Object applicationContext = ctx.get(SPRING_CONTEXT);
		if (applicationContext instanceof ApplicationContext ctx) {
			return ctx.getBean(name, clazz);
		}
		return null;
	}
}
