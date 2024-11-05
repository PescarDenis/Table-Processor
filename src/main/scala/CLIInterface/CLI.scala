package CLIInterface

import CLIInterface.Handlers._

class CLI {
  //List of all known options for validation
  private val knownOptions = Set(
    "--input-file", "--input-separator",
    "--output-file", "--stdout", "--output-format",
    "--output-separator", "--headers",
    "--range", "--filter", "--filter-is-empty", "--filter-is-not-empty"
  )

  //List of all parameter handlers, each handling a specific parameter
  private val handlers: List[ParameterHandler] = List(
    new InputFileHandler(),
    new InputSeparatorHandler(),
    new OutputFileHandler(),
    new StdoutHandler(),
    new OutputFormatHandler(),
    new OutputSeparatorHandler(),
    new HeadersHandler(),
    new RangeHandler(),
    new FiltersHandler() // Filters handler for handling filtering parameters
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
  //Helper method to print the help messages
  private def printHelp(): Unit = {
    val visitor = new HelpVisitor()
    handlers.foreach(_.accept(visitor))
    println(visitor.getHelpText)
  }
}
