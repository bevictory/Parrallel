package Akka.MatrixMultiplyMessage;

import Akka.MatrixInverseMessage.Message;
import Akka.util.Vector;

public class PartMatrix implements Message {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	final  Vector vecA;
	final  Vector vecB;
	final int loc;
	public PartMatrix(Vector vecA, Vector vecB,int loc){
		this.vecA = vecA;
		this.loc = loc;
		this.vecB  = vecB;
	}
	public Vector getVecA() {
		return vecA;
	}
	public Vector getVecB() {
		return vecB;
	}
	public int getLoc() {
		return loc;
	}

}
