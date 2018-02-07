package com.github.subtank.task;

import java.io.DataInputStream;
import java.io.IOException;

import com.github.subtank.protocol.Message;
import com.github.subtank.protocol.SocketConnect;

public class MessageReceiveTask extends CustomTask<Void>{
	final static int IGNOREBYTELENGTH = 16;//��Ļ��Ϣ����Ե��ֽ���
	final static byte[] RECEIVEMSG= {0x00,0x06,0x00,0x03}; //���յ���Ϣ
	final static byte[] HEARTBEATRESPONSE ={0x00,0x06,0x00,0x01};//�������ַ��������ص�ֵ
	
	DataInputStream iStream = SocketConnect.getiStream();
	
	@Override
	protected Void start() {
		byte[] receivMsgFlag=new byte[4];
		short msgLength;
		byte[] ignoreBytes=new byte[IGNOREBYTELENGTH];
		int mssageLength=0;
		
		while (!isCancelled()) {
			try {
				if (iStream.read(receivMsgFlag) >= 4) {
					if (	receivMsgFlag[0] == RECEIVEMSG[0]&&
							receivMsgFlag[1] == RECEIVEMSG[1]&&
							receivMsgFlag[2] == RECEIVEMSG[2]&&
							receivMsgFlag[3] == RECEIVEMSG[3]) { // receive msg
						msgLength = iStream.readShort();
						if (msgLength > 0) {
							byte[] rcvmsg = new byte[msgLength];
							iStream.read(rcvmsg);
							mssageLength = iStream.readInt();
							iStream.read(ignoreBytes);
							mssageLength-=IGNOREBYTELENGTH;
							//get real msg
							if(mssageLength>0){
								byte[] msg=new byte[mssageLength];
								iStream.read(msg);
								messageDecode(new String(msg,"UTF-8"));
							}
						}
					} else if (receivMsgFlag[0]==HEARTBEATRESPONSE[0]&&
								receivMsgFlag[1]==HEARTBEATRESPONSE[1]
								&&receivMsgFlag[2]==HEARTBEATRESPONSE[2]
								&&receivMsgFlag[3]==HEARTBEATRESPONSE[3]) {
						//heart beat receive
					} else {
						//other msg
					}
				}
			} catch (IOException e) {
				//socket closed
			}
		}
		return null;
	}
	
	@Override
	protected void cancelled() {
		super.cancelled();
	}
	
	Message messageType = new Message();
	
	public void messageDecode(String msgStr) {
		int indexOfStrEnd;
		indexOfStrEnd = msgStr.indexOf("{");
		//�����쳣��Ϣ
//		if (msgStr.substring(indexOfStrEnd+1, indexOfStrEnd+2).equals("\"")) {
//			int index = msgStr.indexOf("{",indexOfStrEnd+1);
//			if (index != -1) {
//				msgStr = msgStr.substring(msgStr.indexOf("{", indexOfStrEnd+1));
//			}
//		}
		//���������Ϣ
		indexOfStrEnd = msgStr.indexOf("}}}");
		if (indexOfStrEnd != -1) {
			if (indexOfStrEnd + 2 < msgStr.length()) {
				int index = msgStr.indexOf("{",indexOfStrEnd);
				if (index != -1) {
					messageDecode(msgStr.substring(index));
				}
			}
		} else {
			indexOfStrEnd = msgStr.indexOf("}}");
			if (indexOfStrEnd == -1) {
				//�޷�����Ϣ
				return;
			}
			if (indexOfStrEnd + 2 < msgStr.length()) {
				messageDecode(msgStr.substring(msgStr.indexOf("{", indexOfStrEnd)));
			}
		}
		//������Ϣ����ʽ
		if (messageType.messageDecode(msgStr) == null) {
			return;
		}
		messageFormatted(messageType.messageDecode(msgStr));
	}
}
