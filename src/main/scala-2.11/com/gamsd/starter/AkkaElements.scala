package com.gamsd.starter

import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, Materializer}
import com.gamsd.starter.AppConfig._

import scala.concurrent.ExecutionContextExecutor

trait AkkaElements {

  implicit val system: ActorSystem = ActorSystem("StarterActorSystem", config)
  implicit def executor: ExecutionContextExecutor = system.dispatcher
  implicit val materializer: Materializer = ActorMaterializer()

}
