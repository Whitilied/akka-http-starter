package com.gamsd.starter.http

import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpResponse, MediaTypes, HttpEntity}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.StatusCodes._
import akka.stream.scaladsl.Source
import akka.util.ByteString
import com.gamsd.starter.AkkaElements

object StreamingServer extends AkkaElements with App {

  def run() = {

    val routes = (path("stream") & get) {

      val source = Source(1 to 10)
      val delimiter = "\r\n"

      val bsSource = source
        .map(i => s"""{"count": "$i"}""")
        .map(_.replace("\\r", ""))
        .map(ByteString.fromString(_) ++ ByteString(delimiter))

      val entity = HttpEntity.Chunked.fromData(MediaTypes.`application/json`, bsSource)
      val response = HttpResponse(OK, entity = entity)

      complete(response)
    }

    Http().bindAndHandle(routes, "localhost", 8080)

  }

  run()
  println("Running StreamingServer")

}
