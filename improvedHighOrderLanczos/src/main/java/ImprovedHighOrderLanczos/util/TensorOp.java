package ImprovedHighOrderLanczos.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;



public class TensorOp {
	/**
	 * tensor order multiply tensor 
	 * @param multiplicand
	 * @param factor
	 * @param para
	 * @return
	 */
	@Deprecated
	public static Tensor multiOrder(Tensor multiplicand , Tensor factor,double ...para){
		int multiOrderNum = factor.getOrder();
		int multilicandOrder = multiplicand.getOrder();
		int multiSize =factor.getSize();
		int []dim = new int [multilicandOrder - multiOrderNum];
		int resultSize=1;
		for(int i = multiOrderNum,j=0;i<multilicandOrder; i++,j++){
			int dimension = multiplicand.getDimension()[i];
			dim[j] = dimension;
			resultSize *= dimension;
		}
		
		Tensor result = new Tensor(multiplicand.getOrder()-multiOrderNum,dim);
		if(para.length==1){
			double p = para[0];
			for(int i = 0;i < resultSize ;i++){
				double number =0;
				for(int j=0; j< multiSize; j++){
					number += multiplicand.getByLoc((i*multiSize)+j) * factor.getByLoc(j)*p;
				}
				result.setByLoc(number, i);
			}
		}
		else {
			for(int i = 0;i < resultSize ;i++){
				double number =0;
				for(int j=0; j< multiSize; j++){
					number += multiplicand.getByLoc((i*multiSize)+j) * factor.getByLoc(j);
					//System.out.println( multiplicand.getByLoc((i*multiSize)+j)+" * "+factor.getByLoc(j));
				}
				
				result.setByLoc(number, i);
			}
		}
		return result;
		
	}
	
	
	/**
	 * tensor order multiply tensor , quickly method,
	 * use getByLoc(int loc)method to get element
	 * @param multiplicand
	 * @param factor
	 * @param para
	 * @return
	 */
	public static Tensor multiOrderParallel(Tensor multiplicand , Tensor factor,double ...para){
		
		long time = System.nanoTime();
		
		int multiSize =factor.getSize();
		
		int resultSize=multiplicand.getSize()/multiSize;
		
		Tensor result = new Tensor(1,resultSize);
		
		long num=0;
		if(para.length==1){
			double p = para[0];
			for(int i = 0;i < resultSize ;i++){
				double number =0;	int resultLoc = i*multiSize;
				for(int j=0; j< multiSize; j++){
					number += multiplicand.getByLoc((resultLoc)+j) * factor.getByLoc(j)*p;
				}
				result.setByLoc(number, i);
			}
		}
		else {
			
			for(int i = 0;i < resultSize ;i++){
				double number =0;	int resultLoc = i*multiSize;
				int end =resultLoc+multiSize;
				for(int j=resultLoc,k=0; j< end; j++,k++){
					
					number += multiplicand.getByLoc(j) * factor.getByLoc(k);
					num++;
					//System.out.println( multiplicand.getByLoc((i*multiSize)+j)+" * "+factor.getByLoc(j));
				}
				
				result.setByLoc(number, i);
			}
		}
		
		System.out.println("multiOrderParallel "+(System.nanoTime()-time)+" steps "+num);
		return result;
	}
	
	
	public static Tensor multiOrderParallel(Tensor multiplicand , Tensor[] factor,int resultSize,int multiSize, int workNum){
		
		int blockSize = factor[0].getSize();
		long time = System.currentTimeMillis();
		System.out.println(time);
		Tensor result = new Tensor(1,resultSize);
		int m =0;
			for(int i = 0;i < resultSize ;i++){
				double number =0;
				int resultLoc = i*multiSize;
				for(int j=0; j< workNum; j++){
					int loc = j*blockSize  + resultLoc;
					int length = factor[j].getSize();
					Tensor tmpTensor = factor[j];
					for(int k = 0 ;k<length;k++)
					{
						number += multiplicand.getByLoc(loc+k) * tmpTensor.getByLoc(k);
						m++;
					}
					//System.out.println( multiplicand.getByLoc((i*multiSize)+j)+" * "+factor.getByLoc(j));
				}
				
				result.setByLoc(number, i);
			}
			System.out.println(resultSize*multiSize+" time "+(System.currentTimeMillis())+" "+m);
		return result;
		
	}
	
	/**
	 * tensor subtraction  
	 * @param tensor1 
	 * @param tensor2
	 * @param para 
	 * @return tensor1 - p*tensor2
	 */
	public static Tensor tesorSubStraction(Tensor tensor1,Tensor tensor2,double ...para){
		int resultOrder = tensor1.getOrder();
		Tensor result  = new Tensor(resultOrder, tensor1.getDimension());
		int resultSize =tensor1.getSize();
		if(para.length==1){
			double p = para[0];
			for(int i =0 ;i< resultSize ; i++){
				result.setByLoc(tensor1.getByLoc(i)-p*tensor2.getByLoc(i), i);
			}
		}else {
			for(int i =0 ;i< resultSize ; i++){
				result.setByLoc(tensor1.getByLoc(i)-tensor2.getByLoc(i), i);
			}
		}
		return result;
	}
	/**
	 * tensor norm 
	 * @param tensor1
	 * @return the sum of element`s power
	 */
	public static double  norm2(Tensor tensor1){
		double result = 0;

		
		int resultSize = tensor1.getSize();
		for(int i=0;i< resultSize ; i++){
			result += Math.pow(tensor1.getByLoc(i), 2);
			//System.out.println(tensor1.getByLoc(i));
		}
		
		return result;
		
	}
	
	/**
	 * tensor multiply number
	 * @param tensor1
	 * @param p 
	 */
	public static void   multiNum(Tensor tensor1,double p){
		

		
		int resultSize = tensor1.getSize();
		for(int i=0;i< resultSize ; i++){
			tensor1.setByLoc(p*tensor1.getByLoc(i), i);
		}
		
		
	}
	/**
	 * tensor multiply number
	 * @param tensor1
	 * @param p
	 * @return tensor1*p
	 */
	public static Tensor   multiNum1(Tensor tensor1,double p){
		

		long time = System.nanoTime();
		int resultSize = tensor1.getSize();
		Tensor resutl = new Tensor(1,resultSize);
		for(int i=0;i< resultSize ; i++){
			resutl.setByLoc(p*tensor1.getByLoc(i), i);
		}
		System.out.println("getNextQ "+(System.nanoTime()-time));
		return resutl;
		
	}
	/**
	 * tensor initialization
	 * @param tensor
	 */
	public static void tensorInit(Tensor tensor){
		double []elem = tensor.getTensor();
		Random random = new Random();
		for(int i = 0;i<tensor.getSize();i++){
			elem[i] = i+1;//random.nextDouble();
		}
	}
	/**
	 *  q tensor initialization
	 * @param tensor
	 */
	public static void NormInit(Tensor tensor){
		double []elem = tensor.getTensor();
		Random random = new Random();
		double  sum =0;
		for(int i = 0;i<tensor.getSize();i++){
			elem[i] =i+1;//random.nextDouble();
			sum += Math.pow(elem[i], 2);
		}
		for(int i = 0;i<tensor.getSize();i++){
			elem[i] /=Math.sqrt(sum);
			
		}
	}
	
	/**
	 * tensor part 
	 * @param tensor
	 * @param order the number of order to part
	 * @param num the number of parts 
	 * @return
	 */
	public static List<Tensor> partion(Tensor tensor,int order,int num){
		List<Tensor> result = new ArrayList<Tensor>();
		int basicLength = tensor.getDimLength()[order];
		int []dimension = new int[tensor.getOrder() - order];
		System.arraycopy(tensor.getDimension(), order, dimension, 0, dimension.length);
		int totalsize=1;
		for(int i=0;i<dimension.length;i++){
			totalsize *= dimension[i];
		}
		int blocksize = totalsize/num;
		int lastBlocsize =blocksize;
		if(totalsize%num !=0) {
			lastBlocsize = totalsize%num+blocksize;
		}
		for(int i=0;i<num ;i++){
			int size = i==num-1?lastBlocsize*basicLength:blocksize*basicLength;
			Tensor t = new Tensor(1,size);
			System.arraycopy(tensor.getTensor(),blocksize*basicLength*i , t.getTensor(), 0, size);
			result.add(t);
		}
		return result;
		
		
	}
	
	
	public static Tensor getPartFromTensor(Tensor tensor,int loc,int size){
		
		
			Tensor t = new Tensor(1,size);
			System.arraycopy(tensor.getTensor(),loc , t.getTensor(), 0, size);
		
		return t;
		
		
	}
	
	
	/**
	 * combine tensor parts to  a tensor
	 * @param tensor
	 * @param list
	 * @param order
	 * @param num
	 */
	public static void combine(Tensor tensor,List<Tensor> list,int order,int num){
		
		int basicLength = tensor.getDimLength()[order];
		int []dimension = new int[tensor.getOrder() - order];
		System.arraycopy(tensor.getDimension(), order, dimension, 0, dimension.length);
		int totalsize=1;
		for(int i=0;i<dimension.length;i++){
			totalsize *= dimension[i];
		}
		int blocksize = totalsize/num;
		int lastBlocsize =blocksize;
		if(totalsize%num !=0) {
			lastBlocsize = totalsize%num+blocksize;
		}
		for(int i=0;i<num ;i++){
			int size = i==num-1?lastBlocsize:blocksize*basicLength;
			Tensor t = list.get(i);
			System.arraycopy( t.getTensor(), 0,tensor.getTensor(),size*i , size);
			
		}
		
		
	}
	public static void combine(Tensor tensor,Tensor[] list,int order,int num){
		
	
		
		int totalsize=tensor.getSize();
		int blocksize = totalsize/num;
		int lastBlocsize =blocksize;
		if(totalsize%num !=0) {
			lastBlocsize = totalsize%num+blocksize;
		}
		for(int i=0;i<num ;i++){
			int size = list[i].getSize();
			Tensor t = list[i];
			System.arraycopy( t.getTensor(), 0,tensor.getTensor(),blocksize*i , size);
			
		}
		
		
	}
	
	public static Tensor getNextW(Tensor tensor1,Tensor tensor2,Tensor tensor3,double beta,double alpha){
		long time = System.nanoTime();
		int resultSize = tensor1.getSize();
		Tensor resutl = new Tensor(1,resultSize);
		for(int i=0;i< resultSize ; i++){
			resutl.setByLoc(tensor1.getByLoc(i)/beta-tensor2.getByLoc(i)*beta-alpha*tensor3.getByLoc(i), i);
		}
		System.out.println("getNextW "+(System.nanoTime()-time));
		return resutl;
	}
		

}
