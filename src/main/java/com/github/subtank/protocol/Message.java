package com.github.subtank.protocol;

import org.json.JSONException;
import org.json.JSONObject;

import com.github.subtank.protocol.messageType.DanMu;

public class Message {
	public Object messageDecode(String message) {
		JSONObject jsonObject = null;
		jsonObject = new JSONObject(message);
		return jsonDecode(jsonObject);
	}
	
	public Object jsonDecode(JSONObject jsonObject) {
		Object message = null;
		String type = "";
		try {
			type = jsonObject.getString("type");
			if (type.equals(DanMu.CODE)) {
				DanMu danmu = new DanMu();
				danmu.mTime = jsonObject.getLong("time");
				danmu.mIdentity = jsonObject.getJSONObject("data").getJSONObject("from").getString("identity");
				danmu.mNickname = jsonObject.getJSONObject("data").getJSONObject("from").getString("nickName");
				danmu.mContent = jsonObject.getJSONObject("data").getString("content");
				message = danmu;
			}
		} catch (JSONException e) {
			// TODO: handle exception
		}
		return message;
	}
}
