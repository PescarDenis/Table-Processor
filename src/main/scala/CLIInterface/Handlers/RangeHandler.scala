package CLIInterface.Handlers

import CLIInterface.CLIConfig

class RangeHandler extends BaseParameterHandler[(String, String)]("--range","--range [FROM] [TO]." +" Prints only the specified rectangle of the table.(optional,default is the whole table)") {
  override def handle(args: List[String], config: CLIConfig): (CLIConfig, List[String]) = {
    args match {
      case "--range" :: from :: to :: tail =>
        (config.copy(range = Some((from, to))), tail)
      case _ => (config, args)
    }
  }

  override protected def convertValue(value: String): (String, String) = {
    val parts = value.split(" ")
    (parts(0), parts(1))
  }
}
