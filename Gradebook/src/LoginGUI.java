/*
 * LoginGUI.java
 * 
 * This is the class for the Login GUI.
 * 
 * Gabriel Miller
 * 10/31/2016
 */

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import constants.Constants;

public class LoginGUI {

    public LoginGUI(JFrame frame, boolean LOGIN_ERROR) {
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
        
        // If there was a login input error, 
        // display the error message.
        if (LOGIN_ERROR){
        	warning.setText("Must enter login!");
        }
        controls.add(warning, SwingConstants.LEFT);
        p.add(controls, BorderLayout.CENTER);

        int result = JOptionPane.showConfirmDialog(frame, p, "Log In", JOptionPane.OK_CANCEL_OPTION);
        
        // Handle the "OK" button press
	    if (result == JOptionPane.OK_OPTION) {
	    	
	    	// If login field is empty, relaunch login
	    	if (username.getText().isEmpty()) {
	    		new LoginGUI(frame, true);
	    		
	    	// Else, handle credential info
	    	} else {
	    		// TODO Validate credentials
	    		Constants.username = username.getText();
	    	}
	    }
    }
}