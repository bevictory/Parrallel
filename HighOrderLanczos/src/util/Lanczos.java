package util;

import java.lang.Thread.State;
import java.util.ArrayList;
import java.util.List;



public class Lanczos {
	long startTime;
	long endTime;
	private List<Double > aList = new ArrayList<Double>();
	private List<Double> bList = new ArrayList<Double>();
	public  void deal(Tensor tensor1,int multiOrder){
		double b=1;
		double a=0;
		Tensor u=null,n,q=null;
		int []dim = new int[multiOrder]; 
		for(int i =0;i<multiOrder;i++){
			dim[i] = tensor1.getDimension()[i]; 
		}
		q = new Tensor(multiOrder,dim);
		TensorOp.qInit(q);
		
//		System.out.println(TensorOp.norm2(q));
		startTime = System.currentTimeMillis();
		int count =1;
//		List<Tensor> list = TensorOp.partion(tensor1, 2, 2);
//	    Tensor qlist = TensorOp.getPartFromTensor(q, 0, 8);
		u = TensorOp.multiOrderParallel(tensor1, q);

		while(b-0 >10e-5){
//			System.out.println("q*u");
			a = TensorOp.multiOrderParallel(q, u).getByLoc(0);
			System.out.println((System.currentTimeMillis()-startTime)+" " +" get a");
//			System.out.println("a_"+count+" : "+a);
			aList.add(a);
			n = TensorOp.tesorSubStraction(u, q, a);
//			TensorOp.getPartFromTensor(u, 8, 8).print();
//			TensorOp.getPartFromTensor(q, 8, 8).print();
//			TensorOp.getPartFromTensor(n, 8, 8).print();
			
			//System.out.println(TensorOp.norm2(TensorOp.getPartFromTensor(n, 0, 8))+" norm");
			
			b = Math.sqrt(TensorOp.norm2(n));
			System.out.println((System.currentTimeMillis()-startTime)+" " +" get norm");
			bList.add(b);
//			System.out.print("b_"+count+" : ");
//			System.out.printf("%.20f", b);
//			System.out.println();
//			count=11;
			count++;
			if(b-0 <10e-5||count==101){
				System.out.println("ite num "+(count));
				break;
			}else {
				
				 TensorOp.multiNum(n, 1.0/b);
				 System.out.println((System.currentTimeMillis()-startTime)+" " +" get qNext");
				 u = TensorOp.tesorSubStraction(TensorOp.multiOrderParallel(tensor1, n)
						 , q, b);
				 q=n;
				 
				 
			}
			
		}
		System.out.println(aList);
		System.out.println(bList);
		endTime = System.currentTimeMillis();
		//u.print();
		
	}
	public static void main(String []args){
		Lanczos lanczos = new Lanczos();
		long startTime = System.currentTimeMillis();
		int dim =80;
		Tensor tensor = new Tensor(4,dim,dim,dim,dim);
		
		TensorOp.tensorInit(tensor);
		lanczos.deal(tensor, 2);
		long endTime =System.currentTimeMillis();
		System.out.println("running time "+(lanczos.endTime -lanczos.startTime));
	}

}
