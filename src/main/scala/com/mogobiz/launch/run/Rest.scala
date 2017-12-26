/*
 * Copyright (C) 2015 Mogobiz SARL. All rights reserved.
 */

package com.mogobiz.launch.run

import akka.http.scaladsl.Http
import com.mogobiz.run.config.MogobizRoutes
import com.mogobiz.system.{ActorSystemLocator, BootedMogobizSystem}

object Rest extends App with BootedMogobizSystem with MogobizRoutes {

  ActorSystemLocator(system)

  override val bootstrap = {
    super[MogobizRoutes].bootstrap()
  }

  val banner = """
      | __  __                   _     _
      ||  \/  | ___   __ _  ___ | |__ (_)____
      || |\/| |/ _ \ / _` |/ _ \| '_ \| |_  /
      || |  | | (_) | (_| | (_) | |_) | |/ /
      ||_|  |_|\___/ \__, |\___/|_.__/|_/___|
      |              |___/
      |    """.stripMargin
  println(banner)

  Http().bindAndHandle(routes, Settings.ServerListen, Settings.ServerPort)
}
