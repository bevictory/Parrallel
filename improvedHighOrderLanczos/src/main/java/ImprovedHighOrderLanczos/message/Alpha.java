package ImprovedHighOrderLanczos.message;

public class Alpha implements Message {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	final double alpha;
	public Alpha(double alpha){
		this.alpha = alpha;
	}
	public double getAlpha() {
		return alpha;
	}
	

}
