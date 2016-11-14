package ImprovedHighOrderLanczos.message;

public class Beta implements Message {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	final double beta;
	public Beta(double beta){
		this.beta = beta;
	}
	public double getBeta() {
		return beta;
	}
	

}
