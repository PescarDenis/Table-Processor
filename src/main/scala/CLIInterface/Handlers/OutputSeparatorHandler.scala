package CLIInterface.Handlers

import CLIInterface.CLIConfig

class OutputSeparatorHandler extends BaseParameterHandler[String]("--output-separator","--output-separator [STRING]. "+"Specifies the separator for CSV output (optional, defaults to ',').") {
  override def handle(args: List[String], config: CLIConfig): (CLIConfig, List[String]) = {
    parseOption(args, config, separator => config.copy(outputSeparator = separator)) 
  }

  override protected def convertValue(value: String): String = value
}