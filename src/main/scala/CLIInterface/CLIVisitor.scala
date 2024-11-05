package CLIInterface

//Defines the interface for a visitor
trait CLIVisitor {
  //Visits a ParameterHandler to perform operations defined by the visitor
  def visit(handler: ParameterHandler, optionName: String, description: String): Unit
}

