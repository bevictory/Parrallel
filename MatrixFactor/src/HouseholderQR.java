
/**
 * Household transformation qr 分解
 */
public class HouseholderQR {
	private Matrix A;
	private Matrix tempA=A;
	private Matrix[] Q;
	private Matrix q;
	private Matrix r;
	private int iter;
	public HouseholderQR(Matrix a){
		this.A = a;
		tempA=A;
	}
	public void setQ(Matrix[] q) {
		Q = q;
	}
	public void setQ(Matrix q) {
		this.q = q;
	}
	public void setR(Matrix r) {
		this.r = r;
	}
	public void deal(int loc ){
		Matrix aMatrix=tempA;
		
		
		int size =aMatrix.getRow();
		Vector u = new Vector(aMatrix.getRow());
		double a = Math.sqrt(MatrixOp.norm(aMatrix, 0));
		System.out.println("a " +a);
		for(int i=0;i<size;i++){
			if(i==0) u.vector[i] = aMatrix.elemMatrix[i][0] - a;
			else u.vector[i] = aMatrix.elemMatrix[i][0];
		}
		VectorOp.div(u, Math.sqrt(VectorOp.norm(u)));
		System.out.println("u "+loc);
		u.print();
		Matrix q = VectorOp.outerProduct(u, u);
		
		ident_Sub(q, 2);
		System.out.println("q "+loc);
		q.print();
		tempA=subMatrix(MatrixOp.matrixMultimatrix(q, tempA));
		tempA.print();
		Q[loc] = extendMatrix(q, loc);
		Q[loc].print();
		
	}
	public void qr(){
		int row = A.getRow();
		int col = A.getCol();
		iter = Math.min(row-1, col);
		Q = new Matrix[iter];
		for(int i =0;i<iter;i++){
			deal(i);
		}
		
		
		q.print();
		r.print();
	}
	public void getQProcess(){
		q= MatrixOp.transposition(Q[0]);
		for(int i =1;i<iter;i++){
			q= MatrixOp.matrixMultimatrix(q, MatrixOp.transposition(Q[i]));
		}
	}
	public void getRProcess(){
		r= MatrixOp.transposition(Q[iter-1]);
		for(int i =iter-2;i>=0;i--){
			r= MatrixOp.matrixMultimatrix(r, Q[i]);
		}
		r= MatrixOp.matrixMultimatrix(r, A);
	}
	public Matrix getQ() {
		return q;
	}
	public Matrix getR() {
		return r;
	}
	/**
	 * 单位矩阵-p*m
	 * @param m
	 * @param p
	 */
	public  void ident_Sub(Matrix m,int p){
		int row = m.getRow();
		int col = m.getCol();
		
		for(int i=0;i<row;i++){
			
			for(int j=0;j<col;j++){
				if(i==j) m.elemMatrix[i][j]=1-p* m.elemMatrix[i][j];
				else m.elemMatrix[i][j]=-p* m.elemMatrix[i][j];
			}
					
		}
	}
	/**
	 * 扩展q 在左上角添加k*k的单位矩阵
	 * @param m
	 * @param k
	 * @return
	 */
	public Matrix extendMatrix(Matrix m, int k){
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
	
	public Matrix subMatrix(Matrix m){
		int row = m.getRow();
		int col = m.getCol();
		Matrix result = new Matrix(row-1, col-1);
		for(int i=0;i<row-1;i++){
			
			for(int j=0;j<col-1;j++){
				result.elemMatrix[i][j] = m.elemMatrix[i+1][j+1];
			}
					
		}
		return result;
	}
	public static void main(String []args){
		Matrix matrix = new Matrix(3, 3);
		matrix.elemMatrix[0][0] = 12;
		matrix.elemMatrix[0][1] = -51;
		matrix.elemMatrix[0][2] = 4;
		matrix.elemMatrix[1][0] = 6;
		matrix.elemMatrix[1][1] = 167;
		matrix.elemMatrix[1][2] = -68;
		matrix.elemMatrix[2][0] = -4;
		matrix.elemMatrix[2][1] = 24;
		matrix.elemMatrix[2][2] = -41;
		matrix.print();
		HouseholderQR qr = new HouseholderQR( matrix);
		qr.qr();
		
	}

}
