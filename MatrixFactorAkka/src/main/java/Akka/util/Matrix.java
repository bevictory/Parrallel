package Akka.util;

import java.io.Serializable;

public class Matrix implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int col ;
	private int row;
	double [][]elemMatrix;
	public Matrix(int row, int col){
		this.row = row;
		this.col = col;
		elemMatrix = new double [row][col];
		
	}
	public int getCol() {
		return col;
	}
	public void setCol(int col) {
		this.col = col;
	}
	public int getRow() {
		return row;
	}
	public void setRow(int row) {
		this.row = row;
	}
	public double[][] getMatrix() {
		return elemMatrix;
	}
	public void setMatrix(double[][] matrix) {
		this.elemMatrix = matrix;
	}
	public void print (){
		for(int i=0;i<row;i++){
			for(int j=0;j<col;j++){
				System.out.printf("%.6f",elemMatrix[i][j]);
				System.out.print(" ");
			}
			System.out.println();
		}
	}
	

}
