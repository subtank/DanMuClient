package com.github.subtank.task;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.github.subtank.DanMuClient;
import com.github.subtank.Controler.InterfaceController;
import com.github.subtank.protocol.messageType.DanMu;

import javafx.concurrent.Task;

abstract class CustomTask<V> extends Task<V> {
	@Override
	protected V call() throws Exception {
		try {
			return start();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}
	
	protected abstract V start() throws Exception;
	
	void fileIO(String msg) {
		String file_path = getController().savePath();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String timeMark = dateFormat.format(new Date());
		FileWriter fileWriter = null;
		File file = new File(file_path + "\\" + timeMark + ".txt");
		try {
			fileWriter = new FileWriter(file, true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		PrintWriter writer = new PrintWriter(fileWriter);
		writer.println(msg);
		writer.flush();
		try {
			fileWriter.close();
			writer.close();
			fileWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (getController().logShow()) {
			log(msg);
		}
//		System.out.println(getController().logShow());
	}
	
	void log(String msg) {
		getController().logshow(msg);
	}
	
	void messageFormatted(Object msg) {
		String messageText = null;
		if (msg.getClass().equals(DanMu.class)) {
			DanMu danmu = (DanMu) msg;
			
			String user_Name = danmu.mNickname;
			String user_Content = danmu.mContent;
			
			String user_Identity = "";
			if(Integer.parseInt(danmu.mIdentity)>=60){
				if(danmu.mIdentity.equals("60")) {
					user_Identity = "管理";
				} else if(danmu.mIdentity.equals("90")) {
					user_Identity = "主播";
				} else if(danmu.mIdentity.equals("120")) {
					user_Identity = "超管";
				}
			}
			messageText = user_Name + " : " + user_Content;
			if (!(user_Identity == "")) {
				messageText = "[" + user_Identity + "]" + messageText;
			}
			DateFormat dateFormat = new SimpleDateFormat("hh:mm:ss");
			String timeMark = dateFormat.format(new Date());
			messageText = timeMark + " " + messageText;
			fileIO(messageText);
			
		}
	}
	
	InterfaceController getController() {
        return DanMuClient.interfacecontroller;
    }
}
