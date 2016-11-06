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
import java.io.IOException;
import java.util.ArrayList;
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
import dbclasses.Database;
import dbclasses.Semester;
import dbclasses.Student;
import dbclasses.User;

import com.jcabi.ssh.Shell;

@SuppressWarnings("serial")
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
		new LoginGUI(mainFrame, false, false);
		
		// Get the working directory from server
		try {
			String workingDir = new Shell.Plain(Constants.shell).exec("pwd");
			Constants.directory = workingDir.trim() + "/Gradebook";
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		// Set global constants
		currentUser     = new User(Constants.username, Constants.directory);
		currentSemester = new Semester("Fall 2016",    Constants.directory);
		
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
		if (newCourseWizard.COURSE_CREATED == 1){
			String newCourse = newCourseWizard.getCourseName();
			dcbm.addElement(newCourse);
			courseSelector.setSelectedItem(newCourse);
		}
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
									= new NewAssignmentWizardGUI(currentSemester.getSemesterName(),
																 currentCourse,
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
	 * These values are retrieved by querying the "assignments" file in the database
	 * directory (which contains student names, ID numbers, and grades) and parsing the 
	 * raw data in the function interatively. These values, as well as the values pulled 
	 * from the "assignments_list" file (which contains a list of the assignment names) 
	 * are then mapped to the DefaultTableModel which can then be applied to the JTable.
	 * 
	 */
	public DefaultTableModel prepareTable(String course){
		
		// Get course data
		Database db 		 = new Database();
		String[] courseData  = db.getCourseData		(currentSemester.getSemesterName(), course);
		String[] assignments = db.getAssignmentList (currentSemester.getSemesterName(), course);
		List<Student> students = new ArrayList<Student>();
		
		// Initialize data array with number of students and assignments
		int rows = courseData .length;		// Number of students
		int cols = assignments.length + 2;	// Number of assignments + id column + name column
		
		// Initialize object array
		Object[][] tableData = new Object[rows][cols];
		
		// Parse courseData into student names and grades
		for (String studentData : courseData){
			String[] studentInfo = studentData.split(":");
			Student student = new Student(studentInfo[0],
										  studentInfo[1],
										  studentInfo[2],
										  null);
			
			List<String> grades = new ArrayList<String>();
			for (int i = 3; i < studentInfo.length; i++){
				grades.add(studentInfo[i]);
			}
			student.setGrades(grades);
			students.add(student);
		}
		
		// Populate each cell of the table
		for (int j = 0; j < cols; j++){
			for (int i = 0; i < rows; i++){
				
				// Get the student for the current row
				Student student = students.get(i);
				
				// Populate columns
				if (j == 0){
					tableData[i][j] = student.getId();
				} else if (j == 1){
					tableData[i][j] = student.getLname() + ", " + student.getFname();
				} else {
					List<String> grades = student.getGrades();
					tableData[i][j] = grades.get(j-2);
				}
			}
		}
		
		// Create the column names array
		String columnNames[] = new String[cols];
		columnNames[0] = "ID";
		columnNames[1] = "Name";
		for (int i = 2, j = 0; i < cols; i++, j++){
			columnNames[i] = assignments[j].replace("_", " ");
		}
		
		// Create the table and return it
		tableModel = new DefaultTableModel(tableData, columnNames);
		
		// Calculate averages and add to table
		Object[] column = getRowAverages(tableModel);
		tableModel.addColumn("Average", column);
		Object[] row = getColumnAverages(tableModel);
		tableModel.addRow(row);
		
		return tableModel;
	}
	
	/*
	 * Get Row Averages
	 * 
	 * Get the student average for each assignment based on the grades entered and 
	 * instantiate a column object containing the values to be placed at the right
	 * of the table.
	 * 
	 */
	public Object[] getRowAverages(DefaultTableModel model){
		int items = 0;
		int sum   = 0;
		
		// Initialize row object and set title value
		Object[] column = new Object[model.getRowCount()];
		
		for (int j = 0; j < model.getRowCount(); j++) {
			for (int i = 2; i < model.getColumnCount(); i++) {
				if ((String) model.getValueAt(j, i) != " "){
					sum += Integer.parseInt((String) model.getValueAt(j, i));
					items += 1;
				}
			}
			// Perform calculation
			column[j] = sum/items;
			sum = 0;
			items = 0;
		}
		return column;	
	}
	
	/*
	 * Get Column Averages
	 * 
	 * Get the class average for each assignment based on the grades entered and 
	 * instantiate a row object containing the values to be placed at the bottom
	 * of the table.
	 * 
	 */
	public Object[] getColumnAverages(DefaultTableModel model){
		int items = 0;
		int sum   = 0;
		
		// Initialize row object and set title value
		Object[] row = new Object[model.getColumnCount()];
		row[0] = " ";
		row[1] = "AVERAGE:";
		
		for (int i = 2; i < model.getColumnCount(); i++) {
			for (int j = 0; j < model.getRowCount(); j++) {
				if (model.getValueAt(j, i).toString() != " "){
					sum += Integer.parseInt(model.getValueAt(j, i).toString());
					items += 1;
				}
			}
			// Perform calculation
			row[i] = sum/items;
			sum = 0;
			items = 0;
		}
		return row;	
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
		Database db = new Database();
		String[] courseNames = db.getCourses(currentSemester.getSemesterName());
		
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
