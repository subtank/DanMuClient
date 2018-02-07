package com.github.subtank.protocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class SocketConnect {
	private static Socket socket = null;
	static DataInputStream iStream;
	static DataOutputStream oStream;
	
	public static Socket SocketConnectted(String mIP, int mPort) {
		try {
			socket = new Socket(mIP, mPort);
			oStream=new DataOutputStream(socket.getOutputStream());
			iStream=new DataInputStream(socket.getInputStream());
			return socket;
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return socket;
	}

	public static DataInputStream getiStream() {
		return iStream;
	}

	public static DataOutputStream getoStream() {
		return oStream;
	}
	
	public static void closeIO() {
		try {
			iStream.close();
			oStream.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
