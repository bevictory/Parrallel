package ImprovedHighOrderLanczos.message;

import java.util.List;

import akka.actor.ActorRef;
import akka.actor.dsl.Creators.Act;

public class WorkerRefs implements Message {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	final List<ActorRef>  workers;
	public List<ActorRef> getWorkers() {
		return workers;
	}
	public WorkerRefs(List<ActorRef> list){
		this. workers = list;
	}

}
