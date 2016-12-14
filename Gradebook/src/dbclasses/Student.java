package dbclasses;

import java.util.List;
/**
 * This class provides the data model for students.
 * 
 * @author Gabriel Miller
 * @version 1.0 
 * @since 10/25/2016
 */
public class Student {

	private String id;
	private String fname;
	private String lname;
	private String mi;
	private List<String> grades;
	
	/**
	 * Class constructor.
	 * 
	 * @param id student ID
	 * @param fname first name
	 * @param lname last name
	 * @param mi middle initial
	 */
	public Student (String id, String fname, String lname, String mi) {
		this.id    = id;
		this.fname = fname;
		this.lname = lname;
		this.mi    = mi;
	}

	/*
	 * Getters and Setters
	 */
	
	/**
	 * Gets student ID.
	 * 
	 * @return student ID
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets student ID.
	 * 
	 * @param id student ID
	 * @return none
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Gets student first name.
	 * 
	 * @return student first name
	 */
	public String getFname() {
		return fname;
	}

	/**
	 * Sets student first name.
	 * 
	 * @param fname first name
	 * @return none
	 */
	public void setFname(String fname) {
		this.fname = fname;
	}

	/**
	 * Gets student last name.
	 * 
	 * @return student last name
	 */
	public String getLname() {
		return lname;
	}

	/**
	 * Sets student last name.
	 * 
	 * @param lname last name
	 * @return none
	 */
	public void setLname(String lname) {
		this.lname = lname;
	}

	/**
	 * Gets student middle initial.
	 * 
	 * @return student middle initial
	 */
	public String getMi() {
		return mi;
	}

	/**
	 * Sets student middle initial.
	 * 
	 * @param mi student middle initial
	 * @return none
	 */
	public void setMi(String mi) {
		this.mi = mi;
	}

	/**
	 * Gets student's list of grades.
	 * 
	 * @return list of student's grades
	 */
	public List<String> getGrades() {
		return grades;
	}

	/**
	 * Sets student grades.
	 * 
	 * @param grades list of students grades
	 * @return none
	 */
	public void setGrades(List<String> grades) {
		this.grades = grades;
	}
	
	
}
