package controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Album;
import model.Photo;
import model.Tag;
import model.User;
import model.UserList;

/**
 * PhotoSearchController helps the user to control the photo search functionality 
 * PhotoSearchController implements Error and Logout interface.
 * @author Jasmine Buttar
 * @author Zalak Shingala
 */
public class PhotoSearch implements Error, Logout {
	
	@FXML
	Button createAlbumBtn;
	
	@FXML
	TextField tagValueTextField, tagTypeTextField;
	
	@FXML
	DatePicker fromDate, toDate;
	
	@FXML
	TableView<Tag> tagTable;
	
	@FXML
	TableColumn<Tag,String> tagTypeColumn, tagValueColumn;
	
	@FXML
	TableColumn<Tag,Tag> tagDeleteColumn;
	
	@FXML
	ListView<Photo> photoListView;
	
	private ObservableList<Photo> photoObsList;
	private ObservableList<Tag> tagObsList;
	private User user;
	private List<Photo> photos;
	private List<Tag> tags;
	private UserList ulist;
	
	/**
	 * initializes everything for start of the application
	 * @param mainStage		The main stage
	 */
	public void start(Stage mainStage) {
		tags = new ArrayList<Tag>();
		tagObsList = FXCollections.observableArrayList(tags);
		
		photos = getAllPhotos();
		
		createAlbumBtn.setDisable(photos.isEmpty());
		
		photoObsList = FXCollections.observableArrayList(photos);
		
		photoListView.setCellFactory(new Callback<ListView<Photo>, ListCell<Photo>>(){
			
			@Override
			public ListCell<Photo> call(ListView<Photo> p) {
				
				return new SearchPhotoCell();
			}
		});	
		
		tagTypeColumn.setCellValueFactory(new Callback<CellDataFeatures<Tag, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<Tag, String> t) {
		    	 return new SimpleStringProperty(t.getValue().getType());
		    }
		});
		
		tagValueColumn.setCellValueFactory(new Callback<CellDataFeatures<Tag, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<Tag, String> t) {
		    	 return new SimpleStringProperty(t.getValue().getValue());
		    }
		});
		
		tagDeleteColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		  tagDeleteColumn.setCellFactory(param -> new TableCell<Tag,Tag>() {
	            private final Button deleteButton = new Button("Delete");

	            @Override
	            protected void updateItem(Tag tag, boolean empty) {
	                super.updateItem(tag, empty);
	                if (tag == null) {
	                    setGraphic(null);
	                    return;
	                }
	                setGraphic(deleteButton);
	                deleteButton.setOnAction(event -> {
	                									tagObsList.remove(tag);
	                									tags.remove(tag);});
	            }
		  });
		  
		  tagTable.setItems(tagObsList);
		
		
	}
	
	@FXML
	protected void addTag() {
		String tagType = tagTypeTextField.getText().trim();
		String tagValue = tagValueTextField.getText().trim();
		
		if (tagType.isEmpty()) {
			displayError("You need to include a tag type!");
			return;
		}
		if (tagValue.isEmpty()) {
			displayError("You need to include a tag value!");
			return;
		}
		
		tagTypeTextField.setText("");
		tagValueTextField.setText("");
		
		boolean alreadyContained = false;
		
		for (Tag t: tags) {
			if (t.getType().equals(tagType) && t.getValue().equals(tagValue)) {
				alreadyContained = true;
				break;
			}
		}
		
		Tag tempTag = new Tag(tagType, tagValue);
		
		if (alreadyContained)  {
			displayError("You can't search for duplicate tags!");
			return;
		}
		
		tagObsList.add(tempTag);
		tags.add(tempTag);
	}
	
	/**
	 * return method getAllPhotos gets all the photos
	 * @return List of all photos
	 */
	public List<Photo> getAllPhotos() {
		List<Photo> photos = new ArrayList<Photo>();
		
		List<Album> albums = user.getAlbums();
		
		for(int a = 0; a < albums.size(); a++)
			for (Photo p: albums.get(a).getPhotos())
				if (!photos.contains(p))
					photos.add(p);
		
		return photos;
	}
	
	@FXML
	protected void clearSearchResults() {
		photoObsList.clear();
	}
	
	/**
	 * createAlbumFromResults helps the user to create an album from the search results and also opens a dialog to ask for album name
	 * @param e		when createAlbumFromResults button clicked
	 * @throws IOException		 for dialog
	 */
	@FXML
	protected void createAlbumFromResults(ActionEvent e) throws IOException {
		Dialog<Album> dialog = new Dialog<>();
		dialog.setTitle("Create a New Album");
		dialog.setHeaderText("Add a new album to Photo Album!");
		dialog.setResizable(true);
		   
		Label albumLabel = new Label("Album Name: ");
		TextField albumTextField = new TextField();
		albumTextField.setPromptText("Album Name");
		   
		GridPane grid = new GridPane();
		grid.add(albumLabel, 1, 1);
		grid.add(albumTextField, 2, 1);
		   
		dialog.getDialogPane().setContent(grid);
		   
		ButtonType buttonTypeOk = new ButtonType("Add", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);
		   
		dialog.setResultConverter(new Callback<ButtonType, Album>() {
			@Override
			public Album call(ButtonType b) {
				if (b == buttonTypeOk) {
					   
					String error = checkFields(albumTextField.getText());
					   
					if (error != null) {
						displayError(error);
						return null;
					}
											   
					return new Album(albumTextField.getText().trim());
				}
				return null;
			}

			
		});
		   
		Album tempAlbum = null;
		
		Optional<Album> result = dialog.showAndWait();
		   
		if (result.isPresent()) {
			tempAlbum = (Album) result.get(); 
			user.getAlbums().add(tempAlbum);
			
			for (Photo p: photos) {
				tempAlbum.addPhoto(p);
			}
			
			UserList.write(ulist);
		}
	}
	
	/**
	 * return method checkFields helps the user to check fields if they are not filled, or same one already exists
	 * @param albumName		album name to be checked
	 * @return error message in string format
	 */
	private String checkFields(String albumName) {
		if (albumName.trim().isEmpty())
			return "Please enter Album Name";
		if (user.albumNameExists(albumName))
			return albumName + " already exists.";
		else
			return null;
	}
	
	/**
	 * void method searchPhotos helps the user to search the photos based on the list of tags and the date range
	 * @param e		when "Search" button is clicked
	 */
	@FXML
	protected void searchPhotos(ActionEvent e) {
		List<Photo> allPhotos = getAllPhotos();
		LocalDate from, to;
		
		//cannot search of both tags and date is empty
		if (tags.isEmpty() && fromDate.getValue() == null && toDate.getValue() == null) {
			return;
		}
		if (fromDate.getValue() != null && toDate.getValue() != null && fromDate.getValue().isAfter(toDate.getValue())) {
			displayError("Pick the dates in chronological order!");
		}
			
		if (fromDate.getValue() == null)
			from = LocalDate.MIN;
		else
			from = fromDate.getValue();
		if (toDate.getValue() == null)
			to = LocalDate.MAX;
		else
			to = toDate.getValue();
		
		photos.clear();
		photoObsList.clear();
		
		for (Photo p: allPhotos) {
			if (tags.isEmpty()) {
				if (p.isWithinDateRange(from, to)) {
					photos.add(p);
					photoObsList.add(p);
				}
			}
			else {
				if (p.hasSubset(tags) && p.isWithinDateRange(from, to)) {
					photos.add(p);
					photoObsList.add(p);
				}
			}
		}
		photoListView.setItems(photoObsList);
		createAlbumBtn.setDisable(photos.isEmpty());
		
	}
	
	
	/**
	 * backToAlbums redirects the screen back to album list screen
	 * @param event		when "Back" button is clicked
	 * @throws ClassNotFoundException 		for scene switching
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
	        ctrl.setUlist(ulist);
	        Scene scene = new Scene(parent);
	        
	        Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();	
	        
			ctrl.start(app_stage);
	         
			app_stage.setScene(scene);
			app_stage.show();
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   
	}
	
	/**
	 * setUser lets the controller know which user is logged in
	 * @param u		user to be set
	 */
	public void setUser(User u) {
		user = u;
	}
	
	/**
	 * sets user list
	 * @param ulist		user's list to be set
	 */
	public void setUlist(UserList ulist) {
		this.ulist = ulist;
	}
	
	/**
	 * SearchPhotoCell class that is a subclass of ListCell
	 * @author Jasmine Buttar
	 * @author Zalak Shingala
	 */
	private class SearchPhotoCell extends ListCell<Photo> {
			
		AnchorPane apane = new AnchorPane();
		StackPane spane = new StackPane();
		
		ImageView imageView = new ImageView();
		Label captionLabel = new Label();
		Button deletePhotoBtn = new Button("Delete");
		Button editPhotoBtn = new Button("Edit");
		Button viewPhotoBtn = new Button("View");
			
		public SearchPhotoCell() {
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
			
			AnchorPane.setLeftAnchor(editPhotoBtn, 55.0);
			AnchorPane.setBottomAnchor(editPhotoBtn, 0.0);
			
			AnchorPane.setLeftAnchor(viewPhotoBtn, 115.0);
			AnchorPane.setBottomAnchor(viewPhotoBtn, 0.0);
			
			editPhotoBtn.setVisible(false);
			viewPhotoBtn.setVisible(false);
			apane.getChildren().addAll(spane, captionLabel,
					editPhotoBtn, viewPhotoBtn);
			
			apane.setPrefHeight(55.0);
			
			captionLabel.setMaxWidth(300.0);
			
			setGraphic(apane);
		}
			
		@Override
		public void updateItem(Photo photo, boolean empty) {
			super.updateItem(photo, empty);
			setText(null);
			if(photo == null)
			{
				imageView.setImage(null);
				captionLabel.setText("");
				editPhotoBtn.setVisible(false);
				viewPhotoBtn.setVisible(false);
			}
			if (photo != null) {
				imageView.setImage(photo.getImage());
				captionLabel.setText("Caption: " + photo.getCaption());
				editPhotoBtn.setVisible(true);
				viewPhotoBtn.setVisible(true);
				
				editPhotoBtn.setOnAction(new EventHandler<ActionEvent>() {
					@Override public void handle(ActionEvent e) {
						editPhoto(e, photo);
					}
				});
				
				viewPhotoBtn.setOnAction(new EventHandler<ActionEvent>() {
					@Override public void handle(ActionEvent e) {
						try {
							viewPhoto(e, photo);
						} catch (ClassNotFoundException | IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} 
					}
				});
			}	
		}
		
		/**
		 * void method editPhoto helps the user to edit the caption
		 * @param e		when the "Edit" button for the caption of the photo is clicked
		 * @param photo		photo to be edited
		 */
		public void editPhoto(ActionEvent e, Photo photo) {
			
			Dialog<Album> dialog = new Dialog<>();
			dialog.setTitle("Edit Caption");
			dialog.setHeaderText("Edit the caption for the selected photo");
			dialog.setResizable(true);
			   
			Label captionLabel = new Label("Caption: ");
			TextArea captionTextArea = new TextArea();
			captionTextArea.setPromptText(photo.getCaption());
			   
			GridPane grid = new GridPane();
			grid.add(captionLabel, 1, 1);
			grid.add(captionTextArea, 2, 1);
			   
			dialog.getDialogPane().setContent(grid);
			   
			ButtonType buttonTypeOk = new ButtonType("Ok", ButtonData.OK_DONE);
			dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);
			   
			   Optional<Album> result = dialog.showAndWait();
			   
					if (result.isPresent()) {
					   photo.setCaption(captionTextArea.getText());
					  updateItem(photo, true);
					  try{
						  UserList.write(ulist);
					  }
					  catch(Exception i)
					  {
						  i.printStackTrace();
					  }
				   
				   }
					   

		}
		
		/**
		 * void method viewPhoto helps user to view the photo by PhotoDisplayController and PhotoView.fxml
		 * @param e		when the "View" button is clicked for the phtoo
		 * @param photo		photo to be viewed
		 * @throws IOException		for scene switching
		 * @throws ClassNotFoundException		for scene switching
		 */
		public void viewPhoto(ActionEvent e, Photo photo) throws IOException, ClassNotFoundException {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/PhotoView.fxml"));
	        Parent parent = (Parent) loader.load();
	        PhotoDisplay ctrl = loader.<PhotoDisplay>getController();
	        //send user index to album list controller
	        ctrl.setPhotoIndex(photos.indexOf(photo));
	        ctrl.setPhotos(photos);
	        ctrl.setUser(user);
	        ctrl.setUlist(ulist);
	        ctrl.setKey(PhotoDisplay.CAME_FROM_PHOTO_SEARCH);
	        Scene scene = new Scene(parent);
	        
	        Stage app_stage = (Stage) ((Node) e.getSource()).getScene().getWindow();	
	        
			ctrl.start(app_stage);
	         
			app_stage.setScene(scene);
			app_stage.show();
		}
	}
	
}
