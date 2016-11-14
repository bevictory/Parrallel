package Akka.akka;
import java.util.ArrayList;

import javax.swing.RowFilter;

import Akka.MatrixMultiplyMessage.MatrixMultiplyInit;
import Akka.MatrixMultiplyMessage.MatrixMultiplyWorkerInit;
import Akka.MatrixMultiplyMessage.PartMatrix;
import Akka.MatrixMultiplyMessage.Result;
import Akka.util.Matrix;
import Akka.util.MatrixOp;
import Akka.util.Vector;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.actor.dsl.Creators.Act;
import scala.annotation.implicitNotFound;

public class MatrixMultiplyManager2 extends UntypedActor{

	Matrix A;
	Matrix B;
	Matrix result;
	ArrayList<ActorRef> workerList =null;
	int blockRow;
	int blockCol;
	int seq=0;
	long startTime;
	@Override
	public void onReceive(Object message) throws Exception {
		// TODO Auto-generated method stub
		if(message instanceof MatrixMultiplyInit){
			int dim=500;
			A = MatrixOp.matrixRandomInit(dim, dim);
			B= MatrixOp.matrixRandomInit(dim, dim);
//			A.print();
//			System.out.println();
//			B.print();
			startTime = System.nanoTime();
			MatrixOp.matrixMultimatrix(A, B);
			System.out.println("running time "+(System.nanoTime()-startTime));
			result = new Matrix(dim, dim);
			
			
			workerList = new ArrayList<ActorRef>();
			for(int i=0;i<8;i++)
			workerList.add(getContext().actorOf(Props.create(MatrixMultiplyWorker.class),
					"worker"+i));
			
			
			startTime = System.nanoTime();
			distribute(A, B);
		}
		if(message instanceof Result){
			seq++;
			Result mes = (Result) message;
			int loc = mes.getLoc()-4;
			int rowLoc = loc/2;
			int colLoc = loc%2;
			int rowBegin = rowLoc *blockRow;
			int colBegin = colLoc * blockCol;
			
			int rowEnd = rowBegin+mes.getResult().getRow();
			int colEnd = colBegin + mes.getResult().getCol();
			MatrixOp.combineMatrix(result, mes.getResult(),
					rowBegin, colBegin, rowEnd, colEnd);
			
			if(seq == 4){
//				result.print();
				System.out.println("distribute running time "+(System.nanoTime()-startTime));
				
			}
		}
	}
	public void distribute(Matrix A,Matrix B){
		int rowNum=2,colNum =2;
		 blockRow = A.getRow()/rowNum;
		 blockCol = A.getCol()/colNum;
		int lastBlockRow = A.getRow()%rowNum+blockRow;
		int lastBlockCol = A.getCol()%colNum + blockCol;
		boolean first = true;
		
		
		
		
			for(int i =0;i<rowNum;i++){
				for(int j=0;j<colNum;j++){
					int vecASize = i == rowNum -1? lastBlockRow:blockRow;
					int vecBSize = j== colNum -1? lastBlockCol:blockCol;
					int loc =(i)*rowNum+j;
					ActorRef  coWorker;
					
						coWorker = workerList.get(loc +rowNum*colNum);
				
						
						
					
					
					workerList.get(loc).tell(new MatrixMultiplyWorkerInit(vecASize , vecBSize, loc,
							getSelf(),coWorker),ActorRef.noSender() );
					coWorker =ActorRef.noSender();workerList.get(colNum*rowNum+loc).tell(new MatrixMultiplyWorkerInit(vecASize , vecBSize, loc+colNum*rowNum,
							getSelf(),coWorker),ActorRef.noSender() );
				}
			}
			
		
		int num =0;
		while (true) {

			for (int i = 0; i < rowNum; i++) {
				for (int j = 0; j < colNum; j++) {
					int loc = (i ) * rowNum + j;
					int vecASize = i == rowNum - 1 ? lastBlockRow : blockRow;
					int vecBSize = j == colNum - 1 ? lastBlockCol : blockCol;
					for (int k = 0; k < colNum; k++) {

						if (k == 0) {
							Vector partA, partB;
							if (num < vecASize) {
								partA = getPartFromA(i * blockRow + num, k * blockCol, k * blockCol + blockCol);

								// }
								// if(num < vecBSize){
								partB = getPartFromB(k * blockRow, k * blockRow + blockRow, j * blockCol + num);
								workerList.get(loc).tell(new PartMatrix(partA, partB, num), getSelf());
							}

						} else {
							loc += rowNum * colNum;
							Vector partA, partB;
							if (num < vecASize) {
								
								partA = getPartFromA(i * blockRow + num, k * blockCol, k * blockCol + lastBlockCol);

								// }
								// if(num < vecBSize){
								partB = getPartFromB(k * blockRow, k * blockRow + lastBlockRow, j * blockCol + num);
								workerList.get(loc).tell(new PartMatrix(partA, partB, num), getSelf());
							}

						}

					}

					
				}
			}
			num++;
			if (num == lastBlockCol ) {
				break;
			}
		}
		
	}
	
	public Vector getPartFromA(int rowBegin,int colBegin,int colEnd){
		double []vec = new double[colEnd -colBegin];
		for(int i=0, k =colBegin;k<colEnd;k++,i++){
			vec[i] = A.getMatrix()[rowBegin][k];
		}
		return new Vector(vec);
	}
	public Vector getPartFromB(int rowBegin,int rowEnd,int colBegin){
		double []vec = new double[rowEnd -rowBegin];
		for(int i=0,k =rowBegin;k<rowEnd;k++,i++){
			vec[i] = B.getMatrix()[k][colBegin];
		}
		return new Vector(vec);
	}
	

}
