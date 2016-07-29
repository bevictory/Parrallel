package Akka.akka;

import com.typesafe.config.ConfigFactory;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Deploy;
import akka.actor.Props;

public class App {
	public static void main(String []args) throws Exception{
		ActorSystem system = ActorSystem.create("remote_program",ConfigFactory.load("manager"));
		ActorRef master = system.actorOf(Props.create(Master.class));
		ActorRef remote = system.actorOf(Props.create(RemoteActor.class)
				,"remoteActorAddr");
		remote.tell("hello world", master);
	}
}
