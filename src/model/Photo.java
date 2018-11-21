package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javafx.scene.image.Image;


/**
 * Photo class is a model for photo
 * Photo implements Serializable interface
 * @author      Jasmine Buttar
 * @author		Zalak Shingala
 */
public class Photo implements Serializable {

	private SerializableImage image;
	private String caption;
	private List<Tag> tags;
	private Calendar cal;
	
	/**
	 * no-arg Photo constructor which iitializes, caption, tags, cal, and image
	 */
	public Photo() {
		caption = "";
		tags = new ArrayList<Tag>();
		cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0);
		image = new SerializableImage();
	}
	
	/**
	 * Photo contructor  that takes an image as a parameter
	 * @param i		image
	 */
	public Photo(Image i) {
		this();
		image.setImage(i);
	}
	
	
	
	/**
	 * void method addTag adds the tag to the photo
	 * @param type		tag type
	 * @param value		tag value
	 */
	public void addTag(String type, String value) {
		tags.add(new Tag(type, value));
	}
	
	/**
	 * void method editTag edits the tag value or tyoe at the provided index
	 * @param index		indez of the tag in tag's list
	 * @param type		tag type
	 * @param value		tag value
	 */
	public void editTag(int index, String type, String value) {
		tags.get(index).setType(type);
		tags.get(index).setValue(value);
	}
	
	/**
	 * void method removeTag removes the tag from the tag list of provided indez
	 * @param index		index of the tag to be removed from the tag list
	 */
	public void removeTag(int index) {
		tags.remove(index);
	}
	
	/**
	 * return method gets the tag from the provided index
	 * @param index		index of the tag from tag list
	 * @return Tag object at provided index
	 */
	public Tag getTag(int index) {
		return tags.get(index);
	}
	
	/**
	 * void method setCaption assigns the caption to the passed string
	 * @param caption	caption String
	 */
	public void setCaption(String caption) {
		this.caption = caption;
	}
	
	/**
	 * return method getCaption returns the caption of the photo
	 * @return caption		photo caption
	 */
	public String getCaption() {
		return caption;
	}
	
	/**
	 * return method getCalender returns the Calendar object
	 * @return cal			photo's date
	 */
	public Calendar getCalendar() {
		return cal;
	}
	
	/**
	 * return method getDate returns String of the photo's date
	 * @return string 		string format for the date
	 */
	public String getDate() {
		String[] str = cal.getTime().toString().split("\\s+");
		return str[0] + " " + str[1] + " " + str[2] + ", " + str[5];
	}
	
	/**
	 * return method getImage returns an Image object
	 * @return image		photo
	 */
	public Image getImage() {
		return image.getImage();
	}
	
	/**
	 * return method getSerializableImage gets SerializableImage object
	 * @return The serializable image
	 */
	public SerializableImage getSerializableImage() {
		return image;
	}
	
	
	/**
	 * return method hasSubset checks if the given tlist is a subset of tags or not
	 * @param tlist		list to be compared
	 * @return boolean value of whether tlist is a subset of tags
	 */
	public boolean hasSubset(List<Tag> tlist) {
		Set<Tag> allTags = new HashSet<Tag>();
		allTags.addAll(tags);
		
		for (Tag t: tlist) {
			if (!allTags.contains(t))
				return false;
		}
		
		return true;
	}
	
	/**
	 * return method isWithinDateRange checks if the photo's date is within the range of fromDate and toDate
	 * @param fromDate	 from Local Date 
	 * @param toDate	 to Local Date
	 * @return boolean value of whether the photo date is within the date range or not
	 */
	public boolean isWithinDateRange(LocalDate fromDate, LocalDate toDate) {
		LocalDate date = cal.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		
		return date.isBefore(toDate) && date.isAfter(fromDate) || date.equals(fromDate) || date.equals(toDate);
	}
	
	/**
	 * return method getTags gets the list of tags
	 * @return entire list of tags of the photo
	 */
	public List<Tag> getTags() {
		return tags;
	}
	
}
