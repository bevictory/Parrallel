package Akka.MatrixInverseMessage;

import Akka.util.Vector;

public class XIteration implements Message{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	final Vector x;
	int begin; int end;



	public XIteration(Vector x, int begin, int end) {
		super();
		this.x = x;
		this.begin = begin;
		this.end = end;
	}



	public int getBegin() {
		return begin;
	}



	public void setBegin(int begin) {
		this.begin = begin;
	}



	public int getEnd() {
		return end;
	}



	public void setEnd(int end) {
		this.end = end;
	}



	public Vector getX() {
		return x;
	}
	
	

}
