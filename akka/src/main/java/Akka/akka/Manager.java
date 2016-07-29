package Akka.akka;





import java.sql.Time;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jboss.netty.util.internal.SystemPropertyUtil;

import Akka.message.AResult;
import Akka.message.Init;
import Akka.message.ManageInit;
import Akka.message.NormResult;
import Akka.message.QReuslt;
import Akka.message.Result;
import Akka.message.ResultTensor;
import Akka.message.Tensor;
import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.Address;
import akka.actor.Deploy;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.remote.RemoteScope;


public class Manager extends UntypedActor{
	
	List<ActorRef> workList;
	List<Tensor> partAList;
	int workNum;
	int resultSeq=0;
	int aSeq=0;
	int qSeq = 0;
	Tensor[] qList;
	double normResult=0.0;
	double aResult =0.0;
	Tensor qTensor;
	int iter=1;
	Tensor a=null,q=null;
	long startTime;
	long endTime;
	List<Double> aList = new ArrayList<Double>();
	List<Double> bList= new ArrayList<Double>();
	@Override
	public void onReceive(Object message) throws Exception {
		// TODO Auto-generated method stub
		/**
		 * create  workers and send partA and q to workers
		 *
		 */
		if(message instanceof ManageInit){
			System.out.println("application start manageInit..");
			workList = new ArrayList<ActorRef>();
			workList.add(getContext().actorOf(Props.create(WorkActor.class),"woker1"));
			workList.add(getContext().actorOf(Props.create(WorkActor.class),"woker2"));
			//workList.add(getContext().actorOf(Props.create(WorkActor.class),"remoteActorAddr"));
//			Address address = new Address("akka.tcp", "RemoteSys", "192.168.18.39", 2553);
//			workList.add(getContext().actorOf(Props.create(WorkActor.class).withDeploy(new Deploy(new RemoteScope(
//					address)))));
			workNum = workList.size();
			qList = new Tensor[workNum];
			
			
			
			
			a = new Tensor(4,4,4,4,4);
			TensorOp.tensorInit(a);
			
			partAList = TensorOp.partion(a, 2, workNum);
			
			
			q = new Tensor(2,4,4);
			TensorOp.qInit(q);
			int blockSize = q.getSize()/workNum;
			int i=0;
			for(ActorRef ref: workList){
				ref.tell(new Init(partAList.get(i), q, i,blockSize), getSelf());
				i++;
			}
			startTime = System.currentTimeMillis();
		}
		if(message instanceof AResult){
			aSeq ++;
			
			System.out.println("[Manager] : receive AResult_ "+aSeq);
			
			aResult += 
					((AResult)message).getA();
			if(aSeq == workNum){
				
				System.out.println("[Manager] : broadcast a ");
				
				
				
				aList.add(aResult);
				for(ActorRef ref: workList){
					ref.tell(new AResult(aResult), getSelf());						
				}
				aResult =0;
				aSeq =0;
			}
		}
		if(message instanceof NormResult){
			
			resultSeq ++;
			System.out.println("[Manager] : receive normreuslt_ "+resultSeq);
			normResult += 
					((NormResult)message).getB();
			if(resultSeq == workNum){
				
				
				normResult = Math.sqrt(normResult);
				
				
				
				System.out.println("[Manager] : broadcast NormResult "+normResult);
				bList.add(normResult);
				iter++;
				if(iter ==11)
				{	
					endTime = System.currentTimeMillis();
					
					Thread.sleep(5000);
//					for(ActorRef ref: workList){
//						ref.tell(new Result(), getSelf());						
//					}
					for(ActorRef ref: workList){
						context().stop(ref);						
					}
					
					
					System.out.println("[Manager] running time" +(endTime-startTime));
					return;
				}
				if(normResult - 0 <10e-10){
					endTime = System.currentTimeMillis();
					for(ActorRef ref: workList){
						context().stop(ref);						
					}
					System.out.println("[Manager] running time" +(endTime-startTime));
				}else{
					for(ActorRef ref: workList){
						ref.tell(new NormResult(normResult), getSelf());						
					}
					
				}
				normResult = 0;resultSeq =0;
			}
		}
		
		if(message  instanceof QReuslt){
			qSeq ++;
			System.out.println("[Manager] : receive Qreuslt "+qSeq);
			
			qList[((QReuslt) message).getLoc()] =((QReuslt) message).getQ(); 
			
			if(qSeq == workNum){
				
				int blockSize =q.getSize()/workNum;
//				int blockSize = qList[0].getSize();
				
				TensorOp.combine(q, qList, 0, workNum);
				
				
				for(ActorRef ref: workList){
					
					ref.tell(new QReuslt(q,0,blockSize ), getSelf());
					
				}
				System.out.println("[Manager] : broadcast q ");
				qSeq =0;
			}
		}
		if(message instanceof ResultTensor){
			System.out.println("[Manager] : get result");
			for(ActorRef ref: workList){
				context().stop(ref);						
			}
		}
		
	}
	@Override
	public void postStop(){
		System.out.println("aList:"+aList);
		System.out.println("bList:"+bList);
	}

}
