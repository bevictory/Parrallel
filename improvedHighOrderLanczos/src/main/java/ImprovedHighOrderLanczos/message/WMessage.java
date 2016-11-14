package ImprovedHighOrderLanczos.message;

import ImprovedHighOrderLanczos.util.Tensor;

public class WMessage implements Message {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	int loc ;
	Tensor partW;
	public WMessage(Tensor partW, int loc) {
	// TODO Auto-generated constructor stub
		this.partW = partW;
		this.loc = loc;
	}
	public int getLoc() {
		return loc;
	}
	public void setLoc(int loc) {
		this.loc = loc;
	}
	public Tensor getPartW() {
		return partW;
	}
	public void setPartW(Tensor partW) {
		this.partW = partW;
	}

}
