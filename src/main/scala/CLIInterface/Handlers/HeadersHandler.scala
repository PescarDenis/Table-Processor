package CLIInterface.Handlers

import CLIInterface.CLIConfig

class HeadersHandler extends BaseParameterHandler[Boolean]("--headers", "Turns on printing of headers (optional, default is false).") {
  override def handle(args: List[String], config: CLIConfig): (CLIConfig, List[String]) = {
    parseFlag(args, config, headers => config.copy(headers = headers))
  }

  override protected def convertValue(value: String): Boolean = true
}
