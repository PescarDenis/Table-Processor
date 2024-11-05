package Main

import ProcessingModules._
import CLIInterface._
import PrettyPrint._
import scala.io.StdIn
import Table.DefinedTabels.BaseTable

object Main {
  def printWelcomeMessage(): Unit = {
    println("Welcome to the Table Processor CLI!")
    println("Please provide everything after the sbt statement in double quotes.")
    println("Run the program first by inputting the desired file and check that none of the cells are rendered as ERROR.")
    println("If you are unsure where to start, type --help or -h for guidance.")
  }

  def main(args: Array[String]): Unit = {
    // Register PrettyPrinters
    PrettyPrinterRegistry.register("csv", sep => new CSVPrettyPrinter(sep))
    PrettyPrinterRegistry.register("md", _ => new MarkdownPrettyPrinter())

    val cli = new CLI()

    // Show welcome message and enter command loop
    printWelcomeMessage()
    var running = true

    while (running) {
      print("\nEnter a command: ")
      val input = StdIn.readLine().trim

      input match {
        case "--help" | "-h" =>
          cli.printHelp()
        case "exit" =>
          println("Exiting the CLI. Goodbye!")
          running = false
        case _ =>
          val config = cli.parse(input.split("\\s+").toList)
          config match {
            case Some(conf) if conf.inputFile.isDefined =>
              try {
                processWorkflow(conf)
              } catch {
                case ex: Exception =>
                  println(s"An error occurred: ${ex.getMessage}")
              }
            case Some(_) =>
              println("Error: --input-file is required. Exiting program.")
              sys.exit(1) // Exit immediately if input file is not provided
            case None =>
              println("Invalid input or configuration. Please try again.")
          }
      }
    }
  }

  private def processWorkflow(config: CLIConfig): Unit = {
    val table = loadTable(config)
    evaluateTable(table)
    applyFilters(config, table)
    val selectedTable = selectRange(config, table)
    outputTable(config, selectedTable)
  }

  private def loadTable(config: CLIConfig): BaseTable = {
    println("\nLoading table...")
    val inputLoader = new InputLoader(config)
    val table = inputLoader.loadTable()
    println("Table loaded successfully.")
    table
  }

  private def evaluateTable(table: BaseTable): Unit = {
    println("\nEvaluating formulas...")
    val evaluator = new Evaluator(table)
    evaluator.evaluateAll()
    println("Formulas evaluated successfully.")
  }

  private def applyFilters(config: CLIConfig, table: BaseTable): Unit = {
    if (config.filters.nonEmpty) {
      println("\nApplying filters...")
      val filtering = new Filtering(config, table)
      filtering.applyFilters()
      println("Filters applied successfully.")
    } else {
      println("\nNo filters specified. Skipping filtering step.")
    }
  }

  private def selectRange(config: CLIConfig, table: BaseTable): BaseTable = {
    config.range match {
      case Some((start, end)) =>
        println(s"\nSelecting range from $start to $end...")
        val rangeSelector = new RangeSelector(config, table)
        rangeSelector.selectRange()
      case None =>
        println("\nNo range specified. Using the entire table.")
        table
    }
  }

  private def outputTable(config: CLIConfig, table: BaseTable): Unit = {
    println("\nOutputting table...")
    val tableOutput = new TableOutput(config, table)
    tableOutput.output()
    println("Table output completed.")
  }
}
