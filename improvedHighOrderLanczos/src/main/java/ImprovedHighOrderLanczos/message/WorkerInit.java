package ImprovedHighOrderLanczos.message;

import java.util.List;

import ImprovedHighOrderLanczos.util.Tensor;
import akka.actor.ActorRef;
import akka.actor.dsl.Creators.Act;



public class WorkerInit implements Message {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	final Tensor partM;
	final Tensor W;
	final int loc;
	final int blockSize ;
	final int workerNum;
	final List<ActorRef>  workers;
	final ActorRef manager;
	
	
	public List<ActorRef> getWorkers() {
		return workers;
	}
	public WorkerInit(Tensor partM, Tensor W,int workerNum,
			int loc,int blockSize,List<ActorRef> list,ActorRef manager) {
		// TODO Auto-generated constructor stub
		this.partM = partM;
		this.W = W;
		this.loc = loc;
		this.blockSize = blockSize;
		this.workerNum = workerNum;
		this.workers = list;
		this.manager = manager;
	}
	public ActorRef getManager() {
		return manager;
	}
	public int getWorkerNum() {
		return workerNum;
	}
	public Tensor getPartM() {
		return partM;
	}
	public Tensor getW() {
		return W;
	}
	public int getLoc() {
		return loc;
	}
	public int getBlockSize() {
		return blockSize;
	}

}
