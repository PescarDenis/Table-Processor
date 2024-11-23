package CLIInterface

import CLIInterface.Handlers.*
import CLIInterface.Visitors.HelpVisitor

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
    var remainingArgs = args
    var config = CLIConfig()

    //Process arguments iteratively until all arguments are handled
    while (remainingArgs.nonEmpty) {
      var argsProcessed = false

      for (handler <- handlers if !argsProcessed) {
        val (updatedConfig, argsAfterHandler) = handler.handle(remainingArgs, config)

        //Check if the handler processed any arguments
        if (argsAfterHandler != remainingArgs) {
          config = updatedConfig
          remainingArgs = argsAfterHandler
          argsProcessed = true //Flag as processed and exit the handler loop
        }
      }

      //If no handler processed the current argument, it's an unknown argument
      if (!argsProcessed) {
        println(s"Error: Unrecognized argument '${remainingArgs.head}'")
        printHelp()
        return None
      }
    }

    //Check for the required input file parameter
    if (config.inputFile.isEmpty) {
      println("Error: --input-file is required.")
      printHelp()
      None
    } else {
      Some(config)
    }
  }


  // Helper method to print the help page
   def printHelp(): Unit = {
    val visitor = new HelpVisitor()
    handlers.foreach(_.accept(visitor))
    println(visitor.getHelpText)
  }
}
