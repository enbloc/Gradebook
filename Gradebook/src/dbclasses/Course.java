/*
 * Course.java
 * 
 * This class provides the data model for courses, as well as the methods to 
 * create, delete, and update the text files that contain their information.
 * 
 * Gabriel Miller
 * 10/25/2016
 */
package dbclasses;

public class Course {

	private String   courseName;
	private String   semesterName;
	private String   folder;
	private String[] students;
	private String[] assignments;
	
	public Course (String courseName, String semesterName, String upFolder){
		this.courseName   = courseName;
		this.semesterName = semesterName;
		this.folder       = upFolder + "/" + courseName;
	}

	/*
	 * Getters and Setters
	 */
	
	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

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

	public String[] getStudents() {
		return students;
	}

	public void setStudents(String[] students) {
		this.students = students;
	}

	public String[] getAssignments() {
		return assignments;
	}

	public void setAssignments(String[] assignments) {
		this.assignments = assignments;
	}
}
