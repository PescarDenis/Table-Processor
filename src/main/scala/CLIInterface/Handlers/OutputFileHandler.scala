package CLIInterface.Handlers
import CLIInterface._

class OutputFileHandler extends BaseParameterHandler[String]("--output-file","--output-file [FILE]. "+"Specifies the output file (optional).") {
  override def handle(args: List[String], config: CLIConfig): (CLIConfig, List[String]) = {
    parseOption(args, config, file => config.copy(outputFile = Some(file)))
  }

  override protected def convertValue(value: String): String = value
}


