package ImprovedHighOrderLanczos.message;

public class AlphaAndBeta implements Message {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	final double alpha;
	final double beta;
	public AlphaAndBeta(double alpha,double beta){
		this.alpha = alpha;
		this.beta = beta;
	}
	public double getBeta() {
		return beta;
	}
	public double getAlpha() {
		return alpha;
	}
	
}
