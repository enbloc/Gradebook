/* GradebookGUI.java
 * 
 * Gabriel Miller
 * 10/1/2016
 * 
 * Class file to implement the graphical user interface
 * for the grade book application.
 * 
 */

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import constants.Constants;
import dbclasses.Assignment;
import dbclasses.Course;
import dbclasses.Database;
import dbclasses.Grade;
import dbclasses.Semester;
import dbclasses.Student;
import dbclasses.User;

public class GradebookGUI extends JApplet {

	// Class Variables
	private JFrame 				mainFrame;
	private JTabbedPane 		tabbedPane;
	private JPanel 				coursesTab;
	private JPanel 				reportsTab;
	private JPanel 				archiveTab;
	private JPanel 				statisticsTab;
	private JPanel				settingsTab;
	private JPanel 				coursesPanel1;
	private JPanel 				coursesPanel2;
	private JTable 				coursesTable;
	private JScrollPane 		coursesTableContainer;
	private JButton				newAssignmentBtn;
	private JButton				newCourseBtn;
	private JComboBox<String> 	courseSelector;
	private DefaultTableModel   tableModel;
	private DefaultComboBoxModel<String> dcbm;
	
	// Temp Static Objects
	private static User 	currentUser;
	private static Semester currentSemester;
	
	// GUI Class Constructor
	public GradebookGUI(){
		//this.userName = userName;
		prepareGUI();
	}
	
	public static void main(String[] args) {
		new GradebookGUI();
	}
	
	// Initialize the GUI elements
	private void prepareGUI(){
		
		// Initialize the main window
		mainFrame = new JFrame("Gradebook");
		mainFrame.setSize(1366,768);
		mainFrame.addWindowListener(new WindowAdapter() {
	         public void windowClosing(WindowEvent windowEvent){
	            System.exit(0);
	         }        
	      });
		
		//Login Interface
		mainFrame.setVisible(true);
		new LoginGUI(mainFrame, false);
		
		// Create or verify user folder if it exists
		String workingDir = System.getProperty("user.dir");
		Constants.directory = workingDir + "/" + Constants.username; 
		currentUser     = new User    (Constants.username, Constants.directory);
		currentSemester = new Semester("Fall 2016",        Constants.directory);
		Database db = new Database();
		db.createOrVerifyUser();
		
		// Initialize the tabbed area
		tabbedPane = new JTabbedPane();
		
		// Initialize the border titles
		TitledBorder coursesControlTitle, coursesTableTitle;
		coursesControlTitle = BorderFactory.createTitledBorder("Controls");
		coursesTableTitle   = BorderFactory.createTitledBorder("Gradebook");
		
		/*
		 * Courses Tab Setup
		 */
		coursesTab = new JPanel();
		coursesTab.setLayout(new BoxLayout(coursesTab, BoxLayout.Y_AXIS));
		
		// Course Control Panel
		coursesPanel1 	 = new JPanel();
		coursesPanel1.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 10));
		newAssignmentBtn = new JButton("New Assignment");
		newAssignmentBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) { 
			    createAssignment();
			  } 
		});
		newCourseBtn  	 = new JButton("New Course");
		newCourseBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) { 
			    createCourse();
			  } 
		});
		
		// Prepare Course Selector Box
		dcbm = prepareCourseSelector();
		courseSelector = new JComboBox<String>(dcbm);
		courseSelector.setPrototypeDisplayValue("Computer Science II");
		courseSelector.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) { 
				DefaultTableModel dataModel = prepareTable(courseSelector.getSelectedItem().toString());
				coursesTable.setModel(dataModel); 
				dataModel.fireTableChanged(null);
			  } 
		});

		// Add Buttons to control panel
		coursesPanel1.add(courseSelector);
		coursesPanel1.add(newAssignmentBtn);
		coursesPanel1.add(newCourseBtn);
		coursesPanel1.setBorder(coursesControlTitle);
		coursesPanel1.setMaximumSize(coursesPanel1.getPreferredSize());
		
		// Course Table Panel
		coursesPanel2 = new JPanel();
		coursesPanel2.setLayout(new GridLayout(1,1));
		coursesTable  = new JTable();
		coursesTableContainer = new JScrollPane(coursesTable);
		coursesPanel2.add(coursesTableContainer);
		coursesPanel2.setBorder(coursesTableTitle);
		
		// Initialize Courses Tab
		coursesTab.add(coursesPanel1);
		coursesTab.add(coursesPanel2);
		tabbedPane.addTab("Courses", null, coursesTab, "Tooltip for the Courses Panel");
		tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);
		
		/*
		 * Reports Panel Setup
		 */
		reportsTab = (JPanel) makeTextPanel("Reports Panel");
		tabbedPane.addTab("Reports", null, reportsTab, "Tooltip for the Reports Panel");
		tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);
		
		/*
		 * Archive Panel Setup
		 */
		archiveTab = (JPanel) makeTextPanel("Archive Panel");
		tabbedPane.addTab("Archive", null, archiveTab, "Tooltip for the Archive Panel");
		tabbedPane.setMnemonicAt(2, KeyEvent.VK_3);
		
		/*
		 * Statistics Panel Setup
		 */
		statisticsTab = (JPanel) makeTextPanel("Statistics Panel");
		tabbedPane.addTab("Statistics", null, statisticsTab, "Tooltip for the Statistics Panel");
		tabbedPane.setMnemonicAt(3, KeyEvent.VK_4);
		
		/*
		 * Settings Panel Setup
		 */
		settingsTab = (JPanel) makeTextPanel("Settings Panel");
		tabbedPane.addTab("Settings", null, settingsTab, "Tooltip for the Settings Panel");
		tabbedPane.setMnemonicAt(4, KeyEvent.VK_5);
		
		mainFrame.add(tabbedPane);
		mainFrame.setVisible(true);
	}
	
	/*
	 * New Course Wizard
	 * 
	 * This function runs when the "New Course" button is pressed. It first
	 * creates the New Course Wizard window, then takes the user inputs and
	 * creates a course in the directory. It then adds the new course to the
	 * course selector combo box in the controls panel, and displays the new
	 * course on the main page.
	 * 
	 */
	public void createCourse(){
		NewCourseWizardGUI newCourseWizard = new NewCourseWizardGUI();
		String newCourse = newCourseWizard.getCourseName();
		dcbm.addElement(newCourse);
		courseSelector.setSelectedItem(newCourse);
	}
	
	/*
	 * Assignment Creation Function
	 * 
	 * This function runs when the "New Assignment" button is pressed. It first
	 * runs the New Assignment Wizard, then takes the user inputs and creates 
	 * the assignment.txt file in the "Assignments" folder for the course. It then
	 * updates the current table with the new course.
	 * 
	 */
	public void createAssignment(){
		String currentCourse = courseSelector.getSelectedItem().toString();
		NewAssignmentWizardGUI newAssignmentWizard 
									= new NewAssignmentWizardGUI(currentCourse,
																 currentSemester.getSemesterName(),
																 currentUser.getUsername());
		
		prepareTable(courseSelector.getSelectedItem().toString());
		coursesTable.setModel(tableModel); 
		tableModel.fireTableChanged(null);
	}
	
	/*
	 * prepareTable()
	 * 
	 * Retrieve and initialize the values for the Gradebook table.
	 * 
	 * These values are retrieved by first querying the students.txt file to
	 * get the list of students in the class, as well as the list of assignments
	 * in the assignments folder, to get the dimensions for the data array so that
	 * the array can be initialized. The array can then be populated iteratively by
	 * mapping the student IDs to the IDs in each assignment text file and plugging 
	 * those values into the data array.
	 */
	public DefaultTableModel prepareTable(String courseName){
		Course course = new Course( courseName, 
								    currentSemester.getSemesterName(),
								    currentSemester.getFolder());
		
		// Get students list and assignments list
		List<Student>    students       = course.getStudents();
		List<Assignment> assignments    = course.getAllAssignments();
		String[]         assignmentList = course.getAssignmentList();
		
		// Initialize data array with number of students and assignments
		int rows = students.size();			// Number of students
		int cols = assignments.size() + 1;	// Number of assignments + name column
		
		// Initialize object array
		Object[][] tableData = new Object[rows][cols];
		
		// Populate each cell of the table
		for (int i = 0; i < rows; i++){
			for (int j = 0; j < cols; j++){
				
				// TODO MAKE THIS WAY MORE EFFICIENT
				// TODO ADD CHECKS
				// Populate name column
				if (j == 0){
					Student student = students.get(i);
					tableData[i][j] = student.getLname() + ", " + student.getFname();
				} else {
					Assignment assignment = assignments.get(j-1);
					List<Grade> grades = assignment.getGrades();
					Grade grade = grades.get(i);
					tableData[i][j] = grade.getGrade();
				}
			}
		}
		
		// Create the column names array
		String columnNames[] = new String[cols];
		columnNames[0] = "Names";
		for (int i = 1, j = 0; i < cols; i++, j++){
			columnNames[i] = assignmentList[j].replace(".txt", "");
		}
		
		// Create the table and return it
		tableModel = new DefaultTableModel(tableData, columnNames);
		return tableModel;
	}
	
	/*
	 * Prepare the Course Selector JComboBox
	 * 
	 * This function populates the Course Selector Combo Box by pulling the 
	 * course names from the current semester as specified by the user settings.
	 *  
	 */
	public DefaultComboBoxModel<String> prepareCourseSelector(){
		// Prepare Course Selector Box
		Semester semester = new Semester(currentSemester.getSemesterName(), Constants.directory);
		String[] courseNames = semester.getCourses();
		if (courseNames != null){
			dcbm = new DefaultComboBoxModel<String>(courseNames);
		} else {
			dcbm = new DefaultComboBoxModel<String>();
		}
		return dcbm;
	}
	
	/*
	 * Update Student Grade
	 * 
	 * This function responds to the changing of a grade value in the Gradebook table.
	 * 
	 */
	public void updateGrade(){
		// TODO Create update grade function
	}
	
	// Code to create tabbed section
	protected Component makeTextPanel(String text) {
	    JPanel panel = new JPanel(false);
	    JLabel filler = new JLabel(text);
	    filler.setHorizontalAlignment(JLabel.CENTER);
	    panel.setLayout(new GridLayout(1, 1));
	    panel.add(filler);
	    return panel;
	}

}
