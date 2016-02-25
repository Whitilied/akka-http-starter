package com.gamsd.starter.streams

import akka.stream.scaladsl.{Keep, Sink}
import com.gamsd.starter.AkkaElements
import com.gamsd.starter.http.client.StreamingClient

import scala.util.{Failure, Success}

object APIClientStream extends AkkaElements with App {

  val mat = StreamingClient.stream()
    .map(_.toString)
    .map({x => println(x); x})
    .map(_ => 1)
    .toMat(Sink.fold[Int, Int](0)(_ + _))(Keep.right)
    .run()

  mat.onComplete({
    case Success(s) => println(s"Count: $s"); system.terminate(); System.exit(0)
    case Failure(ex) => {
      println(s"Something bad happened. Is the StreamingServer really running on localhost:8080?")
    }; system.terminate(); System.exit(1)
  })

}
