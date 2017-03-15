package server

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory
import model.Employee

import scala.io.StdIn


/**
  * Created by serrodcal on 1/3/17.
  */
object HttpServer extends App {

    val config = ConfigFactory.load()

    implicit val system = ActorSystem(config.getString("application.actor-system"))
    implicit val materializer = ActorMaterializer()

    // needed for the future flatMap/onComplete in the end
    implicit val executionContext = system.dispatcher

    val logger = Logging(system, getClass)

    val route : Route = post {
        path(config.getString("application.context") / config.getString("application.resource")) {
          logger.info("Message recived")
          entity(as[Employee]) { employee =>
            val idItem = employee.id
            val nameItem = employee.name
            complete((StatusCodes.OK, s"Employee {$idItem} is $nameItem."))
          }
        }
      }

    val host = config.getString("application.host")
    val port = config.getInt("application.port")
    val bindingFuture = Http().bindAndHandle(route, host, port)

    logger.info(s"Server online at http://$host:$port/\nPress RETURN to stop...")
    StdIn.readLine() // let it run until user presses return
    logger.info(s"Server stopped :(")
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done

}
