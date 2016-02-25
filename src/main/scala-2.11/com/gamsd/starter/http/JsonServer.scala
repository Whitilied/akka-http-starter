package com.gamsd.starter.http

import akka.http.scaladsl.Http
import akka.http.scaladsl.marshalling.ToResponseMarshallable
import akka.http.scaladsl.model.ContentTypes._
import akka.http.scaladsl.model.{HttpEntity, StatusCode}
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import com.gamsd.starter.AkkaElements
import org.json4s.{JValue, JNull, DefaultFormats}
import org.json4s.native.JsonMethods._
import org.json4s.native.Serialization._

import scala.concurrent.Future


object JsonServer extends AkkaElements with App {

  def run() = {

    implicit val formats = DefaultFormats.lossless

    implicit def fromJsonFutureToResponseMarshallable(f: Future[(StatusCode, JValue)]): ToResponseMarshallable = {
      f.map[ToResponseMarshallable]{ case (status, json) =>
        json match {
          case JNull => status -> HttpEntity.empty(`application/json`)
          case _ => status -> HttpEntity(`application/json`, write(json))
        }
      }
    }

    implicit def responseWithBody(r: (StatusCode, JValue)): ToResponseMarshallable = {
      val (status, json) = r
      status -> HttpEntity(`application/json`, write(json))
    }

    val routes = (get & path("echo" / Segment)) { s =>
      val json = parse(s"""{"message": "$s"}""")
      val res = Future.successful(OK -> json)
      complete(res)
    }

    Http().bindAndHandle(routes, "localhost", 8080)

  }

  run()
  println("Running JsonServer")

}
