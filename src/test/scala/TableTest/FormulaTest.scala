package TableTest

import Evaluation.EvaluationTypes.{FloatResult, IntResult, EmptyResult}
import Evaluation.EvaluationError
import org.scalatest.funsuite.AnyFunSuite
import Table.ParseTableCells
import Table.TableEntries.{Formula, Number, Empty}
import ExpressionAST.EvaluationContext

class FormulaTest extends AnyFunSuite {

  test("general case scenario test") {
    val context = new EvaluationContext(Map(
      ParseTableCells(1, 2) -> Number(1, 2), // B1 with no formula (1-based)
      ParseTableCells(1, 3) -> Number(1, 3)  // C1 with no formula (1-based)
    ))

    context.lookup(ParseTableCells(1, 2)).set("7")  // B1 = 7
    context.lookup(ParseTableCells(1, 3)).set("10") // C1 = 10

    val formulaA1 = new Formula(1, 1)  // Formula for A1 (1-based indexing)
    formulaA1.set("=B1 + C1")

    val resultA1 = formulaA1.evaluate(context, Set.empty) // Evaluate with an empty visited set

    assert(resultA1 == IntResult(17))
  }

  test("test scenario with referencing a formula to an empty cell") {
    val context = new EvaluationContext(Map(
      ParseTableCells(1, 1) -> new Formula(1, 1),    // A1: Formula (1-based)
      ParseTableCells(1, 2) ->  Number(1, 2),     // B1: Number (1-based)
      ParseTableCells(1, 3) ->  Number(1, 3),     // C1: Number (1-based)
      ParseTableCells(1, 4) ->  Empty(1, 4),      // D1: Empty (1-based)
      ParseTableCells(1, 5) -> new Formula(1, 5)     // E1: Another formula (1-based)
    ))

    // Set values for B1 and C1
    context.lookup(ParseTableCells(1, 2)).set("10") // B1 = 10
    context.lookup(ParseTableCells(1, 3)).set("5")  // C1 = 5

    // (A1 = B1 - C1)
    context.lookup(ParseTableCells(1, 1)).set("=B1 - C1")

    // Assert A1 evaluates correctly
    context.lookup(ParseTableCells(1, 1)) match {
      case formula: Formula =>
        val resultA1 = formula.evaluate(context, Set.empty)
        assert(resultA1 == IntResult(5)) // A1 = 10 - 5 = 5
    }

    context.lookup(ParseTableCells(1, 5)).set("=A1 * C1")

    // Assert E1 evaluates correctly
    context.lookup(ParseTableCells(1, 5)) match {
      case formula: Formula =>
        val resultE1 = formula.evaluate(context, Set.empty)
        assert(resultE1 == IntResult(25)) // E1 = 5 * 5 = 25
    }

    // Set A1 to reference D1, an empty cell (A1 = D1)
    context.lookup(ParseTableCells(1, 1)).set("=D1")

    // Assert that A1 returns EmptyResult when referencing an empty cell
    context.lookup(ParseTableCells(1, 1)) match {
      case formula: Formula =>
        val resultA1 = formula.evaluate(context, Set.empty)
        assert(resultA1 == EmptyResult)
    }
  }
}
