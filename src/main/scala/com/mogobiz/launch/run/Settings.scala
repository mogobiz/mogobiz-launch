/*
 * Copyright (C) 2015 Mogobiz SARL. All rights reserved.
 */

package com.mogobiz.launch.run

import com.typesafe.config.ConfigFactory

object Settings {
  private val config =
    ConfigFactory.load().withFallback(ConfigFactory.load("default-application"))

  val ServerListen = config.getString("http.interface")
  val ServerPort = config.getInt("http.port")
}
