package com.gamsd.starter.http.auth

import akka.http.scaladsl.server.directives.Credentials.{Missing, Provided}
import akka.http.scaladsl.server.directives.Credentials

case class User(name: String)

object CustomAuthenticator {

  def authenticate(uc: Credentials): Option[User] = uc match {
    case p: Provided => if(p.verify("scaladores30")) Some(User(p.identifier)) else None
    case Missing => None
  }

}