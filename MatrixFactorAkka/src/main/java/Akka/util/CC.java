package Akka.util;

public class CC {
	//Dj Fj Ϊ�ԽǾ���
	int row ,col;
	Vector X;
	Matrix O,D1,D2,F1,F2,H1;
	public void init(){
		X = VectorOp.randomInit(row);
		D1 = MatrixOp.diagInit(row, row);
		D2 = MatrixOp.diagInit(row, row);
		F1 = MatrixOp.diagInit(row, row);
		F2 = MatrixOp.diagInit(row, row);
		S = MatrixOp.upperTrianRandomInit(col, col);
		Da = MatrixOp.diagInit(row, row);
		Ds = MatrixOp.diagInit(col, col);
		Fa = MatrixOp.diagInit(col, col);
		Fs = MatrixOp.diagInit(col, col);
		
	}
	
	
	public  void generateH1(Vector v){
		 H1 =VectorOp.outerProduct(v, v);
		 MatrixOp.ident_Sub(H1, 2, VectorOp.norm(v));
	}
	
	public void generateH_(Matrix H){
		Matrix H2 = MatrixOp.extendMatrix(H, 1);
		Matrix H1_s=MatrixOp.matrixMultiDiag( MatrixOp.diagMultiMatrix(D1, H1),F1);
		Matrix H2_s=MatrixOp.matrixMultiDiag( MatrixOp.diagMultiMatrix(D2, H2),F2);		
	}
	public void generateDF0(){
		Matrix DF0 = MatrixOp.diagMultidiag(MatrixOp.diagonalMatrixInverse(F1),MatrixOp.diagonalMatrixInverse(D2));
		
	}
	
	
	Matrix A,S,A_s,S_s,A1_s;
	Matrix Da,Fa,Ds,Fs;
	public void generateA(){
		Fa = MatrixOp.diagonalMatrixInverse(F2);
		Ds = MatrixOp.diagonalMatrixInverse(Da);
		A_s = MatrixOp.matrixMultiDiag(MatrixOp.diagMultiMatrix(Fa, A), Da);
		S_s= MatrixOp.matrixMultiDiag(MatrixOp.diagMultiMatrix(Fs, S), Ds);
	}
	public void generateA1(Matrix A1_c){
		A1_s =MatrixOp.matrixMultiDiag(MatrixOp.diagMultiMatrix(MatrixOp.diagonalMatrixInverse(D1) ,A1_c)
				, MatrixOp.diagonalMatrixInverse(Fs));
	}
	
	
	Matrix Q,Q1,DQ,FQ,Q1_s,DHQ_s;
	public void generateQ1_s(){
		Q1_s =MatrixOp.diagMultidiag(MatrixOp.diagMultidiag(FQ, Q1),DQ);
	}
	public void generateDHQ_s(){
		MatrixOp.diagMultidiag(D1, MatrixOp.diagonalMatrixInverse(FQ));
	}
	public void generateQ(Matrix Q_s){
		Q = MatrixOp.matrixMultiDiag(MatrixOp.diagMultiMatrix(F2, Q_s),MatrixOp.diagonalMatrixInverse(DQ));
	}
	
	Matrix R,R1,DR,FR,R1_s,DHR_s;
	public void generateR1_s(){
		R1_s =MatrixOp.matrixMultiDiag(MatrixOp.diagMultiMatrix(FR, R1),DR);
	}
	public void generateDHR_s(){
		MatrixOp.diagMultidiag( MatrixOp.diagonalMatrixInverse(DR),Fs);
	}
	public void generateR(Matrix R_s){
		R = MatrixOp.matrixMultiDiag(MatrixOp.diagMultiMatrix(MatrixOp.diagonalMatrixInverse(FR), R_s),Ds);
	}
	

}
