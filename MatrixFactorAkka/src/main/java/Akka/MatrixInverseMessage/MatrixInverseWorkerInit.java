package Akka.MatrixInverseMessage;

import java.util.ArrayList;

import Akka.util.Matrix;
import akka.actor.ActorRef;
import akka.actor.LocalActorRef;

public class MatrixInverseWorkerInit implements Message{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	final Matrix partA;
	final int loc;
	final int begin;
	final int end;
	final ArrayList<ActorRef> workerList;
	final double atol;
	final int workerNum;
	final ActorRef manager;
	
	public ArrayList<ActorRef> getWorkerList() {
		return workerList;
	}
	public double getAtol() {
		return atol;
	}
	public int getWorkerNum() {
		return workerNum;
	}
	public ActorRef getManager() {
		return manager;
	}
	public MatrixInverseWorkerInit(Matrix partA, int loc, int begin, int end, ArrayList<ActorRef> workerList,
			double atol, int workerNum, ActorRef manager) {
		super();
		this.partA = partA;
		this.loc = loc;
		this.begin = begin;
		this.end = end;
		this.workerList = workerList;
		this.atol = atol;
		this.workerNum = workerNum;
		this.manager = manager;
	}
	public Matrix getPartA() {
		return partA;
	}
	public int getLoc() {
		return loc;
	}
	public int getBegin() {
		return begin;
	}
	public int getEnd() {
		return end;
	}
	

}
