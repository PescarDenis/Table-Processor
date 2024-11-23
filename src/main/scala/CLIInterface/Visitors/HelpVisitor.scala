package CLIInterface.Visitors

import CLIInterface.Handlers.ParameterHandler


//Builds a help message by visiting each parameter handler
class HelpVisitor extends CLIVisitor {

  private val helpText = new StringBuilder("Available parameters:\n")

  //visit each handler, appending option name and description to help text
  override def visit(handler: ParameterHandler, optionName: String, description: String): Unit = {
    helpText.append(s"->> $optionName: $description\n")
  }
  //return the complete help text
  def getHelpText: String = helpText.toString()
}


