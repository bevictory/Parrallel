package Akka.message;

public class NormResult implements Message {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final double b;
	public NormResult (double b){
		this.b = b;
	}
	public double getB() {
		return b;
	}

}
