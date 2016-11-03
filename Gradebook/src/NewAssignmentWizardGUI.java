/*
 * NewAssignmentWizard.java
 * 
 * This is the class that creates the New Assignment Wizard prompt after 
 * the New Assignment button is selected on any page. It contains several
 * fields through which the user inputs the necessary data to create 
 * a new assignment.
 * 
 * Gabriel Miller
 * 10/25/2016
 */
import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import dbclasses.Database;

public class NewAssignmentWizardGUI {

	private JFrame  mainFrame;
	private JPanel  mainPanel;
	private JPanel  labels;
	private JPanel  controls;
	private JTextField assignmentField;

	
	public NewAssignmentWizardGUI(String currentCourse, String currentSemester, String currentUser){
		
		mainPanel = new JPanel(new BorderLayout());

		// Set up for labels panel
        labels = new JPanel(new GridLayout(0,1));
        labels.add(new JLabel("Assignment Name", SwingConstants.RIGHT));
        mainPanel.add(labels, BorderLayout.WEST);

        // Set up for controls panel
        controls         = new JPanel(new GridLayout(0,1));
        assignmentField  = new JTextField();
        controls .add(assignmentField);
        mainPanel.add(controls, BorderLayout.CENTER);

        // Generate Wizard
        int result = JOptionPane.showConfirmDialog(
            mainFrame, mainPanel, "New Assignment", JOptionPane.OK_CANCEL_OPTION);
        
        // Handle assignment creation
	    if (result == JOptionPane.OK_OPTION) {
	    	if (assignmentField.getText() != null) {
	    		Database db = new Database();
	    		db.addAssignment(currentCourse, currentSemester, assignmentField.getText());
	    	} else {
	    		// TODO Error message for empty input field
	    	}
	    }
	}
}
