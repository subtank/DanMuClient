package com.github.subtank.protocol;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GetConnectConstant {
	private static int mPort, mRid, mAppid;//, mErrno;
	private static String mIP,mAuthtype, mSign, mTs;//, mErrMsg;
	
	public static int getmPort() {
		return mPort;
	}

	public static void setmPort(int mPort) {
		GetConnectConstant.mPort = mPort;
	}

	public static int getmRid() {
		return mRid;
	}

	public static void setmRid(int mRid) {
		GetConnectConstant.mRid = mRid;
	}

	public static int getmAppid() {
		return mAppid;
	}

	public static void setmAppid(int mAppid) {
		GetConnectConstant.mAppid = mAppid;
	}

	public static String getmIP() {
		return mIP;
	}

	public static void setmIP(String mIP) {
		GetConnectConstant.mIP = mIP;
	}

	public static String getmAuthtype() {
		return mAuthtype;
	}

	public static void setmAuthtype(String mAuthtype) {
		GetConnectConstant.mAuthtype = mAuthtype;
	}

	public static String getmSign() {
		return mSign;
	}

	public static void setmSign(String mSign) {
		GetConnectConstant.mSign = mSign;
	}

	public static String getmTs() {
		return mTs;
	}

	public static void setmTs(String mTs) {
		GetConnectConstant.mTs = mTs;
	}

//	public static String getmErrMsg() {
//		return mErrMsg;
//	}
//
//	public static void setmErrMsg(String mErrMsg) {
//		GetConnectConstant.mErrMsg = mErrMsg;
//	}

//	public static int getmErrno() {
//		return mErrno;
//	}
//
//	public static void setmErrno(int mErrno) {
//		GetConnectConstant.mErrno = mErrno;
//	}

	public static boolean jsonCode(JSONObject json) {
		try{
			JSONObject jsonData = (JSONObject) json.get("data");
			JSONArray jsonArray=jsonData.getJSONArray("chat_addr_list");
			String danMuAddress = jsonArray.getString(0);
			mIP = danMuAddress.split(":")[0];
			mPort = Integer.parseInt(danMuAddress.split(":")[1]);
			mRid = (int) jsonData.getInt("rid");
			mAppid = (int) jsonData.getInt("appid");
			mAuthtype = jsonData.getString("authType");
			mSign = jsonData.getString("sign");
			mTs = String.valueOf(jsonData.getLong("ts"));
//			mErrno = json.getInt("errno");
//			mErrMsg = json.getString("errmsg");
		}catch(JSONException e){
			return false;
		}catch(ClassCastException e){
			return false;
		}
		return true;
	}
}
