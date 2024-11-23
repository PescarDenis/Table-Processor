package CLIInterface.Handlers

import CLIInterface.CLIConfig
import CLIInterface.Visitors.CLIVisitor

//Basic interface for handling command-line interface
trait ParameterHandler {
  //Processes a list of command-line arguments and updates the configuration based on the arguments handled
  def handle(args: List[String], config: CLIConfig): (CLIConfig, List[String])
  //Accepts a visitor, allowing it to retrieve the description
  def accept(visitor: CLIVisitor): Unit
 //Returns the name of the option this handler is responsible for
  def optionName: String
}
