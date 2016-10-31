/*
 * Assignment.java
 * 
 * This class provides the data model for assignments, as well as the methods to 
 * create, delete, and update the text files that contain their information.
 * 
 * Gabriel Miller
 * 10/25/2016
 */
package dbclasses;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Assignment {

	private String		folder;
	private String      name;
	private List<Grade> grades;
	
	public Assignment(String name, String folder){
		this.name   = name;
		this.folder = folder;
	}
	
	// Retrieve all of the student ID/grade pairs from the .txt file
	public List<Grade> getGrades(){
		List<String> lines = new ArrayList<String>();
		this.grades = new ArrayList<Grade>();
		
		try {
			lines = Files.readAllLines(Paths.get(this.folder + "/" + name), Charset.forName("Cp1252"));
		} catch (IOException e) {
			// TODO Error catch
			e.printStackTrace();
		}
		for (String line : lines){
			String[] info = line.split(":");
			Grade grade = new Grade(info[0], info[1]);
			grades.add(grade);
		}
		
		return grades;
	}
}
