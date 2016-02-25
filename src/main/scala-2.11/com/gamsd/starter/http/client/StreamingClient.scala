package com.gamsd.starter.http.client

import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpMethods, HttpRequest, HttpResponse}
import akka.stream.scaladsl.{Framing, Source}
import akka.util.ByteString
import com.gamsd.starter.AkkaElements
import org.json4s.DefaultFormats
import org.json4s.native.JsonParser._

case class Wat(count: String)

object StreamingClient extends AkkaElements {

  implicit val formats = DefaultFormats.lossless

  val (host, port) = ("localhost", 8080)
  val client = Http(system).outgoingConnection(host, port)

  val delimiter = "\r\n"
  val frameSize = 1048576

  def stream(): Source[Wat, Any] = {

    def chunkConsumer(res: HttpResponse) = {
      res.entity.dataBytes
        .via(Framing.delimiter(ByteString(delimiter), frameSize, allowTruncation = true))
        .map[Wat](bs => { parse(bs.utf8String).extract[Wat] })
    }

    val req = HttpRequest(method = HttpMethods.GET, uri = s"http://$host:$port/stream")
    val res: Source[Wat, Any] = Source.single(req).via(client).flatMapConcat(chunkConsumer)

    res
  }

}
