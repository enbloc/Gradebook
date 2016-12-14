package dbclasses;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.jcabi.ssh.Shell;

import constants.Constants;
import threads.GradeUpdateThread;
/**
 * Database.java
 * 
 * This class handles all of the operations necessary to handle the creation, insertion,
 * deletion, and updates of the file directory database system for the Gradebook application.
 * This includes the above methods as they pertain to semesters, courses, assignments, rubrics,
 * students, and individual grades. 
 * 
 * @author Gabriel Miller
 * @version 1.0
 * @since 10/15/2016
 * 
 */
/*
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
public class Database {
	
	// Class to assist with directory/file path construction
	DatabasePathBuilder PathBuilder;
	
	/**
	 * Class constructor that creates a DatabasePathBuilder.
	 */
	public Database(){
		PathBuilder = new DatabasePathBuilder();
	}
	
	/*
	 * Semester Methods
	 */
	
	// Create a new semester under the user directory
	/**
	 * Creates a new semester under the user directory.
	 * 
	 * @param semester current semester
	 */
	public void createSemesterFolder(String semester){
		
		String semesterFolderPath = PathBuilder.buildPath(semester);
		try {
			new Shell.Plain(Constants.shell).exec("mkdir " + semesterFolderPath);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	/**
	 * Deletes the semester directory.
	 * 
	 * @param semester current semester
	 */
	public void deleteSemester(String semester){
		
		String semesterFolderPath = PathBuilder.buildPath(semester);
		try {
			new Shell.Plain(Constants.shell).exec("rm -rf " + semesterFolderPath);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	/**
	 * Gets list of semesters under the current user directory
	 * 
	 * @return array of semester names
	 */
	public String[] getSemesters(){
		String semesters[] = null;
		String rawSemesters[] = null;
		try {
			String semesterData = new Shell.Plain(Constants.shell).exec("ls " + Constants.directory);
			rawSemesters = semesterData.split("[\\r\\n]+");
			semesters = new String[rawSemesters.length];
			for (int i = 0; i < rawSemesters.length; i++){
				semesters[i] = rawSemesters[i].replace("_", " ");
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
	
	/**
	 * Creates the course folder, as well as all subordinate files and folders.
	 * 
	 * @param semester current semester
	 * @param course current course
	 */
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
		createGradeScheme(semester, course);
		// TODO createAttendanceFile(semester, course);
	}
	
	/**
	 * Gets course data for preparation of the course table
	 * 
	 * @param semester current semester
	 * @param course current course
	 * @return array of lines from the "assignments" file, not yet parsed
	 */
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
	
	/**
	 * Deletes directory with the given course name.
	 * 
	 * @param semester current semester
	 * @param course course to be deleted
	 */
	public void deleteCourse(String semester, String course){
		
		String courseFolderPath = PathBuilder.buildPath(semester, course);
		try {
			new Shell.Plain(Constants.shell).exec("rm -rf " + courseFolderPath);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	/**
	 * Gets all courses under the current semester directory.
	 * 
	 * @param semester current semester
	 * @return array of course names in the current semester
	 */
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
	
	/**
	 * Creates the "students" file and populate it with the class roster.
	 * 
	 * @param semester current semester
	 * @param course current course
	 */
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
	
	/**
	 * Adds a student to the directory.
	 * 
	 * @param semester current semester
	 * @param course current course
	 * @param studentID unique ID for the student
	 * @param fName student first name
	 * @param lName student last name
	 * @param mName student middle name
	 * @param columnCount number of columns in the "assignments" file
	 * @param NEW_COURSE flag that is set to true if the course to which the student is being added is a new course
	 * 
	 */
	public void addStudent(String semester, String course, String studentID, String fName, String lName, String mName, String columnCount, boolean NEW_COURSE){
		
		String assignmentsFilePath = PathBuilder.buildPath(semester, course, "assignments");
		String addStudentCommand = null;
		if (NEW_COURSE){
			addStudentCommand   = "echo \"" + studentID + ":" + fName + ":" + lName + ":" + mName + "\" >> " + assignmentsFilePath + //" | " +
										 //"awk -F : '$1==\"" + studentID + "\"{OFS=\":\"; $" + columnCount + "=\" \"}1' " + assignmentsFilePath +
										 //" >> tmp1 && mv tmp1 " + assignmentsFilePath + 
										 " | sort -t : -k 3 " + assignmentsFilePath + " >> tmp2 && mv tmp2 " + assignmentsFilePath;
		} else {
			addStudentCommand = "echo \"" + studentID + ":" + fName + ":" + lName + ":" + mName + "\" >> " + assignmentsFilePath + " | " +
								"awk -F : '$1==\"" + studentID + "\"{OFS=\":\"; $" + columnCount + "=\" \"}1' " + assignmentsFilePath +
								" >> tmp1 && mv tmp1 " + assignmentsFilePath + 
					 			" | sort -t : -k 3 " + assignmentsFilePath + " >> tmp2 && mv tmp2 " + assignmentsFilePath;
		}
		
		GradeUpdateThread gut1 = new GradeUpdateThread("gut1", addStudentCommand);
		gut1.start();
	}
	
	/**
	 * Deletes a student from the directory (unimplemented).
	 */
	public void deleteStudent(){
		// TODO Delete a student from the directory
	}
	
	/**
	 * Updates a student's information in the "assignments" file (unimplemented).
	 */
	public void updateStudent(){
		// TODO Update a student's information in the directory
	}
	
	
	/**
	 * Gets the list of students in the specified course.
	 * 
	 * @param semester current semester
	 * @param course current course
	 * @return list of students enrolled in the current course
	 */
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
	
	/**
	 * Create "assignments" and "assignments_list" files in the current course directory.
	 * 
	 * @param semester current semester
	 * @param course current course
	 */
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
	
	/**
	 * Add an assignment to both the "assignments" file and the "assignments_list" file.
	 * 
	 * @param semester current semester
	 * @param course current course
	 * @param assignment assignment name
	 * @param category assignment category
	 */
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
	
	/**
	 * Delete an assignment from the assignment files (unimplemented).
	 */
	public void deleteAssignment(){
		// TODO Delete an assignment from the assignment directory
	}
	
	/**
	 * Get the list of assignments in the specified course.
	 * 
	 * @param semester current semester
	 * @param course current course 
	 * @return list of assignments in the specified course
	 */
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
	
	/**
	 * Updates the grade of a student when the information changes.
	 *  
	 * @param semester current semester
	 * @param course current course
	 * @param studentID student ID
	 * @param column the specific column for which the student info must be updated
	 * @param grade the new grade 
	 */
	public void updateGrade(String semester, String course, String studentID, int column, String grade){
		
		String assignmentsFilePath = PathBuilder.buildPath(semester, course, "assignments");
		String updateCommand 	   = "awk -F : '$1==\"" + studentID + 
				  			   		 "\"{OFS=\":\";$" + String.valueOf(column - 1) + 
				  			   		 "=\"" + grade + 
				  			   		 "\"}1' " + assignmentsFilePath + 
				  			   		 " > tmp && mv tmp " + assignmentsFilePath;
		
		GradeUpdateThread gut = new GradeUpdateThread("gut1", updateCommand);
		gut.start();
	}
	
	/*
	 * Rubric Methods
	 */
	
	/**
	 * Create a rubric file for the specified course.
	 * 
	 * @param semester current semester
	 * @param course current course
	 */
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
	
	/**
	 * Update the rubric file when adjustments are made.
	 * 
	 * @param semester current semester
	 * @param course current course
	 * @param newGcs list of grade categories that will be used to update the rubric file
	 */
	public void updateRubric(String semester, String course, List<GradeCategory> newGcs){
		
		String rubricFilePath = PathBuilder.buildPath(semester, course, "rubric");
		String dataString     = new String();
		
		for (GradeCategory gc : newGcs){
			dataString = dataString.concat(
					gc.getCategory() + ":" + 
							String.valueOf((int)(gc.getWeight() * 100)) + "\n");
		}
		
		String command = "echo \"" + dataString + "\" > " + rubricFilePath;
		GradeUpdateThread gut = new GradeUpdateThread("gut", command);
		gut.start();
	}
	
	/**
	 * Gets course rubric information.
	 * 
	 * @param semester current semester
	 * @param course current course
	 * @return list of grade category objects that 
	 */
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
	
	/**
	 * Creates grade scheme file in the specified course directory.
	 * 
	 * @param semester current semester
	 * @param course current course
	 */
	public void createGradeScheme(String semester, String course){
		// TODO Populate scheme file with configs
		String schemeFilePath = PathBuilder.buildPath(semester, course, "gradescheme");
		String gradeSchemeInfo = "A:100:94\nA-:93.99:90\nB+:89.99:87\nB:86.99:84\nB-:83.99:80\nC+:79.99:77\nC:76.99:74\nC-:73.99:70\nF:69.99:0";
		try {
			new Shell.Plain(Constants.shell).exec("echo \"" + gradeSchemeInfo + "\" >> " + schemeFilePath);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	/**
	 * Update grade scheme file with new configurations (unimplemented).
	 * 
	 * @param semester current semester
	 * @param course current course
	 */
	public void updateGradeScheme(String semester, String course){
		
	}
	
	/**
	 * Retrieves the grade scheme as a list of grade ranges.
	 * @param semester current semester
	 * @param course current course
	 * @return list of grade range objects generated from the grade scheme file
	 */
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
