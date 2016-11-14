package Akka.akka;

import java.beans.VetoableChangeListener;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import Akka.MatrixMultiplyMessage.MatrixMultiplyWorkerInit;
import Akka.MatrixMultiplyMessage.PartMatrix;
import Akka.MatrixMultiplyMessage.PartMatrixResult;
import Akka.MatrixMultiplyMessage.Result;
import Akka.util.Matrix;
import Akka.util.MatrixOp;
import Akka.util.Vector;
import Akka.util.VectorOp;
import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import junit.runner.ReloadingTestSuiteLoader;

public class MatrixMultiplyWorker extends UntypedActor{

	int loc;
	int vecASize;
	int vecBSize;
	Vector[] vecAList;
	Vector[] vecBList;
	ActorRef coWorker;
	ActorRef manager;
	Matrix result;
	ExecutorService excutor;
	Future<Boolean> com;
	class Compute implements Callable<Boolean> {
		int location ;
		public void setLocation(int location) {
			this.location = location;
		}
		
		public Boolean call() throws Exception {
			// TODO Auto-generated method stub
			for(int i =0;i<location;i++){
				result.getMatrix()[location][i] =
						VectorOp.innerProduct(vecAList[location],vecBList[i]);
			}
			return true;
		}
	}
	@Override
	public void onReceive(Object message) throws Exception {
		// TODO Auto-generated method stub
		if(message instanceof MatrixMultiplyWorkerInit)
		{
			
			MatrixMultiplyWorkerInit mes = (MatrixMultiplyWorkerInit) message;
			this.loc = mes.getLoc();
			this.vecASize = mes.getVecASize();
			this.vecBSize = mes.getVecBSize();
			this.manager = mes.getManager();
			this.coWorker = mes.getCoWorker();
			vecAList = new Vector[this.vecASize];
			vecBList = new Vector[this.vecBSize];
			result  = new Matrix(vecASize, vecBSize);
			excutor = Executors.newSingleThreadExecutor();
			System.out.println("MatrixMultiplyWorker "+loc+" matrix multiply init");
		}
		if(message instanceof PartMatrix){
			PartMatrix mes = (PartMatrix) message;
			int location = mes.getLoc();
//			System.out.println("worker "+loc+": location"+location);
			vecAList[location] = mes.getVecA();
			vecBList[location] = mes.getVecB();
//			mes.getVecA().print();
//			mes.getVecB().print();
			result.getMatrix()[location][location]=
					VectorOp.innerProduct(vecAList[location], vecBList[location]);
			Compute compute = new Compute();
			compute.setLocation(location);
			
				com=excutor.submit(compute);
			
			for(int i =0;i<location;i++){
				result.getMatrix()[i][location] = 
						VectorOp.innerProduct(vecAList[i],vecBList[location]);
			}
			if(location ==vecASize-1&&loc <4&&com.get()){
				
				coWorker.tell(new PartMatrixResult(result), getSelf());
			}
			
		}
		if(message instanceof PartMatrixResult){
			PartMatrixResult mes = (PartMatrixResult) message;
			if(com.get())
				MatrixOp.add(result, mes.getResult());
			manager.tell(new Result(loc, result), getSelf());
		}
	}

}
