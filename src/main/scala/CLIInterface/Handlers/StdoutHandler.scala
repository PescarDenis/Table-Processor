package CLIInterface.Handlers
import CLIInterface._


class StdoutHandler extends BaseParameterHandler[Boolean]("--stdout", "Prints the table to standard output (optional, default is true).") {
  override def handle(args: List[String], config: CLIConfig): (CLIConfig, List[String]) = {
    parseFlag(args, config, flag => config.copy(outputToStdout = flag))
  }

  override protected def convertValue(value: String): Boolean = true
}

