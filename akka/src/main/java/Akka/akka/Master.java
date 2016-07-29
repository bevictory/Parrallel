package Akka.akka;

import akka.actor.UntypedActor;
import scala.collection.parallel.ParIterableLike.Count;

public class Master extends UntypedActor{
	int count =0;
	@Override
	public void onReceive(Object message) throws Exception {
		// TODO Auto-generated method stub
		if(count==3) return;
		if(message instanceof String){
			getSender().tell(message, getSelf());
			count++;
			System.out.println("master tell: "+message);
		}
		
	}

}
