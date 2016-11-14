package Akka.message;

import Akka.util.Matrix;

public class Q1CC implements Message {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Matrix q;
	public Matrix getQ() {
		return q;
	}
	public Q1CC(Matrix q){
		this.q = q;
	}

}
