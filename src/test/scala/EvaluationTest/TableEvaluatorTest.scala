package EvaluationTest

import Evaluation.EvaluationTypes.{FloatResult, IntResult}
import org.scalatest.funsuite.AnyFunSuite
import Table.TableEntries.{Empty, Formula, Number}
import Table.ParseTableCells
import Evaluation.TableEvaluator
import ExpressionAST.EvaluationContext
import Evaluation.EvaluationError
class TableEvaluatorTest extends AnyFunSuite {

  test("General Table evaluation scenario") {

    // Create the context with a mixture of formulas, numbers, and empty cells
    val context = new EvaluationContext(Map(
      ParseTableCells(4, 6) -> new Formula(4, 6), // F4 (row 4, col 6): =C2 * F13
      ParseTableCells(2, 3) -> Number(2, 3), // C2 (row 2, col 3): 18
      ParseTableCells(11, 17) -> Number(11, 17), // Q11 (row 11, col 17): 4
      ParseTableCells(13, 6) -> Empty(13, 6), // F13 (row 13, col 6): Empty
      ParseTableCells(2, 1) -> new Formula(2, 1) // A2 (row 2, col 1): =Q11 / C2
    ))

    // Set C2 and Q11 values
    context.lookup(ParseTableCells(2, 3)).set("18") // C2 = 18
    context.lookup(ParseTableCells(11, 17)).set("4") // Q11 = 4

    // Set formulas
    context.lookup(ParseTableCells(4, 6)).set("=C2 * F13") // F4 = C2 * F13 (F13 is empty)
    context.lookup(ParseTableCells(2, 1)).set("=Q11 / C2") // A2 = Q11 / C2

    // Initialize TableEvaluator
    val evaluator = new TableEvaluator(context)

    val evaluatedCells = evaluator.evaluateAllCells()

    // Print the evaluated results
    evaluatedCells.foreach { case (cellPos, result) =>
      println(s"Cell ${cellPos.row}, ${cellPos.col} evaluated to: $result")
    }
  }
  test("plain reference to an empty cell") {
    val context = new EvaluationContext(Map(
      ParseTableCells(1, 1) -> new Formula(1, 1), // A1 = B1
      ParseTableCells(1, 2) -> Empty(1, 2))) // B1 empty

    context.lookup(ParseTableCells(1, 1)).set("=B1") // C2 = 18

    val evaluator = new TableEvaluator(context)

    val evaluatedCells = evaluator.evaluateAllCells()

    // Print the evaluated results
    evaluatedCells.foreach { case (cellPos, result) =>
      println(s"Cell ${cellPos.row}, ${cellPos.col} evaluated to: $result")
    }
  }
  test("Circular dependency: Formula references itself") {
    // Create context with a formula that references itself
    val context = new EvaluationContext(Map(
      ParseTableCells(1, 1) -> new Formula(1, 1) // A1 as a formula
    ))

    // Set A1 to reference itself (A1 = A1 + 1)
    context.lookup(ParseTableCells(1, 1)).set("=A1 + 1")

    // Initialize TableEvaluator
    val evaluator = new TableEvaluator(context)

    // Evaluate A1, expecting a circular dependency error
    val thrownException = intercept[IllegalArgumentException] {
      evaluator.evaluateAllCells() // Attempt to evaluate all cells
    }


    // Check that the exception contains the circular dependency error message
    assert(thrownException.getMessage.contains("Circular dependency detected at cell: A1"))
  }
  test("Circular dependency: Formula referecing each others") {
    // Create context with a formula that references itself
    val context = new EvaluationContext(Map(
      ParseTableCells(1, 1) -> new Formula(1, 1), // A1 as a formula
      ParseTableCells(1, 2) -> new Formula(1, 2)
    ))

    // Set A1 to reference itself (A1 = A1 + 1)
    context.lookup(ParseTableCells(1, 1)).set("=B1 + 1")
    context.lookup(ParseTableCells(1, 2)).set("=A1 + 1")

    val evaluator = new TableEvaluator(context)
    val thrownException = intercept[IllegalArgumentException] {
      evaluator.evaluateAllCells() // Attempt to evaluate all cells

    }

    // Assert that the exception contains the correct circular dependency error message
    assert(thrownException.getMessage.contains("Circular dependency detected at cell: B1"))
  }
}
