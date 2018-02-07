package com.github.subtank.Controler;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import com.github.subtank.DanMuClient;
import com.github.subtank.protocol.ConnectDanMuServer;
import com.github.subtank.task.HeartBeatTask;
import com.github.subtank.task.MessageReceiveTask;
import com.github.subtank.task.VerifyTask;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;

public class InterfaceController implements Initializable {
	
	@FXML
	private Label Title, RoomId, ShowDML, LoggerSavePath;
	@FXML
	private TextField RoomIdText, SavePathText;
	@FXML
	private CheckBox ShowDMLCheck;
	@FXML
	private Button SavePath, Start;
	@FXML
	private TextArea LoggerText;
	
	public void LanguageController() {
//		multi-language support WIP
//		String lang = "english";
	}
	
	public void selectSavePath(ActionEvent event) {
		DirectoryChooser dc = new DirectoryChooser();
		File localDir = new File("");
		File dir = new File(localDir.getAbsolutePath());
		dc.setInitialDirectory(dir.exists() ? dir : new File(System.getProperty("user.home")));
		File file = dc.showDialog(DanMuClient.stage);
//		fix NPE to local directory
		SavePathText.setText(file == null ? localDir.getAbsolutePath() : file.getAbsolutePath());
	}
	
	public void startLogger(ActionEvent event) {
		disable(true);
		if (!VerifyTask.verifyID(RoomIdText.getText())) {
			LoggerText.setText("房间号非法，请重新输入！\n");
			disable(false);
		} else {
			disable(true);
			Start.setDisable(false);
			Start.setText("Stop Logger");
			ConnectDanMuServer logger = new ConnectDanMuServer();
			if (logger.connectToDanMuServer(RoomIdText.getText())) {
				LoggerText.setText("成功连接到房间号为：" + RoomIdText.getText() + " 的弹幕服务器！\n");
				//弹幕连接处理
				HeartBeatTask heartBeatTask = new HeartBeatTask();
				Thread heartbeat = new Thread(heartBeatTask);
				heartbeat.start();
				MessageReceiveTask messageReceiveTask = new MessageReceiveTask();
				Thread messagereceive = new Thread(messageReceiveTask);
				messagereceive.start();
				Start.setOnAction(stop -> {
					//停止各种子进程
					heartBeatTask.cancel();
					messageReceiveTask.cancel();
					disable(false);
					Start.setText("Start Logger");
					Start.setOnAction(start -> {
						startLogger(event);
						});
					});
			} else {
				LoggerText.setText("连接弹幕服务器失败！");
			}
		}
	}
	
	public void disable(boolean b) {
		Start.setDisable(b);
		ShowDMLCheck.setDisable(b);
		SavePath.setDisable(b);
		RoomIdText.setDisable(b);
	}
	
	public boolean logShow() {
		return ShowDMLCheck.isSelected();
	}
	
	public void logshow(String msg) {
		LoggerText.setText(LoggerText.getText() + msg + "\n");
	}
	
	public String savePath() {
		return SavePathText.getText();
	}
	
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}
	
}
