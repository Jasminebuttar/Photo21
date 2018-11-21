package controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Album;
import model.Photo;
import model.Tag;
import model.User;
import model.UserList;

/**
 * PhotoDisplayController controls the display of the photo
 * PhotoDisplayController implements Error interface and Logout interface
 * @author Jasmine Buttar
 * @author Zalak Shingala
 */

public class PhotoDisplay implements Error, Logout {

	public final static int CAME_FROM_ALBUM_CONTENT = 0,
			CAME_FROM_PHOTO_SEARCH = 1;
	
	@FXML
	ListView<Tag> tagListView;
	
	@FXML
	ImageView photoImageView;
	
	@FXML
	Text captionText, photoDateText;
	
	@FXML
	Button previousPhotoBtn, nextPhotoBtn;
	
	private ObservableList<Tag> obsList;
	private int photoIndex;
	private Album album;
	private User user;
	private List<Photo> photos;
	private UserList ulist;
	private int key;
	
	/**
	 * void method initializes all details of the photo at the start of the application
	 * @param mainStage		main stage
	 */
	public void start(Stage mainStage) {
		if (key == CAME_FROM_ALBUM_CONTENT)
			photos = album.getPhotos();
		
		updatePhotoDetails();

	}
	
	/**
	 * void method handleLogoutButton logs the user out of the account when logout button is clicked
	 * @param event		when the "Logout" button is clicked
	 * @throws ClassNotFoundException		for scene switching
	 */
	@FXML 
	protected void handleLogoutButton(ActionEvent event) throws ClassNotFoundException {
    	logoutAction(event);          
	}
	
	/**
	 * void method back helps user go back to previous screen when the "Back" button is clicked
	 * Go back to the previous scene. This depends on the key assigned to this controller.
	 * @param event		when the "Back" button is clicked
	 * @throws ClassNotFoundException		for scene switching
	 */
	@FXML
	protected void back(ActionEvent event) throws ClassNotFoundException {
		Parent parent;
	   	 
		try {
			if (key == CAME_FROM_ALBUM_CONTENT) {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AlbumContent.fxml"));
		        parent = (Parent) loader.load();
		        PhotoList ctrl = loader.<PhotoList>getController();
		        //send user index to album list controller
		        ctrl.setUser(user);
		        ctrl.setAlbum(album);
		        ctrl.setUlist(ulist);
		        Scene scene = new Scene(parent);
		        
		        Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();	
		        
				ctrl.start(app_stage);
		         
				app_stage.setScene(scene);
				app_stage.show();
			}
			if (key == CAME_FROM_PHOTO_SEARCH) {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Search.fxml"));
		        parent = (Parent) loader.load();
		        PhotoSearch ctrl = loader.<PhotoSearch>getController();
		        //send user index to album list controller
		        ctrl.setUser(user);
		        ctrl.setUlist(ulist);
		        Scene scene = new Scene(parent);
		        
		        Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();	
		        
				ctrl.start(app_stage);
		         
				app_stage.setScene(scene);
				app_stage.show();
			}
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	/**
	 * void method addTag helps the user to add the tag to the selected photo
	 * @param event		when "Add Tag" button is clicked
	 */
	@FXML
	protected void addTag(ActionEvent event) {
		
		Dialog<Album> dialog = new Dialog<>();
		dialog.setTitle("Add Tag");
		dialog.setHeaderText("Adding Tag to the selected photo");
		dialog.setResizable(true);
		   
		Label keyLabel = new Label("Tag Key: ");
		TextField keyTextField = new TextField();
		Label valueLabel = new Label("Tag Value: ");
		TextField valueTextField = new TextField();
		
		GridPane grid = new GridPane();
		grid.add(keyLabel, 1, 1);
		grid.add(keyTextField, 2, 1);
		grid.add(valueLabel, 1, 2);
		grid.add(valueTextField, 2, 2);
		
		dialog.getDialogPane().setContent(grid);
		   
		ButtonType buttonTypeOk = new ButtonType("Add", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);
	//wait for response from add button
		
		
		Optional<Album> result = dialog.showAndWait();
		
		String error = checkFields(keyTextField.getText(), valueTextField.getText());
					   
			
		   
			   
			// if user presses Add, add the user to the overall list
			if (result.isPresent()) {
				if (error != null) {
					displayError(error);
						
				}
				else
				{	
				Photo p = photos.get(photoIndex);
				p.addTag(keyTextField.getText(),valueTextField.getText());			
				updatePhotoDetails();  
				try{
					  UserList.write(ulist);
				  }
				  catch(Exception i)
				  {
					  i.printStackTrace();
				  }
				}
			}
				 
		}		   
	
	/**
	 * void method checkFields	checks the fields if they already exist, or any value is not entered
	 * @param type		tag type
	 * @param value		tag value
	 * @return error message in string format to indicate what the error is
	 */
	private String checkFields(String type, String value) {
		for(Tag t : photos.get(photoIndex).getTags())
		{
			if(t.getType().equals(type) && t.getValue().equals(value))
			return "The tag that you've entered already exists. Please try another one.";
		}
		if (type.equals("")|| value.equals(""))
			return "Please enter both tag type and tag value";
		
		return null;
	}
	
	/**
	 * void method previousPhoto helps the user to display the previous photo
	 * @param event		when "<" button is clicked
	 */
	@FXML
	protected void previousPhoto(ActionEvent event) {
		photoIndex--;
		updatePhotoDetails();
	}
	
	/**
	 * void method nextPhoto helps the user to display the next photo
	 * @param event		when ">" button is clicked
	 */
	@FXML
	protected void nextPhoto(ActionEvent event) {
		photoIndex++;
		updatePhotoDetails();
	}
	
	/**
	 * void method updatePhotoDetails helps user to update image, caption, or tag list
	 */
	public void updatePhotoDetails() {
		photoImageView.setImage(photos.get(photoIndex).getImage());
		captionText.setText("Caption: " + photos.get(photoIndex).getCaption());
		photoDateText.setText("Photo Date: " + photos.get(photoIndex).getDate());
		obsList = FXCollections.observableArrayList(photos.get(photoIndex).getTags());
		
		tagListView.setCellFactory(new Callback<ListView<Tag>, ListCell<Tag>>(){
			
			@Override
			public ListCell<Tag> call(ListView<Tag> t) {
				
				return new TagCell();
			}
		});	
		
		tagListView.setItems(obsList);
		
		previousPhotoBtn.setDisable(photoIndex == 0);
		nextPhotoBtn.setDisable(photoIndex == photos.size()-1);
	}
	
	/**
	 * void method setAlbum to keep record of which album is picked
	 * @param a		album to be set
	 */
	public void setAlbum(Album a) {
		album = a;
	}
	
	/**
	 * void method setUser sets the user to the passed user to let the controller know who is currently logged in
	 * @param u		user to be set
	 */
	public void setUser(User u) {
		user = u;
	}
	
	/**
	 * void method setPhotoIndex sets the index of the photo to the passed int value i
	 * @param i		index of the photo to be set
	 */
	public void setPhotoIndex(int i) {
		photoIndex = i;
	}
	
	/**
	 * void method setUlist sets the current object's user's list to the pass user's list
	 * @param ulist		user's list
	 */
	public void setUlist(UserList ulist) {
		this.ulist = ulist;
	}
	
	/**
	 * void method setPhoto sets the current object's photos to the passed list of photos
	 * @param photos	photo list to be set
	 */
	public void setPhotos(List<Photo> photos) {
		this.photos = photos;
	}
	
	/**
	 * void method setKey sets the current object's key to the passed key
	 * @param key		key to be set
	 */
	public void setKey(int key) {
		this.key = key;
	}
	
	/**
	 * TagCell is the subclass of ListCell that helps to display the Tag's data in the ListView cell
	 * @author Jasmine Buttar
	 * @author Zalak Shingala
	 */
	private class TagCell extends ListCell<Tag> {
			
		AnchorPane apane = new AnchorPane();
		
		Label tagLabel = new Label();
		Button deleteTagBtn = new Button("Delete");
		Button editTagBtn = new Button("Edit");
			
		public TagCell() {
			super();
			
			AnchorPane.setLeftAnchor(tagLabel, 0.0);
			AnchorPane.setTopAnchor(tagLabel, 0.0);
			
			AnchorPane.setRightAnchor(deleteTagBtn, 0.0);
			AnchorPane.setTopAnchor(deleteTagBtn, 0.0);
			
			AnchorPane.setRightAnchor(editTagBtn, 80.0);
			AnchorPane.setTopAnchor(editTagBtn, 0.0);
			
			tagLabel.setMaxWidth(200.0);
			
			deleteTagBtn.setVisible(false);
			editTagBtn.setVisible(false);
			
			apane.getChildren().addAll(tagLabel, deleteTagBtn, editTagBtn);
			
			setGraphic(apane);
		}
			
		@Override
		public void updateItem(Tag tag, boolean empty) {
			super.updateItem(tag, empty);
			setText(null);
			
			if (tag != null) {
				tagLabel.setText(tag.toString());
				deleteTagBtn.setVisible(true);
				editTagBtn.setVisible(true);
				
				deleteTagBtn.setOnAction(new EventHandler<ActionEvent>() {
					@Override public void handle(ActionEvent e) {
						deleteTag(e, tag);
					}
				});
				
				editTagBtn.setOnAction(new EventHandler<ActionEvent>() {
					@Override public void handle(ActionEvent e) {
						editTag(e, tag);
					}
				});
				
			}	
		}
	}
		
		/**
		 * void method deleteTag helps the user to remove the tag from the tag list of particular photo
		 * @param e		when "Dlete" button for the tag is clicked
		 * @param tag	tag to be deleted
		 */
		public void deleteTag(ActionEvent e, Tag tag) {
			Alert alert = 
	 				   new Alert(AlertType.INFORMATION);
	 		   alert.setTitle("Delete Tag");
	 		   alert.setHeaderText(
	 				   "Delete");

	 		   String content = "Are you sure that you want to delete the tag " + tag.getType() + " : " + tag.getValue()+"?";

	 		   alert.setContentText(content);

	 		   Optional<ButtonType> result = alert.showAndWait();
	 		   
	 		  if(result.isPresent())
	 		  {
	 			 Photo p = photos.get(photoIndex);
	 			 for(int t = 0; t< p.getTags().size(); t++)
	 			 {
	 				 if(p.getTag(t).equals(tag))
	 				 {
	 					 p.removeTag(t);
	 					 updatePhotoDetails();
	 					try {
	 						
	 						UserList.write(ulist);
	 						
	 					} catch (Exception i) {
	 						// TODO Auto-generated catch block
	 						i.printStackTrace();
	 					}
	 				 }
	 			 }	 		 
				
				
				
	 		  }
		}
		
		/**
		 * void method editTag helps the user to edit the tag of the photo
		 * @param e		when "Edit Tag" button is clicked
		 * @param tag	tag to be edited
		 */
		public void editTag(ActionEvent e, Tag tag) {
			 Dialog<User> dialog = new Dialog<>();
			   dialog.setTitle("Edit Tag");
			   dialog.setHeaderText("Change the key or value of the tag");
			   dialog.setResizable(true);
			   
			   Label keyLabel = new Label("Tag Key: ");
				TextField keyTextField = new TextField();
				keyTextField.setText(tag.getType());
				Label valueLabel = new Label("Tag Value: ");
				TextField valueTextField = new TextField();
				valueTextField.setText(tag.getValue());
				
				GridPane grid = new GridPane();
				grid.add(keyLabel, 1, 1);
				grid.add(keyTextField, 2, 1);
				grid.add(valueLabel, 1, 2);
				grid.add(valueTextField, 2, 2);
				
				dialog.getDialogPane().setContent(grid);
				   
				ButtonType buttonTypeOk = new ButtonType("Ok", ButtonData.OK_DONE);
				dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);
				
				
				Optional<User> result = dialog.showAndWait();
				
				String error = checkFields(keyTextField.getText(), valueTextField.getText());
							   
		
					if (result.isPresent()) {
						if (error != null) {
							displayError(error);
						
						}
						else
						{
							Photo p = photos.get(photoIndex);
							for(int t = 0; t< p.getTags().size(); t++)
				 			 {
				 				 if(p.getTag(t).equals(tag))
				 				 {
				 					 p.editTag(t, keyTextField.getText(), valueTextField.getText());
				 					 updatePhotoDetails();
									  try{
										  UserList.write(ulist);
									  }
									  catch(Exception i)
									  {
										  i.printStackTrace();
									  }
					
					   
				 				 	}
						   
				 			 }
						}
					}
					
				
	
		}
	
}
