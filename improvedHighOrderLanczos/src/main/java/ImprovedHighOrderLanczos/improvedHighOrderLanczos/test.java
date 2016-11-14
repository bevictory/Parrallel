package ImprovedHighOrderLanczos.improvedHighOrderLanczos;

import ImprovedHighOrderLanczos.util.Tensor;
import ImprovedHighOrderLanczos.util.TensorOp;

public class test {
	static class testWork implements Runnable{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			TensorOp.multiOrderParallel(t1, t2);
		}
		
	}
	static Tensor t1 ;
	static Tensor t2; 
	static Tensor t3; 
	public static void main(String[] args) {
		int dim = 120;
		 t1 = new Tensor(4,dim,dim,dim,dim);
		 t2 = new Tensor(2,dim,dim);
		
		TensorOp.multiOrderParallel(t1, t2);
		
		
		
	}

}
