/*
 * LoginGUI.java
 * 
 * This is the class for the Login GUI.
 * 
 * Gabriel Miller
 * 10/31/2016
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.io.IOException;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import constants.Constants;

import com.jcabi.ssh.SSHByPassword;

public class LoginGUI {

	// Variable that tracks login status
	boolean LOGIN_SUCCESS 	  = false;
	boolean LOGIN_INFO_EMPTY  = false;
	boolean LOGIN_AUTH_FAILED = false;
	
    public LoginGUI(JFrame frame, boolean LOGIN_INFO_EMPTY, boolean LOGIN_AUTH_FAILED) {
    	this.LOGIN_INFO_EMPTY  = LOGIN_INFO_EMPTY;
    	this.LOGIN_AUTH_FAILED = LOGIN_AUTH_FAILED;
    	
    	JPanel p = new JPanel(new BorderLayout(5,5));

    	// Set up text labels on the left
        JPanel labels = new JPanel(new GridLayout(0,1,2,2));
        labels.add(new JLabel("User Name", SwingConstants.RIGHT));
        labels.add(new JLabel("Password", SwingConstants.RIGHT));
        labels.add(new JLabel());
        p.add(labels, BorderLayout.WEST);

        // Set up text fields and label on the right
        JPanel controls = new JPanel(new GridLayout(0,1,2,2));
        JTextField username = new JTextField();
        controls.add(username);
        JPasswordField password = new JPasswordField();
        controls.add(password);
        JLabel warning = new JLabel();
        warning.setForeground(Color.RED);
        
        // If there was a login input error, 
        // display the error message.
        if (LOGIN_INFO_EMPTY){
        	warning.setText("Must enter login!");
        } else if (LOGIN_AUTH_FAILED){
        	warning.setText("Authentication Failed!");
        }
        controls.add(warning, SwingConstants.LEFT);
        p.add(controls, BorderLayout.CENTER);

        int result = JOptionPane.showConfirmDialog(frame, p, "Log In", JOptionPane.OK_CANCEL_OPTION);
        
        // Handle the "OK" button press
	    if (result == JOptionPane.OK_OPTION) {
	    	
	    	// If login field is empty, relaunch login
	    	if (username.getText().isEmpty() || 
	    		password.getPassword().length == 0 ){
	    		new LoginGUI(frame, true, false);
	    		LOGIN_SUCCESS = false;
	    		
	    	// Else, handle credential info
	    	} else {
	    		// TODO Validate credentials
	    		Constants.username = username.getText();
	    		Constants.password = new String(password.getPassword());
	    		LOGIN_SUCCESS = sshLogin();
	    		if (!LOGIN_SUCCESS){
	    			new LoginGUI(frame, false, true);
	    		}
	    	}
	    }
    }
    
    /*
     * SSH Login Function
     * 
     * Attempts to log in via SSH to the specified domain and port number
     * in the Constants class using the username and password entered by
     * the user. If there is some failure in the login process, from an 
     * UnknownHostException, authentication failure, or otherwise, the 
     * function will return LOGIN_SUCCESS = false. Otherwise, it will 
     * return true.
     */
    private boolean sshLogin(){
    	boolean LOGGED_IN = false;
    	
    	try {
			Constants.shell = new SSHByPassword(Constants.domain,
											Constants.portNo,
											Constants.username,
											Constants.password);
			Constants.shell.exec("mkdir Gradebook", null, null, null);
			LOGGED_IN = true;
		} catch (UnknownHostException e) {
			// Unknown Host
			e.printStackTrace();
			System.out.println("Unknown Host Exception triggered for wrong uname/pword");
			LOGGED_IN = false;
		} catch (IOException e) {
			// Authentication Failed
			e.printStackTrace();
			System.out.println("Authentication Failed!");
			LOGGED_IN = false;
		}
    	return LOGGED_IN;
    }
}








