package com.github.subtank.protocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import org.json.JSONException;
import org.json.JSONObject;

public class ConnectDanMuServer {
	final static String DANMU_SERVER_URL = "http://riven.panda.tv/chatroom/getinfo?roomid=";
	final static byte[] RESPONSE = {0x00,0x06,0x00,0x06}; //连接弹幕服务器响应
	final static byte[] STARTFLAG= {0x00,0x06,0x00,0x02};  //连接弹幕服务器帧头
	
	private Socket socket;
	DataOutputStream oStream;
	DataInputStream  iStream;
	
	private static int mPort, mRid, mAppid;
	private static String mIP,mAuthtype, mSign, mTs;
	
	//none use msg
//	private static int mErrno;
//	private static String mErrMsg;
	
	public void setConnectConstant() {
		mIP = GetConnectConstant.getmIP();
		mPort = GetConnectConstant.getmPort();
		mRid = GetConnectConstant.getmRid();
		mAppid = GetConnectConstant.getmAppid();
		mAuthtype = GetConnectConstant.getmAuthtype();
		mSign = GetConnectConstant.getmSign();
		mTs = GetConnectConstant.getmTs();
//		mErrno = GetConnectConstant.getmErrno();
//		mErrMsg = GetConnectConstant.getmErrMsg();
	}
	
	public boolean connectToDanMuServer(String roomID) {
		String result = "";
		JSONObject json;
		result = HttpRequest.sendGet(DANMU_SERVER_URL, roomID);
		if (result == null) {
			return false;
		}
		try {
			json = new JSONObject(result);
		} catch (JSONException e) {
			return false;
		}
		if (!GetConnectConstant.jsonCode(json)) {
			return false;
		}
		setConnectConstant();
		try {
			SocketConnect.SocketConnectted(mIP, mPort);//建立socket连接
			oStream = SocketConnect.getoStream();
			iStream = SocketConnect.getiStream();
			oStream.write(getConnectDate());//发送
			
			//接收响应数据
			
			byte readData[]=new byte[6];
			int rpLength=iStream.read(readData);
			if(rpLength>=6){
				if(!(readData[0]==RESPONSE[0]&&readData[1]==RESPONSE[1]&&readData[2]==RESPONSE[2]&&readData[3]==RESPONSE[3])) {
					closeIO();
					SocketConnect.closeIO();
					return false;
				}
				else{
					return true;
					//消息主体，暂时用不到
				/*	short dataLength=(short) (readData[5]|(readData[4]<<8));
					byte[] data=new byte[dataLength];//数据
					is.read(data);//主体数据，appid+r的值，eg:id:845694055\nr:0  暂时用不到
				*/
				}					
			}else {
				closeIO();
				SocketConnect.closeIO();
				return false;
			}		
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		closeIO();
		SocketConnect.closeIO();
		return false;
	}
	
	public byte[] getConnectDate(){
		String danMUMsg = "u:"+mRid+"@"+mAppid+"\nk:1\nt:300\nts:"+mTs+"\nsign:"+mSign+"\nauthtype:"+mAuthtype;
		byte content[]=danMUMsg.getBytes();
		byte length[]={(byte)(content.length>>8),(byte)(content.length&0xff)};
		byte sendMessage[]=new byte[STARTFLAG.length+2+content.length];
		System.arraycopy(STARTFLAG, 0, sendMessage, 0, STARTFLAG.length);
		System.arraycopy(length, 0, sendMessage, STARTFLAG.length, length.length);
		System.arraycopy(content, 0, sendMessage, STARTFLAG.length+length.length, content.length);
		return sendMessage;
	}
	
	public void closeIO() {
		try {
			iStream.close();
			oStream.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
