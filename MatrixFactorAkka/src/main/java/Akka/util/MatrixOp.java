package Akka.util;
import java.util.Random;

import javax.management.relation.RoleUnresolvedList;

import org.omg.PortableServer.ServantActivator;



public class MatrixOp {
	public static double var = 10000;
	public static int sed = 10;
	public static Matrix schemidt(Matrix matrix){
		int row = matrix.getRow();
		int col = matrix .getCol();
		Matrix result = new Matrix(row, col);
		for(int i =0;i<col;i++){
			if(i== 0){
				double norm = 1;//Math.sqrt(norm(matrix, i));
				for(int j =0;j<row;j++){
					result.elemMatrix[j][i] = matrix.elemMatrix[j][i]/norm;
				}
			}else{
				
				for(int j =0;j<row;j++){
					
					double num =0;
					for(int k =0;k<=i-1;k++){
						num += innerProduct(matrix,result, i, k)
								/innerProduct(result, result, k, k)
								*result.elemMatrix[j][k];
					}
					result.elemMatrix[j][i] = matrix.elemMatrix[j][i]-num;
				}
				double norm = 1;//Math.sqrt(norm(result, i));
				for(int j =0;j<row;j++){
					result.elemMatrix[j][i] = result.elemMatrix[j][i]/norm;
				}
			}
		}
		return result;
		
	}
	public static double innerProduct(Matrix matrix,Matrix matrix1,
			int col1,int col2){
		int row = matrix.getRow();
		double result =0;
		for(int i =0 ;i <row; i++){
			result += matrix.elemMatrix[i][col1]*matrix1.elemMatrix[i][col2];
		}
		return result;
	}
	public static void matrixInit(Matrix matrix){
		int row = matrix.getRow();
		int col = matrix .getCol();
		Random random = new Random();
		int count =0;
		
		for(int i=0;i<row;i++){
			for(int j=0;j<col;j++){
				if(i==j)matrix.elemMatrix[i][j] = ++count;
			}
			
		}
	}
	/**
	 * 
	 * @param matrix
	 * @param col
	 * @return
	 */
	public static double norm(Matrix matrix, int col){
		int row = matrix.getRow();
		
		double result =0;
		for(int i=0;i<row;i++){
			
				result +=Math.pow(matrix.elemMatrix[i][col],2) ;
			
			
		}
		return result;
	}
	public static Vector matrixMultiVector(Matrix m,Vector v){
		int row = m.getRow();
		int col = m.getCol();
		Vector result = new Vector(row);
		for(int i=0;i<row;i++){
			result.vector[i]=0;
			for(int j=0;j<col;j++){
				result.vector[i]+=v.vector[j]*m.elemMatrix[i][j];
			}
					
		}
		return result;
	}
	public static Vector matrixMultiVector(Matrix m,Vector v,int exclude){
		int row = m.getRow();
		int col = m.getCol();
		Vector result = new Vector(row);
		for(int i=0;i<row;i++){
			result.vector[i]=0;
			for(int j=0;j<col;j++){
				if(j!=exclude)
					result.vector[i]+=v.vector[j]*m.elemMatrix[i][j];
			}
					
		}
		return result;
	}
	public static Matrix extendMatrix(Matrix m, int k){
		int row = m.getRow();
		int col = m.getCol();
		
		Matrix result = new Matrix(row+k, col+k);
		for(int i =0;i<k;i++){
			result.elemMatrix[i][i] =1;
		}
		for(int i=k;i<row+k;i++){
			
			for(int j=k;j<col+k;j++){
				result.elemMatrix[i][j] = m.elemMatrix[i-k][j-k];
			}
					
		}
		return result;
	}
	public  static void ident_Sub(Matrix m,int p,double p1){
		int row = m.getRow();
		int col = m.getCol();
		
		for(int i=0;i<row;i++){
			
			for(int j=0;j<col;j++){
				if(i==j) m.elemMatrix[i][j]=1-p* m.elemMatrix[i][j]/p1;
				else m.elemMatrix[i][j]=-p* m.elemMatrix[i][j]/p1;
			}
					
		}
	}
	public static Matrix matrixMultimatrix(Matrix m1,Matrix m2){
		int row1 = m1.getRow();
		int col1 = m1.getCol();
		int row2 = m2.getRow();
		int col2 = m2.getCol();
		Matrix result = new Matrix( row1, col2);
		for(int i=0;i<row1;i++){
			
			for(int j=0;j<col2;j++){
				for(int k=0;k<col1;k++){
					result.elemMatrix[i][j]+= m1.elemMatrix[i][k]*m2.elemMatrix[k][j];
				}
			}
					
		}
		return result;
	}
	public static Matrix transposition(Matrix m){
		int row = m.getRow();
		int col = m.getCol();
		Matrix result = new Matrix( col, row);
		for(int i=0;i<col;i++){
			
			for(int j=0;j<row;j++){
				result.elemMatrix[i][j] = m.elemMatrix[j][i]; 
			}
					
		}
		return result;
	}
	public static void deepCopy(Matrix m1,Matrix m2){
		int row = m1.getRow();
		int col = m1.getCol();
		
		for(int i=0;i<row;i++){
			
			for(int j=0;j<col;j++){
				m1.elemMatrix[i][j] = m2.elemMatrix[i][j]; 
			}
					
		}
	}
	public static void add(Matrix m1,Matrix m2){
		int row = m1.getRow();
		int col = m1.getCol();
		
		for(int i=0;i<row;i++){
			
			for(int j=0;j<col;j++){
				m1.elemMatrix[i][j] += m2.elemMatrix[i][j]; 
			}
					
		}
	}
	public static Matrix matrixInverse(Matrix m1){
		int row = m1.getRow();
		int col = m1.getCol();
		Matrix m = new Matrix(row, col);
		deepCopy(m, m1);
		int []rowSwap = new int[row];
		int []colSwap = new int[row];
		int k,i,j;
		for(k=0 ; k < row; k++){
			double max = 0;
			for(i =k;i<row;i++){
				for(j=k;j<col; j++){
					if(Math.abs(m.elemMatrix[i][j]) >max){
						max = Math.abs(m.elemMatrix[i][j]);
						rowSwap[k] = i;
						colSwap[k] = j;
					}
				}
			}
			if(rowSwap[k] != k){
				double tmp;
				for(j=0;j<row;j++){
					tmp = m.elemMatrix[k][j];
					m.elemMatrix[k][j] = m.elemMatrix[rowSwap[k]][j];
					m.elemMatrix[rowSwap[k]][j]=tmp;
				}
			}
			if(colSwap[k] != k){
				double tmp;
				for(i=0;i<row;i++){
					tmp = m.elemMatrix[i][k];
					m.elemMatrix[i][k] = m.elemMatrix[i][colSwap[k]];
					m.elemMatrix[i][colSwap[k]]=tmp;
				}
			}
			m.elemMatrix[k][k] =1.0 /m.elemMatrix[k][k];
			double para = m.elemMatrix[k][k];
			for(j =0;j< row;j++){
				if(j!=k){
					m.elemMatrix[k][j]*= para;
				}
			}
			for(i =0 ;i<row; i++){
				if(i!=k){
					for( j =0;j< col;j++){
						if(j!=k){
							m.elemMatrix[i][j] = m.elemMatrix[i][j]-m.elemMatrix[i][k]*m.elemMatrix[k][j];
						}
					}

				}
				
			}
			for(i=0;i<row;i++){
				if(i!=k){
					m.elemMatrix[i][k] = -m.elemMatrix[i][k]*para;
				}
			}
			
			
			
		}
		for(k = row-1;k>=0;k--){
			double tmp;
			if(colSwap[k]!=k){
				for(j=0;j<row;j++){
					tmp = m.elemMatrix[k][j];
					m.elemMatrix[k][j] =m.elemMatrix[colSwap[k]][j];
					m.elemMatrix[colSwap[k]][j] = tmp;
				}
			}
			if(rowSwap[k]!=k){
				for(i=0;i<row;i++){
					tmp = m.elemMatrix[i][k];
					m.elemMatrix[i][k] =m.elemMatrix[i][rowSwap[k]];
					m.elemMatrix[i][rowSwap[k]] = tmp;
				}
			}
		}
		
		return  m;
	}
	public static void  diagonalMatrxInit(Matrix m1){
		int row = m1.getRow();
		int col = m1.getCol();
		
		for(int i=0;i<row;i++){
			m1.elemMatrix[i][i] =  Math.random();
					
		}
	}
	public static Matrix diagMultidiag(Matrix m1,Matrix m2){
		int row1 = m1.getRow();
		int col1 = m1.getCol();
		int row2 = m2.getRow();
		int col2 = m2.getCol();
		
		Matrix result = new Matrix( row1, col2);
		for(int i=0;i<row1;i++){			
			
			result.elemMatrix[i][i]+= m1.elemMatrix[i][i]*m2.elemMatrix[i][i];												
		}
		return result;
	}
	public static Matrix diagMultiMatrix(Matrix m1,Matrix m2){
		int row1 = m1.getRow();
		int col1 = m1.getCol();
		int row2 = m2.getRow();
		int col2 = m2.getCol();
		
		Matrix result = new Matrix( row1, col2);
		for(int i=0;i<row1;i++){			
			for(int j=0;j<col2;j++){
				result.elemMatrix[i][j]+= m1.elemMatrix[i][i]*m2.elemMatrix[i][j];
			}
															
		}
		return result;
	}
	public static Matrix matrixMultiDiag(Matrix m1,Matrix m2){
		int row1 = m1.getRow();
		int col1 = m1.getCol();
		int row2 = m2.getRow();
		int col2 = m2.getCol();
		
		Matrix result = new Matrix( row1, col2);
		for(int i=0;i<row1;i++){			
			for(int j=0;j<col2;j++){
				result.elemMatrix[i][j]+= m1.elemMatrix[i][j]*m2.elemMatrix[j][j];
			}
															
		}
		return result;
	}
	public static Matrix  diagonalMatrixInverse(Matrix m1){
		int row = m1.getRow();
		int col = m1.getCol();
		Matrix result = new Matrix(row, col);
		deepCopy(result, m1);
		for(int i=0;i<row;i++){
			result.elemMatrix[i][i] =  1.0/result.elemMatrix[i][i];
					
		}
		return result;
	}
	public  static Matrix diagInit(int row, int col){
		Matrix result = new Matrix(row, col);
		Random random = new Random();
		for(int i=0;i<row; i++){
			double num = Math.random()*sed-sed/2;
			while(num  ==0 ){
				num = Math.random()*sed-sed/2;
			}
			result.elemMatrix[i][i] = (double) num;
		}
		return result;
	}
	public static Matrix matrixRandomInit(int row, int col){
		Matrix result = new Matrix(row, col);
		Random random =  new Random();
		for(int i=0;i<row;i++){
			for(int j=0;j<col;j++){
				result.elemMatrix[i][j] = random.nextGaussian()+Math.sqrt(var);  
			}
		}
		return result;
	}
	public static Matrix upperTrianRandomInit(int row, int col){
		Matrix result = new Matrix(row, col);
		Random random =  new Random();
		for(int i=0;i<row;i++){
			for(int j=i;j<col;j++){
				double num = Math.random()*sed-sed/2;
				while (num ==0){
					num =Math.random()*sed-sed/2;
				}
				result.elemMatrix[i][j] = num;  
			}
		}
		return result;
	}
	
	
	public static Matrix getSubMatrix(Matrix m, int rowBegin,int colBegin,int rowEnd, int colEnd){
		Matrix result = new Matrix(rowEnd-rowBegin+1, colEnd - colBegin +1);
		for(int i=rowBegin;i<= rowEnd; i++){
			for(int  j= colBegin; j <= colEnd; j++){
				result.elemMatrix[i-rowBegin][j-colBegin] = m.elemMatrix[i][j];
			}
		}
		return result;
	}
	
	public static void combineMatrix(Matrix m, Matrix result,int rowBegin,int colBegin,int rowEnd, int colEnd){
		
		for(int i=rowBegin;i< rowEnd; i++){
			for(int  j= colBegin; j < colEnd; j++){
				m.elemMatrix[i][j] = result.elemMatrix[i-rowBegin][j-colBegin];
			}
		}
		
	}
	public static void combineVector(Matrix m, Vector result,int partLoc){
		
		
			for(int  j= 0; j < result.getSize(); j++){
				m.elemMatrix[j][partLoc] = result.getVector()[j];
			}
		
		
	}
	public static void main(String []args){
//		Matrix matrix = new Matrix(6, 6);
//		matrixInit(matrix);
//		
//		Matrix reuslt =schemidt(matrix);
//		matrix.print();
//		reuslt.print();
//		System.out.println(innerProduct(reuslt, reuslt,2, 5));
//		
//		Matrix matrix2 = new Matrix(2,2);
//		matrix2.elemMatrix[0][0]=1;
//		matrix2.elemMatrix[0][1]=0;
//		matrix2.elemMatrix[1][0]=0;
//		matrix2.elemMatrix[1][1]=5;
//		
//		matrixInverse(matrix2).print();;
		
		upperTrianRandomInit(3,3).print();
		
		
		
	}
	
	

}
