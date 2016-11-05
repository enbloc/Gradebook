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

import com.jcabi.ssh.Shell;

public class Constants {

	// Global Static Constants
	public static String directory;								// Contains the root working directory on Storm
	public static String username;								// Contains the username for logging into Storm via SSH
	public static String password;								// Contains the passwork for logging into Storm via SSH
	public static String domain = "storm.cis.fordham.edu";		// Contains the Storm server domain address
	public static int    portNo = 22;							// Standard port number for SSH login
	public static Shell  shell;									// SSH shell that is used to execute commands on server
}
