package Akka.message;

import Akka.util.Matrix;

public class DHRCC implements Message {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Matrix dhr;
	public DHRCC(Matrix dhr){
		this.dhr = dhr;
	}
	public Matrix getDhr() {
		return dhr;
	}

}
