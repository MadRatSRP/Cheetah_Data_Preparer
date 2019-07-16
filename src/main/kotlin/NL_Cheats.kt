import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader
import java.sql.*
import java.util.stream.Collectors
import kotlin.system.exitProcess
import kotlin.system.measureTimeMillis

class NL_Cheats {
    object cheats {
        const val basePath = "F:\\MobileContent\\J2ME_Cheats\\"
        const val dbPath = "J2ME_Cheetah\\app\\src\\main\\assets\\databases\\Cheats.db"
        const val titlePath = "Cheats_Application\\nl_cheats\\menu\\"
        const val descriptionPath = "Cheats_Application\\nl_cheats\\txt\\"

        const val TABLE_NAME = "Cheats"
        const val COLUMN_NAME_ID = "Id"
        const val COLUMN_NAME_TITLE = "Title"
        const val COLUMN_NAME_DESCRIPTION = "Description"

        const val SQL_CREATE_CHEATS =
            "CREATE TABLE $TABLE_NAME (" +
                    "$COLUMN_NAME_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "$COLUMN_NAME_TITLE TEXT," +
                    "$COLUMN_NAME_DESCRIPTION TEXT)"
        const val SQL_DELETE_CHEATS =
            "DROP TABLE IF EXISTS $TABLE_NAME"
        const val SQL_SELECT_ALL =
            "SELECT * FROM $TABLE_NAME"

        fun returnInsertQueryWithParameters(title: String, description: String)
                : String =
            "INSERT INTO $TABLE_NAME " +
                "($COLUMN_NAME_TITLE, $COLUMN_NAME_DESCRIPTION) " +
                "VALUES ('$title', '$description')"
    }

    fun dropAndCreateNewCheatsDatabase() {
        println("dropAndCreateNewCheatsDatabase()")

        var conn: Connection? = null
        var stmt: Statement? = null
        try {
            Class.forName("org.sqlite.JDBC")
            conn = DriverManager.getConnection("jdbc:sqlite:${cheats.basePath}${cheats.dbPath}")

            println("Database was opened")

            stmt = conn.createStatement()

            stmt.execute(cheats.SQL_DELETE_CHEATS)
            stmt.execute(cheats.SQL_CREATE_CHEATS)
        }
        catch (ex: ClassNotFoundException) {
            ex.printStackTrace()
        }
        catch (ex: SQLException) {
            ex.printStackTrace()
        }
        finally {
            try {
                stmt?.close()
                conn?.close()
                println("Table Cheats dropped and re-created successfully")
            }
            catch (e: SQLException) { e.printStackTrace() }
        }
    }
    fun callNewFunction() {
        println("callNewFunction()")
        // Дропаем и создаем новую таблицу
        dropAndCreateNewCheatsDatabase()

        val time = measureTimeMillis {
            saveListOfCheats(cheats.basePath + cheats.titlePath + "c1.txt")
            /*saveListOfCheats(cheats.basePath + cheats.titlePath + "c2.txt")
            saveListOfCheats(cheats.basePath + cheats.titlePath + "c3.txt")
            saveListOfCheats(cheats.basePath + cheats.titlePath + "c4.txt")*/
            saveListOfCheats(cheats.basePath + cheats.titlePath + "c5.txt")
            saveListOfCheats(cheats.basePath + cheats.titlePath + "c6.txt")
        }
        print("Затраченное время $time ms")
    }
    fun saveListOfCheats(path: String) {
        println("saveListOfCheats()")
        val pathToFolder = File(path)

        val listOfPairs = readFileAndFormListOfPairs(pathToFolder)
        val updatedListOfPairs = getUpdatedListOfPairsByDescriptionPath(listOfPairs)
        saveUpdatedDataIntoDatabase(updatedListOfPairs)
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
            val completePath = cheats.basePath + cheats.descriptionPath + it.second

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
    fun convertListOfPairsIntoListOfCheats(updatedListOfPairs: ArrayList<Pair<String, String>>) {
        val listOfCheats = ArrayList<Cheat>()

        updatedListOfPairs.forEach {

        }
    }
    /*fun saveUpdatedDataIntoDatabase(updatedListOfPairs: ArrayList<Pair<String, String>>) {
        println("saveUpdatedDataIntoDatabase()")

        var conn: Connection? = null
        var stmt: Statement? = null
        try {
            Class.forName("org.sqlite.JDBC")
            conn = DriverManager.getConnection("jdbc:sqlite:${cheats.basePath}${cheats.dbPath}")

            println("Database was opened")

            stmt = conn.createStatement()

            updatedListOfPairs.forEach {pair ->
                val title = pair.first
                val description = pair.second

                val insertQuery = cheats.returnInsertQueryWithParameters(title, description)
                stmt.execute(insertQuery)
            }

            val rs: ResultSet = stmt.executeQuery(cheats.SQL_SELECT_ALL)
            while (rs.next()) {
                print("\n" + rs.getString(cheats.COLUMN_NAME_TITLE))
                print("\n" + rs.getString(cheats.COLUMN_NAME_DESCRIPTION))
            }
        }
        catch (ex: ClassNotFoundException) {
            ex.printStackTrace()
        }
        catch (ex: SQLException) {
            ex.printStackTrace()
        }
        finally {
            try {
                stmt?.close()
                conn?.close()
                println("\nTable Cheats successfully updated")
            }
            catch (e: SQLException) { e.printStackTrace() }
        }
    }*/
}