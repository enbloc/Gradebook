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
}
