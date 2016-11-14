package Akka.message;

import Akka.util.Matrix;

public class A1CS implements Message {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Matrix A1_c;
	public Matrix getA1_c() {
		return A1_c;
	}
	public A1CS(Matrix a){
		this.A1_c = a;
	}

}
