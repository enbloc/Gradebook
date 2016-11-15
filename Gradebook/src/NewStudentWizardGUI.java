/*
 * NewStudentWizard.java
 * 
 * This is the class that creates the New Student Wizard prompt after 
 * the "Add Student" button is selected on any page. It contains several
 * fields through which the user inputs the necessary data to create 
 * a new student.
 * 
 * Gabriel Miller
 * 10/25/2016
 */
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import dbclasses.Database;

public class NewStudentWizardGUI {

	private String studentID;
	private String studentName;
	private JFrame mainFrame;
	private JPanel mainPanel;
	private JPanel labels;
	private JPanel controls;

	
	public NewStudentWizardGUI(String semester, String course, String columnCount, boolean STUDENT_INFO_EMPTY){
		
		mainPanel = new JPanel(new BorderLayout());

		// Set up text labels on the left
        labels = new JPanel(new GridLayout(0,1,2,2));
        labels.add(new JLabel("Student ID: "    , SwingConstants.RIGHT));
        labels.add(new JLabel("First Name: "    , SwingConstants.RIGHT));
        labels.add(new JLabel("Middle Initial: ", SwingConstants.RIGHT));
        labels.add(new JLabel("Last Name: "     , SwingConstants.RIGHT));
        labels.add(new JLabel());
        mainPanel.add(labels, BorderLayout.WEST);

        // Set up text fields and label on the right
        controls 	 = new JPanel(new GridLayout(0,1,2,2));
        JTextField studentID = new JTextField();
        JTextField fName     = new JTextField();
        JTextField lName     = new JTextField();
        JTextField mName	 = new JTextField();
        JLabel warning 		 = new JLabel();
        warning.setForeground(Color.RED);
        controls.add(studentID);
        controls.add(fName);
        controls.add(mName);
        controls.add(lName);
        controls.add(warning);
        mainPanel.add(controls, BorderLayout.CENTER);

        if (STUDENT_INFO_EMPTY){
        	warning.setText("Must enter required information!");
        }
        
        // Generate Wizard
        int result = JOptionPane.showConfirmDialog(
            mainFrame, mainPanel, "Add Student", JOptionPane.OK_CANCEL_OPTION);
        
        // Handle assignment creation
	    if (result == JOptionPane.OK_OPTION) {
	    	if (studentID.getText().isEmpty() ||
	    		    fName.getText().isEmpty() ||
	    		    lName.getText().isEmpty()) {
	    		new NewStudentWizardGUI(semester, course, columnCount, true);
	    	} else {
	    		Database db = new Database();
	    		db.addStudent(semester, 
	    					  course, 
	    					  studentID.getText(),
	    					  fName.getText(),
	    					  lName.getText(),
	    					  mName.getText(), 
	    					  columnCount);
	    		this.setStudentID(studentID.getText());
	    		this.setStudentName(lName.getText() + ", " +
	    				            fName.getText() + " "  + 
	    							mName.getText());
	    		
	    	}
	    }
	}

	/*
	 * Getters and Setters
	 */
	
	public String getStudentID() {
		return studentID;
	}


	public void setStudentID(String studentID) {
		this.studentID = studentID;
	}


	public String getStudentName() {
		return studentName;
	}


	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
}