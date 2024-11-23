package CLIInterface.Handlers

import CLIInterface.CLIConfig
import Filters.{ChainedFilter, EmptyCellFilter, FilterError, TableFilter, ValueFilter}

class FiltersHandler extends BaseParameterHandler[List[TableFilter]](
  "--filter",
  "Specifies filters to apply to the table. Options: --filter [COLUMN] [OPERATOR] [VALUE], --filter-is-empty [COLUMN], --filter-is-not-empty [COLUMN]. The filters can be chained together by " +
    "applying multiple filters at once.(optional, the filters can be repeated)"
) {


  override def handle(args: List[String], config: CLIConfig): (CLIConfig, List[String]) = {
      val (filters, remainingArgs) = parseFilters(args, config)
      (config.copy(filters = config.filters ++ filters), remainingArgs)
  }


  // Parsing filters logic
  private def parseFilters(args: List[String], config: CLIConfig): (List[TableFilter], List[String]) = {
    var remainingArgs = args
    var filters: List[TableFilter] = List()

    while (remainingArgs.nonEmpty) {
      remainingArgs match {
        case "--filter" :: column :: operator :: value :: tail =>
          try {
            val numericValue = value.toDouble // Attempt to parse the value as Double
            val filter = ValueFilter(column, operator, numericValue)
            filters = filters :+ filter
            remainingArgs = tail
          } catch {
            case _: NumberFormatException =>
              println(s"Error: Invalid value '$value' for --filter. Expected a numeric value.")
              return (filters, remainingArgs) // Stop parsing and return remaining arguments

          }


        case "--filter-is-empty" :: column :: tail =>
              val filter = EmptyCellFilter(column, isEmpty = true)
              filters = filters :+ filter
              remainingArgs = tail

        case "--filter-is-not-empty" :: column :: tail =>
              val filter = EmptyCellFilter(column, isEmpty = false)
              filters = filters :+ filter
              remainingArgs = tail

        case _ =>
              return (filters, remainingArgs)
          }
      }

      // Wrap multiple filters in a ChainedFilter
      if (filters.length > 1) {
        (List(ChainedFilter(filters)), remainingArgs)
      } else {
        (filters, remainingArgs)
      }
    }

    // Required by BaseParameterHandler, but FiltersHandler does not need value conversion
    override protected def convertValue(value: String): List[TableFilter] = List.empty
  }

