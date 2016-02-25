package com.gamsd.starter.http

import akka.http.scaladsl.Http
import com.gamsd.starter.AkkaElements
import akka.http.scaladsl.server.Directives._

object UploadServer extends AkkaElements with App {

  def run() = {

    val routes = (post & path("upload")) {

      uploadedFile("file") { case (metadata, file) =>
        val length = file.length() / 1024
        val message = s"Uploaded file length: $length K\n"

        file.delete()
        complete(message)
      }

    }

    Http().bindAndHandle(routes, "localhost", 8080)

  }

  run()
  println("Running UploadServer")

}
