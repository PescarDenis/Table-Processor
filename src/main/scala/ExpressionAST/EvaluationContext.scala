package ExpressionAST

import Table.ParseTableCells
import Table.TableEntries.TableEntry
import Table.TableInterfaces.RawTableInterface
import Table.TableEntries.Formula
import Evaluation.TableEvaluator
class EvaluationContext(
                         table: RawTableInterface[TableEntry],
                         tableEvaluator: TableEvaluator
                       ) {

  def lookup(cell: ParseTableCells): Expression[_] = {
    table.getCell(cell) match {
      case formula: Formula =>
        formula.getExpression.getOrElse(
          throw new IllegalArgumentException(s"No expression found in cell: $cell")
        )
    }
  }

  def getTableEvaluator: TableEvaluator = tableEvaluator
}

