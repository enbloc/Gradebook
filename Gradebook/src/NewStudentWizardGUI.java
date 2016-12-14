import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import constants.Global;
import dbclasses.Database;

/**
 * This is the class that creates the New Student Wizard prompt after 
 * the "Add Student" button is selected on any page. It contains several
 * fields through which the user inputs the necessary data to create 
 * a new student. Upon completion of the form, the new student is inserted
 * into the database and the main Gradebook GUI is updated.
 * 
 * @author Gabriel Miller
 * @version 1.0 10/25/2016
 */
public class NewStudentWizardGUI {

	private String studentID;
	private String studentName;
	private JFrame mainFrame;
	private JPanel mainPanel;
	private JPanel labels;
	private JPanel controls;

	/**
	 * Flag to keep track of whether or not a student was added.
	 */
	public int STUDENT_CREATED = 0;  // 0 == no student created, 1 == student created
	
	/**
	 * Class constructor that initializes the interface.
	 * 
	 * @param semester current semester
	 * @param course current course
	 * @param columnCount number of columns currently in the database file so that the 
	 * 		  entry may be formatted correctly
	 * @param STUDENT_INFO_EMPTY flag that is set to true if this instance of the student
	 * 		  creation wizard is being recalled because of empty fields so that the appropriate
	 * 		  error message can be displayed
	 */
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
	    		System.out.println("STUDENT NAME: " + fName.getText() + " " + mName.getText() + " " + lName.getText());
	    		Database db = new Database();
	    		db.addStudent(semester, 
	    					  course, 
	    					  studentID.getText(),
	    					  fName.getText(),
	    					  lName.getText(),
	    					  mName.getText(), 
	    					  columnCount,
	    					  Global.IS_NEW_COURSE);
	    		this.setStudentID(studentID.getText());
	    		this.setStudentName(lName.getText() + ", " +
	    				            fName.getText() + " "  + 
	    							mName.getText());
	    		STUDENT_CREATED = 1;
	    	}
	    }
	}

	/*
	 * Getters and Setters
	 */
	/**
	 * Getter for student ID
	 * @return student ID
	 */
	public String getStudentID() {
		return studentID;
	}

	/**
	 * Setter for student ID
	 * @param studentID student ID
	 */
	public void setStudentID(String studentID) {
		this.studentID = studentID;
	}

	/**
	 * Getter for student name
	 * @return student name
	 */
	public String getStudentName() {
		return studentName;
	}

	/**
	 * Setter for student name
	 * @param studentName student name
	 */
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
}