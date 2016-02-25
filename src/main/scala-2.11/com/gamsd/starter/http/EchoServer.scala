package com.gamsd.starter.http

import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import com.gamsd.starter.AkkaElements

import scala.concurrent.Future

object EchoServer extends AkkaElements with App {

  def run() = {

    val routes = (get & path("echo" / Segment)) { s =>
      val f = Future.successful(s)
      complete(f)
    }

    Http().bindAndHandle(routes, "localhost", 8080)

  }

  run()
  println("Running EchoServer")

}
