package Akka.akka;

import com.typesafe.config.ConfigFactory;

import Akka.MatrixInverseMessage.MatrixInverseInit;
import Akka.MatrixMultiplyMessage.MatrixMultiplyInit;
import Akka.message.Init;
import Akka.message.ManageInit;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

public class MatrixFactor {
	public static void main(String []args) throws Exception{
		ActorSystem system = ActorSystem.create("MatrixFactor",ConfigFactory.load().getConfig("LocalSys"));
		
		ActorRef client = system.actorOf(Props.create(MatrixInverseManager.class),"server");
		client.tell(new MatrixInverseInit(), client);
//		ActorRef client = system.actorOf(Props.create(Client.class),"client");
//		client.tell(new Init(10,10), client);
//		System.out.println(system.name());
//		ActorRef master = system.actorOf(Props.create(Master.class));
//		Address address = new Address("akka.tcp", "RemoteSys", "192.168.18.15", 2553);
//		ActorRef remote =system.actorOf(Props.create(RemoteActor.class).withDeploy(new Deploy(new RemoteScope(
//				address))));7
//		System.out.println(remote.toString());
//		remote.tell("hello world", master);
		//system.stop(remote);
		//system.shutdown();
//		Thread.sleep(5000);
		//system.shutdown();
		
		
	}

}
