package Akka.message;

import java.io.Serializable;

import scala.inline;

public class ResultTensor implements Message {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Tensor partA;
	private final int loc;
	
	public ResultTensor(Tensor a, int loc){
		this.partA =a;
		this.loc = loc;
	}
	public Tensor getPartA() {
		return partA;
	}
	public int getLoc() {
		return loc;
	}
	

}
