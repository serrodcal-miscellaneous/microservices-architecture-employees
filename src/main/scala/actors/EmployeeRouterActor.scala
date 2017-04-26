package actors

import akka.actor.{Actor, ActorRef}
import akka.event.{Logging, LoggingAdapter}

/**
  * Created by serrodcal on 25/4/17.
  */
object EmployeeRouterActor {
  final case class Employee(id: Long, name: String)
  final case object StopChild
  final case class ChildResponse(id: Long, name: String)
}

final class EmployeeRouterActor extends Actor with akka.actor.ActorLogging {

  import EmployeeRouterActor._

  private var children = Map.empty[Long, ActorRef]

  override def receive: Receive = {
    case e @ Employee(id, _)  => {
      log.info("Message received!")
      getChild(id) forward  e
    }
    case ChildResponse(id, _) => {
      log.info("Response received from child!")
      stopChild(id)
      log.info("Child has been stopped!")
    }
  }

  private def getChild(id: Long): ActorRef =
    context.child(id.toString).getOrElse {
      val child = context.actorOf(EmployeeEchoActor.apply(), id.toString)
      children += (id -> child)
      child
    }

  private def stopChild(id: Long) = {
    children(id) ! StopChild
    children -= id
  }

}