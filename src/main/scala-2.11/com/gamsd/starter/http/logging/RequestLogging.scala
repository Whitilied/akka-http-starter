package com.gamsd.starter.http.logging

import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.model.{HttpResponse, HttpRequest}
import akka.http.scaladsl.server.RouteResult
import akka.http.scaladsl.server.RouteResult.{Rejected, Complete}

import org.slf4j.LoggerFactory

object RequestLogging {

  private val log = LoggerFactory.getLogger("RequestLogging")

  private def message(req: HttpRequest, res: HttpResponse) = s"${req.method} ${req.uri} => Status:${res.status}"
  private def unknown(req: HttpRequest, res: String) = s"${req.method} ${req.uri} => Unknown response: $res"

  def logRequestResult(req: HttpRequest)(result: RouteResult): Unit = result match {
    case Complete(response) => logRequestResponse(req, response)
    case Rejected(rejections) => logRequestRejections(req)
  }

  def logRequestResponse(req: HttpRequest, res: HttpResponse): Unit = res match {
    case res @ HttpResponse(OK | Accepted | NotFound, _, _, _) => log.info(message(req, res))
    case res @ HttpResponse(_, _, _, _)                        => log.error(message(req, res))
    case _ => log.warn(unknown(req, res.toString))
  }

  def logRequestRejections(req: HttpRequest): Unit = {
    val message = s"${req.method} ${req.uri} => Request was rejected"
    log.warn(message)
  }

}
