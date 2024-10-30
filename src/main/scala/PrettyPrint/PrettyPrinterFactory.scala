package PrettyPrint
import File_Reader.CSVSeparator
object PrettyPrinterFactory {
  def getPrinter(format: String, separator: CSVSeparator, includeHeaders: Boolean): PrettyPrinter = {
    format.toLowerCase match {
      case "csv" => new CSVPrettyPrinter(separator, includeHeaders)
      case "md"  => new MarkdownPrettyPrinter(includeHeaders)
      case _ => throw new IllegalArgumentException(s"Unknown format: $format")
    }
  }
}