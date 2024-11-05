package EvaluationTest
import Evaluation.EvaluationTypes.{FloatResult, IntResult, EmptyResult}
import org.scalatest.funsuite.AnyFunSuite
import Table.TableEntries.{Empty, Formula, Number}
import Table.ParseTableCells
import Evaluation.TableEvaluator
import ExpressionAST.EvaluationContext
import Evaluation.EvaluationError
import Table.DefinedTabels.BaseTable

class TableEvaluatorTest extends AnyFunSuite {
  test("General Table evaluation scenario with formula referencing an empty cell and EvaluationError") {

    // Create a BasicTable
    val table = new BaseTable()

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

    // Evaluate all cells and store results, directly checking the result type
    val cellResults = context.getTable.keys.map { cellPos =>
      val result = evaluator.evaluateCellAndStore(cellPos, Set.empty, table)
      cellPos -> result // Store the result directly
    }.toMap

    // Check that the evaluated results for non-error cells are correct
    assert(cellResults(ParseTableCells(2, 3)) == IntResult(18)) // C2 = 18
    assert(cellResults(ParseTableCells(11, 17)) == IntResult(4)) // Q11 = 4
    assert(cellResults(ParseTableCells(2, 1)) == FloatResult(0.2222222222222222)) // A2 = Q11 / C2 = 4 / 18 (floor division)

    // Check that an EvaluationError occurred for F4 due to F13 being empty
    assert(cellResults(ParseTableCells(4, 6)).isInstanceOf[EvaluationError]) // F4 should return an EvaluationError
    assert(cellResults(ParseTableCells(4, 6)).asInstanceOf[EvaluationError].message.contains("Operation not supported for these types"))
  }

  test("Plain reference to an empty cell") {
    // Create a BasicTable
    val table = new BaseTable()

    // Create the context with an empty cell
    val context = new EvaluationContext(Map(
      ParseTableCells(1, 1) -> new Formula(1, 1), // A1 = B1
      ParseTableCells(1, 2) -> Empty(1, 2) // B1 empty
    ))

    context.lookup(ParseTableCells(1, 1)).set("=B1") // A1 = B1

    // Initialize TableEvaluator
    val evaluator = new TableEvaluator(context)

    // Evaluate all cells and store the results in the table
    evaluator.evaluateAllCellsAndStoreResults(table)

    // Check the evaluated results
    assert(table.getEvaluatedResultAsString(ParseTableCells.parse("A1").get) == "")
  }

  test("Circular dependency: Formula references itself") {
    // Create a BasicTable
    val table = new BaseTable()

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
      evaluator.evaluateAllCellsAndStoreResults(table) // Attempt to evaluate all cells
    }

    // Check that the exception contains the circular dependency error message
    assert(thrownException.getMessage.contains("Circular dependency detected at cell: A1"))
  }

  test("Circular dependency: Formulas referencing each other") {
    // Create a BasicTable
    val table = new BaseTable()

    // Create context with two formulas referencing each other
    val context = new EvaluationContext(Map(
      ParseTableCells(1, 1) -> new Formula(1, 1), // A1 as a formula
      ParseTableCells(1, 2) -> new Formula(1, 2) // B1 as a formula
    ))

    // Set A1 to reference B1 and B1 to reference A1 (circular dependency)
    context.lookup(ParseTableCells(1, 1)).set("=B1 + 1")
    context.lookup(ParseTableCells(1, 2)).set("=A1 + 1")

    // Initialize TableEvaluator
    val evaluator = new TableEvaluator(context)

    // Evaluate, expecting a circular dependency error
    val thrownException = intercept[IllegalArgumentException] {
      evaluator.evaluateAllCellsAndStoreResults(table)
    }

    // Assert that the exception contains the correct circular dependency error message
    assert(thrownException.getMessage.contains("Circular dependency detected at cell: B1"))
  }
  test("Table evaluator, with no problems"){
    val table = new BaseTable()

    // Create the context with a mixture of formulas, numbers, and empty cells
    val context = new EvaluationContext(Map(
      ParseTableCells(4, 6) -> new Formula(4, 6), // F4 (row 4, col 6): =C2 * A2
      ParseTableCells(2, 3) -> Number(2, 3), // C2 (row 2, col 3): 18
      ParseTableCells(11, 17) -> Number(11, 17), // Q11 (row 11, col 17): 4
      ParseTableCells(13, 6) -> Empty(13, 6),
      ParseTableCells(2, 1) -> new Formula(2, 1) // A2 (row 2, col 1): =Q11 / C2
    ))

    // Set C2 and Q11 values
    context.lookup(ParseTableCells(2, 3)).set("18") // C2 = 18
    context.lookup(ParseTableCells(11, 17)).set("4") // Q11 = 4
    // Set formulas
    context.lookup(ParseTableCells(4, 6)).set("=C2 * A2") // F4 = C2 * F13 (F13 is empty)
    context.lookup(ParseTableCells(2, 1)).set("=Q11 / C2") // A2 = Q11 / C2

    // Initialize TableEvaluator
    val evaluator = new TableEvaluator(context)

    val cellResults = context.getTable.keys.map { cellPos =>
      val result = evaluator.evaluateCellAndStore(cellPos, Set.empty, table)
      cellPos -> result // Store the result directly
    }.toMap

    // Check that the evaluated results for non-error cells are correct
    assert(cellResults(ParseTableCells(2, 3)) == IntResult(18)) // C2 = 18
    assert(cellResults(ParseTableCells(11, 17)) == IntResult(4)) // Q11 = 4
    assert(cellResults(ParseTableCells(2, 1)) == FloatResult(0.2222222222222222)) // A2 = Q11 / C2 = 4 / 18 (floor division)
    assert(cellResults(ParseTableCells(4,6)) == FloatResult(4.0))
    assert(cellResults(ParseTableCells(13,6)) == EmptyResult)
  }
  test("Evaluate cells and retrieve evaluated results as strings") {
    val context = new EvaluationContext(Map(
      ParseTableCells(1, 1) -> Number(1, 1), // A1: 10
      ParseTableCells(1, 2) -> new Formula(1, 2), // B1: =A1 + 5
      ParseTableCells(2, 1) -> new Formula(2, 1), // A2: =B1 / 2
      ParseTableCells(2, 2) -> Empty(2, 2) // B2: Empty cell
    ))

    context.lookup(ParseTableCells(1, 1)).set("10") // A1 = 10
    context.lookup(ParseTableCells(1, 2)).set("=A1 + 5") // B1 = A1 + 5
    context.lookup(ParseTableCells(2, 1)).set("=B1 /2") // A2 = B1 / 2

    val table = new BaseTable()
    val evaluator = new TableEvaluator(context)
    evaluator.evaluateAllCellsAndStoreResults(table)

    // Assertions to verify string outputs of evaluated results
    assert(table.getEvaluatedResultAsString(ParseTableCells.parse("A1").get) == "10")
    assert(table.getEvaluatedResultAsString(ParseTableCells.parse("B1").get) == "15")
    assert(table.getEvaluatedResultAsString(ParseTableCells.parse("A2").get) == "7.5")
    assert(table.getEvaluatedResultAsString(ParseTableCells.parse("B2").get) == "")
  }
}
