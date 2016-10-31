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
	
	// Create the semester directory under the User folder
	public void createDirectory(){
		File d = new File(this.folder);
		d.mkdirs();
	}
	
	public Course addCourse(String courseName){
		Course course = new Course(courseName, this.semesterName, this.folder);
		course.createDirectory();
		return course;
	}
	
	public void deleteCourse(){
		// TODO Delete directory with course name
	}
	
	// Get all courses under the semester directory
	public String[] getCourses(){
		File file = new File(this.folder);
		courses = file.list();
		return courses;
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
