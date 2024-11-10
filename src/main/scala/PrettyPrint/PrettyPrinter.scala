package PrettyPrint
import Table.TableInterfaces
import Table.ParseTableCells
import Filters.TableFilter
import Table.TableInterfaces.EvaluatedTableInterface
import Evaluation.EvaluationTypes.EvaluationResult
///base interface that allows the user to print the table in different formats
trait PrettyPrinter {
  def print(
             table: EvaluatedTableInterface[EvaluationResult[_]],
             range: Option[(ParseTableCells, ParseTableCells)],
             filter: Option[TableFilter] ,
             includeHeaders: Boolean
           ): String
}
