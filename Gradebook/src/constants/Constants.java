package constants;

import com.jcabi.ssh.Shell;
/**
 * This class contains the constants needed for various components 
 * of the Gradebook application.
 * 
 * @author Gabriel Miller
 * @version 1.0 10/27/2016
 */
public class Constants {

	// Global Static Constants
	/**
	 * Contains the user's root working directory on Storm
	 */
	public static String directory;
	
	/**
	 * Contains the username for logging into Storm via SSH
	 */
	public static String username;							
	
	/**
	 * Contains the password for logging into Storm via SSH
	 */
	public static String password;								
	
	/**
	 * Contains the Storm server domain address
	 */
	public static String domain = "storm.cis.fordham.edu";		
	
	/**
	 * Standard port number for SSH login
	 */
	public static int    portNo = 22;							
	
	/**
	 * SSH shell that is used to execute commands on server
	 */
	public static Shell  shell;									
	
	/**
	 * The default semester that is shown in the "Courses" tab
	 */
	public static String defaultSemester = "Fall 2016";	
	
}
