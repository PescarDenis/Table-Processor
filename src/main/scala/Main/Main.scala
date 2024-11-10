package Main

import ProcessingModules._
import CLIInterface._
import PrettyPrint._
import Table.DefinedTabels.BaseTable
import Table.TableInterfaces.{RawTableInterface, EvaluatedTableInterface}
import scala.io.StdIn
/*
object Main {
  def printWelcomeMessage(): Unit = {
    println("Welcome to the Table Processor CLI!")
    println("Please provide everything after the sbt statement in double quotes.")
    println("Run the program first by inputting the desired file and check that none of the cells are rendered as ERROR.")
    println("If you are unsure where to start, type --help or -h for guidance.")
  }

  def main(args: Array[String]): Unit = {
    PrettyPrinterRegistry.register("csv", sep => new CSVPrettyPrinter(sep))
    PrettyPrinterRegistry.register("md", _ => new MarkdownPrettyPrinter())

    val cli = new CLI()
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
              sys.exit(1)
            case None =>
              println("Invalid input or configuration. Please try again.")
          }
      }
    }
  }

  private def processWorkflow(config: CLIConfig): Unit = {
    val (rawTable, evaluatedTable) = loadTable(config)
    evaluateTable(rawTable, evaluatedTable)
    applyFilters(config, evaluatedTable)
    val selectedTable = selectRange(config, evaluatedTable)
    outputTable(config, selectedTable)
  }

  private def loadTable(config: CLIConfig): (RawTableInterface[TableEntry], EvaluatedTableInterface[EvaluationResult[_]]) = {
    println("\nLoading table...")
    val inputLoader = new InputLoader(config)
    val table = inputLoader.loadTable() // Returns a BaseTable implementing both interfaces
    println("Table loaded successfully.")
    (table, table)
  }

  private def evaluateTable(
                             rawTable: RawTableInterface[TableEntry],
                             evaluatedTable: EvaluatedTableInterface[EvaluationResult[_]]
                           ): Unit = {
    println("\nEvaluating formulas...")
    val tableEvaluator = new TableEvaluator(new EvaluationContext(rawTable))
    tableEvaluator.evaluateAllCellsAndStoreResults(evaluatedTable)
    println("Formulas evaluated successfully.")
  }

  private def applyFilters(
                            config: CLIConfig,
                            evaluatedTable: EvaluatedTableInterface[EvaluationResult[_]]
                          ): Unit = {
    if (config.filters.nonEmpty) {
      println("\nApplying filters...")
      val filtering = new Filtering(config, evaluatedTable)
      filtering.applyFilters()
      println("Filters applied successfully.")
    } else {
      println("\nNo filters specified. Skipping filtering step.")
    }
  }

  private def selectRange(
                           config: CLIConfig,
                           evaluatedTable: EvaluatedTableInterface[EvaluationResult[_]]
                         ): EvaluatedTableInterface[EvaluationResult[_]] = {
    config.range match {
      case Some((start, end)) =>
        println(s"\nSelecting range from $start to $end...")
        val rangeSelector = new RangeSelector(config, evaluatedTable)
        rangeSelector.selectRange()
      case None =>
        println("\nNo range specified. Using the entire table.")
        evaluatedTable
    }
  }

  private def outputTable(
                           config: CLIConfig,
                           evaluatedTable: EvaluatedTableInterface[EvaluationResult[_]]
                         ): Unit = {
    println("\nOutputting table...")
    val tableOutput = new TableOutput(config, evaluatedTable)
    tableOutput.output()
    println("Table output completed.")
  }
}
 */
