package dbclasses;
/**
 * This class is designed to contain the letter grade ranges in order to provide 
 * letter grade values based on the grade average for the student.
 * 
 * @author Gabriel Miller
 * @version 1.0
 * @since 11/13/2016
 */
public class GradeRange {

	private String letterGrade;
	private double upperBound;
	private double lowerBound;
	
	/**
	 * Class constructor.
	 * 
	 * @param letterGrade letter grade representation
	 * @param upperBound upper bound for the grade
	 * @param lowerBound lower bound for the grade
	 */
	public GradeRange(String letterGrade, double upperBound, double lowerBound){
		this.letterGrade = letterGrade;
		this.upperBound  = upperBound;
		this.lowerBound  = lowerBound;
	}

	/**
	 * Determine whether the specified grade is within the range.
	 * 
	 * @param grade grade to be tested to be in range
	 * @return whether or not grade is in range
	 */
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
	
	/**
	 * Gets the letter grade representation.
	 * 
	 * @return letter grade
	 */
	public String getLetterGrade() {
		return letterGrade;
	}

	/**
	 * Sets the letter grade representation.
	 * 
	 * @param letterGrade letter grade representation
	 * @return none
	 */
	public void setLetterGrade(String letterGrade) {
		this.letterGrade = letterGrade;
	}

	/**
	 * Gets upper bound.
	 * 
	 * @return upper bound
	 */
	public double getUpperBound() {
		return upperBound;
	}

	/**
	 * Sets upper bound.
	 * 
	 * @param upperBound upper bound
	 * @return none
	 */
	public void setUpperBound(double upperBound) {
		this.upperBound = upperBound;
	}

	/**
	 * Gets lower bound.
	 * 
	 * @return lower bound
	 */
	public double getLowerBound() {
		return lowerBound;
	}

	/**
	 * Sets lower bound.
	 * 
	 * @param lowerBound lower bound
	 * @return none
	 */
	public void setLowerBound(double lowerBound) {
		this.lowerBound = lowerBound;
	}
}