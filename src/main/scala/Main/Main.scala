package Main

import ProcessingModules.*
import CLIInterface.*
import ExpressionParser.ParserLogic.ExpressionBuilder.ExpressionBuilder
import TableParser.FileParser
import ExpressionParser.ParsingServices.DefaultExpressionParser
import PrettyPrint.{CSVPrettyPrinter, MarkdownPrettyPrinter, PrettyPrinterRegistry}
object Main {

  def printWelcomeMessage(): Unit = {
    println("Welcome to the Table Processor CLI!")
    println("Run the program with the desired file and parameters.")
    println("""Please enter everything after sbt in double quotes. When using the ; separator, enter it as \";\".""")
    println("For help, use: sbt run --help or -h.")
  }

  def main(args: Array[String]): Unit = {
    val cli = new CLI()

    if (args.isEmpty) {
      printWelcomeMessage()
      return
    }
    val printerRegistry = new PrettyPrinterRegistry()
    printerRegistry.register("csv", sep =>  CSVPrettyPrinter(sep))
    printerRegistry.register("md", _ => new MarkdownPrettyPrinter())
    cli.parse(args.toList) match {
      case Some(config) =>
        try {
          // Step 1: Load the input file and initialize the table
          val inputLoader = new InputLoader(config, new FileParser(new DefaultExpressionParser(new ExpressionBuilder[Any]())))
          val table = inputLoader.loadTable()

          // Step 2: Evaluate all formulas in the table
          val evaluator = new Evaluator(table)
          evaluator.evaluateAll()

          // Step 3: Apply filters to the table
          val filteredModel = new Filtering(config, table).applyFilters()

          // Step 4: Select the specified range of cells
          val rangedModel = new RangeSelector(config, filteredModel).selectRange()

          // Step 5: Output the table in the specified format
          new TableOutput(config, rangedModel,printerRegistry).output()
        } catch {
          case ex: Exception =>
            println(s"An error occurred: ${ex.getMessage}") // Print the errors 
        }

      case None =>
        println("Invalid configuration. Use --help or -h for guidance.")
    }
  }
}
