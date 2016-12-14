
package dbclasses;
/**
 * This class provides the data model for assignments.
 * 
 * @author Gabriel Miller
 * @version 1.0 10/25/2016
 */
public class Assignment {

	private String      name;
	private String		category;
	
	public Assignment(String name, String category){
		this.name   = name;
		this.category = category;
	}

	/*
	 * Getters and Setters
	 */
	
	/**
	 * Getter for assignment name
	 * @return assignment name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Setter for assignment name
	 * @param name assignment name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Getter for assignment category
	 * @return assignment category
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * Setter for assignment category
	 * @param category assignment category
	 */
	public void setCategory(String category) {
		this.category = category;
	}

	
}
