package Akka.message;

import Akka.util.Matrix;

public class A1CC implements Message {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Matrix A1_s;
	public Matrix getA1_s() {
		return A1_s;
	}
	public A1CC(Matrix a){
		this.A1_s = a;
	}

}
