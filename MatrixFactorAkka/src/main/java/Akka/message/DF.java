package Akka.message;

import Akka.util.Matrix;

public class DF implements Message {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Matrix dFMatrix;
	public Matrix getdFMatrix() {
		return dFMatrix;
	}
	public DF(Matrix df){
		this.dFMatrix = df;
	}

}
