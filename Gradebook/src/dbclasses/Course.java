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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

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
	
	public void createDirectory(){
		// Create the course directory under the semester folder
		File d = new File(this.folder);
		d.mkdirs();
		
		// Create the text files and directories
		createStudentsList();
		createRubric();
		createAssignmentFolder();
	}
	
	/*
	 * Student Methods
	 */
	
	// Create the students.txt file and populate it with class roster
	public void createStudentsList(){
		// TODO Populate students file with roster list
		File students = new File(this.folder + "/" + "students.txt");
		try {
	      students.createNewFile();
		} catch (IOException e) {
			// TODO Error catch
			e.printStackTrace();
		}
	}
	
	public void addStudent(){
		// TODO Add a student to the directory
	}
	
	public void deleteStudent(){
		// TODO Delete a student from the directory
	}
	
	public void updateStudent(){
		// TODO Update a student's information in the directory
	}
	
	// Get the list of students in the course
	public List<Student> getStudents(){
		List<Student> students = new ArrayList<Student>();
		List<String> lines = null;
		
		try {
			lines = Files.readAllLines(Paths.get(this.folder + "/" + "students.txt"), Charset.forName("Cp1252"));
		} catch (IOException e) {
			// TODO Error catch
			e.printStackTrace();
		}
		for (String line : lines){
			String[] info = line.split(":");
			Student student = new Student(info[0], info[1], info[2], null);
			students.add(student);
		}
		return students;
	}
	
	/*
	 * Rubric Methods
	 */
	
	// Create the rubric.txt file and populate it with configurations
	public void createRubric(){
		// TODO Populate rubric file with configs
		File rubric   = new File(this.folder + "/" + "rubric.txt");
		try {
	      rubric.createNewFile();
		} catch (IOException e) {
			// TODO Error catch
			e.printStackTrace();
		}
	}
	
	public void updateRubric(){
		// TODO Update the rubric file when adjustments are made
	}
	
	public void getRubric(){
		// TODO Get course rubric information
	}
	
	/*
	 * Assignment Methods
	 */
	
	// Create directory for assignments in Course folder
	public void createAssignmentFolder(){
		File assignments = new File(this.folder + "/assignments");
		assignments.mkdirs();
	}
	
	// Add an assignment to the assignment directory
	public void addAssignment(String assignmentName){
		
		// First, create the assignment text file
		File assignment = new File(this.folder + "/assignments/" + assignmentName + ".txt");
		try {
			assignment.createNewFile();
		} catch (IOException e) {
			// TODO Error catch
			e.printStackTrace();
		}
		
		// Second, pull the student IDs from the student list
		List<Student> students = this.getStudents();
		List<String>  ids      = new ArrayList<String>();
		for (Student s : students){
			ids.add(s.getId());
		}
		
		// Finally, add each of the students to the assignment file with no grade
		try {
			PrintWriter pw = new PrintWriter(assignment);
			for (String id : ids){
				pw.println(id + ": ");
			}
			pw.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// Retrieve assignment object from class folder
	public Assignment getAssignment(String assignmentName){
		// TODO Implement a check to see if the assignment exists
		Assignment assignment = new Assignment(assignmentName, this.folder + "/assignments");
		assignment.getGrades();
		return assignment;
	}
	
	// Retrieve all assignment objects from class folder
	public List<Assignment> getAllAssignments(){
		// TODO Implement a null check
		List<Assignment> assignments = new ArrayList<Assignment>();
		String[] assignmentList = this.getAssignmentList();
		
		for (String assignmentName : assignmentList){
			Assignment a = this.getAssignment(assignmentName);
			assignments.add(a);
		}
		return assignments;
	}
	
	public void deleteAssignment(){
		// TODO Delete an assignment from the assignment directory
	}
	
	public void updateAssignment(){
		// TODO Update an assignment in the assignment directory
	}
	
	// Get the list of assignments in the course
	public String[] getAssignmentList(){
		File file = new File(this.folder + "/assignments");
		assignments = file.list();
		return assignments;
	}
}
