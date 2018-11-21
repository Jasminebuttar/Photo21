
package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * UserList is a model of user's list 
 * UserList class implements Serializable interface
 * @author      Jasmine Buttar
 * @author		Zalak Shingala
 */

public class UserList implements Serializable {
	
	private static final long serialVersionUID = 9221355046218690511L;		
	public static final String storeDir = "dat";
	public static final String storeFile = "users.dat";
	
	private List<User> users;
	
	
	/**
	 * no-arg UserList contructor that sets the users to new arraylist of users
	 */
	public UserList() {
		users = new ArrayList<User>();
	}
	  
	  
	  /** 
	   * return method getUserList gets the entire list of users that exist
	   * @return 	entire list of users
	   */
	  public List<User> getUserList()
	  {
		  return users;
	  }
	  
	  /**
	   * void method addUserToList adds the passed user to the list
	   * @param u 		the use to be added to the user's list	    
	   */  
	  public void addUserToList(User u)
	  {
		  users.add(u);
	  }
	  
	   /**
	   * void method removeUserFromList removes the passed user from the list
	   * @param u		the user to be removed from the user's list	    
	   */
	  public void removeUserFromList(User u)
	  {
		  users.remove(u);
	  }
	  
	  /**
	   * return method isUserInList checks whether the passed username/password combination already exists in user's list
	   * @param un 		username of the user
	   * @param p 		password of the user	
	   * @return boolean 	boolean value of if username/password combination already exists   
	   */
	  public boolean isUserInList(String un, String p)
	  {
		  for(User u : users)
		  {
			  if (u.getUsername().equals(un) && u.getPassword().equals(p))
				  return true;
		  }
		  return false;
	  }
	  
	  /**
	   * return method userExists checks whether the passed username exists or not
	   * @param un 		username to check if it exists in the username's list
	   * @return boolean 	boolean value if the user exists or not    
	   */
	  public boolean userExists(String un)
	  {
		  for(User u : users)
		  {
			  if (u.getUsername().equals(un))
				  return true;
		  }
		  return false;
	  }
	  
	  /**
	   * return method getUserByUsername gets the user by username
	   * @param username	the user's username
	   * @return returns the user with the same username
	   */
	  public User getUserByUsername(String username) {
		  for (User u : users)
		  {
			  if (u.getUsername().equals(username))
				  return u;
		  }
		  return null;
	  }
	  
	  /**
	   * return method toString returns the String to be displayed of all usernames
	   * @return String of all user's usernames
	   */
	  public String toString() {
		  if (users == null)
			  return "no users";
		  String output = "";
		  for(User u : users)
		  {
			  output += u.getUsername() + " ";
		  }
		  return output;
	  }
	   
	  /**
	   * static return method read reads the .dat file and returns the user's list
	   * @return	user's list
	   * @throws IOException		for serialization
	   * @throws ClassNotFoundException		for serialization
	   */
	   public static UserList read() throws IOException, ClassNotFoundException {
		   ObjectInputStream ois = new ObjectInputStream(new FileInputStream(storeDir + File.separator + storeFile));
		   UserList ulist = (UserList) ois.readObject();
		   ois.close();
		   return ulist;
	   }
	   
	   /**
	    * static void method write overrides the .dat file 
	    * @param ulist	user's list
	    * @throws IOException	 for serialization
	    */
	   public static void write (UserList ulist) throws IOException {
		   ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(storeDir + File.separator + storeFile));
		   oos.writeObject(ulist);
		   oos.close();
	   }


}
