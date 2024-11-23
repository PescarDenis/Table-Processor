package CLIInterface

import CLIInterface.Handlers.*
import CLIInterface.Visitors.{ArgumentVisitor, HelpVisitor}

class CLI {
  private val handlers: List[ParameterHandler] = List(
    new InputFileHandler(),
    new InputSeparatorHandler(),
    new OutputFileHandler(),
    new StdoutHandler(),
    new OutputFormatHandler(),
    new OutputSeparatorHandler(),
    new HeadersHandler(),
    new RangeHandler(),
    new FiltersHandler()
  )

  def parse(args: List[String]): Option[CLIConfig] = {
    if (args.contains("--help") || args.contains("-h")) {
      printHelp() // Help function
      return None
    }

    val visitor = ArgumentVisitor(args, CLIConfig())
    handlers.foreach(_.accept(visitor))
    

      if (visitor.remainingArgs.nonEmpty) {
        println(s"Error: Unrecognized argument '${visitor.remainingArgs.head}'")
        printHelp()
        None
      } else if (visitor.config.inputFile.isEmpty) {
        println("Error: --input-file is required.")
        printHelp()
        None
      } else {
        Some(visitor.config)
      }
    }

    // Helper method to print the help page
  private def printHelp(): Unit = {
    val visitor = new HelpVisitor()
    handlers.foreach(_.accept(visitor))
    println(visitor.getHelpText)
  }
}
