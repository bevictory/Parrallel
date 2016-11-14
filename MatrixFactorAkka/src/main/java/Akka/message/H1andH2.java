package Akka.message;

import Akka.util.Matrix;

public class H1andH2 implements Message {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Matrix H1;
	private final Matrix H2;
	public Matrix getH1() {
		return H1;
	}
	public Matrix getH2() {
		return H2;
	}
	public H1andH2(Matrix H1, Matrix H2){
		this.H1 = H1;
		this.H2 = H2;
	}
	

}
