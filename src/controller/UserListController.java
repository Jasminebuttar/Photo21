package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.*;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;

/**
 * UserListController controls the user's list which will be shown in admin's
 * homepage only admin should be able to manage user's list
 * 
 * @author Jasmine Buttar
 * @author Zalak Shingala
 */
public class UserListController implements Error, Logout {

	@FXML
	TableView<User> tbl;

	@FXML
	TableColumn<User, String> usernameCol;

	@FXML
	TableColumn<User, String> pwCol;

	@FXML
	TableColumn<User, String> nameCol;

	@FXML
	TableColumn<User, User> deleteCol;

	private ObservableList<User> obsL;
	private List<User> users = new ArrayList<User>();
	private UserList userL;

	/**
	 * void method start initializes application with existing user's list
	 * 
	 * @param mainStage
	 *            The main stage
	 * @throws IOException
	 *             to read user's list
	 * @throws ClassNotFoundException
	 *             to read user's list
	 */
	public void start(Stage mainStage) throws ClassNotFoundException, IOException {

		userL = UserList.read();
		users = userL.getUserList();

		obsL = FXCollections.observableArrayList(users);

		usernameCol.setCellValueFactory(new Callback<CellDataFeatures<User, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<User, String> u) {
				return new SimpleStringProperty(u.getValue().getUsername());
			}
		});

		pwCol.setCellValueFactory(new Callback<CellDataFeatures<User, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<User, String> u) {
				return new SimpleStringProperty(u.getValue().getPassword());
			}
		});

		deleteCol.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		deleteCol.setCellFactory(param -> new TableCell<User, User>() {
			private final Button deleteButton = new Button("Delete");

			@Override
			protected void updateItem(User user, boolean empty) {
				super.updateItem(user, empty);

				if (user == null) {
					setGraphic(null);
					return;
				}

				setGraphic(deleteButton);
				deleteButton.setOnAction(event -> {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Delete User");
					alert.setHeaderText("Delete");

					String content = "Are you sure that you want to delete " + user.getUsername() + "?";

					alert.setContentText(content);

					Optional<ButtonType> result = alert.showAndWait();

					if (!user.getUsername().equals("admin") && result.isPresent()) {
						obsL.remove(user);
						users.remove(user);
						try {
							UserList.write(userL);
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else if (user.getUsername().equals("admin"))
						displayError("You cannot delete the admin!");
				});
			}
		});

		tbl.setItems(obsL);

		if (!obsL.isEmpty())
			tbl.getSelectionModel().select(0);

	}

	/**
	 * void method handleLogoutButton logs the user out, in this case admin
	 * 
	 * @param event
	 *            when Logout button is clicked
	 * @throws ClassNotFoundException
	 *             for scene switching
	 */
	@FXML
	protected void handleLogoutButton(ActionEvent event) throws ClassNotFoundException {
		logoutAction(event);
	}

	/**
	 * void method handleAddButton takes event(button clicked) as a parameter
	 * and adds a new user to the list
	 * 
	 * @param event
	 *            when "Add Users" is clicked
	 * @throws IOException
	 *             to read user's list
	 */
	@FXML
	private void handleAddButton(ActionEvent event) throws IOException {
		int index = tbl.getSelectionModel().getSelectedIndex();
		Dialog<User> dialog = new Dialog<>();
		dialog.setTitle("Create User");
		dialog.setHeaderText("Add a user to PhotoAlbum");
		dialog.setResizable(true);

		Label usernameLabel = new Label("Username: ");
		Label passwordLabel = new Label("Password: ");
		Label nameLabel = new Label("Full Name: ");
		TextField usernameTextField = new TextField();
		TextField passwordTextField = new TextField();
		TextField nameTextField = new TextField();

		GridPane grid = new GridPane();
		grid.add(usernameLabel, 1, 1);
		grid.add(usernameTextField, 2, 1);
		grid.add(passwordLabel, 1, 2);
		grid.add(passwordTextField, 2, 2);
		grid.add(nameLabel, 1, 3);
		grid.add(nameTextField, 2, 3);

		dialog.getDialogPane().setContent(grid);

		ButtonType buttonTypeOk = new ButtonType("Add", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);

		dialog.setResultConverter(new Callback<ButtonType, User>() {
			@Override
			public User call(ButtonType b) {
				if (b == buttonTypeOk) {

					String error = check(usernameTextField.getText(), passwordTextField.getText(),
							nameTextField.getText());

					if (error != null) {
						displayError(error);
						return null;
					}

					return new User(nameTextField.getText().trim(), usernameTextField.getText().trim(),
							passwordTextField.getText().trim());
				}
				return null;
			}

		});

		Optional<User> result = dialog.showAndWait();

		if (result.isPresent()) {
			User tempUser = (User) result.get();
			obsL.add(tempUser);
			users.add(tempUser);
			UserList.write(userL);

			if (obsL.size() == 1) {
				tbl.getSelectionModel().select(0);
			} else {
				index = 0;
				for (User s : userL.getUserList()) {

					if (s == tempUser) {
						tbl.getSelectionModel().select(index);
						break;
					}

					index++;
				}
			}

		}
	}

	/**
	 * 
	 * return method check makes sure if all fields are filled and returns in
	 * String type
	 * 
	 * @return if all fields are filled, if yes returns null, else shows error
	 *         message
	 */
	private String check(String username, String password, String name) {

		if (name.trim().isEmpty())
			return "Please enter your Full Name.";
		else if (username.trim().isEmpty())
			return "Please enter your Username.";
		else if (password.trim().isEmpty())
			return "Please enter your Password.";

		if (userL.userExists(username))
			return "The username that you've entered is already taken, please try another one.";
		else
			return null;
	}

}
