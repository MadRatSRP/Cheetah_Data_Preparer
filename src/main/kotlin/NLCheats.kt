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
        const val encodingCP1251 = "windows-1251"

        const val nameOfFileWithCheatTitles = "c%d.mnu"
        const val nameOfFileWithCheatJson = "NLCheats_Cheat#%d.json"
        const val nameOfFileWithFAQTitles = "fq.mnu"
        const val nameOfFileWithFAQJson = "NLCheats_FAQ.json"

        const val basePath = "F:\\MobileContent\\Cheats\\"
        const val pathToNLCheats = "J2ME_Cheetah\\app\\src\\main\\assets\\PreparedFilesWithCheats\\NL_Cheats\\"
        const val pathToTitles = "FoldersWithCheats\\NL_Cheats\\mnu\\"
        const val pathToDescription = "FoldersWithCheats\\NL_Cheats\\txt\\"
    }

    fun callNewFunction(): Job = GlobalScope.launch {
        val pathToFileWithCheatTitles = NL_Cheats.basePath + NL_Cheats.pathToTitles + NL_Cheats.nameOfFileWithCheatTitles
        val pathToFileWithCheatJson = NL_Cheats.basePath + NL_Cheats.pathToNLCheats + NL_Cheats.nameOfFileWithCheatJson

        val pathToFileWithFAQTitles = NL_Cheats.basePath + NL_Cheats.pathToTitles + NL_Cheats.nameOfFileWithFAQTitles

        val formAndSaveFirstFileWithCheats = async {
            saveFileWithCheatsAsJson(pathToFileWithCheatTitles.format(1),
                pathToFileWithCheatJson.format(1))
        }
        val formAndSaveSecondFileWithCheats = async {
            saveFileWithCheatsAsJson(pathToFileWithCheatTitles.format(2),
                pathToFileWithCheatJson.format(2))
        }
        val formAndSaveThirdFileWithCheats = async {
            saveFileWithCheatsAsJson(pathToFileWithCheatTitles.format(3),
                pathToFileWithCheatJson.format(3))
        }
        val formAndSaveFourthFileWithCheats = async {
            saveFileWithCheatsAsJson(pathToFileWithCheatTitles.format(4),
                pathToFileWithCheatJson.format(4))
        }
        val formAndSaveFifthFileWithCheats = async {
            saveFileWithCheatsAsJson(pathToFileWithCheatTitles.format(5),
                pathToFileWithCheatJson.format(5))
        }
        val formAndSaveSixthFileWithCheats = async {
            saveFileWithCheatsAsJson(pathToFileWithCheatTitles.format(6),
                pathToFileWithCheatJson.format(6))
        }
        val formAndSaveFileWithFAQs = async {
            saveFileWithFAQsAsJson(pathToFileWithFAQTitles)
        }
        val time = measureTimeMillis {
            /*formAndSaveFirstFileWithCheats.await()
            formAndSaveSecondFileWithCheats.await()
            formAndSaveThirdFileWithCheats.await()
            formAndSaveFourthFileWithCheats.await()
            formAndSaveFifthFileWithCheats.await()
            formAndSaveSixthFileWithCheats.await()*/
            formAndSaveFileWithFAQs.await()
        }
        print("Затраченное время $time ms")
    }
    private fun saveFileWithCheatsAsJson(pathToTitle: String, pathToJSON: String) {
        val arrayListOfStrings = readFileAndFormArrayListOfStrings(pathToTitle)
        val arrayListOfPairs = convertArrayListOfStringsIntoArrayListOfPairs(arrayListOfStrings)

        val updatedListOfPairs = getUpdatedListOfPairsByDescriptionPath(arrayListOfPairs)
        val listOfCheats = convertListOfPairsIntoListOfCheats(updatedListOfPairs)
        val listOfJsons = getListOfJsons(listOfCheats)
        putListOfJsonsIntoLocalFile(pathToJSON, listOfJsons)
    }
    private fun saveFileWithFAQsAsJson(pathToTitle: String/*, pathToJSON: String*/) {
        val arrayListOfStrings = readFileAndFormArrayListOfStrings(pathToTitle)
        val arrayListOfPairs = convertArrayListOfStringsIntoArrayListOfPairs(arrayListOfStrings)
        val updatedListOfPairs = getUpdatedListOfPairsByDescriptionPath(arrayListOfPairs)
        print("${updatedListOfPairs.size}")
    }
    private fun readFileAndFormArrayListOfStrings(pathToTitle: String): ArrayList<String> {
        val file = File(pathToTitle)
        // Объявляем и инициилизируем bufferedReader для файла с кодировкой windows-1251
        // и список с элементами типа String
        val bufferedReader = returnBufferedReader(NL_Cheats.encodingCP1251, file)
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
        val updatedListOfPairs = ArrayList<Pair<String, String>>()
        val pathToFileWithDescription = NL_Cheats.basePath + NL_Cheats.pathToDescription + "%s"

        listOfPairs.forEach {
            print("AAA"+ it.second+"\n")
            val file = File(pathToFileWithDescription.format(it.second))

            val bufferedReader = returnBufferedReader(NL_Cheats.encodingCP1251, file)

            val title = it.first
            val description = bufferedReader.lines().collect(Collectors.joining("\n"))

            updatedListOfPairs.add(Pair(title, description))

            bufferedReader.close()
        }
        return updatedListOfPairs
    }
    private fun convertListOfPairsIntoListOfCheats(updatedListOfPairs: ArrayList<Pair<String, String>>)
            : ArrayList<Entity> {
        val listOfCheats = ArrayList<Entity>()

        updatedListOfPairs.forEach {
            listOfCheats.add(Entity(it.first, it.second))
        }
        return listOfCheats
    }
    private fun getListOfJsons(listOfEntities: ArrayList<Entity>)
            : ArrayList<String> {
        val listOfJsons = ArrayList<String>()

        val moshi = Moshi.Builder().build()
        val jsonAdapter: JsonAdapter<Entity> = moshi.adapter(Entity::class.java)

        listOfEntities.forEach {
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
    private fun returnBufferedReader(charsetName: String, file: File): BufferedReader {
        return BufferedReader(
            InputStreamReader(
                FileInputStream(file),charsetName)
        )
    }
}