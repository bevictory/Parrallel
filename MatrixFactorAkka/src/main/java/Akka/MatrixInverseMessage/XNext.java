package Akka.MatrixInverseMessage;

import Akka.util.Matrix;
import Akka.util.Vector;

public class XNext implements Message{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	final int begin;
	final int end;
	final int partLoc;
	final Vector xNext;
	public XNext(int begin, int end, int partLoc, Vector xNext) {
		super();
		this.begin = begin;
		this.end = end;
		this.partLoc = partLoc;
		this.xNext = xNext;
	}
	public int getBegin() {
		return begin;
	}
	public int getEnd() {
		return end;
	}
	public int getPartLoc() {
		return partLoc;
	}
	public Vector getxNext() {
		return xNext;
	}
	
	
	

}
