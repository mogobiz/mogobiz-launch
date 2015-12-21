name := "mogobiz-launch"

Revolver.settings

mainClass in Revolver.reStart := Some("com.mogobiz.launch.run.RestAll")

// javaOptions := Seq("-Xdebug", "-Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=5005")

scalariformSettings