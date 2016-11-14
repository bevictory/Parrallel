package Akka.MatrixMultiplyMessage;

import Akka.MatrixInverseMessage.Message;
import Akka.util.Matrix;

public class PartMatrixResult implements Message {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	final Matrix result;
	public PartMatrixResult(Matrix result){
		this.result = result;
	}
	public Matrix getResult() {
		return result;
	}
	

}
