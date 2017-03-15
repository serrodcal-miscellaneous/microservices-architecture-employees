package server

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory
import routes.EmployeeRoutes

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

    val employeeRoutes = new EmployeeRoutes()
    val routes = employeeRoutes.route // ~ otherRoutes.route

    val host = config.getString("application.host")
    val port = config.getInt("application.port")
    val bindingFuture = Http().bindAndHandle(routes, host, port)

    logger.info(s"Server online at http://$host:$port/\nPress RETURN to stop...")
    StdIn.readLine() // let it run until user presses return
    logger.info(s"Server stopped :(")
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done

}
