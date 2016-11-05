/*
 * Assignment.java
 * 
 * This class provides the data model for assignments, as well as the methods to 
 * create, delete, and update the text files that contain their information.
 * 
 * Gabriel Miller
 * 10/25/2016
 */
package dbclasses;

import java.util.List;

public class Assignment {

	private String		folder;
	private String      name;
	private List<Grade> grades;
	
	public Assignment(String name, List<Grade> grades){
		this.name   = name;
		this.grades = grades;
	}

	/*
	 * Getters and Setters
	 */
	
	public String getFolder() {
		return folder;
	}

	public void setFolder(String folder) {
		this.folder = folder;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Grade> getGrades() {
		return grades;
	}

	public void setGrades(List<Grade> grades) {
		this.grades = grades;
	}	
}
