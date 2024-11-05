package CLIInterface.Handlers

import CLIInterface.CLIConfig

class OutputFormatHandler extends BaseParameterHandler[String]("--output-format", "Specifies the format of output (csv or md).") {
  override def handle(args: List[String], config: CLIConfig): (CLIConfig, List[String]) = {
    parseOption(args, config, format => config.copy(outputFormat = format))
  }

  override protected def convertValue(value: String): String = value
}

