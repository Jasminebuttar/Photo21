package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.image.Image;

/**
 * Album class is the model for album
 * Album implements Serializable interface.
 * @author      Jasmine Buttar
 * @author		Zalak Shingala
 */
public class Album implements Serializable {

	private String name;
	private List<Photo> photos;
	private Photo oldestPhoto;
	private Photo earliestPhoto;
	
	/**
	 * Album constructor that takes name as a parameter
	 * @param name		album's name
	 */
	public Album(String name) {
		this.name = name;
		oldestPhoto = null;
		earliestPhoto = null;
		photos = new ArrayList<Photo>();
	}
	
	/**
	 * void method setName sets the current current album name to the name given in paramter
	 * @param name		album's name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * return method getName returns the String of album name
	 * @return album's name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * void method addPhoto adds the photo to the album
	 * @param photo		selected photo that will be to the album
	 */
	public void addPhoto(Photo photo) {
		photos.add(photo);
		findOldestPhoto();
		findEarliestPhoto();
	}
	
	/**
	 * void method removePhoto takes the index as a parameter
	 * @param index		index of the photo being removed
	 */
	public void removePhoto(int index) {
		photos.remove(index);
		findOldestPhoto();
		findEarliestPhoto();
	}
	
	/**
	 * return method getPhoto takes the index as a paramter and returns Photo type respective to index
	 * @param index		index of photo in the list
	 * @return photo at the given index
	 */
	public Photo getPhoto(int index) {
		return photos.get(index);
	}
	
	/**
	 * return  method getCount counts the number of photos exist in the album
	 * @return number of photos in the album
	 */
	public int getCount() {
		return photos.size();
	}
	
	/**
	 * void method findOldestPhoto finds the least recent photo from the album
	 */
	public void findOldestPhoto() {
		if (photos.size() == 0)
			return;
		
		Photo temp = photos.get(0);
		
		for (Photo p : photos)
			if (p.getCalendar().compareTo(temp.getCalendar()) < 0)
				temp = p;
		
		oldestPhoto = temp;
	}
	
	/**
	 * void method findEarliestPhoto finds the most recent photo from the album
	 */
	public void findEarliestPhoto() {
		if (photos.size() == 0)
			return;
		
		Photo temp = photos.get(0);
		
		for (Photo p : photos)
			if (p.getCalendar().compareTo(temp.getCalendar()) > 0)
				temp = p;
		
		earliestPhoto = temp;
	}
	
	/**
	 * return method getOldestPhotoDate gets the date of least recent photo
	 * @return returns the date of least recent photo from the album
	 */
	public String getOldestPhotoDate() {
		if (oldestPhoto == null)
			return "NA";
		return oldestPhoto.getDate();
	}
	
	/**
	 * return method getEarliestPhotoDate gets the date of most recent photo
	 * @return returns the date of most recent photo from the album
	 */
	public String getEarliestPhotoDate() {
		if (earliestPhoto == null)
			return "NA";
		return earliestPhoto.getDate();
	}
	
	/**
	 * return method getDateRange gets the date range of the photos in the album
	 * @return returns the date range of least recent and most recent photo
	 */
	public String getDateRange() {
		return getOldestPhotoDate() + " - " + getEarliestPhotoDate();
	}
	
	/**
	 * return method getAlbumPhoto gets the album's main photo
	 * @return sets the album's main photo to the first picture in the album
	 */
	public Image getAlbumPhoto() {
		if (photos.isEmpty())
			return null;
		return photos.get(0).getImage();
	}
	
	/**
	 * return method getPhotos gets the list of photos exist in the album
	 * @return photo list of the album
	 */
	public List<Photo> getPhotos() {
		return photos;
	}
	
	/**
	 * return method findIndexByPhoto finds the index of photo by photo
	 * @param photo		selected photo
	 * @return  index of the selected photo
	 */
	public int findIndexByPhoto(Photo photo) {
		for (int i = 0; i < photos.size(); i++)
			if (photos.get(i).equals(photo))
				return i;
		return -1;
	}
	
}
