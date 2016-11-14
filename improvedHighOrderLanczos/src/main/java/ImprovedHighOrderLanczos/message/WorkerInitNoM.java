package ImprovedHighOrderLanczos.message;

import java.awt.Dimension;
import java.util.List;

import ImprovedHighOrderLanczos.util.Tensor;
import akka.actor.ActorRef;
import akka.actor.dsl.Creators.Act;



public class WorkerInitNoM implements Message {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	final double MDim;
	final Tensor W;
	final int loc;
	final int blockSize ;
	final int workerNum;
	final List<ActorRef>  workers;
	final ActorRef manager;
	
	
	public List<ActorRef> getWorkers() {
		return workers;
	}
	public WorkerInitNoM(double MDim, Tensor W,int workerNum,
			int loc,int blockSize,List<ActorRef> list,ActorRef manager) {
		
		// TODO Auto-generated constructor stub
		this.MDim = MDim;
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
	public double getMDim() {
		return MDim;
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
