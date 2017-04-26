package routes

import actors.{EmployeeEchoActor, EmployeeRouterActor}
import akka.actor.{ActorSystem, Props}
import akka.event.Logging
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, StatusCodes}
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory
import org.scalatest.{Matchers, WordSpec}
import server.HttpServer.getClass

/**
  * Created by serrodcal on 15/3/17.
  */
class EmployeeRoutesTest extends WordSpec with Matchers with ScalatestRouteTest {

  val config = ConfigFactory.load()

  // needed for the future flatMap/onComplete in the end
  implicit val executionContext = system.dispatcher

  implicit val logger = Logging(system, getClass)

  implicit val employeeEchoActor = system.actorOf(Props[EmployeeRouterActor], name = "employeeRouterActor")

  val employeeRoutes = new EmployeeRoutes()

  val employeeRoute = employeeRoutes.route

  val echoPostRequest = Post(
    "/employee/echo",
    HttpEntity(ContentTypes.`application/json`, """{"id":1, "name":"John"}""")
  )

  "The service" should {

    """return a Employee {1} is John. message for POST request with {"id":1,"name":"John"}""" in {

      echoPostRequest ~> Route.seal(employeeRoute) ~> check {

        status == StatusCodes.OK
        responseAs[String] shouldEqual "Employee {1} is John."

      }

    }

  }

}
