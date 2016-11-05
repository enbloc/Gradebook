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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.jcabi.ssh.Shell;

import constants.Constants;

public class Database {
	
	/*
	 * Semester Methods
	 */
	
	// Create a new semester under the user directory
	public void createSemesterFolder(String semester){
		try {
			new Shell.Plain(Constants.shell).exec("mkdir " + "/" + semester.replace(" ", "_"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	// Delete semester directory
	public void deleteSemester(String semester){
		try {
			new Shell.Plain(Constants.shell).exec("rm -rf " + Constants.directory + "/" + semester.replace(" ", "_"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	// Get list of semesters under the user directory
	public String[] getSemesters(){
		String semesters[] = null;
		try {
			String semesterData = new Shell.Plain(Constants.shell).exec("ls " + Constants.directory);
			semesters = semesterData.split("[\\r\\n]+");
			for (String semester : semesters){
				semester.replace("_", " ");
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return semesters;
	}
	
	/*
	 * Course Methods
	 */
	
	// Create the course folder, as well as the necessary files and folders
	public void createCourseFolder(String semester, String course){
		
		// Create the course directory under the semester folder
		String semesterDirectoryPath = Constants.directory + "/" + semester.replace(" ", "_");
		String courseDirectoryPath   = semesterDirectoryPath + "/" + course.replace(" ", "_");
		
		try {
			new Shell.Plain(Constants.shell).exec("mkdir " + semesterDirectoryPath);
			new Shell.Plain(Constants.shell).exec("mkdir " + courseDirectoryPath);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		// Create the text files and directories
		createStudentsList(semester, course);
		createRubric(semester, course);
		createAssignmentFolder(semester, course);
	}
	
	// Delete directory with course name
	public void deleteCourse(String semester, String course){
		String courseDirectory = Constants.directory        + "/" + 
								 semester.replace(" ", "_") + "/" +
								 course.replace  (" ", "_");
		try {
			new Shell.Plain(Constants.shell).exec("rm -rf " + courseDirectory);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	// Get all courses under the semester directory
	public String[] getCourses(String semester){
		String semesterDirectory = Constants.directory + "/" + semester.replace(" ", "_");
		String courses[] = null;
		try {
			String courseData = new Shell.Plain(Constants.shell).exec("ls " + semesterDirectory);
			courses = courseData.split("[\\r\\n]+");
			for (int i = 0; i < courses.length; i++){
				courses[i].replace("_", " ");
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return courses;
	}
	
	/*
	 * Student Methods
	 */
	
	// Create the students.txt file and populate it with class roster
	public void createStudentsList(String semester, String course){
		// TODO Populate students file with roster list
		String studentsDirectoryPath = Constants.directory        + "/" + 
									   semester.replace(" ", "_") + "/" +
									   course.replace  (" ", "_") + "/" +
									   "students.txt";
		try {
			new Shell.Plain(Constants.shell).exec("touch " + studentsDirectoryPath);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
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
		String[] studentData = null;
		String studentsDirectoryPath = Constants.directory        + "/" + 
				   					   semester.replace(" ", "_") + "/" +
				   					   course.replace  (" ", "_") + "/" +
				   					   "students.txt";
		try {
			String rawData = new Shell.Plain(Constants.shell).exec("cat " + studentsDirectoryPath);
			studentData = rawData.split("[\\r\\n]+");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		if (studentData != null){
			for (String studentInfo : studentData){
				String[] info = studentInfo.split(":");
				Student student = new Student(info[0], info[1], info[2], null);
				students.add(student);
			}
		}
		return students;
	}
	
	/*
	 * Assignment Methods
	 */
	
	// Create directory for assignments in Course folder
	public void createAssignmentFolder(String semester, String course){
		String assignmentsDirectoryPath = Constants.directory        + "/" + 
										  semester.replace(" ", "_") + "/" +
										  course.replace  (" ", "_") + "/" +
										  "assignments";
		try {
			new Shell.Plain(Constants.shell).exec("mkdir " + assignmentsDirectoryPath);
			new Shell.Plain(Constants.shell).exec("touch " + assignmentsDirectoryPath + "/assignment_list.txt");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	// Add an assignment to the assignment directory
	public void addAssignment(String semester, String course, String assignment){
		
		String courseDirectoryPath = Constants.directory        + "/" + 
											  semester.replace(" ", "_") + "/" +
											  course.replace  (" ", "_");
		
		String assignmentDirectoryPath      = courseDirectoryPath + "/" +
					 					      "assignments"       + "/" + 
					 					      assignment.replace(" ", "_") + ".txt";
		
		String assignmentsListDirectoryPath = courseDirectoryPath + "/" +
				  						 	  "assignments"       + "/" + 
				  						 	  "assignment_list.txt";
		
		// First, create the assignment text file
		try {
			new Shell.Plain(Constants.shell).exec("touch " + assignmentDirectoryPath);
			new Shell.Plain(Constants.shell).exec("echo \"" + assignment + ".txt\"" + " >> " + assignmentsListDirectoryPath);
			String message = new Shell.Plain(Constants.shell).exec("awk -F : 'BEGIN {ORS=\": \\n\"} {print $1}' " + courseDirectoryPath + "/students.txt > " + assignmentDirectoryPath);
			System.out.println(message);
		} catch (IOException e) {
			// TODO Error catch
			e.printStackTrace();
		}
		
		// Second, pull the student IDs from the student list
		//List<Student> students = this.getStudents(semester, course);
		//List<String>  ids      = new ArrayList<String>();
		//for (Student s : students){
		//	ids.add(s.getId());
		//}
		
		// Finally, add each of the students to the assignment file with no grade
		//try {
		//	for (String id : ids){
		//		new Shell.Plain(Constants.shell).exec("echo \"" + id + ": \"" + " >> " + assignmentDirectoryPath);
		//	}
		//} catch (IOException e) {
			// TODO Auto-generated catch block
		//	e.printStackTrace();
		//}
	}
	
	// Retrieve assignment object from class folder
	public Assignment getAssignment(String semester, String course, String assignmentName){
		// TODO Implement a check to see if the assignment exists
		List<Grade> grades = getGrades(semester, course, assignmentName);
		Assignment assignment = new Assignment(assignmentName, grades);
		return assignment;
	}
	
	// Retrieve all assignment objects from class folder
	public List<Assignment> getAllAssignments(String semester, String course){
		// TODO Implement a null check
		List<Assignment> assignments = new ArrayList<Assignment>();
		String[] assignmentList = this.getAssignmentList(semester, course);
		
		for (String assignmentName : assignmentList){
			Assignment a = getAssignment(semester, course, assignmentName);
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
		String assignmentsDirectory = Constants.directory        + "/" + 
									  semester.replace(" ", "_") + "/" +
									  course.replace  (" ", "_") + "/" + 
									  "assignments/assignment_list.txt";
		String assignments[] = null;
		try {
			String assignmentData = new Shell.Plain(Constants.shell).exec("cat " + assignmentsDirectory);
			assignments = assignmentData.split("[\\r\\n]+");
			for (int i = 0; i < assignments.length; i++){
				assignments[i].replace("_", " ");
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return assignments;
	}
	
	// Retrieve all of the student ID/grade pairs from the .txt file
	// TODO OPTIMIZE THIS FUNCTION
	public List<Grade> getGrades(String semester, String course, String assignment){
		List<Grade>  grades = new ArrayList<Grade>();
		String[] gradeData = null;
		String assignmentDirectory = Constants.directory          + "/" + 
				  					 semester.replace  (" ", "_") + "/" +
				  					 course.replace    (" ", "_") + "/" + 
				  					 "assignments"                + "/" +
				  					 assignment.replace(" ", "_");
		try {
			String rawData = new Shell.Plain(Constants.shell).exec("cat " + assignmentDirectory);
			gradeData = rawData.split("[\\r\\n]+");
			for (String data : gradeData){
				System.out.println("-" + data + "-");
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		for (String gradeInfo : gradeData){
			String[] info = gradeInfo.split(":");
			Grade grade = new Grade(info[0], info[1]);
			grades.add(grade);
		}
		
		return grades;
	}
	
	/*
	 * Rubric Methods
	 */
	
	// Create the rubric.txt file and populate it with configurations
	public void createRubric(String semester, String course){
		// TODO Populate rubric file with configs
		String rubricDirectoryPath = Constants.directory        + "/" + 
									 semester.replace(" ", "_") + "/" +
									 course.replace  (" ", "_") + "/" +
									 "rubric.txt";
		try {
			new Shell.Plain(Constants.shell).exec("touch " + rubricDirectoryPath);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public void updateRubric(){
		// TODO Update the rubric file when adjustments are made
	}
	
	public void getRubric(){
		// TODO Get course rubric information
	}
	
}
