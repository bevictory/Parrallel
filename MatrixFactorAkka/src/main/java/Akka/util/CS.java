package Akka.util;

public class CS {
	Matrix  X,H1_s,H2_s,DF0,O_s;
	
	int row ,col;
	public void init(){
		X = MatrixOp.diagInit(row, col);
	}
	public void generateO_s(){
		O_s = MatrixOp.matrixMultimatrix(MatrixOp.matrixMultimatrix(H1_s, DF0),H2_s);
	}
	
	Matrix A_s,S_s,A1_c,A1_s;
	public void generateA1(){
		A1_c = MatrixOp.matrixMultimatrix(MatrixOp.matrixMultimatrix(O_s, A_s),S_s);
	}
	
	public void qr(){
		
	}
	
	Matrix Q,R,DHQ,Q_c,R_c;
	public void generateQ_c(Matrix Q1_c){
		Q_c=MatrixOp.matrixMultimatrix(MatrixOp.matrixMultimatrix(MatrixOp.transposition(O_s), DHQ),Q1_c);
	}
	
	public void generateQ_c(Matrix R1_c,Matrix DHR){
		R_c=MatrixOp.matrixMultimatrix(MatrixOp.matrixMultimatrix(R1_c,DHR),MatrixOp.matrixInverse(S_s));
	}
	
	

}
