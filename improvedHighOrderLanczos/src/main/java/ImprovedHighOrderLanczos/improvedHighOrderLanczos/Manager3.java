package ImprovedHighOrderLanczos.improvedHighOrderLanczos;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import org.omg.CosNaming._BindingIteratorImplBase;

import ImprovedHighOrderLanczos.message.Alpha;
import ImprovedHighOrderLanczos.message.AlphaAndBeta;
import ImprovedHighOrderLanczos.message.Beta;
import ImprovedHighOrderLanczos.message.Delta;
import ImprovedHighOrderLanczos.message.DeltaAndEpsil;
import ImprovedHighOrderLanczos.message.Epsil;
import ImprovedHighOrderLanczos.message.ManagerInit;
import ImprovedHighOrderLanczos.message.ManagerInitWithInfo;
import ImprovedHighOrderLanczos.message.Stop;
import ImprovedHighOrderLanczos.message.WorkerInit;
import ImprovedHighOrderLanczos.message.WorkerInitNoM;
import ImprovedHighOrderLanczos.util.Tensor;
import ImprovedHighOrderLanczos.util.TensorOp;
import akka.actor.ActorRef;
import akka.actor.Address;
import akka.actor.Deploy;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.remote.RemoteScope;
import scala.noinline;
import scala.annotation.implicitNotFound;

public class Manager3 extends UntypedActor{
	
	
	List<ActorRef> workerList;
	int workerNum;
	double delta;
	double epsil;
	double beta;
	double alpha;
	int seq=0;
	int deltaSeq =0;
	int epsilSeq = 0;
	
	int dim ;
	int workerIps[] = {158,159,160,41,86,84,73,72};
	
	File file = new File("ImprovedHighOrderLanczos");
	OutputStreamWriter writer ;
	public Manager3() throws FileNotFoundException{
		writer = new OutputStreamWriter(new FileOutputStream(file));
	}
	int iter=1;
	long startTime ;
	boolean isStart = true;
	long reveiveTime;
	@Override
	public void onReceive(Object message) throws Exception {
		// TODO Auto-generated method stub
		//initialization
		
		
		
		if(message instanceof ManagerInitWithInfo){
			
			isStart = true;
			iter =1;
			
			ManagerInitWithInfo mes = (ManagerInitWithInfo ) message;
			 workerNum = mes.getWorkerNum();
			 dim = mes.getDim();
			workerList = new ArrayList<ActorRef>();
			for(int i =0;i<workerNum;i++){
				Address address = new Address("akka.tcp", "RemoteSys", "192.168.18."+workerIps[i], 2555);
				workerList.add(getContext().actorOf(Props.create(WorkerActor3.class).withDeploy(new Deploy(new RemoteScope(
						address)))));
			}
			System.out.println("manager workerNum "+workerNum);
			Tensor WTensor = new Tensor(2,dim,dim);
			TensorOp.NormInit(WTensor);
			
			// forward data to workers
			int blockSize = WTensor.getSize()/workerNum;
			
			int lastBlockSize = WTensor.getSize() - blockSize*(workerNum-1);
			int i = 0;
			for(ActorRef ref: workerList){
				
				//ref.tell(new WorkerInit(MList.get(i), WTensor, workerNum,i, i ==workerNum-1?lastBlockSize:blockSize,workerList,getSelf()), getSelf());
				ref.tell(new WorkerInitNoM(dim*dim, WTensor, workerNum,i, i ==workerNum-1?lastBlockSize:blockSize,workerList,getSelf()), getSelf());
//				WorkerInitNoM mes =new WorkerInitNoM(dim*dim, WTensor, workerNum,i, i ==workerNum-1?lastBlockSize:blockSize,workerList,getSelf());
//				SendInit t = new SendInit();
//				t.setMes(mes);
//				executor.execute(t);
				i++;
			}
			startTime = System.currentTimeMillis();
			
			
			
		}
		
		
		
		if(message instanceof ManagerInit){
			
			workerList = new ArrayList<ActorRef>();
			
			 Address address = new Address("akka.tcp", "RemoteSys", "192.168.18.158", 2555);
				workerList.add(getContext().actorOf(Props.create(WorkerActor3.class).withDeploy(new Deploy(new RemoteScope(
						address)))));
				 address = new Address("akka.tcp", "RemoteSys", "192.168.18.159", 2555);
				workerList.add(getContext().actorOf(Props.create(WorkerActor3.class).withDeploy(new Deploy(new RemoteScope(
						address)))));
				 address = new Address("akka.tcp", "RemoteSys", "192.168.18.160", 2555);
					workerList.add(getContext().actorOf(Props.create(WorkerActor3.class).withDeploy(new Deploy(new RemoteScope(
							address)))));
				address = new Address("akka.tcp", "RemoteSys", "192.168.18.41", 2555);
					workerList.add(getContext().actorOf(Props.create(WorkerActor3.class).withDeploy(new Deploy(new RemoteScope(
							address)))));
				address = new Address("akka.tcp", "RemoteSys", "192.168.18.72", 2555);
						workerList.add(getContext().actorOf(Props.create(WorkerActor3.class).withDeploy(new Deploy(new RemoteScope(
								address)))));
				address = new Address("akka.tcp", "RemoteSys", "192.168.18.73", 2555);
				workerList.add(getContext().actorOf(Props.create(WorkerActor3.class).withDeploy(new Deploy(new RemoteScope(
								address)))));
				address = new Address("akka.tcp", "RemoteSys", "192.168.18.84", 2555);
				workerList.add(getContext().actorOf(Props.create(WorkerActor3.class).withDeploy(new Deploy(new RemoteScope(
						address)))));
				address = new Address("akka.tcp", "RemoteSys", "192.168.18.85", 2555);
				workerList.add(getContext().actorOf(Props.create(WorkerActor3.class).withDeploy(new Deploy(new RemoteScope(
						address)))));
			
//			workerList.add(getContext().actorOf(Props.create(WorkerActor3.class),"woker1"));
//			workerList.add(getContext().actorOf(Props.create(WorkerActor3.class),"woker2"));
//			Address address = new Address("akka.tcp", "RemoteSys", "192.168.18.80", 2555);
//			workerList.add(getContext().actorOf(Props.create(WorkerActor3.class).withDeploy(new Deploy(new RemoteScope(
//					address)))));
//			 address = new Address("akka.tcp", "RemoteSys", "192.168.18.71", 2555);
//			workerList.add(getContext().actorOf(Props.create(WorkerActor3.class).withDeploy(new Deploy(new RemoteScope(
//					address)))));
//			address = new Address("akka.tcp", "RemoteSys", "192.168.18.73", 2555);
//			workerList.add(getContext().actorOf(Props.create(WorkerActor3.class).withDeploy(new Deploy(new RemoteScope(
//					address)))));
//		address = new Address("akka.tcp", "RemoteSys", "192.168.18.39", 2555);
//			workerList.add(getContext().actorOf(Props.create(WorkerActor3.class).withDeploy(new Deploy(new RemoteScope(
//					address)))));
//			
//			workerList.add(getContext().actorOf(Props.create(WorkerActor2.class),"woker3"));
//			workerList.add(getContext().actorOf(Props.create(WorkerActor.class),"woker4"));
			
			
			workerNum = workerList.size();
			System.out.println("manager workerNum "+workerNum);
			List<Tensor> MList = new ArrayList<Tensor>();
			
			// M initialization
			int dim = 160;
//			Tensor MTensor = new Tensor(4,dim,dim,dim,dim);
//			TensorOp.tensorInit(MTensor);
//			
//			//part M tensor
//			MList = TensorOp.partion(MTensor, 2, workerNum);
			//W initialization
			Tensor WTensor = new Tensor(2,dim,dim);
			TensorOp.NormInit(WTensor);
			
			// forward data to workers
			int blockSize = WTensor.getSize()/workerNum;
			int lastBlockSize = WTensor.getSize() - blockSize*(workerNum-1);
			int i = 0;
			for(ActorRef ref: workerList){
				
				//ref.tell(new WorkerInit(MList.get(i), WTensor, workerNum,i, i ==workerNum-1?lastBlockSize:blockSize,workerList,getSelf()), getSelf());
				ref.tell(new WorkerInitNoM(dim*dim, WTensor, workerNum,i, i ==workerNum-1?lastBlockSize:blockSize,workerList,getSelf()), getSelf());
				i++;
			}
			startTime = System.currentTimeMillis();
			
			
		}
		if(message instanceof Stop){
			seq++;
			if(seq == workerNum){
				System.out.println("Running time "+(System.currentTimeMillis()-startTime));
				return;
			}
		}
		
		if(message instanceof Epsil){
			Epsil mes   = (Epsil) message;
			epsil += mes.getEpsil();
			epsilSeq++;
			if(epsilSeq ==workerNum){
				epsilSeq =0;
				beta = Math.sqrt(epsil);
				Beta sendMes = new Beta(beta);
				long startSend = System.nanoTime();
				for(ActorRef ref : workerList){
					ref.tell(sendMes, getSelf());
					System.out.println("Manager send beta "+(System.nanoTime()-startSend));
					startSend = System.nanoTime();
				}
			}
		}
		if(message instanceof Delta){
			deltaSeq++;
			Delta mes = (Delta) message;
			delta += mes.getDelta();
			if(deltaSeq == workerNum){
				System.out.println("[Manager ]: "+(System.nanoTime()-reveiveTime));
				deltaSeq =0;
				iter++;
				
				alpha  = delta / epsil;
				if(iter == 101){
					System.out.println("Running time "+(System.currentTimeMillis()-startTime));
					writer.write("workerNum "+workerNum+" dim: "+dim+" Running time "+(System.currentTimeMillis()-startTime)+"\n");
					writer.flush();
					
					for(ActorRef ref :workerList){
						context().stop(ref);
					}
					
					
					if(dim <180){
					getSelf().tell(new ManagerInitWithInfo(workerNum, dim+10), ActorRef.noSender());
					}
				
					else {
						if(workerNum<8) getSelf().tell(new ManagerInitWithInfo(workerNum+1, 50), ActorRef.noSender());
						else return ;
					}
				
//				if(dim <250&&workerNum==6){
//					getSelf().tell(new ManagerInitWithInfo(workerNum, dim+10), ActorRef.noSender());
//				}
//				else if(dim < 260&&workerNum >6){
//					getSelf().tell(new ManagerInitWithInfo(workerNum, dim+10), ActorRef.noSender());
//				}
//				else {
//					if(workerNum<8) getSelf().tell(new ManagerInitWithInfo(workerNum+1, 240), ActorRef.noSender());
//					else return ;
//				}
					
					
					
//					if(dim <180){
//						getSelf().tell(new ManagerInitWithInfo(workerNum, dim+10), ActorRef.noSender());
//					}
//					else if(dim <230&&workerNum>=4){
//						getSelf().tell(new ManagerInitWithInfo(workerNum, dim+10), ActorRef.noSender());
//					}
//					else if(dim < 250&& workerNum ==6){
//						 getSelf().tell(new ManagerInitWithInfo(workerNum, dim+10), ActorRef.noSender());
//					}
//					else if(dim < 260&& workerNum >6){
//						getSelf().tell(new ManagerInitWithInfo(workerNum, dim+10), ActorRef.noSender());
//					}
//					else {
//						if(workerNum<8) getSelf().tell(new ManagerInitWithInfo(workerNum+1, 50), ActorRef.noSender());
//						else return ;
//					}
					
					
//					if(dim <250&&workerNum==6){
//						getSelf().tell(new ManagerInitWithInfo(workerNum, dim+10), ActorRef.noSender());
//					}
//					else if(dim < 260&&workerNum >6){
//						getSelf().tell(new ManagerInitWithInfo(workerNum, dim+10), ActorRef.noSender());
//					}
//					else {
//						if(workerNum<8) getSelf().tell(new ManagerInitWithInfo(workerNum+1, 240), ActorRef.noSender());
//						else return ;
//					}
				}
				reveiveTime = System.nanoTime();
				Alpha sendMes = new Alpha(alpha);
				long startSend = System.nanoTime();
				for(ActorRef ref : workerList){
					ref.tell(sendMes, getSelf());
					System.out.println("Manager send alpha "+(System.nanoTime()-startSend));
					startSend = System.nanoTime();
				}
				
				
				delta = epsil = 0;
			}
		}
		if(message instanceof DeltaAndEpsil){
			
			seq ++;
			DeltaAndEpsil mes = (DeltaAndEpsil)message;
			delta += mes.getDelta();
			epsil += mes.getEpsil();
			if(isStart){
				isStart = false;
				startTime = System.currentTimeMillis();
			}
			//System.out.println("delta epsil receive "+seq);
			if(seq == workerNum){
				System.out.println("[Manager ]: "+(System.nanoTime()-reveiveTime));
				seq =0;
				iter++;
				beta = Math.sqrt(epsil);
				alpha  = delta / epsil;
				if(iter == 101){
					System.out.println("Running time "+(System.currentTimeMillis()-startTime));
					return;
				}
				reveiveTime = System.nanoTime();
				AlphaAndBeta sendMes = new AlphaAndBeta(alpha, beta);
				long startSend = System.nanoTime();
				for(ActorRef ref : workerList){
					ref.tell(sendMes, getSelf());
					System.out.println("Manager send AB "+(System.nanoTime()-startSend));
					startSend = System.nanoTime();
				}
				
				
				delta = epsil = 0;
			}
			
		}
		
		
	}

}
