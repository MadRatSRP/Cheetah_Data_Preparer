import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SchemaUtils.create
import org.jetbrains.exposed.sql.SchemaUtils.drop
import org.jetbrains.exposed.sql.transactions.transaction

import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader
import java.util.stream.Collectors
import kotlin.system.measureTimeMillis

class NL_Cheats {
    fun workingWithFAQs() {
        val listOfFAQs = readFileOfFAQs()
        
    }

    fun saveListOfCheats(path: String) {
        val pathToFolder = File(path)

        val listOfPairs = readFileAndFormListOfPairs(pathToFolder)
        val updatedListOfPairs = getUpdatedListOfPairsByDescriptionPath(listOfPairs)
        saveUpdatedDataIntoDatabase(updatedListOfPairs)
    }

    fun formListOfPairs(path: String): Job = GlobalScope.launch{
        dropAndCreateNewCheatsDatabase()

        val formFirstListOfPairs = async {
            saveListOfCheats(path + "c1.txt")
        }
        val formSecondListOfPairs = async {
            saveListOfCheats(path + "c2.txt")
        }
        val formThirdListOfPairs = async {
            saveListOfCheats(path + "c3.txt")
        }
        val formFourthListOfPairs = async {
            saveListOfCheats(path + "c4.txt")
        }
        val formFifthListOfPairs = async {
            saveListOfCheats(path + "c5.txt")
        }
        val formSixthListOfPairs = async {
            saveListOfCheats(path + "c6.txt")
        }
        val time = measureTimeMillis {
            formFirstListOfPairs.await()
            formSecondListOfPairs.await()
            formThirdListOfPairs.await()
            formFourthListOfPairs.await()
            formFifthListOfPairs.await()
            formSixthListOfPairs.await()
        }
        print("Затраченное время $time ms")
    }

    fun dropAndCreateNewCheatsDatabase() {
        Database.connect("jdbc:h2:file:C:\\CurrentProjects\\Databases\\NL_Cheats", driver = "org.h2.Driver")

        transaction {
            drop(Cheats)
            create(Cheats)
        }
    }

    fun readFileOfFAQs(): List<String> {
        val filePath = "C:\\Java and etc\\Cheats_Application\\nl_cheats\\txt\\tips\\3.txt"
        val locatedFile = File(filePath)

        val bufferedReader = BufferedReader(
            InputStreamReader(
                FileInputStream(locatedFile),"UTF8")
        )

        //print(listOfFAQs.size)
        return bufferedReader.readLines()
    }

    fun readFileAndFormListOfPairs(file: File)
            : ArrayList<Pair<String, String>> {
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
        val updatedListOfPairs = ArrayList<Pair<String, String>>()

        val path = "C:\\Java and etc\\Cheats_Application\\nl_cheats\\txt\\"

        listOfPairs.forEach {
            val completePath = path + it.second

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

    fun saveUpdatedDataIntoDatabase(updatedListOfPairs: ArrayList<Pair<String, String>>) {
        Database.connect("jdbc:h2:file:C:\\CurrentProjects\\Databases\\NL_Cheats", driver = "org.h2.Driver")

        /*transaction {
            //drop(Cheats)
        }*/

        updatedListOfPairs.forEach {pair ->
            val cheatTitle = pair.first
            val cheatDescription = pair.second

            transaction {
                Cheats.insert {
                    it[title] = cheatTitle
                    it[description] = cheatDescription
                }
            }
        }

        transaction {
            for (cheat in Cheats.selectAll()) {
                println("${cheat[Cheats.id]}: ${cheat[Cheats.title]}: \n${cheat[Cheats.description]}")
            }
        }
    }

    object Cheats: Table() {
        val id = integer("id").autoIncrement().primaryKey()
        val title = varchar("title", length = 50)
        val description = varchar("description", length = 1500)
    }
}