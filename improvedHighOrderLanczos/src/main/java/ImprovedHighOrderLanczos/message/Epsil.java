package ImprovedHighOrderLanczos.message;

public class Epsil implements Message {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	final double epsil;
	public Epsil(double epsil){
		this.epsil = epsil;
	}
	public double getEpsil() {
		return epsil;
	}
	

}
