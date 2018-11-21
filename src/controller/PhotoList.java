package controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.imageio.ImageIO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Album;
import model.Photo;
import model.SerializableImage;
import model.User;
import model.UserList;

/**
 * PhotoListController is the controller that controls the photo list
 * this class implements Error and Logout interface
 * @author Jasmine Buttar
 * @author Zalak Shingala
 */
public class PhotoList implements Error, Logout {

	@FXML
	ListView<Photo> photoListView;
	
	@FXML
	Text albumNameText;
	
	private ObservableList<Photo> obsL;
	private List<Photo> photoL;
	private Album album;
	private User user;
	private UserList userL;
	
	/**
	 * void method start initializes everything in the album at the start
	 * @param mainStage		main stage
	 */
	public void start(Stage mainStage) {
		
		albumNameText.setText(album.getName());
		
		photoL = album.getPhotos();
		
		obsL = FXCollections.observableArrayList(photoL);
		
		photoListView.setCellFactory(new Callback<ListView<Photo>, ListCell<Photo>>(){
			
			@Override
			public ListCell<Photo> call(ListView<Photo> p) {
				
				return new PhotoCell();
			}
		});	
		
		photoListView.setItems(obsL);
	
	}
	@FXML
	/**
	 * void method deleteAlbum deletes the album after Delete button is clicked but before that it confirms if the user wants to change his decision or not 
	 * @param event		when "Delete" button for album is clicked
	 */
	protected void deleteAlbum(ActionEvent event) {
      	  Alert alert = 
 				   new Alert(AlertType.INFORMATION);
 		   alert.setTitle("Delete Album");
 		   alert.setHeaderText(
 				   "Delete");

 		   String content = "Are you sure that you want to delete " + album.getName() + "?";

 		   alert.setContentText(content);

 		   Optional<ButtonType> result = alert.showAndWait();
 		   
 		  if(result.isPresent())
 		  {
 			  user.removeAlbum(album);
 		  
			try {
				UserList.write(userL);
				backToAlbums(event);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
 		  }
		

	}
	
	/**
	 * backToAlbums
	 * On back button press, go back to NonAdminHome screen
	 * @param event		back button press
	 * @throws ClassNotFoundException 	Exception for switching scenes
	 */
	@FXML
	protected void backToAlbums(ActionEvent event) throws ClassNotFoundException {
		Parent parent;
   	 
		try {
				
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/UserHome.fxml"));
	        parent = (Parent) loader.load();
	        AlbumList ctrl = loader.<AlbumList>getController();
	        //send user index to album list controller
	        ctrl.setUser(user);
	        ctrl.setUlist(userL);
	        Scene scene = new Scene(parent);
	        
	        Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();	
	        
			ctrl.start(app_stage);
	         
			app_stage.setScene(scene);
			app_stage.show();
		
		} catch (IOException e) {
			e.printStackTrace();
		}   
	}
	
	/**
	 * void method choosePhoto lets the user choose either jpg or png file to upload
	 * @param event		when "Add Photo" button is clicked
	 * @throws IOException		for to show filechooser
	 */
	@FXML
	protected void choosePhoto(ActionEvent event) throws IOException {
		FileChooser fileChooser = new FileChooser();
		
		FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
        FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
        fileChooser.getExtensionFilters().addAll(extFilterJPG, extFilterPNG);
		
		fileChooser.setTitle("Upload Photo");
		Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		File file = fileChooser.showOpenDialog(app_stage);
		
		if (file == null)
			return;
		
        BufferedImage bufferedImage = ImageIO.read(file);
        Image image = SwingFXUtils.toFXImage(bufferedImage, null);
        
        //check to see if this photo exists in the album
        SerializableImage tempImage = new SerializableImage();
        tempImage.setImage(image);
        for (Photo p: album.getPhotos()) {
        	if (tempImage.equals(p.getSerializableImage())) {
        		displayError("This photo already exists in this album. Please try another one.");
        		return;
        	}
        }
        
        Photo tempPhoto = null;
        boolean photoFound = false;
        
        for (Album a: user.getAlbums()) {
        	for (Photo p: a.getPhotos()) {
        		if (tempImage.equals(p.getSerializableImage())) {
        			tempPhoto = p;
        			photoFound = true;
        			System.out.println("Found the photo");
        			break;
        		}
        		if (photoFound)
        			break;
        	}
        }
        
        if (!photoFound)
        	tempPhoto = new Photo(image);
        
        
        
        album.addPhoto(tempPhoto);
        obsL.add(tempPhoto);
		UserList.write(userL);
		
	}
	
	
	/**
	 * void method handleLogoutButton signs the user out when the "Logout" button is clicked and returns to the first scene which is Login.fxml
	 * @param event		when the "Logout" button is clicked
	 * @throws ClassNotFoundException	for scene switching
	 */
	@FXML 
	protected void handleLogoutButton(ActionEvent event) throws ClassNotFoundException {
    	logoutAction(event);      
	}
	
	/**
	 * PhotoCell is a class that helps to display the photo list in the album
	 * @author Jasmine Buttar
	 * @author Zalak Shingala
	 */
	private class PhotoCell extends ListCell<Photo> {
			
		AnchorPane apane = new AnchorPane();
		StackPane spane = new StackPane();
		
		ImageView imageView = new ImageView();
		Label captionLabel = new Label();
		Button deletePhotoBtn = new Button("Delete");
		Button copyPhotoBtn = new Button("Copy");
		Button editPhotoBtn = new Button("Edit");
		Button movePhotoBtn = new Button("Move");
		Button viewPhotoBtn = new Button("View");
			
		public PhotoCell() {
			super();
		
			imageView.setFitWidth(45.0);
			imageView.setFitHeight(45.0);
			imageView.setPreserveRatio(true);
			
			StackPane.setAlignment(imageView, Pos.CENTER);
			
			spane.getChildren().add(imageView);
			
			spane.setPrefHeight(55.0);
			spane.setPrefWidth(45.0);
			
			AnchorPane.setLeftAnchor(spane, 0.0);
			
			AnchorPane.setLeftAnchor(captionLabel, 55.0);
			AnchorPane.setTopAnchor(captionLabel, 0.0);
			
			AnchorPane.setRightAnchor(deletePhotoBtn, 0.0);
			AnchorPane.setBottomAnchor(deletePhotoBtn, 0.0);
			
			AnchorPane.setLeftAnchor(editPhotoBtn, 55.0);
			AnchorPane.setBottomAnchor(editPhotoBtn, 0.0);
			
			AnchorPane.setRightAnchor(movePhotoBtn, 70.0);
			AnchorPane.setBottomAnchor(movePhotoBtn, 0.0);
			
			AnchorPane.setRightAnchor(copyPhotoBtn, 130.0);
			AnchorPane.setBottomAnchor(copyPhotoBtn, 0.0);
			
			AnchorPane.setLeftAnchor(viewPhotoBtn, 115.0);
			AnchorPane.setBottomAnchor(viewPhotoBtn, 0.0);
			
			deletePhotoBtn.setVisible(false);
			editPhotoBtn.setVisible(false);
			movePhotoBtn.setVisible(false);
			viewPhotoBtn.setVisible(false);
			copyPhotoBtn.setVisible(false);
			
			apane.getChildren().addAll(spane, captionLabel,
					deletePhotoBtn, editPhotoBtn, copyPhotoBtn, movePhotoBtn, viewPhotoBtn);
			
			apane.setPrefHeight(55.0);
			
			captionLabel.setMaxWidth(300.0);
			
			setGraphic(apane);
		}
		
		/**
		 * void method updateItem updates the Photo details
		 */
		@Override
		public void updateItem(Photo photo, boolean empty) {
			super.updateItem(photo, empty);
			setText(null);
			if(photo == null)
			{
				imageView.setImage(null);
				captionLabel.setText("");
				deletePhotoBtn.setVisible(false);
				editPhotoBtn.setVisible(false);
				copyPhotoBtn.setVisible(false);
				movePhotoBtn.setVisible(false);
				viewPhotoBtn.setVisible(false);
			}
			if (photo != null) {
				imageView.setImage(photo.getImage());
				captionLabel.setText("Caption: " + photo.getCaption());
				deletePhotoBtn.setVisible(true);
				editPhotoBtn.setVisible(true);
				copyPhotoBtn.setVisible(true);
				movePhotoBtn.setVisible(true);
				viewPhotoBtn.setVisible(true);
				
				deletePhotoBtn.setOnAction(new EventHandler<ActionEvent>() {
					@Override public void handle(ActionEvent e) {
						deletePhoto(e, photo);
					}
				});
				
				editPhotoBtn.setOnAction(new EventHandler<ActionEvent>() {
					@Override public void handle(ActionEvent e) {
						recaption(e, photo);
					}
				});
				
				movePhotoBtn.setOnAction(new EventHandler<ActionEvent>() {
					@Override public void handle(ActionEvent e) {
						movePhoto(e, photo);
					}
				});
				
				copyPhotoBtn.setOnAction(new EventHandler<ActionEvent>() {
					@Override public void handle(ActionEvent e) {
						copyPhoto(e, photo);
					}
				});
				
				viewPhotoBtn.setOnAction(new EventHandler<ActionEvent>() {
					@Override public void handle(ActionEvent e) {
						try {
							viewPhoto(e, photo);
						} catch (ClassNotFoundException | IOException e1) {
							e1.printStackTrace();
						} 
					}
				});
			}	
		}
		
		/**
		 * deletePhoto
		 * Delete the photo, given an alert
		 * @param event		when the "Delete" button is clicked for the photo
		 * @param photo		photo to delete
		 */
		public void deletePhoto(ActionEvent event, Photo photo) {
			Alert alert = 
	 				   new Alert(AlertType.INFORMATION);
	 		   alert.setTitle("Delete photo");
	 		   alert.setHeaderText(
	 				   "Delete");

	 		   String content = "Are you sure that you want to delete this photo from the album?";

	 		   alert.setContentText(content);

	 		   Optional<ButtonType> result = alert.showAndWait();
	 		   
	 		  if(result.isPresent())
	 		  {
	 			 int i = album.findIndexByPhoto(photo);
	 			 album.removePhoto(i);
	 			 obsL.remove(photo);
	 		  
				try {
					UserList.write(userL);
				
				} catch (Exception e) {
					e.printStackTrace();
				}
				
	 		  }
		}
		
		/**
		 * void method recaption lets the user to edit the caption of the photo
		 * Edit the photo, given an edit dialog
		 * @param e			when the "Edit" button is clicked for the photo
		 * @param photo		selected photo
		 */
		public void recaption(ActionEvent e, Photo photo) {
			
			Dialog<Album> dialog = new Dialog<>();
			dialog.setTitle("Edit Caption");
			dialog.setHeaderText("Editing the caption for this photo.");
			dialog.setResizable(true);
			   
			Label captionLabel = new Label("Caption: ");
			TextArea captionTextArea = new TextArea();
			captionTextArea.setText(photo.getCaption());
			   
			GridPane grid = new GridPane();
			grid.add(captionLabel, 1, 1);
			grid.add(captionTextArea, 2, 1);
			   
			dialog.getDialogPane().setContent(grid);
			captionTextArea.setWrapText(true);
			ButtonType buttonTypeOk = new ButtonType("Ok", ButtonData.OK_DONE);
			dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);
			   
			   Optional<Album> result = dialog.showAndWait();
			   
					if (result.isPresent()) {
					   photo.setCaption(captionTextArea.getText());
					  updateItem(photo, true);
					  try{
						  UserList.write(userL);
					  }
					  catch(Exception i)
					  {
						  i.printStackTrace();
					  }
				   
				   }
					   

		}
		
		/**
		 * void method movePhoto lets the user to move the photo from one album to another album
		 * deletes the moved photo from the current album
		 * @param e			when "Move" button is clicked for the photo
		 * @param photo		photo to be moved to another album
		 */
		public void movePhoto(ActionEvent e, Photo photo) {
			
			Dialog<Album> dialog = new Dialog<>();
			dialog.setTitle("Move Photo");
			dialog.setHeaderText("Move this photo to another album");
			dialog.setResizable(true);
			   
			Label moveLabel = new Label("Album to move this photo to: ");
			
			List<String> albumNames = new ArrayList<String>();
			for(Album a : user.getAlbums())
			{
				String temp = a.getName();
				if(a.getName()!= album.getName())
					albumNames.add(temp);
			}
			
			ObservableList<String> options = 
			FXCollections.observableArrayList(albumNames);
			
			
			ComboBox<String> moveComboBox = new ComboBox<String>(options);
	    
			   
			GridPane grid = new GridPane();
			grid.add(moveLabel, 1, 1);
			grid.add(moveComboBox, 1, 2);
			   
			dialog.getDialogPane().setContent(grid);
			   
			ButtonType buttonTypeOk = new ButtonType("Move", ButtonData.OK_DONE);
			dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);
			   
			//wait for response from move button
			   Optional<Album> result = dialog.showAndWait();
			 
			   //If user presses move
					if (result.isPresent()) {
						String newAlbumName = moveComboBox.getSelectionModel().getSelectedItem();
						Album newAlbum = user.getAlbumByName(newAlbumName);
						newAlbum.addPhoto(photo);
						obsL.remove(photo);
						int index = album.findIndexByPhoto(photo);
						album.removePhoto(index);
						
						//updateAlbumDetails();
					  try{
						  UserList.write(userL);
					  }
					  catch(Exception i)
					  {
						  i.printStackTrace();
					  }
				   
				   }
					   

			
		}
/**
 * void method copyPhoto lets the user copy the photo from one album to another
 * does not delete the photo in the current album
 * @param e		when "Copy" button is clicked for the photo
 * @param photo		photo to be copied to another album
 */
public void copyPhoto(ActionEvent e, Photo photo) {
			
			Dialog<Album> dialog = new Dialog<>();
			dialog.setTitle("Copy Photo");
			dialog.setHeaderText("Copy this photo to another album");
			dialog.setResizable(true);
			   
			Label copyLabel = new Label("Album to copy this photo to: ");
			
			List<String> albumNames = new ArrayList<String>();
			for(Album a : user.getAlbums())
			{
				String temp = a.getName();
				if(a.getName()!= album.getName())
					albumNames.add(temp);
			}
			
			ObservableList<String> options = 
			FXCollections.observableArrayList(albumNames);
			
			
			ComboBox<String> copyComboBox = new ComboBox<String>(options);
	    
			   
			GridPane grid = new GridPane();
			grid.add(copyLabel, 1, 1);
			grid.add(copyComboBox, 1, 2);
			   
			dialog.getDialogPane().setContent(grid);
			   
			ButtonType buttonTypeOk = new ButtonType("Copy", ButtonData.OK_DONE);
			dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);
			   
			   Optional<Album> result = dialog.showAndWait();
			 
					if (result.isPresent()) {
						String newAlbumName = copyComboBox.getSelectionModel().getSelectedItem();
						Album newAlbum = user.getAlbumByName(newAlbumName);
						newAlbum.addPhoto(photo);
					
					  try{
						  UserList.write(userL);
					  }
					  catch(Exception i)
					  {
						  i.printStackTrace();
					  }
				   
				   }
					   

			
		}
		
		/**
		 * void method viewPhoto lets the user to view the selected photo
		 * @param e		when the "View" button is clicked for the photo
		 * @param photo		photo to be displayed
		 * @throws IOException		for scene switching
		 * @throws ClassNotFoundException		for scene switching
		 */
		public void viewPhoto(ActionEvent e, Photo photo) throws IOException, ClassNotFoundException {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/PhotoView.fxml"));
	        Parent parent = (Parent) loader.load();
	        PhotoDisplay ctrl = loader.<PhotoDisplay>getController();
	        ctrl.setPhotoIndex(album.findIndexByPhoto(photo));
	        ctrl.setAlbum(album);
	        ctrl.setUser(user);
	        ctrl.setUlist(userL);
	        ctrl.setKey(PhotoDisplay.CAME_FROM_ALBUM_CONTENT);
	        Scene scene = new Scene(parent);
	        
	        Stage app_stage = (Stage) ((Node) e.getSource()).getScene().getWindow();	
	        
			ctrl.start(app_stage);
	         
			app_stage.setScene(scene);
			app_stage.show();
		}
	}
	
	/**
	 * void method setAlbum sets the album
	 * @param a		album to be set
	 */
	public void setAlbum(Album a) {
		album = a;
	}
	
	/**
	 * void method setUser sets the user
	 * @param u		user to be set
	 */
	public void setUser(User u) {
		user = u;
	}
	
	/**
	 * void method setUList sets the user's list
	 * @param ulist		user's list to be set
	 */
	public void setUlist(UserList ulist) {
		this.userL = ulist;
	}
	
}
