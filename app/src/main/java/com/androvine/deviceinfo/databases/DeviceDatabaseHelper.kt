package com.androvine.deviceinfo.databases

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.androvine.deviceinfo.dataClasses.DeviceDataModel
import java.io.FileOutputStream
import java.io.IOException

class DeviceDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {


    companion object {
        private const val DATABASE_NAME = "device_database.db"
        private const val DATABASE_VERSION = 1

        private const val TABLE_NAME = "devices"
        private const val COLUMN_BRAND = "branding"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_DEVICE = "device"
        private const val COLUMN_MODEL = "model"


    }

    override fun onCreate(db: SQLiteDatabase) {
        // Create table if not exists
        val createTableQuery = "CREATE TABLE IF NOT EXISTS $TABLE_NAME (" +
                "$COLUMN_BRAND TEXT," +
                "$COLUMN_NAME TEXT," +
                "$COLUMN_DEVICE TEXT," +
                "$COLUMN_MODEL TEXT" +
                ")"
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
            Log.e("TAG", "Device Database copied from assets")
        } catch (e: IOException) {
            e.printStackTrace()
        }


    }

    fun getDeviceByModel(model: String): DeviceDataModel? {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_NAME,
            arrayOf(COLUMN_BRAND, COLUMN_NAME, COLUMN_DEVICE, COLUMN_MODEL),
            "$COLUMN_MODEL=?",
            arrayOf(model),
            null,
            null,
            null,
            null
        )

        var deviceDataModel: DeviceDataModel? = null

        cursor.use {
            if (it.moveToFirst()) {
                val brandIndex = it.getColumnIndex(COLUMN_BRAND)
                val nameIndex = it.getColumnIndex(COLUMN_NAME)
                val deviceIndex = it.getColumnIndex(COLUMN_DEVICE)
                val modelIndex = it.getColumnIndex(COLUMN_MODEL)

                val sBrand = it.getString(brandIndex)
                val sName = it.getString(nameIndex)
                val sDevice = it.getString(deviceIndex)
                val sModel = it.getString(modelIndex)

                deviceDataModel = DeviceDataModel(sBrand, sName, sDevice, sModel)
            }
        }

        cursor.close()
        db.close()

        return deviceDataModel
    }

    fun getDeviceByDevice(device: String): DeviceDataModel? {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_NAME,
            arrayOf(COLUMN_BRAND, COLUMN_NAME, COLUMN_DEVICE, COLUMN_MODEL),
            "$COLUMN_DEVICE=?",
            arrayOf(device),
            null,
            null,
            null,
            null
        )

        var deviceDataModel: DeviceDataModel? = null

        cursor.use {
            if (it.moveToFirst()) {
                val brandIndex = it.getColumnIndex(COLUMN_BRAND)
                val nameIndex = it.getColumnIndex(COLUMN_NAME)
                val deviceIndex = it.getColumnIndex(COLUMN_DEVICE)
                val modelIndex = it.getColumnIndex(COLUMN_MODEL)

                val sBrand = it.getString(brandIndex)
                val sName = it.getString(nameIndex)
                val sDevice = it.getString(deviceIndex)
                val sModel = it.getString(modelIndex)

                deviceDataModel = DeviceDataModel(sBrand, sName, sDevice, sModel)
            }
        }

        cursor.close()
        db.close()

        return deviceDataModel
    }

}