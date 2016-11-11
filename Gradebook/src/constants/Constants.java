/*
 * Constants.java
 * 
 * This file contains the constants needed for various components 
 * of the gradebook application.
 * 
 * Gabriel Miller
 * 10/27/2016
 */

package constants;

import java.util.List;

import com.jcabi.ssh.Shell;

import dbclasses.Assignment;
import dbclasses.GradeCategory;

public class Constants {

	// Global Static Constants
	public static String directory;								// Contains the root working directory on Storm
	public static String username;								// Contains the username for logging into Storm via SSH
	public static String password;								// Contains the passwork for logging into Storm via SSH
	public static String domain = "storm.cis.fordham.edu";		// Contains the Storm server domain address
	public static int    portNo = 22;							// Standard port number for SSH login
	public static Shell  shell;									// SSH shell that is used to execute commands on server
	public static String defaultSemester = "Fall 2016";			// The default semester that is shown in the "Courses" tab
	
	// State Dependent Data Holders
	public static List<Assignment> assignments;					// Contains the list of assignments for the current course with their corresponding grade categories
	public static List<GradeCategory> gcs;						// Contains the list of grade categories with associated assignments for current course
}
