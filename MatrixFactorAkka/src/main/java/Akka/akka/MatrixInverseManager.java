package Akka.akka;

import java.util.ArrayList;

import Akka.MatrixInverseMessage.Continue;
import Akka.MatrixInverseMessage.Distance;
import Akka.MatrixInverseMessage.DistanceStop;
import Akka.MatrixInverseMessage.GetX;
import Akka.MatrixInverseMessage.Iteration;
import Akka.MatrixInverseMessage.MatrixInverseInit;
import Akka.MatrixInverseMessage.MatrixInverseWorkerInit;
import Akka.MatrixInverseMessage.XIteration;
import Akka.MatrixInverseMessage.XResult;
import Akka.util.Matrix;
import Akka.util.MatrixOp;
import Akka.util.Vector;
import Akka.util.VectorOp;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import scala.annotation.implicitNotFound;
import scala.collection.immutable.Seq;

public class MatrixInverseManager extends UntypedActor{

	
	Matrix A;
	
	Matrix result;
	int workerNum;
	ArrayList<ActorRef> workerList;
	Vector x;
	Vector xNext;
	int iterComplete =0;
	double atol = 1e-10;
	int blockSize;
	int lastBlockSize;
	int seq[];
	double  dis[];
	boolean isComplete = false;
	@Override
	public void onReceive(Object message) throws Exception {
		// TODO Auto-generated method stub
		if(message instanceof MatrixInverseInit){
			int dim =8;
			A = new Matrix(dim, dim);
			MatrixOp.diagonalMatrxInit(A);
			A.print();
			
			result = new Matrix(dim, dim);
			workerList = new ArrayList<ActorRef>();
			for(int i=0;i<8;i++)
				workerList.add(getContext().actorOf(Props.create(MatrixInverseWorker.class),
					"worker"+i));
			workerNum = workerList.size();
			blockSize = dim/workerNum;
			 lastBlockSize = dim /workerNum +dim%workerNum;
			
			seq = new int[A.getCol()];
			dis =   new double [A.getCol()];
					
			int i=0;
			int begin =0;
			int end = blockSize;
			
			for(ActorRef ref : workerList){
				Matrix partA = MatrixOp.getSubMatrix(A, begin, 0, end-1, A.getCol()-1);
				//partA.print();
				ref.tell(new MatrixInverseWorkerInit(partA, i, begin, end, 
						workerList,atol,workerNum ,  getSelf()), getSelf());
				i++;
				begin+=blockSize;
				if(i == workerNum-1){
					end += lastBlockSize;
				}else end += blockSize;
				
			}
			
			
			
		}
		
		
		if(message instanceof Distance){
			Distance mes = (Distance) message;
//			System.out.println("[manager ]: get distance "+mes.getPartLoc());
			seq[mes.getPartLoc()]++;
			dis[mes.getPartLoc()]+= mes.getDis();
//			System.out.println("[manager ]: "+"get distance from " +mes.getPartLoc() );
			if(seq[mes.getPartLoc()] == workerNum){
				System.out.println("[manager ]: "+"get all distance from " +mes.getPartLoc() );
				if(dis[mes.getPartLoc()]<=atol){
					
					workerList.get(mes.getPartLoc()/blockSize).tell(new GetX(mes.getPartLoc()), ActorRef.noSender());
				}else {
					seq[mes.getPartLoc()] = 0;
					dis[mes.getPartLoc()] = 0;
					for(ActorRef ref : workerList){
						ref.tell(new Continue(mes.getPartLoc()), ActorRef.noSender());
					}
				}
			}
			
		}
		if(message instanceof XResult){
			iterComplete++;
			
			XResult mes = (XResult ) message;
			System.out.println("[manager ]: get XResult "+mes.getPartLoc());
			mes.getX().print();
			MatrixOp.combineVector(result, mes.getX(), mes.getPartLoc());
			if(iterComplete == result.getCol()){
				isComplete = true;
				for(ActorRef ref : workerList){
					context().stop(ref);
				}
			}
		}
		if(message instanceof DistanceStop){
			
			DistanceStop mes = (DistanceStop) message;
//			System.out.println("[manager ]: get distance stop from"
//					+ mes.getPartLoc());
			seq[mes.getPartLoc()] =0;
			dis[mes.getPartLoc()] = 0;
			
		}
//		if(message instanceof XIteration){
//			XIteration partXNext = (XIteration) message;
//			seq++;
//			VectorOp.combine(xNext, partXNext.getX(), partXNext.getBegin(), partXNext.getEnd());
//			if (seq == workerNum) {
//				if (!VectorOp.allclose(x, xNext, atol)) {			
//					x=xNext;
//					for (ActorRef ref : workerList) {
//						
//						ref.tell(new Iteration() , getSelf());
//					}
//
//				}
//				else {
//					x=xNext;
//					for(ActorRef ref : workerList){
//						context().stop(ref);
//					}
//				}
//			}
//		}
		
	}

}
