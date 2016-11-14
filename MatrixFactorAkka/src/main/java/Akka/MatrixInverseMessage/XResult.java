package Akka.MatrixInverseMessage;

import Akka.util.Vector;

public class XResult implements Message {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	final int partLoc;
	final Vector x;
	public XResult(int partLoc, Vector x) {
		super();
		this.partLoc = partLoc;
		this.x = x;
	}
	public int getPartLoc() {
		return partLoc;
	}
	public Vector getX() {
		return x;
	}
	

}
