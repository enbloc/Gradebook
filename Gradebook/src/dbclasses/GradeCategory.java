package dbclasses;

import java.util.ArrayList;
import java.util.List;
/**
 * This class is designed exculsively to assist with the weighted average
 * calculations necessary to determine the student's average for the course.
 * 
 * @author Gabriel Miller
 * @version 1.0
 * @since 11/6/2016
 */
public class GradeCategory {

	private String category;
	private List<Double> grades;
	private double weight;
	private int gradeNumber;
	
	/**
	 * Class constructor.
	 * 
	 * @param category category name
	 * @param weight category weight
	 */
	public GradeCategory(String category, int weight){
		this.category = category;
		this.grades = new ArrayList<Double>();
		this.weight = (weight * .01);
		this.gradeNumber = 0;
	}
	
	/**
	 * Adds the grade to the list of grades in this category.
	 * 
	 * @param grade the grade to be added for calculation
	 * @return none
	 */
	public void addGrade(double grade){
		grades.add(grade);
		gradeNumber++;
	}
	
	/**
	 * Gets the sum of the grades in this category.
	 * 
	 * @return the sum
	 */
	public double getSum(){
		double total = 0;
		for (double grade : grades){
			total += grade;
		}
		return total;
	}
	
	/**
	 * Gets the average of the grades in this category.
	 * 
	 * @return the average
	 */
	public double getAverage(){
		return (getSum())/gradeNumber;
	}
	
	/**
	 * Gets the weighted average of the grades in this category.
	 * 
	 * @return the weighted average
	 */
	public double getWeightedAverage(){
		return getAverage() * weight;
	}
	
	/**
	 * Clears all of the grade values stored in this category.
	 * 
	 * @return none
	 */
	public void clearValues(){
		grades = new ArrayList<Double>();
		gradeNumber = 0;
	}
	
	/*
	 * Getters and Setters
	 */
	
	/**
	 * Gets category name.
	 * 
	 * @return category name
	 */
	public String getCategory() {
		return category;
	}
	
	/**
	 * Sets category name.
	 * 
	 * @param category category name
	 * @return none
	 */
	public void setCategory(String category) {
		this.category = category;
	}
	
	/**
	 * Gets list of grades.
	 * 
	 * @return list of grades
	 */
	public List<Double> getGrades() {
		return grades;
	}
	
	/**
	 * Sets list of grades.
	 * 
	 * @param grades the list of grades to be set
	 * @return none
	 */
	public void setGrades(List<Double> grades) {
		this.grades = grades;
	}
	
	/**
	 * Gets the weight of the category.
	 * 
	 * @return category weight
	 */
	public double getWeight() {
		return weight;
	}
	
	/**
	 * Sets the weight.
	 * 
	 * @param weight category weight
	 * @return none
	 */
	public void setWeight(int weight) {
		this.weight = weight;
	}
	
	/**
	 * Gets the number of grades in the category object.
	 * 
	 * @return number of grades
	 */
	public int getGradeNumber() {
		return gradeNumber;
	}
	
	/**
	 * Sets the number of grades in the category object.
	 * 
	 * @param gradeNumber number of grades
	 * @return none
	 */
	public void setGradeNumber(int gradeNumber) {
		this.gradeNumber = gradeNumber;
	}
	
	
}
