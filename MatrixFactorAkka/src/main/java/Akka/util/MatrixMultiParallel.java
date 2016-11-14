package Akka.util;

import java.util.ArrayList;

import Akka.MatrixMultiplyMessage.MatrixMultiplyWorkerInit;
import Akka.message.BlockMatrix;
import akka.actor.ActorRef;
import akka.remote.RemoteTransportException;
import scala.inline;
import scala.annotation.implicitNotFound;
import scala.collection.generic.BitOperations.Int;

public class MatrixMultiParallel {
	public Matrix[][] matrixCut(Matrix m,int rowNum,int colNum){
		Matrix[][]  result = new Matrix[rowNum][colNum];
		int blockRow = m.getRow()/rowNum;
		int blockCol = m.getCol()/colNum;
		int lastBlockROw = m.getRow()%rowNum+blockRow;
		int lastBlockCol = m.getCol()%colNum + blockCol;
		for(int i = 0;i< rowNum;i++){
			for(int j= 0; j<colNum;j++){
				if(i<rowNum-1&&j<colNum-1){
					result[i][j] = MatrixOp.getSubMatrix(m, i*blockRow, j*blockCol, (i+1)*blockRow-1, (j+1)*blockCol-1);
				}else if(i==rowNum-1&&j<colNum-1){
					result[i][j] = MatrixOp.getSubMatrix(m, i*blockRow, j*blockCol, (i)*blockRow+lastBlockROw-1, (j+1)*blockCol-1);
				}else if(i<rowNum-1&&j==colNum-1){
					result[i][j] = MatrixOp.getSubMatrix(m, i*blockRow, j*blockCol, (i+1)*blockRow-1, (j)*blockCol+lastBlockCol-1);
				}else {
					result[i][j] = MatrixOp.getSubMatrix(m, i*blockRow, j*blockCol, (i)*blockRow+lastBlockROw-1, (j)*blockCol+lastBlockCol-1);
				}
			}
		}
		return result;
		
	}
	public void distribute(Matrix [][]a,Matrix [][]b){
		int rowNum = a.length;
		int colNum = a[0].length;
		boolean first = true;
		for(int i=0;i<rowNum;i++){
			for(int j=0;j<rowNum ;j++){
				for(int k=0;k<colNum ;k++){
					if(first) new BlockMatrix(i, j, a[i][k], b[i][k]);
					else new BlockMatrix(i, j, a[i][k], b[i][k]);
					first=!first;
				}
			}
		}
	}
	public static Vector getPartFromA(Matrix A,int i, int j ,int rowNum,int colNum, int size){
		int rowStart = (i-1)*(A.getRow()/rowNum);
		int colStart =(j-1)*(A.getCol()/colNum);
		int colEnd = colStart+ size;
		double []vec = new double[size];
		for(int k =colStart;k<colEnd;k++){
			vec[k] = A.getMatrix()[rowStart][k];
		}
		return new Vector(vec);
	}
	public static  Vector getPartFromB(Matrix B,int i, int j ,int rowNum,int colNum, int size){
		int rowStart = (i-1)*(B.getRow()/rowNum);
		int colStart =(j-1)*(B.getCol()/colNum);
		int rowEnd = rowStart+ size;
		double []vec = new double[size];
		for(int k =rowStart;k<rowEnd;k++){
			vec[k] = B.getMatrix()[k][colStart];
		}
		return new Vector(vec);
	}
	public void distribute(Matrix A,Matrix B){
		int rowNum=2,colNum =2;
		int blockRow = A.getRow()/rowNum;
		int blockCol = A.getCol()/colNum;
		int lastBlockRow = A.getRow()%rowNum+blockRow;
		int lastBlockCol = A.getCol()%colNum + blockCol;
		boolean first = true;
		
		ArrayList<ActorRef> workerList =null;
		
		for(ActorRef ref: workerList){
			for(int i =0;i<rowNum;i++){
				for(int j=0;j<colNum;j++){
					int vecASize = i == rowNum -1? lastBlockRow:blockRow;
					int vecBSize = j== colNum -1? lastBlockCol:blockCol;
					int loc =(i-1)*rowNum+colNum;
					ActorRef  coWorker;
					if(loc >= rowNum *colNum){
						coWorker = workerList.get(loc - rowNum*colNum);
					}else coWorker =ActorRef.noSender();
					
					ref.tell(new MatrixMultiplyWorkerInit(vecASize , vecBSize, loc,coWorker,coWorker),ActorRef.noSender() );
				}
			}
			
		}
		for(int  i =0;i< rowNum;i++){
			for(int j =0;j<colNum;j++){
				for(int k =0;k<colNum;k++){
					if(k==0) {
						getPartFromA(A, i, k, rowNum, colNum, blockRow);
						getPartFromB(B, i, k, rowNum, colNum, blockCol);
					}else {
						getPartFromA(A, i, k, rowNum, colNum, lastBlockRow);
						getPartFromB(B, i, k, rowNum, colNum, lastBlockCol);
					}
				}
			}
		}
		
	}

}
