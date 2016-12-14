package dbclasses;

import constants.Constants;
/**
 * This class implements several methods to build the directory and file
 * paths in string form in order to improve the readability of the Database
 * code and to avoid hard-coding path names for each function.
 * 
 * @author Gabriel Miller
 * @version 1.0
 * @since 11/9/2016
 * 
 */
public class DatabasePathBuilder {

	/**
	 * Build path for semester directory.
	 * 
	 * @param semester specified semester
	 * @return file path for the specified semester in the working directory
	 */
	public String buildPath(String semester){
		String path = Constants.directory + "/" + semester.replace(" ", "_");
		return path;
	}
	
	/**
	 * Build path for the course directory.
	 * 
	 * @param semester specified semester
	 * @param course specified course
	 * @return file path for the specified course in the working directory
	 */
	public String buildPath(String semester, String course){
		String path = Constants.directory + "/"  + 
					  semester.replace(" ", "_") + "/" + 
					  course  .replace(" ", "_");
		return path;
	}
	
	/**
	 * Build path for a specific file within a course directory.
	 * 
	 * @param semester specified semester
	 * @param course specified course
	 * @param file specified file name
	 * @return file path for the specified file name in the current course in the working directory
	 */
	public String buildPath(String semester, String course, String file){
		String path = Constants.directory + "/"  + 
				  	  semester.replace(" ", "_") + "/" + 
				  	  course  .replace(" ", "_") + "/" +
				  	  file;
		return path;
	}
}
