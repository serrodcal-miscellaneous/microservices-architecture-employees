package actors

import akka.actor.Actor
import model.Employee

/**
  * Created by serrodcal on 15/3/17.
  */
class EmployeeEchoActor extends Actor {

  def receive = {
    case employee: Employee => {
      val idItem = employee.id
      val nameItem = employee.name
      sender ! s"Employee {$idItem} is $nameItem."
    }
    case _ => "Internal Error!"
  }

}
