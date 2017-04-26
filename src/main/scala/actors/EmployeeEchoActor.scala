package actors

import akka.actor.{Actor, Props}
import akka.event.{Logging, LoggingAdapter}

/**
  * Created by serrodcal on 15/3/17.
  */
object EmployeeEchoActor {
  def apply(): Props = Props(new EmployeeEchoActor)
}

class EmployeeEchoActor extends Actor with akka.actor.ActorLogging {

  override def receive = {

    case employee: EmployeeRouterActor.Employee => {
      log.info("Message received!")
      val idItem = employee.id
      val nameItem = employee.name
      sender ! s"Employee {$idItem} is $nameItem."
      context.parent ! EmployeeRouterActor.ChildResponse( idItem, nameItem )

    }
    case EmployeeRouterActor.StopChild => {
      log.info("Stopping :(!")
      context.stop(self)
    }
    case _ => sender ! "Internal Error!"
  }

}
