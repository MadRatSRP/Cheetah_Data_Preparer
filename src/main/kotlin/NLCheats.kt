import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.*
import java.util.stream.Collectors
import kotlin.system.measureTimeMillis

class NLCheats {
    object NL_Cheats {
        const val basePath = "F:\\MobileContent\\Cheats\\"
        const val pathToNLCheats = "J2ME_Cheetah\\app\\src\\main\\assets\\PreparedFilesWithCheats\\NL_Cheats\\"
        const val pathToTitles = "FoldersWithCheats\\NL_Cheats\\mnu\\"
        const val pathToDescription = "FoldersWithCheats\\NL_Cheats\\txt\\"
    }

    fun callNewFunction(): Job = GlobalScope.launch {
        val pathToBaseAndTitles = NL_Cheats.basePath + NL_Cheats.pathToTitles
        val pathToBaseAndNLCheats = NL_Cheats.basePath + NL_Cheats.pathToNLCheats

        val formAndSaveFirstFileWithCheats = async {
            saveListOfCheats(pathToBaseAndTitles + "c1.mnu",
                pathToBaseAndNLCheats + "nl_cheats_#1.json")
        }
        val formAndSaveSecondFileWithCheats = async {
            saveListOfCheats(pathToBaseAndTitles + "c2.mnu",
                pathToBaseAndNLCheats + "nl_cheats_#2.json")
        }
        val formAndSaveThirdFileWithCheats = async {
            saveListOfCheats(pathToBaseAndTitles + "c3.mnu",
                pathToBaseAndNLCheats + "nl_cheats_#3.json")
        }
        val formAndSaveFourthFileWithCheats = async {
            saveListOfCheats(pathToBaseAndTitles + "c4.mnu",
                pathToBaseAndNLCheats + "nl_cheats_#4.json")
        }
        val formAndSaveFifthFileWithCheats = async {
            saveListOfCheats(pathToBaseAndTitles + "c5.mnu",
                pathToBaseAndNLCheats + "nl_cheats_#5.json")
        }
        val formAndSaveSixthFileWithCheats = async {
            saveListOfCheats(pathToBaseAndTitles + "c6.mnu",
                pathToBaseAndNLCheats + "nl_cheats_#6.json")
        }
        val time = measureTimeMillis {
            formAndSaveFirstFileWithCheats.await()
            formAndSaveSecondFileWithCheats.await()
            formAndSaveThirdFileWithCheats.await()
            formAndSaveFourthFileWithCheats.await()
            formAndSaveFifthFileWithCheats.await()
            formAndSaveSixthFileWithCheats.await()
        }
        print("Затраченное время $time ms")
    }
    private fun saveListOfCheats(path: String, jsonName: String) {
        val pathToFolder = File(path)

        val arrayListOfStrings = readFileAndFormArrayListOfStrings(pathToFolder)
        val arrayListOfPairs = convertArrayListOfStringsIntoArrayListOfPairs(arrayListOfStrings)
        val updatedListOfPairs = getUpdatedListOfPairsByDescriptionPath(arrayListOfPairs)
        val listOfCheats = convertListOfPairsIntoListOfCheats(updatedListOfPairs)
        val listOfJsons = getListOfJsons(listOfCheats)
        putListOfJsonsIntoLocalFile(jsonName, listOfJsons)
    }

    private fun returnBufferedReader(charsetName: String, file: File): BufferedReader {
        return BufferedReader(
            InputStreamReader(
                FileInputStream(file),charsetName)
        )
    }
    private fun returnBufferedReader(charsetName: String, fileName: String): BufferedReader {
        return BufferedReader(
            InputStreamReader(
                FileInputStream(fileName),charsetName)
        )
    }
    private fun readFileAndFormArrayListOfStrings(file: File): ArrayList<String> {
        // Объявляем и инициилизируем bufferedReader для файла с кодировкой windows-1251
        // и список с элементами типа String
        val bufferedReader = returnBufferedReader("windows-1251", file)
        val arrayListOfStrings = ArrayList<String>()
        // Сохраняем данные из файла в список
        arrayListOfStrings.addAll(bufferedReader.readLines())
        // Закрываем bufferedReader после использования
        bufferedReader.close()
        // Возвращаем полученный список
        return arrayListOfStrings
    }
    private fun convertArrayListOfStringsIntoArrayListOfPairs(arrayListOfStrings: ArrayList<String>)
            : ArrayList<Pair<String, String>>{
        // Объявляем список пар, названий и описаний
        val listOfPairs = ArrayList<Pair<String, String>>()
        val listOfTitles = ArrayList<String>()
        val listOfPathsToDescriptions = ArrayList<String>()
        // Для каждого элемента, кратного двум, добавляем его в вектор названий
        // В противном случае добавляем его в список описаний
        for (i in 0 until arrayListOfStrings.size) {
            if (i % 2 == 0)
                listOfTitles.add(arrayListOfStrings[i])
            else listOfPathsToDescriptions.add(arrayListOfStrings[i])
        }
        // Формируем список пар из полученных элементов списков
        // с названиями и путями к описанию
        for (i in 0 until listOfTitles.size) {
            listOfPairs.add(
                Pair(listOfTitles[i],
                    listOfPathsToDescriptions[i])
            )
        }
        return listOfPairs
    }
    private fun getUpdatedListOfPairsByDescriptionPath(listOfPairs: ArrayList<Pair<String, String>>)
            : ArrayList<Pair<String, String>> {
        println("getUpdatedListOfPairsByDescriptionPath()")

        val updatedListOfPairs = ArrayList<Pair<String, String>>()

        listOfPairs.forEach {
            val completePath = NL_Cheats.basePath + NL_Cheats.pathToDescription + it.second

            val bufferedReader = returnBufferedReader("windows-1251", completePath)

            val title = it.first
            val description = bufferedReader.lines().collect(Collectors.joining("\n"))

            updatedListOfPairs.add(Pair(title, description))

            bufferedReader.close()
        }
        return updatedListOfPairs
    }
    private fun convertListOfPairsIntoListOfCheats(updatedListOfPairs: ArrayList<Pair<String, String>>)
            : ArrayList<Cheat> {
        val listOfCheats = ArrayList<Cheat>()

        updatedListOfPairs.forEach {
            listOfCheats.add(Cheat(it.first, it.second))
        }
        return listOfCheats
    }
    private fun getListOfJsons(listOfCheats: ArrayList<Cheat>)
            : ArrayList<String> {
        val listOfJsons = ArrayList<String>()

        val moshi = Moshi.Builder().build()
        val jsonAdapter: JsonAdapter<Cheat> = moshi.adapter(Cheat::class.java)

        listOfCheats.forEach {
            val json = jsonAdapter.toJson(it)
            listOfJsons.add(json)
        }
        return listOfJsons
    }
    private fun putListOfJsonsIntoLocalFile(jsonName: String, listOfJsons: java.util.ArrayList<String>) {
        val file = File(jsonName)
        val writer = FileWriter(file)

        writer.write("[")
        listOfJsons.forEach {
            if (listOfJsons.indexOf(it) == listOfJsons.lastIndex)
                writer.write(it)
            else writer.write("$it,")
        }
        writer.write("]")
        writer.close()
    }
}