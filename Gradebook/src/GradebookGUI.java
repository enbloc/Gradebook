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
import javax.swing.ProgressMonitor;
import javax.swing.border.TitledBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import constants.Constants;
import constants.Global;
import dbclasses.Assignment;
import dbclasses.Database;
import dbclasses.GradeCategory;
import dbclasses.GradeRange;
import dbclasses.Student;

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
	private JPanel 				coursesControlPanel;
	private JPanel				coursesRubricPanel;
	private JPanel 				coursesTablePanel;
	private JTable 				coursesTable;
	private JScrollPane 		coursesTableContainer;
	private JButton				newAssignmentBtn;
	private JButton				newCourseBtn;
	private JButton				addStudentBtn;
	private JButton				editRubricBtn;
	private JComboBox<String> 	courseSelector;
	private DefaultTableModel   tableModel;
	private DefaultComboBoxModel<String> dcbm;
	private LoadingWindow		loadingWindow;
	
	// Flags
	private int     ASSIGNMENTS_ADDED = 0;
	private int 	STUDENTS_ADDED = 0;
	private boolean	UPDATE_UI    = true;
	
	// Static variables
	static int loadingProgress = 0;
	
	// GUI Class Constructor
	public GradebookGUI(){
		prepareGUI();
	}
	
	public static void main(String[] args) {
		new GradebookGUI();
	}
	
	// Initialize the GUI elements
	private void prepareGUI(){
		
		// Initialize the main window
		mainFrame = new JFrame("Gradebook");
		mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
		//mainFrame.setUndecorated(true);
		mainFrame.addWindowListener(new WindowAdapter() {
	         public void windowClosing(WindowEvent windowEvent){
	            System.exit(0);
	         }        
	      });
		
		//Login Interface
		mainFrame.setVisible(true);
		loadingWindow = new LoadingWindow();
		new LoginGUI(mainFrame, loadingWindow, false, false);
		
		// Get the working directory from server
		try {
			String workingDir = new Shell.Plain(Constants.shell).exec("pwd");
			Constants.directory = workingDir.trim() + "/Gradebook";
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		// Initialize the tabbed area
		tabbedPane = new JTabbedPane();
		
		// Initialize the border titles
		TitledBorder coursesControlTitle, coursesRubricTitle, coursesTableTitle;
		coursesControlTitle = BorderFactory.createTitledBorder("Controls");
		coursesRubricTitle  = BorderFactory.createTitledBorder("Rubric");
		coursesTableTitle   = BorderFactory.createTitledBorder("Gradebook");
		
		/*
		 * Courses Tab Setup
		 */
		coursesTab = new JPanel();
		coursesTab.setLayout(new BoxLayout(coursesTab, BoxLayout.Y_AXIS));
		
		// Course Control Panel
		coursesControlPanel 	 = new JPanel();
		coursesControlPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 10));
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
		addStudentBtn = new JButton("Add Student");
		addStudentBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) { 
			    addStudent();
			  }
		});
		
		// Prepare Course Selector Box
		dcbm = prepareCourseSelector();
		courseSelector = new JComboBox<String>(dcbm);
		courseSelector.setPrototypeDisplayValue("Computer Science II");
		courseSelector.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) { 
				if (e.getID() == 1001){
					DefaultTableModel dataModel = prepareTable(courseSelector.getSelectedItem().toString());
					coursesTable.setModel(dataModel); 
				}
			  } 
		});

		// Add Buttons to control panel
		coursesControlPanel.add(courseSelector);
		coursesControlPanel.add(newAssignmentBtn);
		coursesControlPanel.add(newCourseBtn);
		coursesControlPanel.add(addStudentBtn);
		coursesControlPanel.setBorder(coursesControlTitle);
		
		// Course Rubric Panel
		coursesRubricPanel = new JPanel();
		coursesRubricPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 10));
		coursesRubricPanel.setBorder(coursesRubricTitle);
		prepareRubricPanel(courseSelector.getSelectedItem().toString());
		
		// Combined Control/Rubric Panel
		JPanel combinedControlPanel = new JPanel();
		combinedControlPanel.setLayout(new GridLayout(0,2));
		combinedControlPanel.add(coursesControlPanel);
		combinedControlPanel.add(coursesRubricPanel);
		JPanel combinedControlPanelWrapper = new JPanel(new FlowLayout());
		combinedControlPanelWrapper.add(combinedControlPanel);
		
		// Close LoadingWindow
		loadingWindow.closeWindow();
		
		// Course Table Panel
		coursesTablePanel = new JPanel();
		coursesTablePanel.setLayout(new GridLayout(1,1));
		coursesTable  = new JTable();
		coursesTableContainer = new JScrollPane(coursesTable);
		coursesTablePanel.add(coursesTableContainer);
		coursesTablePanel.setBorder(coursesTableTitle);
		coursesTable.setModel(prepareTable(courseSelector.getSelectedItem().toString()));
		
		// Center the values of the table
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment( JLabel.CENTER );
		for (int i = 0; i < tableModel.getColumnCount(); i++ ){
			if (i != 1){ 	// Do not center name column
				coursesTable.getColumnModel().getColumn(i).setCellRenderer( centerRenderer );
			}
		}
		
		JPanel courseTabWrapper = new JPanel();
		courseTabWrapper.setLayout(new FlowLayout());
		
		// Initialize Courses Tab
		coursesTab.add(combinedControlPanelWrapper);
		coursesTab.add(coursesTablePanel);
		courseTabWrapper.add(coursesTab);
		tabbedPane.addTab("Courses", null, courseTabWrapper, "Tooltip for the Courses Panel");
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
		NewAssignmentWizardGUI newAssignmentWizard = new NewAssignmentWizardGUI(Constants.defaultSemester,
		    					   												currentCourse,
		    					   												false);
		
		// Create the new assignment column
		// TODO Create a TableColumn and set Model Index to save some work
		TableColumn tableColumn = new TableColumn();
		Object[]    modelColumn = new Object[tableModel.getRowCount()];  
		tableColumn.setHeaderValue(newAssignmentWizard.getNewAssignment());
		for (int i = 0; i < modelColumn.length; i++){
			modelColumn[i] = " ";
		}
		coursesTable.addColumn(tableColumn);		// Add to JTable
		
		// Add column to table model
		UPDATE_UI = false;  // Turn table update flag OFF
		tableModel.addColumn(newAssignmentWizard.getNewAssignment(), modelColumn);
		UPDATE_UI = true;   // Turn table update flag ON again
		
		// Although we can move a column in a JTable after adding it, we cannot do the same     //
		// with the underlying DefaultTableModel (which only allows you to append columns).     //
		// Therefore, we have to calculate how many moves we must make to get from the          // 
		// table model to the desired JTable by getting a count of the number of courses        //
		// that have been added since the last table update, and move columns back accordingly. //
		
		ASSIGNMENTS_ADDED = (tableModel.getColumnCount() - 1) - tableModel.findColumn("Grade");	// Calculate the number of courses added since update
		for (int i = ASSIGNMENTS_ADDED; i > 0; i--){													
			coursesTable.moveColumn(coursesTable.getColumnCount() - i,			// Get the new column at index "last - i"	 
									coursesTable.getColumnCount() - (i+2));		// Move column to index "last - (i+1)", on the left side of "Average" column
		}
		coursesTable.setModel(tableModel); 
		
		// Center the values of the table
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment( JLabel.CENTER );
		for (int i = 0; i < tableModel.getColumnCount(); i++ ){
			if (i != 1){ 	// Do not center name column
				coursesTable.getColumnModel().getColumn(i).setCellRenderer( centerRenderer );
			}
		}
	}
	
	/*
	 * Add Student Function
	 * 
	 * This function runs when the "Add Student" button is pressed. It first
	 * runs the New Student Wizard, then takes the user inputs and creates a new 
	 * line in the "assignments" file on the server. It then updates the table 
	 * with the new student information.
	 * 
	 */
	public void addStudent(){
		String currentCourse = courseSelector.getSelectedItem().toString();
		NewStudentWizardGUI newStudentWizard = new NewStudentWizardGUI(Constants.defaultSemester, 
																	   currentCourse,
																	   String.valueOf(tableModel.getColumnCount()),
																	   false);
		// Create row object from Student Wizard
		int columnCount = tableModel.getColumnCount();
		Object[] row = new Object[columnCount];
		row[0] = newStudentWizard.getStudentID();
		row[1] = newStudentWizard.getStudentName();
		for (int i = 2; i < row.length; i++){
			row[i] = " ";
		}
		
		// Add row to table model
		UPDATE_UI = false;  // Turn table update flag OFF
		tableModel.addRow(row);
		tableModel.moveRow(tableModel.getRowCount() - 1, 
						   tableModel.getRowCount() - 1, 
						   tableModel.getRowCount() - 2);
		UPDATE_UI = true;   // Turn table update flag ON again
		STUDENTS_ADDED += 1;
		coursesTable.setModel(tableModel);
		
		
	}
	
	/*
	 * Edit Rubric Function
	 * 
	 * This function is called whenever the "Edit Rubric" button is pressed. It runs a 
	 * wizard that provides the list of current rubric grade categories (if any) and gives 
	 * the user the option to edit them or add new categories if they want.
	 * 
	 */
	public void editRubric(){
		String currentCourse = courseSelector.getSelectedItem().toString();
		
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
		
		Component parent = (Component) this;
        ProgressMonitor monitor = new ProgressMonitor(parent, "Loading Progress", "Getting Started...", 0, 100);        
        
		// Initialize Database
		Database db = new Database();
		
		// Retrieve course data
		loadingProgress = 0;
		monitor.setProgress(loadingProgress);
		monitor.setNote("Loading course data...");
		String[] courseData = db.getCourseData(Constants.defaultSemester, 
											   course);
		
		// Retrieve Assignment List
		loadingProgress = 20;
		monitor.setProgress(loadingProgress);
		monitor.setNote("Loading assignment data...");
		Global.assignments = db.getAssignmentList(Constants.defaultSemester, 
												  course);
		
		// Retrieve Rubric Data
		loadingProgress = 40;
		monitor.setProgress(loadingProgress);
		monitor.setNote("Loading rubric data...");
		Global.gradeCategories = db.getRubric(Constants.defaultSemester, 
											  course);
		
		// Retrieve Letter Grade Ranges
		loadingProgress = 60;
		monitor.setProgress(loadingProgress);
		monitor.setNote("Loading grade scheme data...");
		Global.gradeRanges = db.getGradeScheme(Constants.defaultSemester, 
											   course);
		
		// Begin loading coursebook interface
		loadingProgress = 80;
		monitor.setProgress(loadingProgress);
		monitor.setNote("Loading interface...");
		
		List<Student> students = new ArrayList<Student>();
		STUDENTS_ADDED = 0;	// Reset counter for students added since table update.
		
		// Initialize data array with number of students and assignments
		int rows = courseData.length;				// Number of students
		int cols = Global.assignments.size() + 2;	// Number of assignments + id column + name column
		
		// Initialize object array
		Object[][] tableData = new Object[rows][cols];
		
		// Parse courseData into student names and grades
		for (String studentData : courseData){
			String[] studentInfo = studentData.split(":", -2);
			if (!studentInfo[0].isEmpty()){
				Student student = new Student(studentInfo[0],
											  studentInfo[1],
											  studentInfo[2],
											  studentInfo[3]);
				
				List<String> grades = new ArrayList<String>();
				for (int i = 4; i < studentInfo.length; i++){
					grades.add(studentInfo[i]);
				}
				student.setGrades(grades);
				students.add(student);
			}
		}
		
		// Populate each cell of the table
		if (!students.isEmpty()){
			for (int j = 0; j < cols; j++){
				for (int i = 0; i < rows; i++){
					
					// Get the student for the current row
					Student student = students.get(i);
					
					// Populate columns
					if (j == 0){
						tableData[i][j] = student.getId();
					} else if (j == 1){
						tableData[i][j] = student.getLname() + ", " + 
									 	  student.getFname() + " " + 
									 	  student.getMi();
					} else {
						List<String> grades = student.getGrades();
						tableData[i][j] = grades.get(j-2);
					}
				}
			}
		}
		
		// Create the column names array
		String columnNames[] = new String[cols];
		columnNames[0] = "ID";
		columnNames[1] = "Name";
		if (!Global.assignments.isEmpty()){
			for (int i = 2; i < cols; i++){
				columnNames[i] = Global.assignments.get(i-2).getName().replace("_", " ");
			}
		}
		
		// Create the table and make appropriate columns uneditable
		tableModel = new DefaultTableModel(tableData, columnNames){
			@Override
		    public boolean isCellEditable(int row, int column)
		    {
				boolean IS_EDITABLE = true;
				
				if (row    == tableModel.getRowCount() - 1     ||
					column == tableModel.findColumn("Grade")   ||
					column == tableModel.findColumn("Average") ||
					column == 0 || 
					column == 1 ){
					IS_EDITABLE = false;
				}
					
				return IS_EDITABLE;
		    }
		};
		
		// Calculate averages and add to table
		if (!students.isEmpty() || !Global.assignments.isEmpty()){
			Object[] column = getRowAverages(tableModel, false);
			tableModel.addColumn("Average", column);
			Object[] row = getColumnAverages(tableModel, false);
			tableModel.addRow(row);
			Object[] letterGradeColumn = getLetterGrades(column);
			tableModel.addColumn("Grade", letterGradeColumn);
		}
		
		// Add a listener for changes in the values of the cells
		tableModel.addTableModelListener(new TableModelListener() {

		      public void tableChanged(TableModelEvent e) {
		    	  if (UPDATE_UI){
			    	  if (e.getType() == TableModelEvent.UPDATE) {
		                    tableModel.removeTableModelListener(this);		// Remove the listener so that it is not called when JTable values change
		                    updateGrade(tableModel, e, course);				// Update the grade on server with separate thread while interface updates
		                    tableModel.addTableModelListener(this);			// Replace the table listener again after the updating is complete
		              }
		    	  }
		      }
		    });
		
		loadingProgress = 100;
		monitor.setProgress(loadingProgress);
		monitor.setNote("Done!");
		monitor.close();
		
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
	public Object[] getRowAverages(DefaultTableModel model, boolean IS_RECALC){
		
		// Counting variables
		int rowCount 	   = 0;
		int colCount 	   = 0;
		double finalGrade  = 0;
		double totalWeight = 0;
		
		// Initialize row and column counts depending on whether or not table is
		// being built for the first time or is becing recalculated after update.
		// This is to prevent the "Average" col/row from being used in recalculation.
		if (IS_RECALC && ASSIGNMENTS_ADDED == 0){
			rowCount = model.getRowCount() - 1;
			colCount = model.getColumnCount() - 2;
		} else if (ASSIGNMENTS_ADDED > 0 && STUDENTS_ADDED == 0){
			rowCount = model.getRowCount() - 1;
			colCount = model.getColumnCount() - 1;
		} else if (ASSIGNMENTS_ADDED == 0 && STUDENTS_ADDED > 0){
			rowCount = model.getRowCount();
			colCount = model.getColumnCount() - 2;
		} else {
			rowCount = model.getRowCount();
			colCount = model.getColumnCount();
		}
		
		// Initialize row object and set title value
		Object[] column = new Object[rowCount];
		
		// For each row in table
		for (int j = 0; j < rowCount; j++) {
			
			System.out.println("ROW #" + j);
			// For each column after "Names"
			for (int i = 2; i < colCount; i++) {
				
				System.out.println("COL #" + i);
				// If the cell value is not blank or a
				// calculated column such as "Average"
				if (!(model.getValueAt(j, i) == null) &&
					!model.getValueAt(j, i).toString().isEmpty() &&
					!model.getValueAt(j, i).equals(" ") &&
					!model.getColumnName(i).equals("Average") && 
					!model.getColumnName(i).equals("Grade")){
					
					System.out.println("CELL == " + model.getValueAt(j, i).toString());
					
					// Get the column name of the cell
					String columnName = model.getColumnName(i);
					String categoryName = null;
					
					// Find the assignment that matches the columnName, and set categoryName == to the assignments category
					for (Assignment a : Global.assignments){
						if (a.getName().replace("_", " ").equals(columnName)){
							categoryName = a.getCategory();
							break;
						}
					}
					
					// Find the GradeCategory that matches the categoryName
					for (GradeCategory g : Global.gradeCategories){
						if (categoryName.equals(g.getCategory())){
							
							// Add the cell value to GradeCategory for calculations
							if (!model.getValueAt(j, i).equals(" ") && 
								!model.getValueAt(j, i).toString().isEmpty()){
								double grade = Double.parseDouble((String.valueOf(model.getValueAt(j, i)).replace(" ", "")));
								g.addGrade(grade);
								break;
							}
						}
					}
				}
			}
			// Perform calculation
			for (GradeCategory g : Global.gradeCategories){
				if (g.getGradeNumber() > 0){
					System.out.println("g.getGradeNumber == " + g.getGradeNumber());
					totalWeight += g.getWeight();
					finalGrade +=  g.getWeightedAverage();
				}
			}
			// Round of the final grade to 2 decimals
			System.out.println("finalGrade == " + finalGrade + " * " + totalWeight);
			finalGrade = (finalGrade / (totalWeight * 100)) * 100;
			double total = Math.round(finalGrade * 100);
			column[j] = total/100;
			
			// Clear all values
			finalGrade  = 0;
			totalWeight = 0;
			for (GradeCategory g : Global.gradeCategories){
				g.clearValues();
			}
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
	public Object[] getColumnAverages(DefaultTableModel model, boolean IS_RECALC){
		
		// Counting variables
		int rowCount = 0;
		int colCount = 0;
		int items 	 = 0;
		double sum   = 0;
		
		// Initialize row and column counts depending on whether or not table is
		// being built for the first time or is becing recalculated after update.
		// This is to prevent the "Average" col/row from being used in recalculation.
		if (IS_RECALC){
			rowCount = model.getRowCount() - 1;
			colCount = model.getColumnCount();
		} else {
			rowCount = model.getRowCount();
			colCount = model.getColumnCount();
		}
		
		// Initialize row object and set title value
		Object[] row = new Object[colCount];
		row[0] = " ";
		row[1] = "AVERAGE:";
		
		// Tally the sum and number of items   //
		// in each column to calculate average //
		for (int i = 2; i < colCount; i++) {
			for (int j = 0; j < rowCount; j++) {
				if (!(model.getValueAt(j, i) == null) &&
					!model.getValueAt(j, i).equals(" ") && 
					!model.getValueAt(j, i).toString().isEmpty() &&
					!model.getColumnName(i).equals("Grade")){
						sum += Double.parseDouble(model.getValueAt(j, i).toString());
						items += 1;
				}
			}
			// Perform calculation
			double total = sum/items;
			total = Math.round(total * 100);
			
			// Check if at the Grade Column
			if (model.getColumnName(i).equals("Grade")){
				for (GradeRange gr : Global.gradeRanges){
					if (gr.isInRange(Double.valueOf(row[i-1].toString()))){
						row[i] = gr.getLetterGrade();
						break;
					}
				}
			} else {
				row[i] = total/100;
			}
			
			sum = 0;
			items = 0;
		}
		return row;	
	}
	
	/*
	 * Get Letter Grades
	 * 
	 * This function takes the individual student averages and converts them to
	 * letter grades, based on the information contained in the gradescheme file.
	 * 
	 */
	public Object[] getLetterGrades(Object[] averages){
		
		Object[] letterGrades = new Object[averages.length];
		for (int i = 0; i < averages.length; i++){
			for (GradeRange gr : Global.gradeRanges){
				if (gr.isInRange(Double.valueOf(averages[i].toString()))){
					letterGrades[i] = gr.getLetterGrade();
					break;
				}
			}
		}
		return letterGrades;
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
		String[] courseNames = db.getCourses(Constants.defaultSemester);
		
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
	 * This is achieved by performing the necessary table recalculations (such as averages
	 * and course grades) while the grade is updated on the server using a separate thread
	 * in order to avoid the overhead of an SSH call.
	 * 
	 */
	public void updateGrade(DefaultTableModel dtm, TableModelEvent e, String course){

		// File Insertion variables
		String newGrade    = String.valueOf(tableModel.getValueAt(e.getFirstRow(), e.getColumn()));  
        String studentID   = String.valueOf(tableModel.getValueAt(e.getFirstRow(), 0));  
        
        // Column/row calculation variables
        int averagesRow    = dtm.getRowCount() - 1;			// Index of the row that contains the class averages
        int averagesColumn = dtm.findColumn("Average");		// Index in table model that contains the "Average" column
        int gradeColumn    = dtm.findColumn("Grade"); 		// Index in table model that contains the "Grade" column
        int fileColumn;										// Stores the column index where the new grade should be inserted in the file
        
        // The count of the number of columns to the right of the //
        // "Grade" column. This also represents the number of     //
        // assignments added since the table was populated from   //
        // the file server. This is critical in the calculation.  //
        ASSIGNMENTS_ADDED = (dtm.getColumnCount() - 1) - gradeColumn;
        
        // Adjust the value of fileColumn depending on //
        // whether or not assignments have been added  //
        // (Offset for the calculated columns)         //
        if (ASSIGNMENTS_ADDED > 0)
        	fileColumn = e.getColumn() + 2;	
        else
        	fileColumn = e.getColumn() + 3;
        
        // Create new thread and insert into DB
        Database db = new Database();
        db.updateGrade(Constants.defaultSemester, 
        			   course, 
        			   studentID,
        			   fileColumn,
        			   newGrade);
        
        // Rebuild "Average" column with new values
        Object[] column = getRowAverages(dtm, true);
        for (int i = 0; i < dtm.getRowCount() - 1; i++){
        	dtm.setValueAt(column[i], i, averagesColumn);
        }
        
        // Reset the flag to 0 in order to permit other //
        // computations, such as standard (in-place)    //
        // grade changes to run without moving columns  //
        ASSIGNMENTS_ADDED = 0;
        
        // Rebuild "Class Average" row with new values
        Object[] row = getColumnAverages(dtm, true);
        for (int j = 2; j < dtm.getColumnCount(); j++){
        	dtm.setValueAt(row[j], averagesRow, j);
        }
        
        // Rebuild "Grade" column with new values
        Object[] letterGradeColumn = getLetterGrades(column);
        for (int n = 0; n < dtm.getRowCount() - 1; n++){
        	dtm.setValueAt(letterGradeColumn[n], n, gradeColumn);
        }
	}
	
	/*
	 * Prepare Rubric Panel
	 * 
	 * Set all of the labels for the rubric panel as per 
	 * the data stored on the rubric server file.
	 * 
	 */
	public void prepareRubricPanel(String course){
		coursesRubricPanel.removeAll();
		Database db = new Database();
		Global.gradeCategories = db.getRubric(Constants.defaultSemester, course);
		for (GradeCategory gc : Global.gradeCategories){
			int weight = (int) (gc.getWeight() * 100);
			coursesRubricPanel.add(new JLabel(gc.getCategory() + " = " + weight + "%"));
		}
		coursesRubricPanel.add(new JButton("Edit Rubric"));
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
