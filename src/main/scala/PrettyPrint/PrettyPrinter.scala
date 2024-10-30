package PrettyPrint
import Table.TableInterface
///base interface that allows the user to print the table in different formats
trait PrettyPrinter {
    def print(table : TableInterface) : String
}
