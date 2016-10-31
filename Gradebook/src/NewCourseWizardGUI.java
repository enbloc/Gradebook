/*
 * NewCourseWizard.java
 * 
 * This is the class that creates the New Course Wizard prompt after 
 * the New Course button is selected on any page. It contains several
 * fields through which the user inputs the necessary data to create 
 * a new course.
 * 
 * Gabriel Miller
 * 10/25/2016
 */
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import constants.Constants;
import dbclasses.Semester;
import dbclasses.User;

public class NewCourseWizardGUI {

	private String  newCourse;
	private JFrame  mainFrame;
	private JPanel  mainPanel;
	private JPanel  labels;
	private JPanel  controls;
	private JComboBox semesters;
	private JTextField semesterField;
	private JTextField courseField;
	private ButtonGroup  bg;
	private JRadioButton rbNew;
	private JRadioButton rbExists;
	
	public NewCourseWizardGUI(){
		
		mainPanel = new JPanel(new BorderLayout(5,5));

		// Get the existing semesters for the JComboBox
		User user = new User("gmiller", Constants.directory);
		String[] semList = user.getSemesters();
		
		// Set up for labels panel
        labels   = new JPanel(new GridLayout(0,1,2,5));
        rbNew    = new JRadioButton("New Semester:");
        rbExists = new JRadioButton("Existing Semester:");
        rbExists.setSelected(true);
        
        bg = new ButtonGroup();
        bg.add(rbExists);
        bg.add(rbNew);
        
        /* 
         * Here the radio buttons are set with listeners
         * to disable their counterpart input box when they
         * are selected. So when the "New Semester" radio
         * button is selected, the "Existing Semester" combo
         * box is disabled, and vice-versa.
         */
        rbExists.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) { 
			    semesterField.setEditable(false);
			    semesters.setEnabled(true);
			  } 
		});
        rbNew.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) { 
			    semesters.setEnabled(false);
			    semesterField.setEditable(true);
			  } 
		});
        
        labels.add(rbExists);
        labels.add(rbNew);
        labels.add(new JLabel("       Course Name:"));
        mainPanel.add(labels, BorderLayout.WEST);

        // Set up for controls panel
        controls = new JPanel(new GridLayout(0,1,2,5));
        semesters = new JComboBox(semList);
        semesterField = new JTextField();
        courseField   = new JTextField();
        semesterField.setEditable(false);
        controls.add(semesters);
        controls.add(semesterField);
        controls.add(courseField);
        mainPanel.add(controls, BorderLayout.CENTER);

        // Generate Wizard
        int result = JOptionPane.showConfirmDialog(
            mainFrame, mainPanel, "New Course", JOptionPane.OK_CANCEL_OPTION);
        
        // Handle course creation
	    if (result == JOptionPane.OK_OPTION) {
	    	if (semesterField.getText() != null &&
	    		  courseField.getText() != null	){
	    		    newCourse = courseField.getText();
	    			if (rbNew.isSelected()){
	    				Semester newSemester = user.addSemester(semesterField.getText());
	    				newSemester.addCourse(newCourse);
	    			} else if (rbExists.isSelected()) {
	    				Semester newSemester = user.addSemester(semesters.getSelectedItem().toString());
	    				newSemester.addCourse(newCourse);
	    			} else {
	    				// TODO Error catch
	    			}
	    	} else {
	    		// TODO Error message if no input is found
	    	}
	    }
	}
	
	// Getter for the name of the new course 
	public String getCourseName(){
		return newCourse;
	}
}
