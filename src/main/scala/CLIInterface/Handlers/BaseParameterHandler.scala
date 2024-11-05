package CLIInterface.Handlers
import CLIInterface._

// Abstract class for handling specific types of command-line parameters
abstract class BaseParameterHandler[T](val optionName: String,val description: String) extends ParameterHandler {

  //parse an option from the command-line arguments
  protected def parseOption(args: List[String], config: CLIConfig, updateConfig: T => CLIConfig): (CLIConfig, List[String]) = {
    args match {
      case `optionName` :: value :: tail => (updateConfig(convertValue(value)), tail)
      case _ => (config, args)
    }
  }
  //method to convert the string value of an option into its appropriate type
  protected def convertValue(value: String): T

  protected def parseFlag(args: List[String], config: CLIConfig, updateConfig: Boolean => CLIConfig): (CLIConfig, List[String]) = {
    args match {
      case `optionName` :: tail => (updateConfig(true), tail)
      case _ => (config, args)
    }
  }
  //Implements the accept method from ParameterHandler to support the visitor pattern.
  override def accept(visitor: CLIVisitor): Unit = visitor.visit(this, optionName, description)
}