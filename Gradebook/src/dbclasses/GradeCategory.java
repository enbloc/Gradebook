/*
 * GradeCategory.class
 * 
 * This class is designed exculsively to assist with the weighted average
 * calculations necessary to determine the student's average for the course.
 * 
 * Gabriel Miller
 * 11/6/2016
 */

package dbclasses;

import java.util.ArrayList;
import java.util.List;

public class GradeCategory {

	private String category;
	private List<Double> grades;
	private double weight;
	private int gradeNumber;
	
	public GradeCategory(String category, int weight){
		this.category = category;
		this.grades = new ArrayList<Double>();
		this.weight = (weight * .01);
		this.gradeNumber = 0;
	}
	
	public void addGrade(double grade){
		grades.add(grade);
		gradeNumber++;
	}
	
	public double getSum(){
		double total = 0;
		for (double grade : grades){
			total += grade;
		}
		return total;
	}
	
	public double getAverage(){
		return (getSum())/gradeNumber;
	}
	
	public double getWeightedAverage(){
		return getAverage() * weight;
	}
	
	public void clearValues(){
		grades = new ArrayList<Double>();
		gradeNumber = 0;
	}
	
	/*
	 * Getters and Setters
	 */
	
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public List<Double> getGrades() {
		return grades;
	}
	public void setGrades(List<Double> grades) {
		this.grades = grades;
	}
	public double getWeight() {
		return weight;
	}
	public void setWeight(int weight) {
		this.weight = weight;
	}
	public int getGradeNumber() {
		return gradeNumber;
	}
	public void setGradeNumber(int gradeNumber) {
		this.gradeNumber = gradeNumber;
	}
	
	
}
