package Akka.message;

public class QReuslt implements Message {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Tensor q;
	private final int loc;
	private final int blockSize;
	public QReuslt (Tensor q,int loc,int blockSize){
		this.q = q;
		this.loc = loc;
		this.blockSize = blockSize;
	}
	
	
	public Tensor getQ() {
		return q;
	}


	public int getLoc() {
		return loc;
	}
	

}
