package Akka.util;

import java.io.Serializable;

public class Vector implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int size;
	double []vector;
	public Vector(int size){
		this.size = size ;
		vector = new double[size];
	}
	public Vector(double []vec){
		this.size = vec.length ;
		vector =vec;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public double[] getVector() {
		return vector;
	}
	public void setVector(double[] vector) {
		this.vector = vector;
	}
	public void reset(){
		for(int i =0;i<size;i++){
			vector[i]=0;
		}
	}
	public void print (){
		for(int i=0;i<size;i++){
			
				System.out.printf("%.6f",vector[i]);
				System.out.print(" ");
			
			
		}System.out.println();
	}

}
