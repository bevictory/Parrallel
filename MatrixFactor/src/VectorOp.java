import java.util.Random;
import java.util.zip.ZipEntry;

import org.omg.PortableServer.ServantActivator;

public class VectorOp {
	public static double var = 10000;
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
	public static Vector randomInit(int size){
		Vector result = new Vector(size);
		Random random = new Random();
		for(int i=0;i<size;i++){
			result.vector[i] = random.nextDouble()+Math.sqrt(var);
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
