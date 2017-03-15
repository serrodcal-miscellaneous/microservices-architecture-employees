package routes

import akka.event.Logging
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpHeader, StatusCodes}
import akka.http.scaladsl.server.ContentNegotiator.Alternative.{ContentType, MediaType}
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.{Matchers, WordSpec}

/**
  * Created by serrodcal on 15/3/17.
  */
class EmployeeRoutesTest extends WordSpec with Matchers with ScalatestRouteTest {

  implicit val logger = Logging(system, getClass)

  val employeeRoutes = new EmployeeRoutes()

  val employeeRoute = employeeRoutes.route

  val echoPostRequest = Post(
    "/employee/echo",
    HttpEntity(ContentTypes.`application/json`, """{"id":1, "name":"John"}""")
  )

  "The service" should {

    "return a Employee {1} is John message for POST request with {\"id\":1,\"name\":\"John\"}" in {

      echoPostRequest ~> Route.seal(employeeRoute) ~> check {

        status == StatusCodes.OK
        responseAs[String] shouldEqual "Employee {1} is John."

      }

    }

  }

}
