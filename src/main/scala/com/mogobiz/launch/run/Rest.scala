package com.mogobiz.launch.run

import akka.io.IO
import com.mogobiz.run.actors.{ActorSystemLocator}
import com.mogobiz.run.jobs.CleanCartJob
import com.mogobiz.run.mail.EmailService
import com.mogobiz.run.services.MogobizRoutes
import com.mogobiz.system.BootedMogobizSystem
import spray.can.Http

object Rest extends App with BootedMogobizSystem  with MogobizRoutes {

  ActorSystemLocator(system)

  //init the email service with the system Actor
  EmailService(system,"emailService")

  //init jobs
  CleanCartJob.start(system)
  val banner =
    """
      | __  __                   _     _
      ||  \/  | ___   __ _  ___ | |__ (_)____
      || |\/| |/ _ \ / _` |/ _ \| '_ \| |_  /
      || |  | | (_) | (_| | (_) | |_) | |/ /
      ||_|  |_|\___/ \__, |\___/|_.__/|_/___|
      |              |___/
      |    """.stripMargin
  println(banner)

  IO(Http)(system) ! Http.Bind(routesServices, interface = Settings.Interface, port = Settings.Port)
}