/*
 * GradeRange.class
 * 
 * This class is designed to contain the letter grade ranges in order to provide 
 * letter grade values based on the grade average for the student.
 * 
 * Gabriel Miller
 * 11/13/2016
 */

package dbclasses;

public class GradeRange {

	private String letterGrade;
	private double upperBound;
	private double lowerBound;
	
	public GradeRange(String letterGrade, double upperBound, double lowerBound){
		this.letterGrade = letterGrade;
		this.upperBound  = upperBound;
		this.lowerBound  = lowerBound;
	}

	public boolean isInRange(double grade){
		boolean IN_RANGE = false;
		
		if (grade <= upperBound && grade >= lowerBound){
			IN_RANGE = true;
		}
		
		return IN_RANGE;
	}
	/*
	 * Getters and Setters
	 */
	
	public String getLetterGrade() {
		return letterGrade;
	}

	public void setLetterGrade(String letterGrade) {
		this.letterGrade = letterGrade;
	}

	public double getUpperBound() {
		return upperBound;
	}

	public void setUpperBound(double upperBound) {
		this.upperBound = upperBound;
	}

	public double getLowerBound() {
		return lowerBound;
	}

	public void setLowerBound(double lowerBound) {
		this.lowerBound = lowerBound;
	}
}