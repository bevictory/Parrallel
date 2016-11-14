package Akka.message;

import Akka.util.Matrix;

public class BlockMatrix implements Message {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int rowLoc;
	private int colLoc;
	private Matrix aMatrix;
	private Matrix bMatrix;
	public BlockMatrix(int rowLoc, int colLoc,Matrix aMatrix,Matrix bMatrix) {
		
		// TODO Auto-generated constructor stub
		
		this.rowLoc = rowLoc;
		this.colLoc = colLoc;
		this.aMatrix = aMatrix;
		this.bMatrix = bMatrix;
		
	}
	public int getRowLoc() {
		return rowLoc;
	}
	public void setRowLoc(int rowLoc) {
		this.rowLoc = rowLoc;
	}
	public int getColLoc() {
		return colLoc;
	}
	public void setColLoc(int colLoc) {
		this.colLoc = colLoc;
	}
	public Matrix getaMatrix() {
		return aMatrix;
	}
	public void setaMatrix(Matrix aMatrix) {
		this.aMatrix = aMatrix;
	}
	public Matrix getbMatrix() {
		return bMatrix;
	}
	public void setbMatrix(Matrix bMatrix) {
		this.bMatrix = bMatrix;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
