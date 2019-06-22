import kotlinx.coroutines.*
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.InputStreamReader
import java.util.*
import kotlin.system.measureTimeMillis

suspend fun main() {
    processingOfNLCheats()
}
suspend fun processingOfNLCheats() {
    val path = "C:\\Java and etc\\Cheats_Application\\nl_cheats\\menu\\"

    val nlCheats = NL_Cheats()

    nlCheats.formListOfPairs(path).join()//.cancelAndJoin()

    nlCheats.workingWithFAQs()
}


fun kek() {
    val filePath = "C:\\Java and etc\\Cheats_Application\\chemax_mobile\\data\\cheats\\cheats.txt"
    val bufferedReader = BufferedReader(InputStreamReader(
        FileInputStream(filePath),"UTF8")
    )

    bufferedReader.skip(22)

    val stringBuilder = StringBuilder()

    /*val givenSymbol = bufferedReader.read()

    stringBuilder.append(givenSymbol.toChar())

    print(stringBuilder.toString())*/

    bufferedReader.skip(2)

    /*stringBuilder.append(bufferedReader.read().toChar())

    print(stringBuilder.toString())*/

    bufferedReaderToScanner(bufferedReader)
}
fun bufferedReaderToScanner(bufferedReader: BufferedReader) {
    val scanner = Scanner(bufferedReader)

    /*while (scanner.hasNext()) {
        val cheatTitle = getCheatTitle(scanner)
        print(cheatTitle)*/

    /*scanner.useDelimiter("\\*")

    val cheatTitle = scanner.next()
    print("\nTitle:$cheatTitle")*/

    //scanner.useDelimiter("")



    /*val cheatDescription = scanner.next()
    print("\nDescription:$cheatDescription")*/


    //scanner.nextLine().replace("&~", "")
    //scanner.useDelimiter("&~")
    /*}
    scanner.close()*/



    /*bufferedReader.lines().forEach {
        val cheatTitle = getCheatTitle(bufferedReader)
        print(cheatTitle)
    }*/

    while (bufferedReader.read() != null) {
        val cheatTitle = getCheatTitle(bufferedReader)
        print(cheatTitle)
    }



    /*scanner.useDelimiter("\\*")
    val string = scanner.next()
    print(string)

    print(scanner.next())

    print(scanner.useDelimiter("&~\\*").next())*/
}
fun getCheatTitle(bufferedReader: BufferedReader): String {
    /*scanner.useDelimiter("\\*")

    return ("\nTitle:${scanner.next()}")*/


    //print("\nTitle:$cheatTitle")*/

    //scanner.useDelimiter("")

    val scanner = Scanner(bufferedReader)

    val cheatTitle = scanner.useDelimiter("\\*").nextLine()

    return cheatTitle
}