package Akka.akka;



import Akka.message.AResult;
import Akka.message.Init;
import Akka.message.NormResult;
import Akka.message.QReuslt;
import Akka.message.Result;
import Akka.message.ResultTensor;
import Akka.message.Tensor;
import akka.actor.UntypedActor;


public class WorkActor extends UntypedActor {
	double b=0;
	double a=0;
	Tensor partA,partQ,qPre,qNext=null,partU,partN;
	int loc;
	int blockSize;
	@Override
	public void onReceive(Object message) throws Exception {
		// TODO Auto-generated method stub
		if(message instanceof Init ){
			init((Init)message);
			
//			computeU();
//			computeA();
			
			partU = TensorOp.multiOrderParallel(partA, qPre);
			
			a = TensorOp.multiOrderParallel(partQ, partU).get(0);
			
			System.out.println("[Worker"+loc+"] :Init and computeU /coumputeA");
			System.out.println("[Worker"+loc+"] :Send part a to manager "+a);
			
			getSender().tell(new AResult(a), getSelf());
		}
		if(message instanceof AResult){
			a = ((AResult)message).getA();
			
//			computeNandNorm();
			
			partN = TensorOp.tesorSubStraction(partU, partQ, a);
			
			b = TensorOp.norm2(partN);
			
			System.out.println("[Worker"+loc+"] :Get a and computeNandNorm "+a);
			System.out.println("[Worker"+loc+"] :Send normResult to manager "+b);
			
			getSender().tell(new NormResult(b), getSelf());
		}
		if(message instanceof NormResult){
			
			b = ((NormResult)message).getB();
			
//			computeQ();
			
			TensorOp.multiNum(partN, 1.0/b);
			qNext = partN;
			
			System.out.println("[Worker"+loc+"] :Get normResult and computeQ "+b);
			
			getSender().tell(new QReuslt(qNext, loc,blockSize), getSelf());
		}
		if(message instanceof QReuslt){
			qNext = ((QReuslt)(message)).getQ();
			
			
//			computeUnext();
//			qPre = qNext;
//			getParQ();
//			computeA();
			
			 partU = TensorOp.tesorSubStraction(TensorOp.multiOrderParallel(partA, qNext)
					 , partQ, b);
			 qPre = qNext;
			 getParQ();
			 
			 a = TensorOp.multiOrderParallel(partQ, partU).get(0);
			 
			
			
			System.out.println("[Worker"+loc+"] :computeUnext and send a to manager "+a);
			
			getSender().tell(new AResult(a), getSelf());
		}
		if(message instanceof Result){
			System.out.println("[Worker"+loc+"] :send result");
			
			getSender().tell(new ResultTensor(partA, loc), getSelf());
		}
			
	}
	public void init(Init message){
		this.partA = message.getPartA();
		//partA.print();
		
		this.qPre = message.getQ();
		//qPre.print();
		this.loc = message.getLoc();
		this.blockSize = message.getBlockSize();
//		System.out.println("[worker"+loc+"] : init"+partA.getSize());
		getParQ();
		
		
		
	}
	public void getParQ(){
		
		partQ = TensorOp.getPartFromTensor(qPre, loc*blockSize, this.partA.getSize()/qPre.getSize());
		
	}
	

}
