/*
 * Copyright (c) 2014. Mogobiz S.A.S.
 */

package com.mogobiz.launch.run

import com.typesafe.config.ConfigFactory

object Settings {
  private val config = ConfigFactory.load().withFallback(ConfigFactory.load("default-application"))

  val Interface = config.getString("spray.can.server.interface")
  val Port = config.getInt("spray.can.server.port")
}