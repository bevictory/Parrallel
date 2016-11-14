package ImprovedHighOrderLanczos.improvedHighOrderLanczos;

import java.awt.image.FilteredImageSource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.naming.InitialContext;
import javax.print.attribute.SetOfIntegerSyntax;

import ImprovedHighOrderLanczos.improvedHighOrderLanczos.Main.Work;
import ImprovedHighOrderLanczos.message.AlphaAndBeta;
import ImprovedHighOrderLanczos.message.DeltaAndEpsil;
import ImprovedHighOrderLanczos.message.Stop;
import ImprovedHighOrderLanczos.message.WMessage;
import ImprovedHighOrderLanczos.message.WorkerInit;
import ImprovedHighOrderLanczos.message.WorkerInitNoM;
import ImprovedHighOrderLanczos.util.Tensor;
import ImprovedHighOrderLanczos.util.TensorOp;
import akka.actor.Actor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import scala.annotation.implicitNotFound;
import scala.collection.parallel.immutable.ParMap;


public class WorkerActor extends UntypedActor{
	
	
	Tensor partM;
	Tensor W;
	int loc;
	int blockSize;
	int workerNum;
	Tensor partW;
	Tensor partU;
	Tensor partQ;
	Tensor partWNext;
	Tensor partQNext;
	
	Tensor[] WArray ;
	
	double delta;
	double epsil;
	double beta;
	double alpha;
	int seq=0;
	int deSeq=0;
	int iter =1;
	List<ActorRef> workerList;
	ActorRef manager;
	
	long time ;
	
	class ComputeEpsil implements Callable<Double>{

		public Double call() throws Exception {
			// TODO Auto-generated method stub
//			long time = System.nanoTime();
//			System.out.println("computeEpsil "+time);
			double epsil = TensorOp.multiOrderParallel(partW, partW).getByLoc(0);
//			System.out.println("computeEpsil "+(System.nanoTime()-time));
			return epsil;
		}

		
		
	}
	
	
	class SendMessage implements Runnable{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			long sendTime = System.nanoTime();
			WMessage sendMes = new WMessage(partWNext, loc);
			for(int i =0;i<workerNum;i++){
				if(i!=loc){
					workerList.get(i).tell(sendMes, getSelf());
					System.out.println("[worker "+loc+" ]: sendWMessage to "+i+" "+(System.nanoTime()-sendTime));
					sendTime = System.nanoTime();
				}
			}
		}
		
	}
	
	class SendDeltaEpsil implements Runnable{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			DeltaAndEpsil sendMes = new DeltaAndEpsil(delta, epsil);
			
			for(int i =0;i<workerNum;i++){
				if(i!=loc){
					workerList.get(i).tell(sendMes, getSelf());
					System.out.println("[worker "+loc+" ]: "+"send Delta Epsil to "+ i );
				}
			}
		}
		
	}
	
	ExecutorService executor;
	Future<Double> epsil_future;
	long  receiveTime;
	long  computeQTime;
	long  receiveDETime;
	long  receiveABTime;
	public void init(WorkerInit message){
		this.partM = message.getPartM();
		this.W = message.getW();
		this.loc = message.getLoc();
		this.blockSize = message.getBlockSize();
		this.workerNum = message.getWorkerNum();
		
//		System.out.println("worker workerNum "+workerNum+" partM size "+partM.getSize()
//		+" w size "+W.getSize());
		
		workerList = new ArrayList<ActorRef>();
		workerList = message.getWorkers();
//		System.out.println("worker "+loc + workerList);
		this.manager = message.getManager();
		executor = Executors.newSingleThreadExecutor();
		partW = TensorOp.getPartFromTensor(W, loc*(W.getSize()/workerNum), blockSize);
	}
	public void initNoM(WorkerInitNoM message){
		System.gc();
		this.W = message.getW();
		this.loc = message.getLoc();
		this.blockSize = message.getBlockSize();
		this.workerNum = message.getWorkerNum();
		this.partM = new Tensor(1,(int)message.getMDim()*blockSize);
		TensorOp.tensorInit(this.partM);
//		System.out.println("worker workerNum "+workerNum+" partM size "+partM.getSize()
//		+" w size "+W.getSize());
		
		workerList = new ArrayList<ActorRef>();
		workerList = message.getWorkers();
//		System.out.println("worker "+loc + workerList);
		this.manager = message.getManager();
		executor = Executors.newSingleThreadExecutor();
		
		partW = TensorOp.getPartFromTensor(W, loc*(W.getSize()/workerNum), blockSize);
	}
	
	@Override
	public void onReceive(Object message) throws Exception {
		// TODO Auto-generated method stub
		
		if(message instanceof WorkerInitNoM){
			initNoM((WorkerInitNoM)message);
			
			
			WArray = new Tensor[workerNum];
			partQ = new Tensor(1,partW.getSize());
			System.out.print("[worker "+loc+" ]: ");
			partU = TensorOp.multiOrderParallel(partM, W);
			//System.out.println("partU size "+ partU.getSize());
			//long time  = System.currentTimeMillis();
			Future<Double> epsil_future =executor.submit(new ComputeEpsil());
			
			delta =  TensorOp.multiOrderParallel(partU, partW).getByLoc(0);
//			epsil = TensorOp.multiOrderParallel(partW, partW).getByLoc(0);
			epsil = epsil_future.get();
			receiveABTime = System.nanoTime();
			//System.out.println("running " +(System.currentTimeMillis()-time));
//			System.out.println("[worker "+loc+" ]: "+"send Delta Epsil");
//			executor.execute(new SendDeltaEpsil());
			
			getSender().tell(new DeltaAndEpsil(delta, epsil), getSelf());
			
//			DeltaAndEpsil sendMes = new DeltaAndEpsil(delta, epsil);
//			
//			for(int i =0;i<workerNum;i++){
//				if(i!=loc){
//					workerList.get(i).tell(sendMes, getSelf());
//					System.out.println("[worker "+loc+" ]: "+"send Delta Epsil to "+ i );
//				}
//			}
//			
//			
//			if(1 == workerNum){
//				
//				getSelf().tell(new DeltaAndEpsil(delta, epsil), getSelf());;
//				
//				
//			}
			
		}
		
		
		if(message instanceof WorkerInit){
			init((WorkerInit)message);
			
			
			WArray = new Tensor[workerNum];
			partQ = new Tensor(1,partW.getSize());
			System.out.print("[worker "+loc+" ]: ");
			partU = TensorOp.multiOrderParallel(partM, W);
			//System.out.println("partU size "+ partU.getSize());
			//long time  = System.currentTimeMillis();
			 epsil_future =executor.submit(new ComputeEpsil());
			
			delta =  TensorOp.multiOrderParallel(partU, partW).getByLoc(0);
//			epsil = TensorOp.multiOrderParallel(partW, partW).getByLoc(0);
			epsil = epsil_future.get();
			receiveABTime = System.nanoTime();
			//System.out.println("running " +(System.currentTimeMillis()-time));
//			System.out.println("[worker "+loc+" ]: "+"send Delta Epsil");
//			executor.execute(new SendDeltaEpsil());
			
			getSender().tell(new DeltaAndEpsil(delta, epsil), getSelf());
			
//			DeltaAndEpsil sendMes = new DeltaAndEpsil(delta, epsil);
//			
//			for(int i =0;i<workerNum;i++){
//				if(i!=loc){
//					workerList.get(i).tell(sendMes, getSelf());
//					System.out.println("[worker "+loc+" ]: "+"send Delta Epsil to "+ i );
//				}
//			}
//			
//			
//			if(1 == workerNum){
//				
//				getSelf().tell(new DeltaAndEpsil(delta, epsil), getSelf());;
//				
//				
//			}
			
		}
		
		if(message instanceof AlphaAndBeta){
			System.out.println("[worker "+loc+" ]: "+"receiveAB "+(System.nanoTime()-receiveABTime));
			alpha = ((AlphaAndBeta)message).getAlpha();
			beta = ((AlphaAndBeta)message).getBeta();
			
			partQNext = TensorOp.multiNum1(partW, beta);
			
			partWNext = TensorOp.getNextW(partU, partQ, partQNext, beta, alpha);
			System.out.println("[worker "+loc+" ]: "+" compute alpha and partWNext "+(System.nanoTime()-computeQTime));
			time = System.nanoTime();
			
//			WMessage sendMes = new WMessage(partWNext, loc);
//			for(int i =0;i<workerNum;i++){
//				if(i!=loc){
//					workerList.get(i).tell(sendMes, getSelf());
//				}
//			}
			
			executor.execute(new SendMessage());
			 epsil_future =executor.submit(new ComputeEpsil());
			
			System.out.println("send Wmessage "+(System.nanoTime()-time));
			partU.reset();
			long computePartU = System.nanoTime();
			computeU(partM, partWNext, loc);
			//epsil = TensorOp.multiOrderParallel(partWNext,partWNext).getByLoc(0);
			WArray[loc] = partWNext;
			
			System.out.println("[worker "+loc+" ]: "+" computePartU "+(System.nanoTime()-computePartU));
			receiveTime = System.nanoTime();
			
			if(workerNum ==1){
				partW = partWNext;
				partQ = partQNext;
//				long time = System.currentTimeMillis();
				
//				Future<Double> epsil_future =executor.submit(new ComputeEpsil());
				
				delta =  TensorOp.multiOrderParallel(partU, partW).getByLoc(0);
//				epsil = TensorOp.multiOrderParallel(partW, partW).getByLoc(0);
				epsil = epsil_future.get();
				
//				System.out.println("runing "+(System.currentTimeMillis() - time));
				
//				getSelf().tell(new DeltaAndEpsil(delta, epsil), getSelf());;
				//executor.execute(new SendDeltaEpsil());
				manager.tell(new DeltaAndEpsil(delta, epsil), getSelf());
			}
		}
		
		if(message instanceof WMessage){
			WMessage mes = (WMessage)message;
			if(seq==0)System.out.println("[worker "+loc+" ]: "+" receive first Wmessage"+(System.nanoTime()-receiveTime));
			if(seq!=0)System.out.println("[worker "+loc+" ]: "+" receive Wmessage"+(System.nanoTime()-receiveTime));
			if(mes.getLoc() != loc){
				WArray[mes.getLoc()] = mes.getPartW();
				seq++;
				computeU(partM, mes.getPartW(), mes.getLoc());
				receiveTime = System.nanoTime();
			}
			if(seq == workerNum-1){
				System.out.println("[worker "+loc+" ]: "+"computeU "+(System.nanoTime()-time));
				seq = 0;
				partW = partWNext;
				partQ = partQNext;
				long time = System.nanoTime();
//				System.out.println("worker computeEpsil "+loc+" "+time);
//				Future<Double> epsil_future =executor.submit(new ComputeEpsil());
//				System.out.println("[worker "+loc+" ]: "+"worker computeDelta "+loc+" "+System.nanoTime());
				delta =  TensorOp.multiOrderParallel(partU, partW).getByLoc(0);
//				System.out.println("worker computeDelta "+loc+" "+System.nanoTime());
////				epsil = TensorOp.multiOrderParallel(partW, partW).get(0);
				epsil = epsil_future.get();
//				System.out.println("worker getEpsil "+loc+" "+System.nanoTime());
//				System.out.println("[worker "+loc+" ]: "+"compute delta "+loc+" "+(System.nanoTime()-time));
				
				computeQTime = System.nanoTime();
				receiveABTime = System.nanoTime();
				receiveDETime =System.nanoTime();
//				executor.execute(new SendDeltaEpsil());
				
				manager.tell(new DeltaAndEpsil(delta, epsil), getSelf());
				
			}
		}
		
	}
	
	public void computeU(Tensor tensor1, Tensor partW,int loc){
		// W size 
		int basicSize = W.getSize();
		//System.out.println("basicSize "+basicSize );
		//partU size, 
		int size = partU.getSize();
		
//		long time = System.nanoTime();
//		System.out.println("[worker "+this.loc+" ]: "+"partU size "+size+" partW size "+partW.getSize());
		
		int beginLoc = basicSize / workerNum * loc;
		int from = 0;
		long steps=0;
		for (int i = 0; i < size; i++) {
			int start = from + beginLoc;
			int end = start + partW.getSize();
//			System.out.println("start "+start+" end "+end);
			for (int j = start, k = 0; j < end; j++, k++) {
				partU.setByLoc(partU.getByLoc(i) + tensor1.getByLoc(j) * partW.getByLoc(k), i);
				steps++;
			}

			from += basicSize;
		}
//		System.out.println("[worker "+this.loc+" ]: "+"compute Part U "+(System.nanoTime()-time)+" steps "+steps);
		
	}

}
