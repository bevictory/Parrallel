package Akka.message;

import Akka.util.Matrix;

public class Q1andR1CS implements Message{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Matrix Q1;
	private final Matrix R1;
	public Matrix getQ1() {
		return Q1;
	}
	public Matrix getR1() {
		return R1;
	}
	public Q1andR1CS(Matrix q, Matrix r){
		this.Q1 = q;
		 this.R1 = r;
	}

}
