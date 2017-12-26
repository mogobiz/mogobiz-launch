/*
 * Copyright (C) 2015 Mogobiz SARL. All rights reserved.
 */

package com.mogobiz.launch.run

import akka.http.scaladsl.Http
import com.mogobiz.pay.config.MogopayRoutes
import com.mogobiz.run.config.MogobizRoutes
import com.mogobiz.system.{ActorSystemLocator, BootedMogobizSystem}

object RestAll
    extends App
    with BootedMogobizSystem
    with MogobizRoutes
    with MogopayRoutes {
  ActorSystemLocator(system)

  override val bootstrap = {
    super[MogobizRoutes].bootstrap()
    super[MogopayRoutes].bootstrap()
  }

  override val routes = super[MogobizRoutes].routes ~ super[MogopayRoutes].routes

  val banner =
    """
      | __  __                   _     _          __  __  __
      ||  \/  | ___   __ _  ___ | |__ (_)____    / / |  \/  | ___   __ _  ___  _ __   __ _ _   _
      || |\/| |/ _ \ / _` |/ _ \| '_ \| |_  /   / /  | |\/| |/ _ \ / _` |/ _ \| '_ \ / _` | | | |
      || |  | | (_) | (_| | (_) | |_) | |/ /   / /   | |  | | (_) | (_| | (_) | |_) | (_| | |_| |
      ||_|  |_|\___/ \__, |\___/|_.__/|_/___| /_/    |_|  |_|\___/ \__, |\___/| .__/ \__,_|\__, |
      |              |___/                                         |___/      |_|          |___/
      |    """.stripMargin
  println(banner)

  Http().bindAndHandle(routes, Settings.ServerListen, Settings.ServerPort)
}
