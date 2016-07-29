package Akka.akka;

import javax.swing.text.AbstractDocument.Content;

import com.typesafe.config.ConfigFactory;

import Akka.message.ManageInit;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Address;
import akka.actor.Deploy;
import akka.actor.Props;
import akka.remote.RemoteScope;
import scala.remote;


public class HighOrderLanczos {
	public static void main(String []args) throws Exception{
		ActorSystem system = ActorSystem.create("HighOrderLanczos",ConfigFactory.load().getConfig("LocalSys"));
		ActorRef manager = system.actorOf(Props.create(Manager.class),"manager");
		
		
		
		manager.tell(new ManageInit(), manager);
//		System.out.println(system.name());
//		ActorRef master = system.actorOf(Props.create(Master.class));
//		Address address = new Address("akka.tcp", "RemoteSys", "192.168.18.15", 2553);
//		ActorRef remote =system.actorOf(Props.create(RemoteActor.class).withDeploy(new Deploy(new RemoteScope(
//				address))));
//		System.out.println(remote.toString());
//		remote.tell("hello world", master);
		//system.stop(remote);
		//system.shutdown();
		Thread.sleep(5000);
		system.shutdown();
		
		
	}
}
