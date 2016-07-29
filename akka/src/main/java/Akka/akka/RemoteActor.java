package Akka.akka;

import akka.actor.UntypedActor;
import scala.collection.parallel.ParIterableLike.Count;

public class RemoteActor extends UntypedActor{
	int count =0;
	@Override
	public void onReceive(Object message) throws Exception {
		// TODO Auto-generated method stub
		if(message instanceof String){
			getSender().tell(message+" "+count, getSelf());
			count++;
			System.out.println(message);
		}
	}
	@Override
	public void preStart(){
		System.out.println("create remoteActor");
	}
	

}
