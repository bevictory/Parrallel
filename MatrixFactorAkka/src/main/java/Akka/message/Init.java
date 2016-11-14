package Akka.message;

public class Init implements Message {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final int row;
	private final int col;
	public Init(int row, int col){
		this.row = row;
		this.col = col;
	}
	
	public int getRow() {
		return row;
	}
	public int getCol() {
		return col;
	}

}
