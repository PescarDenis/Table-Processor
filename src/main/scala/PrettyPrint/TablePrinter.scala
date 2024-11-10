package PrettyPrint
import Table.TableInterfaces
import Filters.TableFilter
import Table.ParseTableCells
import OutputDestination.OutputHandler
import Table.TableInterfaces.EvaluatedTableInterface
import Evaluation.EvaluationTypes.EvaluationResult
//Classs that prints the table
//parametrize it over prettyprinter
class TablePrinter[T <: PrettyPrinter](printer : T) {
  def printTable(
                  table: EvaluatedTableInterface[EvaluationResult[_]],
                  outputHandler: OutputHandler,
                  range: Option[(ParseTableCells, ParseTableCells)],
                  filter: Option[TableFilter],
                  includeHeaders: Boolean
                ): Unit = {
    val content = printer.print(table, range, filter, includeHeaders)
    outputHandler.write(content)
  }
}