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
 *                                --> students 			(contains roster for course)
 *                                --> rubric   			(contains grading rubric for course)
 *                                --> assignments		(contains list of student IDs and grades)
 *                                --> assignments_list	(contains list of assignment names)
 *                                --> attendance		(contains list of student IDs and attendance record)	 
 */
package dbclasses;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.jcabi.ssh.Shell;

import constants.Constants;
import threads.GradeUpdateThread;

public class Database {
	
	// Class to assist with directory/file path construction
	DatabasePathBuilder PathBuilder;
	
	public Database(){
		PathBuilder = new DatabasePathBuilder();
	}
	
	/*
	 * Semester Methods
	 */
	
	// Create a new semester under the user directory
	public void createSemesterFolder(String semester){
		
		String semesterFolderPath = PathBuilder.buildPath(semester);
		try {
			new Shell.Plain(Constants.shell).exec("mkdir " + semesterFolderPath);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	// Delete semester directory
	public void deleteSemester(String semester){
		
		String semesterFolderPath = PathBuilder.buildPath(semester);
		try {
			new Shell.Plain(Constants.shell).exec("rm -rf " + semesterFolderPath);
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
	// TODO ADAPT TO NEW FORM
	public void createCourseFolder(String semester, String course){
		
		// Create the course directory under the semester folder
		String semesterFolderPath = PathBuilder.buildPath(semester);
		String courseFolderPath   = PathBuilder.buildPath(semester, course);
		
		try {
			new Shell.Plain(Constants.shell).exec("mkdir " + semesterFolderPath);
			new Shell.Plain(Constants.shell).exec("mkdir " + courseFolderPath);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		// Create the text files and directories
		createStudentsList(semester, course);
		createRubric(semester, course);
		createAssignmentFiles(semester, course);
		// TODO createAttendanceFile(semester, course);
	}
	
	// Get course data for the preparation of course table
	public String[] getCourseData(String semester, String course){
		
		String assignmentsFilePath = PathBuilder.buildPath(semester, course, "assignments");
		String[] courseData = null;
		
		try {
			String rawData = new Shell.Plain(Constants.shell).exec("cat " + assignmentsFilePath);
			courseData = rawData.split("[\\r\\n]+");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		return courseData;
	}
	
	// Delete directory with course name
	public void deleteCourse(String semester, String course){
		
		String courseFolderPath = PathBuilder.buildPath(semester, course);
		try {
			new Shell.Plain(Constants.shell).exec("rm -rf " + courseFolderPath);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	// Get all courses under the semester directory
	public String[] getCourses(String semester){
		
		String semesterFolderPath = PathBuilder.buildPath(semester);
		String rawCourses[] = null;
		String courses[]    = null;
		try {
			String courseData = new Shell.Plain(Constants.shell).exec("ls " + semesterFolderPath);
			rawCourses = courseData.split("[\\r\\n]+");
			courses    = new String[rawCourses.length];
			for (int i = 0; i < rawCourses.length; i++){
				courses[i] = rawCourses[i].replace("_", " ");
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
		String studentsFilePath = PathBuilder.buildPath(semester, course, "students");
		try {
			new Shell.Plain(Constants.shell).exec("touch " + studentsFilePath);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	// Add a student to the directory
	public void addStudent(String semester, String course, String studentID, String fName, String lName, String mName, String columnCount){
		
		String assignmentsFilePath = PathBuilder.buildPath(semester, course, "assignments");
		String addStudentCommand   = "echo \"" + studentID + ":" + fName + ":" + lName + "\" >> " + assignmentsFilePath + " | " +      //TODO FIX THIS COMMAND
									 "awk -F : '$1==\"" + studentID + "\"{OFS=\":\"; $" + columnCount + "=\" \"}1' " + assignmentsFilePath +
									 " >> tmp1 && mv tmp1 " + assignmentsFilePath + 
									 " | sort -t : -k 3 " + assignmentsFilePath + " >> tmp2 && mv tmp2 " + assignmentsFilePath;
		
		GradeUpdateThread gut1 = new GradeUpdateThread("gut1", addStudentCommand);
		gut1.start();
	}
	
	public void deleteStudent(){
		// TODO Delete a student from the directory
	}
	
	public void updateStudent(){
		// TODO Update a student's information in the directory
	}
	
	// Get the list of students in the course
	public List<Student> getStudents(String semester, String course){
		
		List<Student> students  = new ArrayList<Student>();
		String[] studentData    = null;
		String studentsFilePath = PathBuilder.buildPath(semester, course, "students");
		
		try {
			String rawData = new Shell.Plain(Constants.shell).exec("cat " + studentsFilePath);
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
	public void createAssignmentFiles(String semester, String course){
		
		String assignmentsFilePath 	   = PathBuilder.buildPath(semester, course, "assignments");
		String assignmentsListFilePath = PathBuilder.buildPath(semester, course, "assignments_list");
		try {
			new Shell.Plain(Constants.shell).exec("touch " + assignmentsFilePath);
			new Shell.Plain(Constants.shell).exec("touch " + assignmentsListFilePath);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	// Add an assignment to the assignment directory
	public void addAssignment(String semester, String course, String assignment, String category){

		String assignmentsFilePath 	   = PathBuilder.buildPath(semester, course, "assignments");
		String assignmentsListFilePath = PathBuilder.buildPath(semester, course, "assignments_list");
		String assignmentsCommand      = "awk -F: '{$(NF+1)=\" \";}1' OFS=: " + assignmentsFilePath + " > tmp && mv tmp " + assignmentsFilePath;
		String assignmentsListCommand  = "echo \"" + assignment + ":" + category +"\" >> " + assignmentsListFilePath;
		
		GradeUpdateThread gut1 = new GradeUpdateThread("gut1", assignmentsCommand);
		GradeUpdateThread gut2 = new GradeUpdateThread("gut2", assignmentsListCommand);
		gut1.start();
		gut2.start();
	}
	
	public void deleteAssignment(){
		// TODO Delete an assignment from the assignment directory
	}
	
	// Get the list of assignments in the course
	public List<Assignment> getAssignmentList(String semester, String course){
		
		List<Assignment> assignments   = new ArrayList<Assignment>(); 
		String assignmentData[]  	   = null;
		String assignmentsListFilePath = PathBuilder.buildPath(semester, course, "assignments_list");
		
		try {
			String rawData = new Shell.Plain(Constants.shell).exec("cat " + assignmentsListFilePath);
			assignmentData = rawData.split("[\\r\\n]+");
			for (String item : assignmentData){
				String assignmentInfo[] = item.split(":");
				if (!assignmentInfo[0].isEmpty()){
					Assignment assignment = new Assignment(assignmentInfo[0].replace("_", " "), assignmentInfo[1]);
					assignments.add(assignment);
				}
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return assignments;
	}
	
	// Update grade of a student when the information is changed
	public void updateGrade(String semester, String course, String studentID, int column, String grade){
		
		String assignmentsFilePath = PathBuilder.buildPath(semester, course, "assignments");
		String updateCommand 	   = "awk -F : '$1==\"" + studentID + 
				  			   		 "\"{OFS=\":\";$" + String.valueOf(column) + 
				  			   		 "=\"" + grade + 
				  			   		 "\"}1' " + assignmentsFilePath + 
				  			   		 " > tmp && mv tmp " + assignmentsFilePath;
		
		GradeUpdateThread gut = new GradeUpdateThread("gut1", updateCommand);
		gut.start();
	}
	
	/*
	 * Rubric Methods
	 */
	
	// Create the rubric.txt file and populate it with configurations
	public void createRubric(String semester, String course){
		// TODO Populate rubric file with configs
		String rubricFilePath = PathBuilder.buildPath(semester, course, "rubric");
		try {
			new Shell.Plain(Constants.shell).exec("touch " + rubricFilePath);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public void updateRubric(){
		// TODO Update the rubric file when adjustments are made
	}
	
	// Get course rubric information
	public List<GradeCategory> getRubric(String semester, String course){
		
		String rubricData[] 				= null;
		String rubricFilePath 				= PathBuilder.buildPath(semester, course, "rubric");
		List<GradeCategory> gradeCategories = new ArrayList<GradeCategory>(); 
		
		try {
			String rawData = new Shell.Plain(Constants.shell).exec("cat " + rubricFilePath);
			rubricData = rawData.split("[\\r\\n]+");
			for (String item : rubricData){
				String rubricInfo[] = item.split(":");
				if (!rubricInfo[0].isEmpty()){
					GradeCategory gw = new GradeCategory(rubricInfo[0], Integer.parseInt(rubricInfo[1]));
					gradeCategories.add(gw);
				}
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return gradeCategories;
	}
	
	/*
	 * Grade Scheme Methods
	 */
	
	// Create grade scheme file in the course directory
	public void createGradeScheme(String semester, String course){
		// TODO Populate scheme file with configs
		String schemeFilePath = PathBuilder.buildPath(semester, course, "gradescheme");
		try {
			new Shell.Plain(Constants.shell).exec("touch " + schemeFilePath);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	// Update grade scheme file with new configurations
	public void updateGradeScheme(String semester, String course){
		
	}
	
	// Retrieve the grade scheme as a list of GradeRanges
	public List<GradeRange> getGradeScheme(String semester, String course){
		String schemeData[] 				= null;
		String schemeFilePath 				= PathBuilder.buildPath(semester, course, "gradescheme");
		List<GradeRange> gradeRanges	    = new ArrayList<GradeRange>(); 
		
		try {
			String rawData = new Shell.Plain(Constants.shell).exec("cat " + schemeFilePath);
			schemeData = rawData.split("[\\r\\n]+");
			for (String item : schemeData){
				String schemeInfo[] = item.split(":");
				if (!schemeInfo[0].isEmpty()){
					GradeRange gr = new GradeRange(schemeInfo[0], Double.valueOf(schemeInfo[1]), Double.valueOf(schemeInfo[2]));
					gradeRanges.add(gr);
				}
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return gradeRanges;
	}
	
	
	
	
	
}
