package controller;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * Error interface is used to show error in alert dialog boxes
 * @author Jasmine Buttar
 * @author Zalak Shingala
 */
public interface Error {
	
	/**
	 * displayError displays error in alert dialog box when something is unacceptable
	 * @param err		error information shown in alert 
	 */
	default void displayError(String err) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Error");
		alert.setHeaderText("Error");
		String content = err;
		alert.setContentText(content);
		alert.showAndWait();
	}

}
