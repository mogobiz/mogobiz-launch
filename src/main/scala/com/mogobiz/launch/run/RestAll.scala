/*
 * Copyright (C) 2015 Mogobiz SARL. All rights reserved.
 */

package com.mogobiz.launch.run

import akka.actor.Props
import akka.io.IO
import spray.can.Http
import com.mogobiz.run.config.MogobizRoutes
import com.mogobiz.run.jobs.CleanCartJob
import com.mogobiz.pay.config.{ MogopayRoutes }
import com.mogobiz.system.{ ActorSystemLocator, BootedMogobizSystem, RoutedHttpService }

object RestAll extends App with BootedMogobizSystem with MogobizRoutes with MogopayRoutes {
  ActorSystemLocator(system)

  //init jobs
  com.mogobiz.pay.jobs.ImportRatesJob.start(system)
  com.mogobiz.pay.jobs.ImportCountriesJob.start(system)
  com.mogobiz.pay.jobs.CleanAccountsJob.start(system)
  CleanCartJob.start(system)

  override val bootstrap = {
    super[MogopayRoutes].bootstrap()
    com.mogobiz.session.boot.DBInitializer()
    com.mogobiz.notify.boot.DBInitializer()
  }

  override val routes = super[MogobizRoutes].routes ~ super[MogopayRoutes].routes

  override val routesServices = system.actorOf(Props(new RoutedHttpService(routes)))

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

  IO(Http)(system) ! Http.Bind(routesServices, interface = Settings.Interface, port = Settings.Port)
}