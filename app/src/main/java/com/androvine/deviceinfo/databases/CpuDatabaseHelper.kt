package com.androvine.deviceinfo.databases

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.androvine.deviceinfo.dataClasses.CpuDBModel
import java.io.FileOutputStream
import java.io.IOException

class CpuDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "cpu_database.db"
        private const val DATABASE_VERSION = 1

        private const val TABLE_NAME = "cpus"
        private const val COLUMN_MODEL = "model"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_FAB = "fab"
        private const val COLUMN_GPU = "gpu"
        private const val COLUMN_CORE = "core"
        private const val COLUMN_VENDOR = "vendor"
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Create table if not exists
        val createTableQuery =
            "CREATE TABLE IF NOT EXISTS $TABLE_NAME (" + "$COLUMN_MODEL TEXT PRIMARY KEY, " + "$COLUMN_NAME TEXT, " + "$COLUMN_FAB TEXT, " + "$COLUMN_GPU TEXT, " + "$COLUMN_CORE TEXT, " + "$COLUMN_VENDOR TEXT" + ")"

        db.execSQL(createTableQuery)
    }


    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Handle database upgrade if needed
    }

    fun copyDatabaseFromAssets(context: Context) {

        val databaseFile = context.getDatabasePath(DATABASE_NAME)

        // If the database already exists, do nothing
        if (databaseFile.exists()) {
            return
        }

        try {
            // Ensure the parent directory exists
            val parentDir = databaseFile.parentFile
            parentDir?.mkdirs()

            // Copy the database file from assets to the app's internal storage
            val inputStream = context.assets.open(DATABASE_NAME)
            val outputStream = FileOutputStream(databaseFile)
            inputStream.copyTo(outputStream)

            inputStream.close()
            outputStream.close()
            Log.e("TAG", "CPU Database copied from assets")
        } catch (e: IOException) {
            e.printStackTrace()
        }


    }
//
//    fun addCpuData(cpuData: CpuDBModel) {
//        val db = writableDatabase
//
//        val contentValues = ContentValues()
//        contentValues.put(COLUMN_MODEL, cpuData.model)
//        contentValues.put(COLUMN_NAME, cpuData.name)
//        contentValues.put(COLUMN_FAB, cpuData.fab)
//        contentValues.put(COLUMN_GPU, cpuData.gpu)
//        contentValues.put(COLUMN_CORE, cpuData.core)
//        contentValues.put(COLUMN_VENDOR, cpuData.vendor)
//
//        val id = db.insert(TABLE_NAME, null, contentValues)
//        Log.e("TAG", "Cpu data inserted with id: $id")
//        db.close()
//
//    }

    fun getCpuDataByModel(model: String): CpuDBModel? {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_NAME,
            arrayOf(COLUMN_MODEL, COLUMN_NAME, COLUMN_FAB, COLUMN_GPU, COLUMN_CORE, COLUMN_VENDOR),
            "$COLUMN_MODEL=?",
            arrayOf(model),
            null,
            null,
            null,
            null
        )

        var cpuData: CpuDBModel? = null

        cursor.use {
            if (it.moveToFirst()) {
                val nameIndex = it.getColumnIndex(COLUMN_NAME)
                val fabIndex = it.getColumnIndex(COLUMN_FAB)
                val gpuIndex = it.getColumnIndex(COLUMN_GPU)
                val coreIndex = it.getColumnIndex(COLUMN_CORE)
                val vendorIndex = it.getColumnIndex(COLUMN_VENDOR)

                val name = it.getString(nameIndex)
                val fab = it.getString(fabIndex)
                val gpu = it.getString(gpuIndex)
                val core = it.getString(coreIndex)
                val vendor = it.getString(vendorIndex)

                cpuData = CpuDBModel(model, name, fab, gpu, core, vendor)
            }
        }

        cursor.close()
        db.close()

        return cpuData
    }

}