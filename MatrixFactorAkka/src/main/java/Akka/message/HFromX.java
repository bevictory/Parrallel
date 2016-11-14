package Akka.message;

import java.security.PublicKey;

import Akka.util.Matrix;

public class HFromX implements Message {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Matrix H;
	public HFromX (Matrix H){
		this.H = H;
		
	}
	public Matrix getH() {
		return H;
	}
	
}
