package Akka.MatrixInverseMessage;

public class Distance implements Message {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	final double dis ;
	final int partLoc;
	public double getDis() {
		return dis;
	}
	public int getPartLoc() {
		return partLoc;
	}
	public Distance(double dis, int partLoc) {
		super();
		this.dis = dis;
		this.partLoc = partLoc;
	}
	
}
