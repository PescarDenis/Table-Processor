package CLIInterface.Visitors

import CLIInterface.CLIConfig
import CLIInterface.Handlers.ParameterHandler

//Visitor pattern for processing the arguments of the CLI and updating the config
case class ArgumentVisitor(initialArgs: List[String], initialConfig: CLIConfig) extends CLIVisitor {
  var remainingArgs: List[String] = initialArgs //Holds the remaining arguments, processing each of the argument one at a time
  var config: CLIConfig = initialConfig //Current state of the config for each argument


  override def visit(handler: ParameterHandler, optionName: String, description: String): Unit = {
    val (updatedConfig, argsLeft) = handler.handle(remainingArgs, config) //Let the handler process the arguments and update the configuration.
    //Update the config and the remaining args
    config = updatedConfig
    remainingArgs = argsLeft
  }
}
