/*
 * Grade.java
 * 
 * This is a simple class used to make the implementation of Assignment class
 * methods slightly easier by doing away with HashMaps and their associated 
 * iterators and creating a custom class instead.
 * 
 * Gabriel Miller
 * 10/29/2016
 */

package dbclasses;

public class Grade {

	private String studentID;
	private String grade;
	
	public Grade(String studentID, String grade){
		this.studentID = studentID;
		this.grade     = grade;
	}

	/*
	 * Getters and Setters
	 */
	public String getStudentID() {
		return studentID;
	}

	public void setStudentID(String studentID) {
		this.studentID = studentID;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}
	
	
}
