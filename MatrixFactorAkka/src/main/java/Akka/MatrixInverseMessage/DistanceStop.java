package Akka.MatrixInverseMessage;

public class DistanceStop implements Message {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	final int partLoc;
	public DistanceStop(int partLoc) {
		super();
		this.partLoc = partLoc;
	}
	public int getPartLoc() {
		return partLoc;
	}
	

}
