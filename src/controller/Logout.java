package controller;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Logout interface is used to log the user out 
 * goes back to the scene of Login.fxml when Logout button is cliced
 * @author Jasmine Buttar
 * @author Zalak Shingala
 */
public interface Logout {

	/**
	 * the default void method logoutAction signs the user out of the account
	 * @param event		when "Logout" button is clicked
	 * @throws ClassNotFoundException	Exception for which switches scenes
	 */
	default void logoutAction(ActionEvent event) throws ClassNotFoundException {
    	Parent p;
    	 
		try {
						
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Login.fxml"));
			p = (Parent) loader.load();
		
			Scene scene = new Scene(p);
							
			Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();			             
			app_stage.setScene(scene);
			app_stage.show();  
						
			 
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}         
         
	}
}
