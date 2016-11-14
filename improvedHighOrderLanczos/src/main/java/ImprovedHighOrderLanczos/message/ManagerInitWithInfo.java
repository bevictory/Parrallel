package ImprovedHighOrderLanczos.message;

public class ManagerInitWithInfo implements Message{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	final int workerNum;
	final int dim ;
	public ManagerInitWithInfo(int workerNum, int dim){
		this.workerNum = workerNum;
		this.dim = dim;
	}
	public int getWorkerNum() {
		return workerNum;
	}
	public int getDim() {
		return dim;
	}
	

}
