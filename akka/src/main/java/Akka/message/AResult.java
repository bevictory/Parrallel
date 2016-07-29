package Akka.message;

public class AResult implements Message{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final double a;
	public AResult(double  a){
		this.a = a;
	}
	public double getA() {
		return a;
	}
	
	

}
