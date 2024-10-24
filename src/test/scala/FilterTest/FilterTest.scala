package FilterTest
import org.scalatest.funsuite.AnyFunSuite
import Filters._
import Table.ParseTableCells
import Evaluation.EvaluationTypes._
import ExpressionAST.EvaluationContext
import File_Reader.MockCSVReader
import Evaluation.TableEvaluator
import Table.Table
// Unit test suite
class FilterTest extends AnyFunSuite {

  // Helper method to parse and get table cell or throw an exception if invalid
  def parseCell(cell: String): ParseTableCells = {
    ParseTableCells.parse(cell).getOrElse(
      throw new IllegalArgumentException(s"Invalid cell reference: $cell")
    )
  }

  val mockData: List[List[String]] = List(
    List("5", "10", ""), // Row 1: A1 = 5, B1 = 10, C1 is empty
    List("3", "", "8"), // Row 2: A2 = 3, B2 is empty, C2 = 8
    List("", "2", "4") // Row 3: A3 is empty, B3 = 2, C3 = 4
  )

  // Create a mock CSVReader with the predefined data
  val mockCSVReader = new MockCSVReader(mockData)

  // Create a sample table using the mock CSVReader
  val table = new Table()
  table.parse(mockCSVReader)

  // Create an EvaluationContext with the parsed table's rows
  val mockContext = new EvaluationContext(table.getRows)

  // Instantiate TableEvaluator with the EvaluationContext
  val evaluator = new TableEvaluator(mockContext)

  // Evaluate all cells
  val evaluatedTable: Map[ParseTableCells, EvaluationResult[_]] = evaluator.evaluateAllCells()

  // Helper method to apply a filter to all evaluated rows
  def filterRows(filters: List[TableFilter[_]]): Seq[ParseTableCells] = {
    val chainedFilter =  ChainedFilter(filters)
    evaluatedTable.filter { case (cellPos, result) =>
      chainedFilter.matches(Map(cellPos -> result))
    }.keys.toSeq
  }

  test("EmptyCellFilter matches empty cells") {
    val emptyFilter =  EmptyCellFilter(parseCell("B2"), isEmpty = true)
    val result = filterRows(List(emptyFilter))
    assert(result == Seq(parseCell("B2"))) // Only B2 is empty in row 2
  }

  test("ValueFilter matches rows with values greater than 4 in column A") {
    val valueFilter = new ValueFilter[Int](parseCell("A1"), ">", 4)
    val result = filterRows(List(valueFilter))
    assert(result == Seq(parseCell("A1"))) // Only A1 in row 1 is greater than 4
  }

  test("ChainedFilter matches rows with combined conditions") {
    val filters = List(
      new ValueFilter[Int](parseCell("A1"), ">", 4), // Column A1 > 4
       EmptyCellFilter(parseCell("C1"), isEmpty = true) // Column C1 is empty
    )
    val result = filterRows(filters)
    assert(result == Seq(parseCell("A1"))) // Only A1 in row 1 matches both conditions
  }

  test("ValueFilter handles comparisons in column B") {
    val valueFilter = new ValueFilter[Int](parseCell("B1"), ">", 5)
    val result = filterRows(List(valueFilter))
    assert(result == Seq(parseCell("B1"))) // Only B1 has value > 5
  }
}