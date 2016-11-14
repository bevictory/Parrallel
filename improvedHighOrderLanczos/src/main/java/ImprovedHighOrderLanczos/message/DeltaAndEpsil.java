package ImprovedHighOrderLanczos.message;

public class DeltaAndEpsil implements Message {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	final double delta;
	final double epsil;
	public DeltaAndEpsil(double delta,double epsil){
		this.delta = delta;
		this.epsil = epsil;
	}
	public double getDelta() {
		return delta;
	}
	public double getEpsil() {
		return epsil;
	}
	
	
}
  