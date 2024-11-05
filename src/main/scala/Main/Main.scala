package Main

import ProcessingModules._
import CLIInterface._

object Main {
  def printWelcomeMessage(): Unit = {
    println("Welcome to the Table Processor CLI!")
    println("Please provide everything after the sbt statement in double quotes.")
    println("Run the program first by inputing the desired file and check that none of the cells are rendered as ERROR")
    println("If you are unsure where to start type sbt run --help or -h.")
  }

  def main(args: Array[String]): Unit = {
    val cli = new CLI()

    if (args.isEmpty) {
      printWelcomeMessage()
      return
    }

    val configOpt = cli.parse(args.toList)

    configOpt match {
      case Some(config) =>
        // Step 1: Load the input file
        val inputLoader = new InputLoader(config)
        val table = inputLoader.loadTable()

        // Step 2: Evaluate all formulas
        val evaluator = new Evaluator(table)
        evaluator.evaluateAll()

        // Step 3: Apply filters
        val filterProcessor = new Filtering(config, table)
        filterProcessor.applyFilters()

        // Step 4: Select the specified range of cells
        val rangeSelector = new RangeSelector(config, table)
        val selectedTable = rangeSelector.selectRange()

        // Step 5: Output the table in the specified format
        val tableOutput = new TableOutput(config, selectedTable)
        tableOutput.output()

      case None =>
        println("Failed to execute due to invalid configuration.")
    }
  }
}
