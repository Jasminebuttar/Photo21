package controller;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.*;	
	 
/**
 * LoginController controls the login page which is the first scene
 * @author Jasmine Buttar
 * @author Zalak Shingala
 */
public class Login {
		
		
	@FXML private Button loginButton;
	@FXML private TextField usernameField;
	@FXML private TextField passwordField;
	@FXML private Text actionTarget;
	    
	/**
	 * void method handleLoginButtonAction handles Login button
	 * checks if username and passsword combination is correct, otherwise error message shows up
	 * checks if the username is of admin's or not and shows home screen according to what type of account it is
	 * @param event			when "Login" button is clicked
	 * @throws ClassNotFoundException		for scene switching
	 */
	@FXML protected void handleLoginButtonAction(ActionEvent event) throws ClassNotFoundException {
	    	
		String username = usernameField.getText();
		String password = passwordField.getText();
	    	
	    Parent p;
	    	 
	    try {
			System.out.println(UserList.read());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	    	
	    UserList ulist = null;
		try {
			ulist = UserList.read();
			System.out.println(ulist);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	    	
		try {
					
			if(ulist.isUserInList(username, password)) {
				if(username.equals("admin") && password.equals("admin")){	 
					FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AdminHome.fxml"));
					p = (Parent) loader.load();
					        
					UserListController ctrl = loader.getController();
					Scene scene = new Scene(p);
								
					Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();	
				                
					ctrl.start(app_stage);
				             
				    app_stage.setScene(scene);
				    app_stage.show();  
							
				}
				else{
					FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/UserHome.fxml"));
					p = (Parent) loader.load();
					AlbumList ctrl = loader.<AlbumList>getController();
					ctrl.setUlist(ulist);
					ctrl.setUser(ulist.getUserByUsername(username));
					Scene scene = new Scene(p);
					        
					Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();	
				                
					ctrl.start(app_stage);
				             
				    app_stage.setScene(scene);
				    app_stage.show();  
				}
					
			}
		else {
				actionTarget.setText("Incorrect username/password. Please try again.");
		}
			} catch (IOException e) {
				e.printStackTrace();
			}
		
			
	         
	         
	}
	    
	   
}
