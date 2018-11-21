package model;

import java.io.Serializable;

/**
 * Tag class implements Serializable interface
 * it has two attributes: type and value for the photo
 * @author     Jasmine Buttar
 * @author		Zalak Shingala
 */
public class Tag implements Serializable {

	private String type, value;
	
	/**
	 * Tag Constructor takes type and value as a parameter and assigns them to the current object's tag and value
	 * @param type		 tag type
	 * @param value		 tag value
	 */
	public Tag(String type, String value) {
		this.type = type;
		this.value = value;
	}
	
	/**
	 * return method getType gets the type of the tag
	 * @return type		 tag type
	 */
	public String getType() {
		return type;
	}
	
	/**
	 * return method getValue gets the value of the tag
	 * @return value	 tag value
	 */
	public String getValue() {
		return value;
	}
	
	/**
	 * void method setType sets the current object's type to the type passed
	 * @param type		tag type
	 */
	public void setType(String type) {
		this.type = type;
	}
	
	/**
	 * void method setValue sets the current object's value to the value passed
	 * @param value		tag value
	 */
	public void setValue(String value) {
		this.value = value;
	}
	
	/**
	 * return method toString returns the String to be displayed
	 * @return string of the form "type: value"
	 */
	public String toString() {
		return type + ": " + value;
	}
	
	/**
	 * return method hashCode to store the tags in the hash set
	 */
	@Override
	public int hashCode() {
		return value.hashCode()+type.hashCode();
	}
	
	/**
	 * boolean method equals overrides the equals method from search results
	 * @return boolean value whether the object is equal to the tag
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj==null || !(obj instanceof Tag))
			   return false;

		Tag t =(Tag ) obj;

        return t.getValue().equals(value) && t.getType().equals(type);
	}
	
}
