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
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import constants.Constants;
import dbclasses.Assignment;
import dbclasses.Database;
import dbclasses.GradeCategory;
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
	private JComboBox<String> 	courseSelector;
	private DefaultTableModel   tableModel;
	private DefaultComboBoxModel<String> dcbm;
	
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
		
		// Prepare Course Selector Box
		dcbm = prepareCourseSelector();
		courseSelector = new JComboBox<String>(dcbm);
		courseSelector.setPrototypeDisplayValue("Computer Science II");
		courseSelector.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) { 
				System.out.println("e.getID() == " + e.getID());
				if (e.getID() == 1001){
					DefaultTableModel dataModel = prepareTable(courseSelector.getSelectedItem().toString());
					coursesTable.setModel(dataModel); 
					//dataModel.fireTableChanged(null);
				}
			  } 
		});

		// Add Buttons to control panel
		coursesControlPanel.add(courseSelector);
		coursesControlPanel.add(newAssignmentBtn);
		coursesControlPanel.add(newCourseBtn);
		coursesControlPanel.setBorder(coursesControlTitle);
		
		// Course Rubric Panel
		coursesRubricPanel = new JPanel();
		coursesRubricPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 10));
		coursesRubricPanel.add(new JLabel("Homework = 35%"));
		coursesRubricPanel.add(new JLabel("Quiz = 20%"));
		coursesRubricPanel.add(new JLabel("Exam = 45%"));
		coursesRubricPanel.setBorder(coursesRubricTitle);
		
		// Combined Control/Rubric Panel
		JPanel combinedControlPanel = new JPanel();
		combinedControlPanel.setLayout(new GridLayout(0,2));
		combinedControlPanel.add(coursesControlPanel);
		combinedControlPanel.add(coursesRubricPanel);
		JPanel combinedControlPanelWrapper = new JPanel(new FlowLayout());
		combinedControlPanelWrapper.add(combinedControlPanel);
		
		// Course Table Panel
		coursesTablePanel = new JPanel();
		coursesTablePanel.setLayout(new GridLayout(1,1));
		coursesTable  = new JTable();
		coursesTableContainer = new JScrollPane(coursesTable);
		coursesTablePanel.add(coursesTableContainer);
		coursesTablePanel.setBorder(coursesTableTitle);
		
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
		new NewAssignmentWizardGUI(Constants.defaultSemester,
		    					   currentCourse,
		    					   Constants.username);
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
		Database db 		 		  = new Database();
		String[] courseData  		  = db.getCourseData		(Constants.defaultSemester, course);
		List<Assignment> assignments  = db.getAssignmentList    (Constants.defaultSemester, course);
		List<GradeCategory> gw 		  = db.getRubric			(Constants.defaultSemester, course);
		List<Student> students 	   	  = new ArrayList<Student>();
		
		// Initialize data array with number of students and assignments
		int rows = courseData .length;		// Number of students
		int cols = assignments.size() + 2;	// Number of assignments + id column + name column
		
		// Initialize object array
		Object[][] tableData = new Object[rows][cols];
		
		// Parse courseData into student names and grades
		for (String studentData : courseData){
			String[] studentInfo = studentData.split(":");
			if (!studentInfo[0].isEmpty()){
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
						tableData[i][j] = student.getLname() + ", " + student.getFname();
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
		if (!assignments.isEmpty()){
			for (int i = 2; i < cols; i++){
				columnNames[i] = assignments.get(i-2).getName().replace("_", " ");
			}
		}
		
		// Create the table and return it
		tableModel = new DefaultTableModel(tableData, columnNames);
		
		// Calculate averages and add to table
		if (!students.isEmpty() || !assignments.isEmpty()){
			Object[] column = getRowAverages(tableModel, gw, assignments, false);
			tableModel.addColumn("Average", column);
			Object[] row = getColumnAverages(tableModel, false);
			tableModel.addRow(row);
		}
		
		// Add a listener for changes in the values of the cells
		tableModel.addTableModelListener(new TableModelListener() {

		      public void tableChanged(TableModelEvent e) {
		    	  if (e.getType() == TableModelEvent.UPDATE) {
	                    tableModel.removeTableModelListener(this);
	                    updateGrade(tableModel, e, gw, assignments, course);
	                    tableModel.addTableModelListener(this);
	              }
		      }
		    });
		
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
	public Object[] getRowAverages(DefaultTableModel model, List<GradeCategory> gcs, List<Assignment> assignments, boolean IS_RECALC){
		
		// Counting variables
		int rowCount 	   = 0;
		int colCount 	   = 0;
		double finalGrade  = 0;
		double totalWeight = 0;
		
		// Initialize row and column counts depending on whether or not table is
		// being built for the first time or is becing recalculated after update.
		// This is to prevent the "Average" col/row from being used in recalculation.
		if (IS_RECALC){
			rowCount 	= model.getRowCount() - 1;
			colCount = model.getColumnCount() - 1;
		} else {
			rowCount 	= model.getRowCount();
			colCount = model.getColumnCount();
		}
		
		// Initialize row object and set title value
		Object[] column = new Object[rowCount];
		
		for (int j = 0; j < rowCount; j++) {
			System.out.println("ROW #" + j);
			for (int i = 2; i < colCount; i++) {
				System.out.println("COL #" + i);
				if ((String) model.getValueAt(j, i) != " "){
					String columnName = model.getColumnName(i);
					String categoryName = null;
					for (Assignment a : assignments){
						if (a.getName().replace("_", " ") == columnName){
							System.out.println(a.getName() + " == " + columnName);
							categoryName = a.getCategory();
							break;
						}
					}
					for (GradeCategory g : gcs){
						if (categoryName.equals(g.getCategory())){
							if (!model.getValueAt(j, i).equals(" ")){
								int grade = Integer.parseInt((String) model.getValueAt(j, i));
								g.addGrade(grade);
								break;
							}
						}
					}
				}
			}
			// Perform calculation
			for (GradeCategory g : gcs){
				System.out.println("CALCULATION: ");
				if (g.getGradeNumber() > 0){
					System.out.println("Adding in category: " + g.getCategory());
					System.out.println("TotalWeight == " + totalWeight);
					totalWeight += g.getWeight();
					System.out.println("TotalWeight += " + g.getWeight());
					System.out.println("FinalGrade == " + finalGrade);
					finalGrade +=  g.getWeightedAverage();
				}
			}
			finalGrade = finalGrade * totalWeight;
			System.out.println("Final Grade == " + finalGrade + " * " + totalWeight);
			double total = Math.round(finalGrade * 100);
			column[j] = total/100;
			
			// Clear all values
			finalGrade  = 0;
			totalWeight = 0;
			for (GradeCategory g : gcs){
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
		
		for (int i = 2; i < colCount; i++) {
			for (int j = 0; j < rowCount; j++) {
				if (model.getValueAt(j, i).toString() != " "){
					if (!model.getValueAt(j, i).equals(" ")){
						sum += Double.parseDouble(model.getValueAt(j, i).toString());
						items += 1;
					}
				}
			}
			// Perform calculation
			double total = sum/items;
			total = Math.round(total * 100);
			row[i] = total/100;
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
	 * 
	 */
	public void updateGrade(DefaultTableModel dtm, TableModelEvent e, List<GradeCategory> gcs, List<Assignment> assignments, String course){
		System.out.println("Cell " + e.getFirstRow() + ", "
                + e.getColumn() + " changed. The new value: "
                + tableModel.getValueAt(e.getFirstRow(),
                e.getColumn()));
        String studentID = (String) tableModel.getValueAt(e.getFirstRow(), 0);
        int fileColumn = e.getColumn() + 2; // Change to +3 when MI is added
        String newGrade =  String.valueOf(tableModel.getValueAt(e.getFirstRow(), e.getColumn()));
        Database db = new Database();
        db.updateGrade(Constants.defaultSemester, 
        			   course, 
        			   studentID,
        			   fileColumn,
        			   newGrade);
        int averagesColumn = dtm.findColumn("Average");
        int classAveragesRow = dtm.getRowCount() - 1;
        
        Object[] column = getRowAverages(dtm, gcs, assignments, true);
        for (int i = 0; i < dtm.getRowCount() - 1; i++){
        	dtm.setValueAt(column[i], i, averagesColumn);
        }
        
        Object[] row = getColumnAverages(dtm, true);
        for (int j = 2; j < dtm.getColumnCount(); j++){
        	dtm.setValueAt(row[j], classAveragesRow, j);
        }
        //dtm.fireTableDataChanged();
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
