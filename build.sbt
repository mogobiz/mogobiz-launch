name := "mogobiz-launch"

Revolver.settings

mainClass in Revolver.reStart := Some("com.mogobiz.launch.run.RestAll")

unmanagedClasspath in Runtime <<= (unmanagedClasspath in Runtime, baseDirectory) map { (cp, bd) =>
  val confHome = sys.props.get("MOGOBIZ_HOME").map(mh => new File(mh, "run/conf").getAbsolutePath).getOrElse("conf")
  cp :+ Attributed.blank(bd / confHome)
}

javaOptions := Seq("-Xdebug", "-Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005", "-Xmx4G", "-XX:MaxPermSize=512m")

scalariformSettings

