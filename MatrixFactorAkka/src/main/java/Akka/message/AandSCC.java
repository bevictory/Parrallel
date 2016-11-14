package Akka.message;

import Akka.util.Matrix;
import Akka.util.MatrixOp;

public class AandSCC implements Message {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Matrix A_s;
	private final Matrix S_s;
	public Matrix getA_s() {
		return A_s;
	}
	public Matrix getS_s() {
		return S_s;
	}
	public AandSCC (Matrix a,Matrix s){
		this.A_s = a;
		this.S_s = s;
	}

}
