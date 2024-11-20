package TableTest

import Evaluation.EvaluationTypes.{EmptyResult, IntResult}
import ExpressionAST.EvaluationContext
import Table.TableEntries.{Empty, Formula, Number}
import org.scalatest.funsuite.AnyFunSuite

class TableEntryTest extends AnyFunSuite {

  test("Empty cell should be empty and evaluate to EmptyResult") {
    val emptyCell = Empty(1, 1)
    assert(emptyCell.isEmpty)
    assert(emptyCell.get == "")
    val context = new EvaluationContext(null) // Mock context
    assert(emptyCell.evaluate(context, Set.empty) == EmptyResult)
  }

  test("Number cell should store valid positive integers") {
    val numberCell =  Number(1, 2231)
    numberCell.set("5")
    assert(!numberCell.isEmpty)
    assert(numberCell.get == "5")
  }

  test("Number cell should reject non-numeric input") {
    val numberCell =  Number(111, 31321)
    assertThrows[IllegalArgumentException] {
      numberCell.set("abc") // Non-numeric input
    }
  }

  test("Number cell should reject negative numbers") {
    val numberCell =  Number(1, 2)
    assertThrows[IllegalArgumentException] {
      numberCell.set("-1") // Negative number
    }
  }
  test("Number cell should evaluate to IntResult if set") {
    val numberCell = Number(1, 2)
    numberCell.set("10")
    val context = new EvaluationContext(null) // Mock context
    assert(numberCell.evaluate(context, Set.empty) == IntResult(10))
  }
  test("Number cell should throw exception if evaluated without being set") {
    val numberCell = Number(1, 2)
    val context = new EvaluationContext(null) // Mock context
    val thrown = intercept[IllegalStateException] {
      numberCell.evaluate(context, Set.empty)
    }
    assert(thrown.getMessage.contains("No value set for Number at (1, 2)"))
  }

}
