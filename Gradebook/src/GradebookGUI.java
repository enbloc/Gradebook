/* GradebookGUI.java
 * 
 * Gabriel Miller
 * 10/1/2016
 * 
 * Class file to implement the graphical user interface
 * for the grade book application.
 * 
 */

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;

public class GradebookGUI extends JApplet {

	// Class Variables
	private JFrame 		mainFrame;
	private JTabbedPane tabbedPane;
	private JPanel 		coursesTab;
	private JPanel 		studentsTab;
	private JPanel 		reportsTab;
	private JPanel 		archiveTab;
	private JPanel 		importTab;
	private JPanel 		settingsTab;
	private JPanel 		coursesPanel1;
	private JPanel 		coursesPanel2;
	private JTable 		coursesTable;
	private JScrollPane coursesTableContainer;
	private JButton		newAssignmentBtn;
	private JButton		newCourseBtn;
	private JComboBox 	coursesSelect;
	
	private String[] columnNames = {
			"Student First Name",
            "Student Last Name",
            "Homework #1",
            "Quiz #1",
            "Midterm Exam"
           	};
	
	private String[] courseNames = {
			"Select Course",
			"Computer Science I",
			"Computer Science II",
			"Data Structures",
			"Computer Algorithms"
			};
	
	private Object[][] data = {
		    {"Kathy", "Smith",
		     new Integer(95), new Integer(85), new Integer(100)},
		    {"John", "Doe",
		     new Integer(95), new Integer(85), new Integer(100)},
		    {"Sue", "Black",
		     new Integer(95), new Integer(85), new Integer(100)},
		    {"Jane", "White",
		     new Integer(95), new Integer(85), new Integer(100)},
		    {"Kathy", "Smith",
		     new Integer(95), new Integer(85), new Integer(100)},
		    {"John", "Doe",
		     new Integer(95), new Integer(85), new Integer(100)},
		    {"Sue", "Black",
		     new Integer(95), new Integer(85), new Integer(100)},
		    {"Jane", "White",
		     new Integer(95), new Integer(85), new Integer(100)},
		    {"Kathy", "Smith",
		     new Integer(95), new Integer(85), new Integer(100)},
		    {"John", "Doe",
		     new Integer(95), new Integer(85), new Integer(100)},
		    {"Sue", "Black",
		     new Integer(95), new Integer(85), new Integer(100)},
		    {"Jane", "White",
		     new Integer(95), new Integer(85), new Integer(100)},
		    {"Kathy", "Smith",
		     new Integer(95), new Integer(85), new Integer(100)},
		    {"John", "Doe",
		     new Integer(95), new Integer(85), new Integer(100)},
		    {"Sue", "Black",
		     new Integer(95), new Integer(85), new Integer(100)},
		    {"Jane", "White",
		     new Integer(95), new Integer(85), new Integer(100)},
		    {"Kathy", "Smith",
		     new Integer(95), new Integer(85), new Integer(100)},
		    {"John", "Doe",
		     new Integer(95), new Integer(85), new Integer(100)},
		    {"Sue", "Black",
		     new Integer(95), new Integer(85), new Integer(100)},
		    {"Jane", "White",
		     new Integer(95), new Integer(85), new Integer(100)},
		    {"Kathy", "Smith",
		     new Integer(95), new Integer(85), new Integer(100)},
		    {"John", "Doe",
		     new Integer(95), new Integer(85), new Integer(100)},
		    {"Sue", "Black",
		     new Integer(95), new Integer(85), new Integer(100)},
		    {"Jane", "White",
		     new Integer(95), new Integer(85), new Integer(100)},
		    {"Kathy", "Smith",
		     new Integer(95), new Integer(85), new Integer(100)},
		    {"John", "Doe",
		     new Integer(95), new Integer(85), new Integer(100)},
		    {"Sue", "Black",
		     new Integer(95), new Integer(85), new Integer(100)},
		    {"Jane", "White",
		     new Integer(95), new Integer(85), new Integer(100)},
		    {"Kathy", "Smith",
		     new Integer(95), new Integer(85), new Integer(100)},
		    {"John", "Doe",
		     new Integer(95), new Integer(85), new Integer(100)},
		    {"Sue", "Black",
		     new Integer(95), new Integer(85), new Integer(100)},
		    {"Jane", "White",
		     new Integer(95), new Integer(85), new Integer(100)},
		    {"Kathy", "Smith",
		     new Integer(95), new Integer(85), new Integer(100)},
		    {"John", "Doe",
		     new Integer(95), new Integer(85), new Integer(100)},
		    {"Sue", "Black",
		     new Integer(95), new Integer(85), new Integer(100)},
		    {"Jane", "White",
		     new Integer(95), new Integer(85), new Integer(100)},
		    {"Kathy", "Smith",
		     new Integer(95), new Integer(85), new Integer(100)},
		    {"John", "Doe",
		     new Integer(95), new Integer(85), new Integer(100)},
		    {"Sue", "Black",
		     new Integer(95), new Integer(85), new Integer(100)},
		    {"Jane", "White",
		     new Integer(95), new Integer(85), new Integer(100)},
		    {"Kathy", "Smith",
		     new Integer(95), new Integer(85), new Integer(100)},
		    {"John", "Doe",
		     new Integer(95), new Integer(85), new Integer(100)},
		    {"Sue", "Black",
		     new Integer(95), new Integer(85), new Integer(100)},
		    {"Jane", "White",
		     new Integer(95), new Integer(85), new Integer(100)},
		    {"Joe", "Brown",
		     new Integer(95), new Integer(85), new Integer(100)}
		};
	
	// GUI Class Constructor
	public GradebookGUI(){
		prepareGUI();
	}
	
	public static void main(String[] args) {
		GradebookGUI gg = new GradebookGUI();
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
		newCourseBtn  	 = new JButton("New Course");
		coursesSelect 	 = new JComboBox(courseNames);
		coursesSelect.setSelectedIndex(0);
		coursesPanel1.add(coursesSelect);
		coursesPanel1.add(newAssignmentBtn);
		coursesPanel1.add(newCourseBtn);
		coursesPanel1.setBorder(coursesControlTitle);
		coursesPanel1.setMaximumSize(coursesPanel1.getPreferredSize());
		
		// Course Table Panel
		coursesPanel2 = new JPanel();
		coursesPanel2.setLayout(new GridLayout(1,1));
		coursesTable = new JTable(data, columnNames);
		coursesTableContainer = new JScrollPane(coursesTable);
		coursesPanel2.add(coursesTableContainer);
		coursesPanel2.setBorder(coursesTableTitle);
		
		// Initialize Courses Tab
		coursesTab.add(coursesPanel1);
		coursesTab.add(coursesPanel2);
		tabbedPane.addTab("Courses", null, coursesTab, "Tooltip for the Courses Panel");
		tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);
		

		/*
		 * Students Panel Setup
		 */
		studentsTab = (JPanel) makeTextPanel("Students Panel");
		tabbedPane.addTab("Students", null, studentsTab, "Tooltip for the Students Panel");
		tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);
		
		/*
		 * Reports Panel Setup
		 */
		reportsTab = (JPanel) makeTextPanel("Reports Panel");
		tabbedPane.addTab("Reports", null, reportsTab, "Tooltip for the Reports Panel");
		tabbedPane.setMnemonicAt(2, KeyEvent.VK_3);
		
		/*
		 * Archive Panel Setup
		 */
		archiveTab = (JPanel) makeTextPanel("Archive Panel");
		tabbedPane.addTab("Archive", null, archiveTab, "Tooltip for the Archive Panel");
		tabbedPane.setMnemonicAt(3, KeyEvent.VK_4);
		
		/*
		 * Import Panel Setup
		 */
		importTab = (JPanel) makeTextPanel("Import Panel");
		tabbedPane.addTab("Import", null, importTab, "Tooltip for the Import Panel");
		tabbedPane.setMnemonicAt(4, KeyEvent.VK_5);
		
		/*
		 * Settings Panel Setup
		 */
		settingsTab = (JPanel) makeTextPanel("Settings Panel");
		tabbedPane.addTab("Settings", null, settingsTab, "Tooltip for the Settings Panel");
		tabbedPane.setMnemonicAt(5, KeyEvent.VK_6);
		
		mainFrame.add(tabbedPane);
		mainFrame.setVisible(true);
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
