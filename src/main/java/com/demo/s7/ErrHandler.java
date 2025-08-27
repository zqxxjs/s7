package com.demo.s7;

import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.ActionContext;
import org.apache.struts2.StrutsException;
import org.apache.struts2.UnknownHandler;
import org.apache.struts2.action.Action;
import org.apache.struts2.config.entities.ActionConfig;
import org.apache.struts2.config.entities.ExceptionMappingConfig;
import org.apache.struts2.config.entities.ResultConfig;
import org.apache.struts2.result.Result;
import org.apache.struts2.views.freemarker.FreemarkerResult;

/**
 * 默认未知处理器，用于处理未知Action、未知结果和未知方法
 */
public class ErrHandler implements UnknownHandler {

	/**
	 * 处理未知Action：当找不到对应Action配置时调用
	 */
	@Override
	public ActionConfig handleUnknownAction(String namespace, String actionName) throws StrutsException {
		// 创建默认的Action配置，指向一个通用的未知Action处理类
		Map<String, ResultConfig> results = new HashMap<>();

		// 配置默认结果：指向404错误页面
		ResultConfig resultConfig = new ResultConfig.Builder(Action.SUCCESS, FreemarkerResult.class.getName())
				.addParam("location", "/WEB-INF/classes/templates/errors/404.ftl").build();

		results.put("success", resultConfig);

		// 创建异常映射：处理可能的异常
		Map<String, ExceptionMappingConfig> exceptionMappings = new HashMap<>();
		exceptionMappings.put("java.lang.Exception",
				new ExceptionMappingConfig.Builder("exception", "java.lang.Exception", "error").build());

		// 构建并返回默认Action配置
		return new ActionConfig.Builder(namespace, actionName, UnknownAction.class.getName() // 自定义的未知Action处理类
		).addResultConfigs(results).build();
	}

	/**
	 * 处理未知结果：当Action返回无法匹配的结果码时调用
	 */
	@Override
	public Result handleUnknownResult(ActionContext actionContext, String actionName, ActionConfig actionConfig,
			String resultCode) throws StrutsException {
		try {
			return new FreemarkerResult("/WEB-INF/classes/templates/errors/500.ftl");
		} catch (Exception e) {
			throw new StrutsException("处理未知结果时发生错误", e);
		}
	}

	/**
	 * 处理未知方法：当Action中找不到指定方法时调用
	 */
	@Override
	public Object handleUnknownActionMethod(Object action, String methodName) {
		try {
			return action.getClass().getMethod(methodName).invoke(action);
		} catch (Exception e) {
			// 返回错误结果码
			return "error";
		}
	}

	/**
	 * 未知Action的默认处理类
	 */
	public static class UnknownAction {
		// 未知Action的默认处理方法
		public String execute() {
			// 可以在这里添加日志记录
			ActionContext.getContext().put("errorMessage", "请求的资源不存在");
			return "success";
		}
	}
}
