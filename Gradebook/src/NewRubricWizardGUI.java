import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import constants.Constants;
import constants.Global;
import dbclasses.Assignment;
import dbclasses.Database;
import dbclasses.GradeCategory;

/**
 * This is the class that creates the New/Edit Rubric Wizard prompt after 
 * the Edit Rubric button is selected on any page. It is populated with the
 * current list of grade categories (Homework, Quiz, Exam, etc) with their
 * corresponding weights listed as a percent (out of 100). The wizard allows 
 * for these values to be edited, deleted, or created, with the caveat that 
 * all category weights must add up to 100.
 * 
 * @author Gabriel Miller
 * @version 1.0 11/14/2016
 */
public class NewRubricWizardGUI {
	
	private JFrame mainFrame;
	private JPanel mainPanel;
	private JPanel tablePanel;
	private JPanel controlPanel;
	private JTable table;
	private JButton addBtn;
	private JButton delBtn;
	private JScrollPane tableContainer;
	private DefaultTableModel tableModel;
	private DefaultTableCellRenderer centerRenderer;
	
	// Flags
	/**
	 * Flag that is set to <code>true</code> if the rubric was changed
	 * and therefore grades must be recalculated.
	 */
	public  boolean RECALCULATE_GRADES = false;
	private boolean CATEGORY_ADDED = false;
	
	/**
	 * Class constructor that handles the initialization of the wizard interface.
	 */
	public NewRubricWizardGUI(){
		
		// Initialize Panels
		mainPanel    = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		tablePanel   = new JPanel(new FlowLayout());
		controlPanel = new JPanel(new FlowLayout());
		
		// Create and Initialize JTable
		table 		   = new JTable();
		tableContainer = new JScrollPane(table);
		tablePanel.add(tableContainer);
		table.setModel(prepareTable());
		table.setPreferredSize(new Dimension(200,150));
		table.setPreferredScrollableViewportSize(table.getPreferredSize());
		table.setFillsViewportHeight(true);
		
		// Center the values of the table
		centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment( JLabel.CENTER );
		for (int i = 0; i < tableModel.getColumnCount(); i++ ){
			table.getColumnModel().getColumn(i).setCellRenderer( centerRenderer );
		}
	    
		// Create and set up controls panel
		addBtn = new JButton("Add Category");
		addBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) { 
			    addCategory();
			  } 
		});
		delBtn = new JButton("Delete Category");
		delBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) { 
			    deleteCategory();
			  } 
		});
		controlPanel.add(addBtn);
		controlPanel.add(delBtn);
		
		// Compile main panel
		mainPanel.add(tablePanel);
		mainPanel.add(controlPanel);
		
	    // Generate Wizard
	    String[] optionButtons = {"Done", "Cancel"};
	    int result = JOptionPane.showOptionDialog(mainFrame, 
	    										  mainPanel,
	    										  "Edit Rubric", 
	    										  JOptionPane.OK_CANCEL_OPTION, 
	    										  JOptionPane.PLAIN_MESSAGE, 
	    										  null, 
	    										  optionButtons, 
	    										  optionButtons[0]);
	    
	    // Handle Option Button Press
	    if (result == 0) {
	    	// "Done" Button Pressed
	    	boolean UNIQUE_CATEGORIES   = true;		// Tracks whether or not there are any repeat categories in the wizard
	    	boolean PERCENTAGE_TOTAL    = true;		// Tracks whether or not the "Total" sum of the percentages == 100
	    	boolean ASSIGNMENT_CHANGED  = false;	// Tracks whether or not a category used by a current assignment has changed 
	    	boolean GC_ASSIGNMENT_MATCH = false;	// Tracks whether or not a match is made between the new GradeCategories and an existing assignment
	    	List<Assignment> affectedAssignments = new ArrayList<Assignment>();
	    	
	    	// Check to see if all values in "Category" are unique
	    	for (int i = 0; i < tableModel.getRowCount() - 1; i++){
	    		String categoryName = tableModel.getValueAt(i, 0).toString();
	    		
	    		for (int j = (i+1); j < tableModel.getRowCount(); j++){
	    			if (categoryName.equals(tableModel.getValueAt(j, 0))){
	    				UNIQUE_CATEGORIES = false;
	    			}
	    		}
	    	}
	    	
	    	// Check to see if the total percentage == 100%
	    	if (!tableModel.getValueAt(tableModel.getRowCount() - 1, 1).toString().equals("100")){
	    		PERCENTAGE_TOTAL = false;
	    	}
	    	
	    	// If Categories are unique and the percentages add up to 100, then parse new GradeCategories
	    	if (UNIQUE_CATEGORIES && PERCENTAGE_TOTAL){
	    		List<GradeCategory> newGcs = new ArrayList<GradeCategory>();
	    		for (int i = 0; i < tableModel.getRowCount() - 1; i++){
	    			newGcs.add(new GradeCategory( String.valueOf(tableModel.getValueAt(i, 0)),
	    										 Integer.valueOf(tableModel.getValueAt(i, 1).toString())));
	    		}
	    		
	    		// Add assignments to list that no longer have a category
	    		for (Assignment assignment : Global.assignments){
	    			GC_ASSIGNMENT_MATCH = false;
	    			for (GradeCategory gc : newGcs){
	    				if (assignment.getCategory().equals(gc.getCategory())){
	    					GC_ASSIGNMENT_MATCH = true;
	    					break;
	    				}
	    			}
	    			// If there is no match, add to list
	    			if (!GC_ASSIGNMENT_MATCH){
	    				affectedAssignments.add(assignment);
	    				ASSIGNMENT_CHANGED = true;
	    			}
	    		}
	    		
	    		if (ASSIGNMENT_CHANGED){
		    		// TODO Call CorrectAssignmentsWizard
		    	} else {
		    		Global.gradeCategories.clear();
		    		Global.gradeCategories = newGcs;
		    		Database db = new Database();
		    		db.updateRubric(Constants.defaultSemester, Global.currentCourse, newGcs);
		    		RECALCULATE_GRADES = true;
		    	}
	    	}
	    	
	    	
	    	
	   	} else {					
	   		// Any "Cancel" or Exit Button Pressed
	   		
	   	}
	}
	
	/**
	 * Prepares the small table containing rubric information for the wizard.
	 * This information is retrieved from the current course's rubric file.
	 * 
	 * @return the table model for the rubric wizard
	 */
	@SuppressWarnings("serial")
	private DefaultTableModel prepareTable(){
		
		// Find row and column sizes
		int rows  = Global.gradeCategories.size() + 1;	// Each row is a category/% pair
		int cols  = 2;									// Two columns, "Category" and "%"
		int total = 0;									// Total sum of percentages in table				
		
		// Set column names
		String columnNames[] = new String[cols];
		columnNames[0] = "Category";
		columnNames[1] = "%";
				
		// Initialize object array
		Object[][] tableData = new Object[rows][cols];
		
		// Initialize table data
		for (int i = 0; i < rows; i++){
			if (i < rows - 1){
				GradeCategory gc = Global.gradeCategories.get(i);
				tableData[i][0] = gc.getCategory();
				tableData[i][1] = (int) (gc.getWeight() * 100);
				total += (int) (gc.getWeight() * 100);
			} else {
				tableData[i][0] = "TOTAL";
				tableData[i][1] = total;
			}
		}
		
		// Create the table and make appropriate columns uneditable
		tableModel = new DefaultTableModel(tableData, columnNames){
			@Override
		    public boolean isCellEditable(int row, int column)
		    {
				return !(row == tableModel.getRowCount() - 1);
		    }
		};
		
		// Add a listener for changes in the values of the cells
		tableModel.addTableModelListener(new TableModelListener() {
			public void tableChanged(TableModelEvent e) {
	            tableModel.removeTableModelListener(this);		// Remove the listener so that it is not called when JTable values change
	            updateTotal();									// Update the total value at the bottom of the table when a value is changed
	            tableModel.addTableModelListener(this);			// Replace the table listener again after the updating is complete
			}
		});
		
		return tableModel;
	}
	
	/**
	 * Adds another category to the rubric wizard.
	 */
	private void addCategory(){

		Object[] row = new Object[2];
		row[0] = "Category";
		row[1] = 0;
		
		CATEGORY_ADDED = true;
		tableModel.addRow(row);
		tableModel.moveRow(tableModel.getRowCount() - 1, 
						   tableModel.getRowCount() - 1,
						   tableModel.getRowCount() - 2);
		CATEGORY_ADDED = false;
	}
	
	/**
	 * Deletes a category from the rubric wizard.
	 */
	private void deleteCategory(){
		
		int selectedRow = table.getSelectedRow();
		if (selectedRow != tableModel.getRowCount() - 1){
			tableModel.removeRow(selectedRow);
		}
	}
	
	/**
	 * Update the "Total" value when the percentage is changed.
	 */
	private void updateTotal(){
		
		if (!CATEGORY_ADDED){
			int rows  = tableModel.getRowCount();
			int total = 0;
			
			// Initialize table data
			for (int i = 0; i < rows; i++){
				if (i < rows - 1){
					total += Integer.valueOf(tableModel.getValueAt(i, 1).toString());
				} else {
					tableModel.setValueAt(total, i, 1);
				}
			}
		}
	}
}

