/*
 * User.java
 * 
 * Class that contains the information and settings for the application
 * user, as well as the directory for their stored information pertaining 
 * to courses, students, assignments, and other data.
 * 
 * Gabriel Miller
 * 10/25/16
 */
package dbclasses;

public class User {
	
	private String   username;
	private String   folder;
	private String[] semesters;

	public User(String username, String folder){
		this.username = username;
		this.folder   = folder;
	}

	/*
	 * Getters and Setters
	 */
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFolder() {
		return folder;
	}

	public void setFolder(String folder) {
		this.folder = folder;
	}

	public String[] getSemesters() {
		return semesters;
	}

	public void setSemesters(String[] semesters) {
		this.semesters = semesters;
	}
}
