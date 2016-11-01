/*
 * Database.java
 * 
 * This class handles all of the operations necessary to handle the creation, insertion,
 * deletion, and updates of the file directory database system for the Gradebook application.
 * This includes the above methods as they pertain to semesters, courses, assignments, rubrics,
 * students, and individual grades. 
 * 
 * The directory structure is as follows:
 * 
 * 	User Folder
 * 	     |
 * 	     --> Semester Folder 1
 *       --> Semester Folder 2
 *                    |
 *                    --> Course Folder 1
 *                    --> Course Folder 2
 *                    --> Course Folder 3
 *                               |
 *                                --> students.txt 		(contains roster for course)
 *                                --> rubric.txt   		(contains grading rubric for course)
 *                                --> Assignments Folder	(contains all assignments in .txt files)
 *                                             |
 *                                              --> Assignment 1.txt (contains individual grades for each student)
 *                                              --> Assignment 2.txt 
 */
package dbclasses;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import constants.Constants;

public class Database {

	private String userDirectory;
	
	/*
	 * User Methods
	 */
	
	// Check to see if user folder exists, 
	// and if not, then create it.
	public boolean createOrVerifyUser(){
		boolean FOLDER_EXISTS = false;
		
		Path path = Paths.get(Constants.directory);
		if (Files.exists(path)){
			FOLDER_EXISTS = true;
		} else {
			FOLDER_EXISTS = false;
			
			// Create User folder
			File d = new File(Constants.directory);
			d.mkdirs();
		}
		return FOLDER_EXISTS;
	}
	
	/*
	 * Semester Methods
	 */
	
	// Create a new semester under the user directory
	public Semester addSemester(String semesterName){
		Semester semester = new Semester(semesterName, this.userDirectory);
		semester.createDirectory();
		return semester;
	}
	
	public void deleteSemester(String semesterName){
		// TODO Delete semester directories
	}
	
	// Get list of semesters under the user directory
	public String[] getSemesters(){
		File file = new File(this.userDirectory);
		String semesters[] = file.list();
		return semesters;
	}
	
	/*
	 * Course Methods
	 */
	
	public void createCourseDirectory(String semester, String course){
		// Create the course directory under the semester folder
		String courseDirectoryPath = this.userDirectory + 
									 "/" + semester + 
									 "/" + course;
		File d = new File(courseDirectoryPath);
		d.mkdirs();
		
		// Create the text files and directories
		createStudentsList(semester, course);
		createRubric(semester, course);
		createAssignmentFolder(semester, course);
	}
	
	// Add a course to the specified semester folder
	public Course addCourse(String semesterName, String courseName){
		Course course = new Course(courseName, semesterName, this.userDirectory);
		course.createDirectory();
		return course;
	}
	
	public void deleteCourse(){
		// TODO Delete directory with course name
	}
	
	// Get all courses under the semester directory
	public String[] getCourses(String semester){
		File file = new File(this.userDirectory + "/" + semester);
		String courses[] = file.list();
		return courses;
	}
	
	/*
	 * Student Methods
	 */
	
	// Create the students.txt file and populate it with class roster
	public void createStudentsList(String semester, String course){
		// TODO Populate students file with roster list
		File students = new File(this.userDirectory + 
								 "/" + semester + 
								 "/" + course + 
								 "/students.txt");
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
	public List<Student> getStudents(String semester, String course){
		List<Student> students = new ArrayList<Student>();
		List<String> lines = null;
		
		try {
			lines = Files.readAllLines(Paths.get(this.userDirectory + 
												 "/" + semester + 
												 "/" + course + 
												 "/students.txt"), Charset.forName("Cp1252"));
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
	 * Assignment Methods
	 */
	
	// Create directory for assignments in Course folder
	public void createAssignmentFolder(String semester, String course){
		File assignments = new File(this.userDirectory + 
									"/" + semester +
									"/" + course + 
									"/assignments");
		assignments.mkdirs();
	}
	
	// Add an assignment to the assignment directory
	public void addAssignment(String semester, String course, String assignmentName){
		
		// First, create the assignment text file
		File assignment = new File(this.userDirectory + 
								   "/" + semester + 
								   "/" + course + 
								   "/assignments/" + 
								   assignmentName + ".txt");
		try {
			assignment.createNewFile();
		} catch (IOException e) {
			// TODO Error catch
			e.printStackTrace();
		}
		
		// Second, pull the student IDs from the student list
		List<Student> students = this.getStudents(semester, course);
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
	public Assignment getAssignment(String semester, String course, String assignmentName){
		// TODO Implement a check to see if the assignment exists
		Assignment assignment = new Assignment(assignmentName, this.userDirectory + 
																"/" + semester +
																"/" + course + 
																"/assignments");
		assignment.getGrades();
		return assignment;
	}
	
	// Retrieve all assignment objects from class folder
	public List<Assignment> getAllAssignments(String semester, String course){
		// TODO Implement a null check
		List<Assignment> assignments = new ArrayList<Assignment>();
		String[] assignmentList = this.getAssignmentList(semester, course);
		
		for (String assignmentName : assignmentList){
			Assignment a = this.getAssignment(semester, course, assignmentName);
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
	public String[] getAssignmentList(String semester, String course){
		File file = new File(this.userDirectory + 
							"/" + semester +
							"/" + course + 
							"/assignments");
		String assignments[] = file.list();
		return assignments;
	}
	
	/*
	 * Rubric Methods
	 */
	
	// Create the rubric.txt file and populate it with configurations
	public void createRubric(String semester, String course){
		// TODO Populate rubric file with configs
		File rubric   = new File(this.userDirectory + 
								 "/" + semester + 
								 "/" + course + 
								 "/rubric.txt");
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
	
}
