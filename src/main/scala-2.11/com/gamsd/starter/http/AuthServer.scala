package com.gamsd.starter.http

import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import com.gamsd.starter.AkkaElements
import com.gamsd.starter.http.auth.{User, CustomAuthenticator}

object AuthServer extends AkkaElements with App {

  def run() = {

    def routes(user: User) = (get & path("echo" / Segment)) { s =>
      val message = s"User: ${user.name}, Message: $s"
      complete(message)
    }

    val authRoutes =
      authenticateBasic(realm = "Lançamentos", CustomAuthenticator.authenticate) { user =>
        routes(user)
      }

    Http().bindAndHandle(authRoutes, "localhost", 8080)

  }

  run()
  println("Running AuthServer")

}
