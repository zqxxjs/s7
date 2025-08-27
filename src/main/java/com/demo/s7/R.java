package com.demo.s7;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;

/**
 * 响应结果封装类 结果格式统一 固定为：<br/>
 * {"code":${数字},"msg":"字符串","result":{"count":${数字},"data":[]}}<br/>
 * <br/>
 * 默认成功响应：<br/>
 * {"code":200,"msg":"success","result":{"count":0,"data":[]}}<br/>
 * <br/>
 * 默认失败响应：<br/>
 * {"code":500,"msg":"fail","result":{"count":0,"data":[]}}<br/>
 * <br/>
 * 
 * @author swt
 *
 */
public class R {

	private static final int SUCCESS_CODE = 200;
	private static final String SUCCESS_MSG = "操作成功.";
	private static final int FAIL_CODE = 500;
	private static final String PARAM_ERROR = "参数错误,请检查后再试!";
	private static final String FAIL_MSG = "error";
	private static final String RESULT = "result";
	private static final String DATA = "data";
	private static final String COUNT = "count";

	private R() {
	}

	public static Map<String, Object> success() {
		return mapResult(SUCCESS_CODE, SUCCESS_MSG);
	}

	public static Map<String, Object> success(int code) {
		return mapResult(code, SUCCESS_MSG);
	}

	public static Map<String, Object> success(String msg) {
		return mapResult(SUCCESS_CODE, msg);
	}

	public static Map<String, Object> success(int code, String msg) {
		return mapResult(code, msg);
	}

	public static Map<String, Object> success(Collection<?> data) {
		return mapResult(SUCCESS_CODE, SUCCESS_MSG, dataNode(data.size(), data));
	}

	public static Map<String, Object> success(Collection<?> data, long count) {
		return mapResult(SUCCESS_CODE, SUCCESS_MSG, dataNode(count, data));
	}

	public static Map<String, Object> fail() {
		return mapResult(FAIL_CODE, PARAM_ERROR);
	}

	public static Map<String, Object> fail(int code) {
		return mapResult(code, FAIL_MSG);
	}

	public static Map<String, Object> fail(String msg) {
		return mapResult(FAIL_CODE, msg);
	}

	public static Map<String, Object> fail(int code, String msg) {
		return mapResult(code, msg);
	}

	public static Map<String, Object> mapResult(int code, String msg) {
		return mapResult(code, msg, null);
	}

	public static Map<String, Object> mapResult(int code, String msg, Collection<?> data, long count) {
		Map<String, Object> response = dataNode(count, data);
		return mapResult(code, msg, response);
	}

	public static <T> Map<String, Object> mapResult(int code, String msg, T[] data, long count) {
		Map<String, Object> response = dataNode(count, data);
		return mapResult(code, msg, response);
	}

	public static Map<String, Object> mapResult(int code, String msg, Map<String, Object> data) {
		Map<String, Object> result = new HashMap<>(3);
		result.put("code", code);
		result.put("msg", msg);

		if (data == null) {
			data = dataNode(0, null);
		}
		result.put(RESULT, data);
		return result;
	}

	/**
	 * 从固定格式的map中获取data对象
	 *
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> getData(Map<String, Object> jsonResponseMap) {
		Map<String, Object> map = MapUtils.getMap(jsonResponseMap, RESULT);
		if (map != null && map.containsKey(DATA)) {
			Object data = map.get(DATA);
			if (data instanceof List) {
				return (List<T>) data;
			}
		}
		return new ArrayList<>(0);
	}

	/**
	 * 从固定格式的map中获取count对象
	 *
	 */
	@SuppressWarnings("unchecked")
	public static Long getCount(Map<String, Object> jsonResponseMap) {
		Map<String, Object> map = MapUtils.getMap(jsonResponseMap, RESULT);
		if (map != null && map.containsKey(COUNT)) {
			return MapUtils.getLong(map, COUNT);
		}
		return 0L;
	}

	/**
	 * 数据节点，两个kv键值对，k固定为 count,data
	 *
	 */
	private static Map<String, Object> dataNode(long count, Object data) {
		Map<String, Object> response = new HashMap<>(2);

		if (count < 0)
			count = 0;

		response.put(COUNT, (int) count);

		if (data != null && (data instanceof Collection || data.getClass().isArray())) {
			if (data instanceof Object[] d) {
				data = Arrays.asList(d);
			}
			response.put(DATA, data);
		} else if (data != null) {
			Object[] arr = { data };
			response.put(DATA, arr);
		} else {
			response.put(DATA, new ArrayList<>(0));
		}
		return response;
	}
}
