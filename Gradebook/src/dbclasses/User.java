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

import java.io.File;

public class User {
	
	private String   username;
	private String   folder;
	private String[] semesters;

	public User(String username, String folder){
		this.username = username;
		this.folder   = folder;
	}
	
	// Create a new semester under the user directory
	public Semester addSemester(String semesterName){
		Semester semester = new Semester(semesterName, this.folder);
		semester.createDirectory();
		return semester;
	}
	
	public void deleteSemester(String semesterName){
		// TODO Delete semester directories
	}
	
	// Get list of semesters under the user directory
	public String[] getSemesters(){
		File file = new File(this.folder);
		semesters = file.list();
		return semesters;
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

	public void setSemesters(String[] semesters) {
		this.semesters = semesters;
	}
	
	
}
