package Akka.util;
import java.util.Random;
import java.util.zip.ZipEntry;

import org.omg.PortableServer.ServantActivator;

import scala.annotation.implicitNotFound;

public class VectorOp {
	public static double var = 100;
	public static Matrix outerProduct(Vector v1,Vector v2){
		int row = v1.getSize();
		int col = row;
		Matrix result = new Matrix(row, col);
		for(int i =0 ;i < row; i++){
			for(int j=0;j< col;j++){
				result.elemMatrix[i][j]=v1.vector[i]*v2.vector[j];
			}
		}
		return result;
	}
	public static double innerProduct(Vector v1, Vector v2){
		int size = v1.getSize();
		double result=0;
		for(int i =0;i<size ; i++){
			result+= v1.getVector()[i]*v2.getVector()[i];
		}
		return result;
	}
	public static void vectorInit(Vector v1){
		int size = v1.getSize();
		for(int i=0;i<size;i++){
			v1.vector[i] = Math.random();
		}
	}
	public static double norm(Vector v1){
		int size = v1.getSize();
		double result=0;
		for(int i=0;i<size;i++){
			result+=Math.pow(v1.vector[i],2); 
		}
		return result;
	}
	public static void div(Vector v,double p){
		int size = v.getSize();
		for(int i=0;i<size;i++){
			v.vector[i]/=p;
		}
	}
	public static void combine(Vector m, Vector v,int begin ,int end){
		for(int i=begin; i< end;i++){
			m.getVector()[i] = v.getVector()[i-begin];
		}
	}
	public static boolean allclose(Vector v1, Vector v2, double atol){
		int size = v1.getSize();
		double result=0;
		for(int i =0;i<size;i++){
			result+= Math.pow((v1.getVector()[i]-v2.getVector()[i]),2);
		}
		if(result <= atol) return true;
		else return false;
	}
	public static Vector vectorMultiMatrix(Vector v, Matrix m){
		int row = m.getRow();
		int col = m.getCol();
		Vector result = new Vector(row);
		for(int i=0;i<row;i++){
			result.vector[i]=0;
			for(int j=0;j<row;j++){
				result.vector[i]+=v.vector[j]*m.elemMatrix[j][i];
			}
					
		}
		return result;
	}
	public static Vector getSubVector(Vector m,int begin ,int end){
		Vector result = new Vector(end -begin);
		for(int i =begin;i<end;i++){
			result.getVector()[i-begin] = m.getVector()[i]; 
		}
		return result;
		
	}
	public static Vector randomInit(int size){
		Vector result = new Vector(size);
		Random random = new Random();
		for(int i=0;i<size;i++){
			result.vector[i] = random.nextDouble()+Math.sqrt(var);
		}
		return result;
	}
	public static Vector identiInit(int size){
		Vector result = new Vector(size);
		
		for(int i=0;i<size;i++){
			result.vector[i] = 1;
		}
		return result;
	}
	public static void main(String []args){
		Vector vector = new Vector(2);
		vectorInit(vector);
		vector.print();
		Vector vector1 = new Vector(2);
		vectorInit(vector1);
		vector1.print();
		outerProduct(vector,vector1).print();;
		
		
		
	}

}
