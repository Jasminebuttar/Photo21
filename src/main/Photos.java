package main;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import model.UserList;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;


/**
 * Photos is main class for the Photos21
 * Photos class reads the .dat file and initializes the user's list to it and starts the Login.fxml scene
 * @author Jasmine Buttar
 * @author Zalak Shingala
 */
public class Photos extends Application {
	
	Stage mainStage;
	
	/**
	 * Sets everything up at the start of the application
	 * @throws IOException					for switching scenes
	 * @throws ClassNotFoundException		for switching scenes
	 */
	@Override
	public void start(Stage primaryStage) throws IOException, ClassNotFoundException {
		
		
		UserList ulist2 = UserList.read();
		System.out.println(ulist2);
		
		mainStage = primaryStage;
		
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/view/Login.fxml"));
			BorderPane root = (BorderPane)loader.load();
			Scene scene = new Scene(root);
			
			
			mainStage.setScene(scene);
			mainStage.setTitle("Photo Album");
			mainStage.setResizable(false);
			mainStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
