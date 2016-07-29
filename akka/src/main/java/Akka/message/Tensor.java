package Akka.message;

import Akka.akka.TensorOp;

public class  Tensor implements Message{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int order;
	private int []dimension;
	private int []dimLength;
	private double []tensor;
	private int size;
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	public int[] getDimension() {
		return dimension;
	}
	public void setDimension(int[] dimension) {
		this.dimension = dimension;
	}
	public int[] getDimLength() {
		return dimLength;
	}
	public void setDimLength(int[] dimLength) {
		this.dimLength = dimLength;
	}
	public double[] getTensor() {
		return tensor;
	}
	public void setTensor(double[] tensor) {
		this.tensor = tensor;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}

	public Tensor(){
		
	}
	
	public Tensor(int order,int ... array){
		this.order = order;
		this.dimension = new int [order];
		int length = array.length;
		
		if(order != length) {
			System.out.println("阶数和维数个数要相同");
			return;
		}
		
		int num =1;
		
		this.dimLength = new int[order];
		for(int i=0;i < length; i++){
			this.dimension[i] = array[i];
			this.dimLength[i] = num;
			num *= array[i];
			
					
		}
		this.size = num ;
		this.tensor = new double [num];
	}
	public double getByLoc(int loc ){
		return tensor[loc];
	}
	public void  setByLoc(Double num,int loc ){
		tensor[loc] = num;
	}
	public double get(int ...loc){
		int num = 0;
		if(loc.length != this.order){
			System.out.println("参数个数不对");
		}
		for(int i =0;i<this.order;i++){
			num += (loc[i]) * dimLength[i];
		}
//		System.out.println("loc " +num+" "+tensor[num]);
		return tensor[num];
	}
	public void set(double number,int ...loc){
		int num = 0; 
		if(loc.length != this.order){
			System.out.println("参数个数不对");
			return ;
		}
		for(int i =0;i<this.order;i++){
			num += (loc[i]) * dimLength[i];
		}
		System.out.println("loc " +num);
		 tensor[num] = number;
	}
	
	public void print(){
		if(this.order==0){
			System.out.println(tensor[0]);
			return;
		}
		if(this.order==1){
			for(int i=0;i<size;i++)
			{
				System.out.printf("%.20f",tensor[i]);
				System.out.print( " ");
			}
			System.out.println();
			return;
		}
		int matrixNum =  this.order >2? size/dimLength[2]:1;
		int matrixSize =this.order >2? dimLength[2]:size;
		
		int row = dimension[0];
		int col = dimension[1];
		for(int i =0;i<matrixNum;i++){
			System.out.println("tensor(:,:,"+i+")");
			for(int j=0;j<row;j++){
				for(int k=0;k<col;k++){
					int loc ;
					loc = k*row + j;
					loc += i*matrixSize;
					System.out.printf("%.20f",tensor[loc]);
					System.out.print( " ");
					
				}
				System.out.println();
			}
			System.out.println();
		}
	}
	public static void main(String[]args){
		int [] a ={1,2,1};
		
		Tensor t = new Tensor(3,10,10,10);
//		t.set(20,a);
//		t.set(20, 0,1,0);
//		t.set(20, 1,2,0);
		
		t.print();
		System.out.println(TensorOp.norm2(t));
	}
}
