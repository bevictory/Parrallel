
public class Vector {
	private int size;
	double []vector;
	public Vector(int size){
		this.size = size ;
		vector = new double[size];
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public double[] getVector() {
		return vector;
	}
	public void setVector(double[] vector) {
		this.vector = vector;
	}
	public void print (){
		for(int i=0;i<size;i++){
			
				System.out.printf("%.6f",vector[i]);
				System.out.print(" ");
			
			System.out.println();
		}
	}

}
