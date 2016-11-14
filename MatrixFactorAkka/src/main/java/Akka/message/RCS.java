package Akka.message;

import Akka.util.Matrix;

public class RCS implements Message {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Matrix r;
	public Matrix getR() {
		return r;
	}
	public RCS(Matrix q){
		this.r = q;
	}

}
