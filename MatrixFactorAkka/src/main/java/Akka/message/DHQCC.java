package Akka.message;

import Akka.util.Matrix;

public class DHQCC implements Message {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Matrix dhq;
	public DHQCC(Matrix dhq){
		this.dhq = dhq;
	}
	public Matrix getDhq() {
		return dhq;
	}

}
