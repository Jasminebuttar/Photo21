package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * User class is a model for the user
 * User model implements Serializable interface
 * 
 * @author      Jasmine Buttar
 * @author		Zalak Shingala
 */
public class User implements Serializable {

	private String name, username, password;
	private List<Album> albums;

	

	/**
	 * User constructor that take name, username and password as the parameter and assigns it to the current object's attributes
	 * @param name		user's name
	 * @param username	account's username
	 * @param password	account's password
	 */
	public User(String name, String username, String password) {
		this.name = name;
		this.username = username;
		this.password = password;
		albums = new ArrayList<Album>();
	}
	
	/**
	 * return method getName which returns the user's name in string format
	 * @return user's name 
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * return method getUsername gets the username of the user
	 * @return account's username
	 */
	public String getUsername() {
		return username;
	}
	
	/**
	 * return method getPassword get the password of the account
	 * @return account's password
	 */
	public String getPassword() {
		return password;
	}
	
	/**
	 * Changes the user's password to a new one
	 * @param password		the new password
	 */
	public void changePassword(String password) {
		this.password = password;
	}
	
	/**
	 * return method getAlbum gets the list of albums exist
	 * @return album list
	 */
	public List<Album> getAlbums() {
		return albums;
	}
	
	/**
	 * void method addAlbum creates a new album and adds it to the list
	 * @param albumName  album's name
	 */
	public void addAlbum(String albumName) {
		albums.add(new Album(albumName));
	}
	
	/**
	 * void method addAlbum adds the existing to the album list
	 * @param a - album that already exists
	 */
	public void addAlbum(Album a) {
		albums.add(a);
	}
	
	/**
	 * void method addPhotoToAlbum adds photo at the given index
	 * adds a photo object to an album at album index
	 * @param p 	 photo to be added
	 * @param albumIndex	 index of the album
	 */
	public void addPhotoToAlbum(Photo p, int albumIndex) {
		albums.get(albumIndex).addPhoto(p);
	}
	
	/**
	 * return method albumNameExists checks if the album already exists in the list
	 * @param albumName		album's name 
	 * @return boolean value of whether the album  with the same name is present in the album list or not
	 */
	public boolean albumNameExists(String albumName) {
		for (Album a: albums)
			if (a.getName().toLowerCase().equals(albumName.trim().toLowerCase()))
				return true;
		
		return false;
	}
	
	/**
	 * return method getAlbumIndexByAlbum gets the album's index by album object
	 * @param a		album object
	 * @return album's index respective to the given album object
	 */
	public int getAlbumIndexByAlbum(Album a) {
		for (int i = 0; i < albums.size(); i++)
			if (albums.get(i).getName().equals(a.getName()))
				return i;
		return -1;
	}
	
	
	/**
	 * return method getAlbumByName gets the album object by album's name
	 * @param name		album name
	 * @return album object respective to its name
	 */
	public Album getAlbumByName(String name) {
		for(Album a : albums)
		{
			if(a.getName().equals(name))
				return a;
		}
		return null;
	}
	
	/**
	 * void method removeAlbum removes the passed album from the album list
	 * delete indicated album from user's album list
	 * @param album		album to be removed
	 * 
	 */
	
	public void removeAlbum(Album album)
	{
		albums.remove(album);
	}
	
}
