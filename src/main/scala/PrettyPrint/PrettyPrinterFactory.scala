package PrettyPrint
import File_Reader.CSVSeparator
//factory class that selects what format we want to print
object PrettyPrinterFactory {
  def getPrinter(format: String, separator: CSVSeparator, includeHeaders: Boolean): PrettyPrinter = {
    format.toLowerCase match {
      case "csv" => new CSVPrettyPrinter(separator)
      case "md"  => new MarkdownPrettyPrinter()
      case _ => throw new IllegalArgumentException(s"Unknown format: $format")
    }
  }
}