/*
 * Assignment.java
 * 
 * This class provides the data model for assignments, as well as the methods to 
 * create, delete, and update the text files that contain their information.
 * 
 * Gabriel Miller
 * 10/25/2016
 */
package dbclasses;

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	
}
