package ImprovedHighOrderLanczos.message;

public class Delta implements Message {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	final double delta;
	public Delta(double delta){
		this.delta = delta;
	}
	public double getDelta() {
		return delta;
	}
	

}
