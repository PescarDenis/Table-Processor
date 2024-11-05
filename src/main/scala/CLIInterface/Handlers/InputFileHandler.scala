package CLIInterface.Handlers
import CLIInterface._


class InputFileHandler extends BaseParameterHandler[String]("--input-file", "Specifies the input CSV file (required).") {

  override def handle(args: List[String], config: CLIConfig): (CLIConfig, List[String]) = {
    parseOption(args, config, file => config.copy(inputFile = Some(file)))
  }

  override protected def convertValue(value: String): String = value
}


