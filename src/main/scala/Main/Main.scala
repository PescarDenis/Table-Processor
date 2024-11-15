package Main

import ProcessingModules.*
import CLIInterface.*
import TableParser.FileParser
import ExpressionParser.ParsingServices.DefaultExpressionParser
import ExpressionParser.ParserLogic.ExpressionBuilder
object Main {

  def printWelcomeMessage(): Unit = {
    println("Welcome to the Table Processor CLI!")
    println("Run the program with the desired file and parameters.")
    println("For help, use: sbt run --help or -h.")
  }

  def main(args: Array[String]): Unit = {
    val cli = new CLI()

    if (args.isEmpty) {
      printWelcomeMessage()
      return
    }

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
          new Filtering(config, table).applyFilters()

          // Step 4: Select the specified range of cells
          val selectedTable = new RangeSelector(config, table).selectRange()

          // Step 5: Output the table in the specified format
          new TableOutput(config, selectedTable).output()
        } catch {
          case ex: Exception =>
            println(s"An error occurred: ${ex.getMessage}")
        }

      case None =>
        println("Invalid configuration. Use --help for guidance.")
    }
  }
}
