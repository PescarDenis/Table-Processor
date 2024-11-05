package CLIInterface.Handlers
import CLIInterface._

class InputSeparatorHandler extends BaseParameterHandler[String]("--input-separator",
  "Specifies the input separator (optional, defaults to ',').") {
  override def handle(args: List[String], config: CLIConfig): (CLIConfig, List[String]) = {
    parseOption(args, config, separator => config.copy(inputSeparator = separator))
  }

  override protected def convertValue(value: String): String = value
}


