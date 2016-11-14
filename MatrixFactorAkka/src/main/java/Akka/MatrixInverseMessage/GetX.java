package Akka.MatrixInverseMessage;

public class GetX implements Message{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	final int partLoc;
	public GetX(int partLoc) {
		super();
		this.partLoc = partLoc;
	}
	public int getPartLoc() {
		return partLoc;
	}
	

}
