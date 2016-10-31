/*
 * Student.java
 * 
 * This class provides the data model for students, as well as the methods to 
 * create, delete, and update the text files that contain their information.
 * 
 * Gabriel Miller
 * 10/25/2016
 */
package dbclasses;

public class Student {

	private String id;
	private String fname;
	private String lname;
	private String mi;
	
	public Student (String id, String fname, String lname, String mi) {
		this.id    = id;
		this.fname = fname;
		this.lname = lname;
		this.mi    = mi;
	}

	/*
	 * Getters and Setters
	 */
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public String getLname() {
		return lname;
	}

	public void setLname(String lname) {
		this.lname = lname;
	}

	public String getMi() {
		return mi;
	}

	public void setMi(String mi) {
		this.mi = mi;
	}
}
