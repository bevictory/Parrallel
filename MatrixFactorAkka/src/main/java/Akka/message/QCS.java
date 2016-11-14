package Akka.message;

import Akka.util.Matrix;

public class QCS implements Message {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Matrix q;
	public Matrix getQ() {
		return q;
	}
	public QCS(Matrix q){
		this.q = q;
	}

}
