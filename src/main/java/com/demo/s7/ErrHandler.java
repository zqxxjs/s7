package com.demo.s7;

import java.util.UUID;

import org.apache.struts2.ActionContext;
import org.apache.struts2.StrutsException;
import org.apache.struts2.UnknownHandler;
import org.apache.struts2.config.entities.ActionConfig;
import org.apache.struts2.dispatcher.DefaultDispatcherErrorHandler;
import org.apache.struts2.json.JSONResult;
import org.apache.struts2.result.Result;
import org.apache.struts2.views.freemarker.FreemarkerResult;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * 默认未知处理器，用于处理未知Action、未知结果和未知方法
 */
@Slf4j
@Component
public class ErrHandler extends DefaultDispatcherErrorHandler implements UnknownHandler {

	/**
	 * 处理未知Action：当找不到对应Action配置时调用
	 */
	@Override
	public ActionConfig handleUnknownAction(String namespace, String actionName) throws StrutsException {
		return new ActionConfig.Builder(namespace, actionName, UnknownAction.class.getName()).build();
	}

	/**
	 * 处理未知结果：当Action返回无法匹配的结果码时调用
	 */
	@Override
	public Result handleUnknownResult(ActionContext actionContext, String actionName, ActionConfig actionConfig,
			String resultCode) throws StrutsException {
		try {
			return errJSON();
		} catch (Exception e) {
			throw new StrutsException("处理未知结果时发生错误", e);
		}
	}

	/**
	 * 处理未知方法：当Action中找不到指定方法时调用
	 */
	@Override
	public Object handleUnknownActionMethod(Object action, String methodName) {
		return errJSON();
	}

	@Override
	public void handleError(HttpServletRequest request, HttpServletResponse res, int code, Exception e) {
		try {
			String uuid = logError(e.getMessage());
			AjaxUtil.writeJSON(res, R.fail(404, "错误编号:" + uuid));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * 未知Action的默认处理类
	 */
	public static class UnknownAction {
		// 未知Action的默认处理方法
		public JSONResult execute() {
			return errJSON();
		}
	}

	public static FreemarkerResult errPage() {
		FreemarkerResult fr = new FreemarkerResult("error.ftl");
		setPageErrInfo();
		return fr;
	}

	public static JSONResult errJSON() {
		JSONResult json = new JSONResult();
		setJSONErrInfo();
		json.setRoot("result");
		return json;
	}

	public static void setPageErrInfo() {
		ActionContext.getContext().getValueStack().set("errCode", 404);
		ActionContext.getContext().getValueStack().set("errMsg", "发生了一些错误，等会儿再试试吧！");
	}

	public static void setJSONErrInfo() {
		String uuid = logError("资源路径不存在或请求方式不正确！");
		String errMsg = "资源路径不存在或请求方式不正确，请联系管理员,错误编号：" + uuid;
		ActionContext.getContext().getValueStack().set("result", R.fail(404, errMsg));
	}

	public static String logError(String errMsg) {
		String uuid = UUID.randomUUID().toString();
		HttpServletRequest req = ActionContext.getContext().getServletRequest();
		String uri = req.getRequestURI();
		log.error("visit url:{}, errCode:{}, errMsg:{}", uri, uuid, errMsg);
		return uuid;
	}
}
