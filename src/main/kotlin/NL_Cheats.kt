import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.*
import java.util.stream.Collectors
import kotlin.system.measureTimeMillis

class NL_Cheats {
    object cheats {
        const val basePath = "F:\\MobileContent\\Cheats\\"
        const val pathToNLCheats = "J2ME_Cheetah\\app\\src\\main\\assets\\PreparedFilesWithCheats\\NL_Cheats\\"
        const val pathToTitles = "FoldersWithCheats\\Cheats_Application\\nl_cheats\\menu\\"
        const val pathToDescription = "FoldersWithCheats\\Cheats_Application\\nl_cheats\\txt\\"
    }

    fun callNewFunction(): Job = GlobalScope.launch {
        println("callNewFunction()")

        val formAndSaveFirstFileWithCheats = async {
            saveListOfCheats(cheats.basePath + cheats.pathToTitles + "c1.txt",
                cheats.basePath + cheats.pathToNLCheats + "nl_cheats_#1.json")
        }
        val formAndSaveSecondFileWithCheats = async {
            saveListOfCheats(cheats.basePath + cheats.pathToTitles + "c2.txt",
                cheats.basePath + cheats.pathToNLCheats + "nl_cheats_#2.json")
        }
        val formAndSaveThirdFileWithCheats = async {
            saveListOfCheats(cheats.basePath + cheats.pathToTitles + "c3.txt",
                cheats.basePath + cheats.pathToNLCheats + "nl_cheats_#3.json")
        }
        val formAndSaveFourthFileWithCheats = async {
            saveListOfCheats(cheats.basePath + cheats.pathToTitles + "c4.txt",
                cheats.basePath + cheats.pathToNLCheats + "nl_cheats_#4.json")
        }
        val formAndSaveFifthFileWithCheats = async {
            saveListOfCheats(cheats.basePath + cheats.pathToTitles + "c5.txt",
                cheats.basePath + cheats.pathToNLCheats + "nl_cheats_#5.json")
        }
        val formAndSaveSixthFileWithCheats = async {
            saveListOfCheats(cheats.basePath + cheats.pathToTitles + "c6.txt",
                cheats.basePath + cheats.pathToNLCheats + "nl_cheats_#6.json")
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
    fun saveListOfCheats(path: String, jsonName: String) {
        println("saveListOfCheats()")
        val pathToFolder = File(path)

        val listOfPairs = readFileAndFormListOfPairs(pathToFolder)
        val updatedListOfPairs = getUpdatedListOfPairsByDescriptionPath(listOfPairs)
        val listOfCheats = convertListOfPairsIntoListOfCheats(updatedListOfPairs)
        val listOfJsons = getListOfJsons(listOfCheats)
        putListOfJsonsIntoLocalFile(jsonName, listOfJsons)
    }

    fun readFileAndFormListOfPairs(file: File)
            : ArrayList<Pair<String, String>> {
        println("readFileAndFormListOfPairs()")

        val bufferedReader = BufferedReader(
            InputStreamReader(
                FileInputStream(file),"UTF8")
        )

        val list = bufferedReader.readLines() //as ArrayList<String>

        //print("Размер списка: ${list.size}\n")

        val listOfPairs = ArrayList<Pair<String, String>>()

        val listOfTitles = ArrayList<String>()
        val listOfDescriptionPaths = ArrayList<String>()

        for (i in 0 until list.size) {
            if (i % 2 == 0)
                listOfTitles.add(list[i])
            else listOfDescriptionPaths.add(list[i])
        }

        for (i in 0 until listOfTitles.size) {
            listOfPairs.add(
                Pair(listOfTitles[i],
                    listOfDescriptionPaths[i])
            )
        }
        return listOfPairs
    }
    fun getUpdatedListOfPairsByDescriptionPath(listOfPairs: ArrayList<Pair<String, String>>)
            : ArrayList<Pair<String, String>> {
        println("getUpdatedListOfPairsByDescriptionPath()")

        val updatedListOfPairs = ArrayList<Pair<String, String>>()

        listOfPairs.forEach {
            val completePath = cheats.basePath + cheats.pathToDescription + it.second

            val bufferedReader = BufferedReader(
                InputStreamReader(
                    FileInputStream(completePath),"Cp1251")
            )

            val title = it.first
            val description = bufferedReader.lines().collect(Collectors.joining("\n"))

            updatedListOfPairs.add(Pair(title, description))
        }
        return updatedListOfPairs
    }
    fun convertListOfPairsIntoListOfCheats(updatedListOfPairs: ArrayList<Pair<String, String>>)
            : ArrayList<Cheat> {
        val listOfCheats = ArrayList<Cheat>()

        updatedListOfPairs.forEach {
            listOfCheats.add(Cheat(it.first, it.second))
        }
        return listOfCheats
    }
    fun getListOfJsons(listOfCheats: ArrayList<Cheat>)
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