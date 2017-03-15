package routes

import akka.event.LoggingAdapter
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import model.Employee
import server.JsonSupport

/**
  * Created by serrodcal on 15/3/17.
  */
class EmployeeRoutes (implicit logger: LoggingAdapter) extends JsonSupport {

  val route : Route = post {
    path("employee" / "echo") {
      logger.info("Message recived")
      entity(as[Employee]) { employee =>
        val idItem = employee.id
        val nameItem = employee.name
        complete((StatusCodes.OK, s"Employee {$idItem} is $nameItem."))
      }
    }
  }

}
