package com.github.subtank;

import java.net.URL;

import com.github.subtank.Controler.InterfaceController;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class DanMuClient extends Application{
	private static final String NAME = "pandaTV DanMu Logger";
	private static final String VERSION = "0.0.5";
	private static final String AUTHOR = "subtank";
	public static String title() {
		return String.format("%s by %s V%s", NAME, AUTHOR, VERSION);
	}
	
	public static InterfaceController interfacecontroller;
	public static Parent parent;
	public static Stage stage;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		stage = primaryStage;
		
		String url = "Interfacecontroller.fxml";
		URL interfaceCollection = getClass().getClassLoader().getResource(url);
		
		if (interfaceCollection != null) {
			FXMLLoader loader = new FXMLLoader(interfaceCollection);
			parent = loader.load();
			
			//for threading safe
			interfacecontroller = loader.getController();
			
			stage.setScene(new Scene(parent));
            stage.setResizable(false);
            stage.setTitle(title());
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                public void handle(WindowEvent event) {
                    System.exit(0);
                }
            });
            stage.show();
		} else {
			throw new RuntimeException("Impossible to load interface.fxml file : the application can't start");
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
