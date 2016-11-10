/*
 * DatabasePathBuilder.java
 * 
 * This class implements several methods to build the directory and file
 * paths in string form in order to improve the readability of the Database
 * code and to avoid hard-coding path names for each function.
 * 
 * Gabriel Miller
 * 11/9/2016
 * 
 */
package dbclasses;

import constants.Constants;

public class DatabasePathBuilder {

	// Build path for semester directory
	public String buildPath(String semester){
		String path = Constants.directory + "/" + semester.replace(" ", "_");
		return path;
	}
	
	// Build path for course directory
	public String buildPath(String semester, String course){
		String path = Constants.directory + "/"  + 
					  semester.replace(" ", "_") + "/" + 
					  course  .replace(" ", "_");
		return path;
	}
	
	// Build path for a specific file within a course directory
	public String buildPath(String semester, String course, String file){
		String path = Constants.directory + "/"  + 
				  	  semester.replace(" ", "_") + "/" + 
				  	  course  .replace(" ", "_") + "/" +
				  	  file;
		return path;
	}
}
