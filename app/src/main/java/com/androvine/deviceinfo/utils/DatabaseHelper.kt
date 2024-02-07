package com.androvine.deviceinfo.utils

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.androvine.deviceinfo.dataClasses.CpuDataModel
import org.json.JSONArray

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {



//    val model: String,
//    val name: String,
//    val fab: String,
//    val gpu: String

    companion object {
        private const val DATABASE_NAME = "cpu_database"
        private const val DATABASE_VERSION = 1

        // Table name and column names
        private const val TABLE_NAME = "cpus"
        private const val COLUMN_MODEL = "model"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_FAB = "fab"
        private const val COLUMN_GPU = "gpu"

    }

    var db: SQLiteDatabase = this.writableDatabase


    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery =
            "CREATE TABLE $TABLE_NAME ($COLUMN_MODEL TEXT, $COLUMN_NAME TEXT, $COLUMN_FAB TEXT, $COLUMN_GPU TEXT)"
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertDevice(cpuModel: CpuDataModel) {
        val contentValues = ContentValues().apply {
            put(COLUMN_MODEL, cpuModel.model)
            put(COLUMN_NAME, cpuModel.name)
            put(COLUMN_FAB, cpuModel.fab)
            put(COLUMN_GPU, cpuModel.gpu)
        }
        val id = db.insert(TABLE_NAME, null, contentValues)
        Log.d("DatabaseHelper", "Device inserted " + id)
    }
}

class DeviceListImporter(private val context: Context) {

    fun importDeviceListFromRawResources() {
        val dbHelper = DatabaseHelper(context)

        val resourceId = context.resources.getIdentifier("cpu_gpu_list", "raw", context.packageName)
        if (resourceId != 0) {
            val inputStream = context.resources.openRawResource(resourceId)
            val jsonString = inputStream.bufferedReader().use { it.readText() }
            val jsonArray = JSONArray(jsonString)
            for (j in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(j)
                val deviceDataModel = CpuDataModel(
                    jsonObject.getString("model"),
                    jsonObject.getString("name"),
                    jsonObject.getString("fab"),
                    jsonObject.getString("gpu")
                )
                dbHelper.insertDevice(deviceDataModel)
            }
        }

    }
}
