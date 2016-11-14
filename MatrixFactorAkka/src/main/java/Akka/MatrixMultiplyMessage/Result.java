package Akka.MatrixMultiplyMessage;

import Akka.MatrixInverseMessage.Message;
import Akka.util.Matrix;

public class Result implements Message {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	final int loc;
	final Matrix result;
	public Result(int loc, Matrix result) {
		super();
		this.loc = loc;
		this.result = result;
	}
	public int getLoc() {
		return loc;
	}
	public Matrix getResult() {
		return result;
	}
	

}
