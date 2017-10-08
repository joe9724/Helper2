package com.bitekun.helper.util;

import com.bitekun.helper.bean.HelpPeopleListItem;
import com.bitekun.helper.bean.JsonContent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class JsonUtil {

	private static class NewsUtilInstance {
		private static JsonUtil instance = new JsonUtil();
	}

	public static JsonUtil getInstance() {

		return NewsUtilInstance.instance;
	}

	public static final List<HelpPeopleListItem> BEANSLIST = new ArrayList<HelpPeopleListItem>();

	private String obtainParamz(String value) throws JSONException {
		JSONObject jsonObject = new JSONObject(value);
		String paramz = null;
		if (null != jsonObject) {
			paramz = jsonObject.getString("paramz");
		}
		return paramz;
	}

	/*
	 * private String validStringIsNull(JSONObject jsonObject, String name)
	 * throws JSONException { if (null != jsonObject) { if
	 * (!jsonObject.isNull(name)) { return
	 * Utils.decodeURL(jsonObject.getString(name)); } } return null; }
	 */

	private JSONArray getJsonArray(String url)
			throws JSONException {
		NetGet netGet = new NetGet(url);
		String returnJsonStr = netGet.doGet();
		JSONArray jsonArray = null;
		jsonArray = new JSONArray(returnJsonStr);
		/*JSONArray jsonArray = null;
		if (null != returnJsonStr) {
			returnJsonStr = obtainParamz(returnJsonStr);
			if (null != returnJsonStr) {
				JSONObject jsonObject = new JSONObject(returnJsonStr);
				if (null != jsonObject) {
					returnJsonStr = jsonObject.getString(jsonName);
					jsonArray = new JSONArray(returnJsonStr);
				}
			}
		}*/
		return jsonArray;
	}

	public ArrayList<HelpPeopleListItem> getHelpPeople(String subjectId,
			String pageno, Map<String, JsonContent> map) {
		ArrayList<HelpPeopleListItem> list = new ArrayList<HelpPeopleListItem>();
		try {

			JSONArray jsonArray = getJsonArray(CommonConst.helppeopleURL
					+ pageno);

			if (jsonArray != null) {
				HelpPeopleListItem detailBean = null;
				JSONObject jsonObj = null;
				for (int i = 0; i < jsonArray.length(); i++) {
					jsonObj = jsonArray.getJSONObject(i);
					detailBean = new HelpPeopleListItem();
					//detailBean.setID(jsonObj.getString("id"));
					detailBean.setName(jsonObj.getString("workerName"));
					list.add(detailBean);
				}

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

}
