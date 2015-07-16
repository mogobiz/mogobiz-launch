/*
 * Copyright (C) 2015 Mogobiz SARL. All rights reserved.
 */

package com.mogobiz.launch.run

import com.typesafe.config.ConfigFactory

object Settings {
  private val config = ConfigFactory.load().withFallback(ConfigFactory.load("default-application"))

  val Interface = config.getString("spray.can.server.interface")
  val Port = config.getInt("spray.can.server.port")
}