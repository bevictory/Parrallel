package ImprovedHighOrderLanczos.improvedHighOrderLanczos;

import java.io.ObjectInputStream.GetField;

import com.typesafe.config.ConfigFactory;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.kernel.Bootable;

public class RemoteNodeApplication implements Bootable {

	final ActorSystem system = ActorSystem.create("RemoteSys",ConfigFactory.load().getConfig("RemoteSys") 
		);
	public void shutdown() {
		// TODO Auto-generated method stub
		system.shutdown();
	}

	public void startup() {
		// TODO Auto-generated method stub
		//ActorRef remoteActor =system.actorOf(Props.create(RemoteActor.class),"RemoteActor");
		System.out.println("remoteActor create");
		//remoteActor.tell("hello world",remoteActor);
	}
	

}
