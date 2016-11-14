package Akka.message;

import Akka.util.Matrix;

public class R1CC implements Message {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Matrix r;
	public Matrix getR() {
		return r;
	}
	public R1CC(Matrix q){
		this.r = q;
	}

}
