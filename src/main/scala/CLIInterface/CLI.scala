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
    var config = CLIConfig()
    var remainingArgs = args

    while (remainingArgs.nonEmpty) {
      var argsProcessed = false

      for (handler <- handlers if !argsProcessed) {
        val (updatedConfig, argsAfterHandler) = handler.handle(remainingArgs, config)
        if (argsAfterHandler != remainingArgs) {
          config = updatedConfig
          remainingArgs = argsAfterHandler
          argsProcessed = true
        }
      }

      if (!argsProcessed) {
        println(s"Error: Unrecognized argument '${remainingArgs.head}'")
        printHelp()
        return None
      }
    }
    Some(config)
  }

  def printHelp(): Unit = {
    val visitor = new HelpVisitor()
    handlers.foreach(_.accept(visitor))
    println(visitor.getHelpText)
  }
}
