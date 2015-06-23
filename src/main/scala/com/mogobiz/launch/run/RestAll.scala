package com.mogobiz.launch.run

import akka.actor.Props
import akka.io.IO
import com.mogobiz.run.actors.{TransactionActor, ActorSystemLocator}
import com.mogobiz.run.config.MogobizRoutes
import com.mogobiz.run.jobs.CleanCartJob
import com.mogobiz.run.mail.EmailService
import com.mogobiz.pay.config.{MogopayRoutes}
import com.mogobiz.system.{BootedMogobizSystem, RoutedHttpService}
import spray.can.Http

object RestAll extends App with BootedMogobizSystem with MogobizRoutes with MogopayRoutes  {
  ActorSystemLocator(system)
  //init the email service with the system Actor
  EmailService(system, "emailService")

  com.mogobiz.pay.jobs.ImportRatesJob.start(system)
  com.mogobiz.pay.jobs.ImportCountriesJob.start(system)
  com.mogobiz.pay.jobs.CleanAccountsJob.start(system)

  override val bootstrap = {
    super[MogopayRoutes].bootstrap()
    com.mogobiz.session.boot.DBInitializer()
    com.mogobiz.notify.boot.DBInitializer()
  }

  //init jobs
  CleanCartJob.start(system)
  TransactionActor.start()

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