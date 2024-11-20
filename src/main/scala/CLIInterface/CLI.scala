package CLIInterface

import CLIInterface.Handlers._

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
    
    // Process the arguments and handlers
    var config = CLIConfig() 
    var remainingArgs = args
    
    // Iterate through the arguments
    while (remainingArgs.nonEmpty) {
      var argsProcessed = false

      for (handler <- handlers if !argsProcessed) {
        val (updatedConfig, argsAfterHandler) = handler.handle(remainingArgs, config) // If there are still arguments to process, update the configuration based on the handler
        if (argsAfterHandler != remainingArgs) { 
          config = updatedConfig
          remainingArgs = argsAfterHandler
          argsProcessed = true
        }
      }
      
      // If we introduce a wrong argument show an error of the argument and print the help page
      if (!argsProcessed) {
        println(s"Error: Unrecognized argument '${remainingArgs.head}'")
        printHelp()
        return None
      }
    }
    // Check for the required input file parameter
    if (config.inputFile.isEmpty) {
      println("Error: --input-file is required.")
      printHelp()
      None
    } else {
      Some(config)
    }
  }
  // Helper method to print the help page 
  private def printHelp(): Unit = {
    val visitor = new HelpVisitor()
    handlers.foreach(_.accept(visitor))
    println(visitor.getHelpText)
  }
}
