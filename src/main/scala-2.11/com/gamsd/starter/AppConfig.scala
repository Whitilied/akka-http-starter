package com.gamsd.starter

import com.typesafe.config.{ConfigFactory, Config}

object AppConfig {

  val config: Config = ConfigFactory.load()

}
