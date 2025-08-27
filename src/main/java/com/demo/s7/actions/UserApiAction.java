package com.demo.s7.actions;

import java.util.Map;

import org.apache.struts2.ActionSupport;
import org.apache.struts2.action.Action;
import org.apache.struts2.interceptor.httpmethod.HttpPost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.demo.s7.R;
import com.demo.s7.RequestObject;

/**
 * <code>Set welcome message.</code>
 */
@Controller
@Scope("prototype")
@HttpPost
public class UserApiAction extends ActionSupport {

	private static final Logger logger = LoggerFactory.getLogger(UserApiAction.class);

	private static final long serialVersionUID = 1L;

	private transient Map<String, Object> result;

	private transient Map<String, Object> errorResult = R.fail("非法请求.");

	private transient RequestObject model = new RequestObject();

	public Map<String, Object> getErrorResult() {
		return errorResult;
	}

	public Map<String, Object> getResult() {
		return result;
	}

	public String list() {
		logger.info(" action method running!");
		this.result = R.success();
		return Action.SUCCESS;
	}

	public RequestObject getModel() {
		return model;
	}

	public void setModel(RequestObject model) {
		this.model = model;
	}

	@Override
	public void validate() {
		addFieldError("model", "测试.");
	}

}
