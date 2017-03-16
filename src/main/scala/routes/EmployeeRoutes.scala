package routes

import akka.actor.ActorRef
import scala.util.{Failure, Success}
import akka.util.Timeout
import scala.concurrent.duration._
import akka.event.LoggingAdapter
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import model.Employee
import server.JsonSupport
import akka.pattern.ask

/**
  * Created by serrodcal on 15/3/17.
  */
class EmployeeRoutes (implicit logger: LoggingAdapter, implicit val employeeEchoActor: ActorRef) extends JsonSupport {

  implicit val timeout = Timeout(1 seconds)

  val route : Route = post {
    path("employee" / "echo") {
      logger.info("Message recived")
      entity(as[Employee]) { employee =>
        onComplete((employeeEchoActor ? employee).mapTo[String]) {
          case Success(message: String) => {
            logger.info("Echo!")
            complete(StatusCodes.OK, message)
          }
          case Failure(_) => {
            logger.error("failed to echo!")
            complete(StatusCodes.InternalServerError, "failed to get echo!")
          }
        }
      }
    }
  }

}
