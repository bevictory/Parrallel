package Akka.message;

public class Init implements Message {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Tensor partA ;
	private final Tensor q;
	private final int loc;
	private final int blockSize;
	public Init(Tensor a, Tensor q,int loc,int blockSize){
		this.partA = a;
		this.q = q;
		this.loc = loc;
		this.blockSize = blockSize;
	}
	public int getBlockSize() {
		return blockSize;
	}
	public int getLoc() {
		return loc;
	}
	public Tensor getPartA() {
		return partA;
	}
	public Tensor getQ() {
		return q;
	}

}
