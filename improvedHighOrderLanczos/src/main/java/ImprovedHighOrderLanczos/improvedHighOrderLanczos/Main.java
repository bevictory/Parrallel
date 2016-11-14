package ImprovedHighOrderLanczos.improvedHighOrderLanczos;

import java.util.ArrayList;
import java.util.List;

import org.omg.PortableServer.ServantActivator;

import com.typesafe.config.ConfigFactory;

import ImprovedHighOrderLanczos.message.ManagerInit;
import ImprovedHighOrderLanczos.message.ManagerInitWithInfo;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Address;
import akka.actor.Deploy;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.remote.RemoteScope;
class worker extends UntypedActor{

	@Override
	public void onReceive(Object arg0) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("Woker"+System.nanoTime());
		List<Integer> list = (List<Integer>)arg0;
		for(Integer  a : list){
			System.out.println(a);
		}
		System.out.println("Woker"+System.nanoTime());
	}
	
}
public class Main {
	static class Work implements Runnable{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			System.out.println("Thread"+System.nanoTime());
			
			worker1.tell(list, ActorRef.noSender());
			System.out.println("Thread"+System.nanoTime());
		}
		
	}
	static ActorRef worker1;
	static List<Integer> list ;
	
	public static void main(String[] args) {
		ActorSystem system = ActorSystem.create("ImprovedHighOrderLanczos",
				ConfigFactory.load().getConfig("LocalSys"));
		 worker1 = system.actorOf(Props.create(Manager3.class),"worker");
		 worker1.tell(new ManagerInitWithInfo(2,50), ActorRef.noSender());
		 
		
//		 list = new ArrayList<>();
//		
//		list.add( 100);list.add(200);
//		
//		 
//		Thread thread = new Thread(new Work());
//		System.out.println(System.nanoTime());
//		thread.start();
////		worker1.tell(list, ActorRef.noSender());
//		for(Integer eInteger : list){
//			System.out.println(eInteger);
//		}
//		System.out.println(System.nanoTime());
	}
}
