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
import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import constants.Global;
import dbclasses.Assignment;
import dbclasses.Database;
import dbclasses.GradeCategory;

public class NewAssignmentWizardGUI {

	private String  newAssignment;
	private JFrame  mainFrame;
	private JPanel  mainPanel;
	private JPanel  labels;
	private JPanel  controls;
	private JComboBox<String> category;
	
	// Variable to keep track of whether or not course was created
	public int ASSIGNMENT_CREATED = 0;  // 0 == no course created, 1 == course created
	
	public NewAssignmentWizardGUI(String semester, String course, boolean ASSIGNMENT_INFO_EMPTY){
		
		mainPanel = new JPanel(new BorderLayout());

		// Set up text labels on the left
        labels = new JPanel(new GridLayout(0,1,2,2));
        labels.add(new JLabel("Assignment: ", SwingConstants.RIGHT));
        labels.add(new JLabel("Category: "  , SwingConstants.RIGHT));
        labels.add(new JLabel());
        mainPanel.add(labels, BorderLayout.WEST);

        // Set up text fields and label on the right
        controls = new JPanel(new GridLayout(0,1,2,2));
        JTextField assignment = new JTextField();
        category   = new JComboBox<String>();
        prepareCategories();
        JLabel warning = new JLabel();
        warning.setForeground(Color.RED);
        controls.add(assignment);
        controls.add(category);
        controls.add(warning);
        mainPanel.add(controls, BorderLayout.CENTER);
        
        if (ASSIGNMENT_INFO_EMPTY){
        	warning.setText("Enter required info");
        }

        // Generate Wizard
        int result = JOptionPane.showConfirmDialog(
            mainFrame, mainPanel, "New Assignment", JOptionPane.OK_CANCEL_OPTION);
        
        // Handle assignment creation
	    if (result == JOptionPane.OK_OPTION) {
	    	if (assignment.getText().isEmpty() || 
	    		category.getSelectedItem().toString().isEmpty()) {
	    			new NewAssignmentWizardGUI(semester, course, true);
	    	} else {
	    		Global.assignments.add(new Assignment(assignment.getText(),
	    											  category.getSelectedItem().toString()));
	    		Database db = new Database();
    			db.addAssignment(semester, 
    							 course, 
    							 assignment.getText(), 
    							 category.getSelectedItem().toString());
    			newAssignment = assignment.getText();
    			ASSIGNMENT_CREATED = 1;
	    	}
	    }
	}

	// Prepare combo box for the category selector
	public void prepareCategories(){
		for (GradeCategory gc : Global.gradeCategories){
			category.addItem(gc.getCategory());
		}
	}
	
	
	public String getNewAssignment() {
		return newAssignment;
	}
}
