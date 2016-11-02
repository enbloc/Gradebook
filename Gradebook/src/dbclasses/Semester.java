/*
 * Semester.java
 * 
 * This class provides the data model for semesters, as well as the methods to 
 * create, delete, and update the text files that contain their information.
 * 
 * Gabriel Miller
 * 10/25/2016
 */
package dbclasses;

import java.io.File;

public class Semester {

	private String   semesterName;
	private String   folder;
	private String[] courses;

	public Semester(String semesterName, String upFolder){
		this.semesterName = semesterName;
		this.folder       = upFolder + "/" + semesterName;
	}
	
	/*
	 * Getters and Setters
	 */
	public String getSemesterName() {
		return semesterName;
	}

	public void setSemesterName(String semesterName) {
		this.semesterName = semesterName;
	}

	public String getFolder() {
		return folder;
	}

	public void setFolder(String folder) {
		this.folder = folder;
	}
}
