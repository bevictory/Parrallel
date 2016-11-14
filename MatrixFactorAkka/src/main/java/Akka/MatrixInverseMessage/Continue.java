package Akka.MatrixInverseMessage;

public class Continue implements Message {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	final int partLoc;
	public Continue(int partLoc) {
		super();
		this.partLoc = partLoc;
	}
	public int getPartLoc() {
		return partLoc;
	}
	

}
