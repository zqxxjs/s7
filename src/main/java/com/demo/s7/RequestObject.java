package com.demo.s7;

import java.util.List;
import java.util.Map;

public class RequestObject {

	private Map<String, Object> alias;

	private List<Map<String, Object>> data;

	private List<SearchCondition> conditions;

	public Map<String, Object> getAlias() {
		return alias;
	}

	public void setAlias(Map<String, Object> alias) {
		this.alias = alias;
	}

	public List<Map<String, Object>> getData() {
		return data;
	}

	public void setData(List<Map<String, Object>> data) {
		this.data = data;
	}

	public List<SearchCondition> getConditions() {
		return conditions;
	}

	public void setConditions(List<SearchCondition> conditions) {
		this.conditions = conditions;
	}

}
