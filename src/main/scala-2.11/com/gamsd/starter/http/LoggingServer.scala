package com.gamsd.starter.http

import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.RejectionHandler
import akka.http.scaladsl.server.directives.LoggingMagnet
import com.gamsd.starter.AkkaElements
import com.gamsd.starter.http.logging.RequestLogging

object LoggingServer extends AkkaElements with App {

  def run() = {

    val requestLogging =
      logRequestResult(LoggingMagnet(_ => RequestLogging.logRequestResult)) &
        handleRejections(RejectionHandler.default)

    def routes = (get & path("echo" / Segment)) { s =>
      complete(s)
    }

    val loggingRoutes = requestLogging { routes }

    Http().bindAndHandle(loggingRoutes, "localhost", 8080)

  }

  run()
  println("Running LoggingServer")

}
