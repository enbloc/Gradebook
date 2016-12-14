package constants;

import java.util.List;

import dbclasses.Assignment;
import dbclasses.GradeCategory;
import dbclasses.GradeRange;
/**
 * This class contains all of the global values that are necessary to populate the course interface.
 * These value are held in several lists so that they may be downloaded from the server all at once, 
 * without having to incur the overhead of making an SSH call to the server every time we want to 
 * access the values. They also allow us to add values to these lists first and update the interface
 * immediately while the update to the data on the server takes place in a separate thread.
 * 
 * @author Gabriel Miller
 * @version 1.0 11/13/2016
 * 
 */
public class Global {

	// State Dependent Data Holders
	/**
	 * Contains the course that is currently selected on the Course tab.
	 */
	public static String				currentCourse;			
	
	/**
	 * Contains the list of assignments for the current course with their corresponding grade categories.
	 */
	public static List<Assignment>		assignments;			
	
	/**
	 * Contains the list of grade categories with associated assignments for current course.
	 */
	public static List<GradeCategory> 	gradeCategories;		
	
	/**
	 * Contains the list of letter grade ranges for the current course that are used to populate the "Grade" column.
	 */
	public static List<GradeRange> 		gradeRanges;			// Contains the list of letter grade ranges for the current course that are used to populate the "Grade" column
	
	/**
	 * Global flag that is set to true when the currently selected course has just been created.
	 */
	public static boolean				IS_NEW_COURSE = true;
}
