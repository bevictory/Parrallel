package Akka.MatrixMultiplyMessage;

import Akka.MatrixInverseMessage.Message;
import akka.actor.ActorRef;

public class MatrixMultiplyWorkerInit implements Message{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	final int vecASize;
	final int vecBSize;
	final int loc;
	final ActorRef manager;
	final ActorRef coWorker;
	public MatrixMultiplyWorkerInit(int rowNum, int colNum,int loc
			,ActorRef manager, ActorRef coWorker) {
		// TODO Auto-generated constructor stub
		this.vecASize = rowNum;
		this.vecBSize = colNum;
		this.loc = loc;
		this.manager = manager;
		this.coWorker =coWorker;
	}
	public ActorRef getManager() {
		return manager;
	}
	public ActorRef getCoWorker() {
		return coWorker;
	}
	public int getVecASize() {
		return vecASize;
	}
	public int getVecBSize() {
		return vecBSize;
	}
	public int getLoc() {
		return loc;
	}
	
	
}
