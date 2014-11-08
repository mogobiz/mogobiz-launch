package com.mogobiz.launch.run

import akka.actor.Props
import akka.io.IO
import com.mogobiz.run.actors.{ActorSystemLocator, MogobizActors}
import com.mogobiz.run.config.Settings
import com.mogobiz.run.jobs.CleanCartJob
import com.mogobiz.run.mail.EmailService
import com.mogobiz.pay.config.{MogopayActors, MogopayRoutes}
import com.mogobiz.run.services.MogobizRoutes
import com.mogobiz.system.{BootedMogobizSystem, RoutedHttpService}
import spray.can.Http

object RestAll extends App with BootedMogobizSystem with MogobizActors with MogobizRoutes with MogopayActors with MogopayRoutes {

  ActorSystemLocator(system)

  //init the email service with the system Actor
  EmailService(system, "emailService")

  //init jobs
  CleanCartJob.start(system)

  override val bootstrap = {
    super[MogopayRoutes].bootstrap()
    com.mogobiz.session.boot.DBInitializer()
    com.mogobiz.notify.boot.DBInitializer()
  }

  override val routes = super[MogobizRoutes].routes ~ super[MogopayRoutes].routes

  override val routesServices = system.actorOf(Props(new RoutedHttpService(routes)))

  IO(Http)(system) ! Http.Bind(routesServices, interface = Settings.Interface, port = Settings.Port)
}