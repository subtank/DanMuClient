package com.github.subtank.task;

import java.io.DataOutputStream;
import java.io.IOException;

import com.github.subtank.protocol.SocketConnect;

public class HeartBeatTask extends CustomTask<Void>{
	final static byte[] KEEPLIVE = {0x00,0x06,0x00,0x01};
	DataOutputStream oStream = SocketConnect.getoStream();
	
	//WIP -- auto connect
	
	@Override
	protected Void start() throws Exception {
		while (!isCancelled()) {
			try {
				oStream.write(KEEPLIVE);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			try {
				//120 seconds for each heart beat send
				Thread.sleep(300*1000);
			} catch (InterruptedException e) {
				
			}
		}
		return null;
	}
	@Override
	protected void cancelled() {
		super.cancelled();
	}
	
	
}
