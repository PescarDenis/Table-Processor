package CLIInterface
import Filters.TableFilter
//Defines configuration settings, some of them are set for default
case class CLIConfig(
                      inputFile: Option[String] = None,
                      inputSeparator: String = ",",
                      outputFile: Option[String] = None,
                      outputToStdout: Boolean = true,
                      outputFormat: String = "csv",          
                      outputSeparator: String = ",",          
                      headers: Boolean = false,
                      range: Option[(String, String)] = None,
                      filters: List[TableFilter] = List()
                    )