package com.mogobiz.launch.run

import akka.io.IO
import com.mogobiz.run.actors.{ActorSystemLocator, MogobizActors}
import com.mogobiz.run.jobs.CleanCartJob
import com.mogobiz.run.mail.EmailService
import com.mogobiz.run.services.MogobizRoutes
import com.mogobiz.system.BootedMogobizSystem
import spray.can.Http

object Rest extends App with BootedMogobizSystem with MogobizActors with MogobizRoutes {

  ActorSystemLocator(system)

  //init the email service with the system Actor
  EmailService(system,"emailService")

  //init jobs
  CleanCartJob.start(system)

  IO(Http)(system) ! Http.Bind(routesServices, interface = Settings.Interface, port = Settings.Port)
}