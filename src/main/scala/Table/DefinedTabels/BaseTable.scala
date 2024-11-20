package Table.DefinedTabels

import Evaluation.EvaluationResult
import Evaluation.EvaluationTypes._
import File_Reader.CSVReader
import Table.{TableInterface, TableModel}
import TableParser.{FileParser, ParseTableCells}
import Table.TableEntries.{Empty, TableEntry}
import ExpressionAST.EvaluationContext
import Evaluation.TableEvaluator
import Filters.Row

// Implements the table interface methods and takes the file parser as the constructor 
class BaseTable(parser: FileParser) extends TableInterface {

  private var rawModel: TableModel[TableEntry] = new TableModel(Map()) // raw table model
  private var evaluatedModel: TableModel[EvaluationResult[?]] = new TableModel(Map()) //evaluated table model 
  
  // After parsing we intialize the rows again so we can evaluate
  override def initializeRows(parsedRows: TableModel[TableEntry]): Unit = {
    rawModel = parsedRows
  }
  
  // Get the entry at the cell 
  override def getCell(pos: ParseTableCells): TableEntry = {
    rawModel.getCell(pos).getOrElse(
      throw new IllegalArgumentException(s"There is no cell found at position $pos") // If we try to access a cell outside the table throw an error 
    )
  }
  
  // Get the Row provided for filtering
  override def getRow(rowIndex: Int): Row = {
    new Row(rowIndex, evaluatedModel)
  }

  override def lastRow: Option[Int] = {
    rawModel.nonEmptyPositions.map(_.row).maxOption
  }

  override def lastColumn: Option[Int] = {
    rawModel.nonEmptyPositions.map(_.col).maxOption
  }
  
  // It just gets ALL of the table entries
  override def nonEmptyPositions: Iterable[ParseTableCells] = {
    evaluatedModel.nonEmptyPositions
  }
  
  // get the Raw Entries
  override def getRawEntires: Iterable[ParseTableCells] = rawModel.nonEmptyPositions

  override def storeEvaluatedResult(pos: ParseTableCells, result: EvaluationResult[?]): Unit = {
    evaluatedModel = evaluatedModel.updateCell(pos, result)
  }

  override def getEvaluatedResult(pos: ParseTableCells): Option[EvaluationResult[?]] = {
    evaluatedModel.getCell(pos)
  }
  
  // Not used anymore because we only need the results as string after range selection but useful for testing 
  override def getEvaluatedResultAsString(pos: ParseTableCells): String = {
    evaluatedModel.getCell(pos).map {
      case IntResult(value) => value.toString
      case FloatResult(value) => value.toString
      case EmptyResult       => ""
      case _                 => "ERROR" //it will never reach this case, because we exit when the evaluation fails
    }.getOrElse(" ")
  }
  
  // The parsing is delegated to the FileParser
  override def parse(csvReader: CSVReader): Unit = {
    initializeRows(parser.parse(csvReader))
  }
  
  //The evaluation is delegated to the TableEvaluator 
  override def evaluateAllCells(context: EvaluationContext): Unit = {
    val evaluator = new TableEvaluator(this, context)
    evaluator.evaluateAllCellsAndStoreResults()
  }
}
