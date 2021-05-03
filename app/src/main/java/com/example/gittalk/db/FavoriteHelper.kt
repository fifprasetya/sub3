package com.example.gittalk.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import com.example.gittalk.FavoriteModel
import com.example.gittalk.db.FavoriteContract.FavColumns.Companion.AVATAR
import com.example.gittalk.db.FavoriteContract.FavColumns.Companion.NAME
import com.example.gittalk.db.FavoriteContract.FavColumns.Companion.TABLE_NAME
import com.example.gittalk.db.FavoriteContract.FavColumns.Companion._ID
import kotlin.jvm.Throws

class FavoriteHelper(context: Context) {

    private val databaseHelper: DatabaseHelper = DatabaseHelper(context)
    private lateinit var database: SQLiteDatabase

    companion object{
        private const val DATABASE_TABLE = TABLE_NAME
        private var INSTANCE: FavoriteHelper? = null

        fun getInstance(context: Context): FavoriteHelper =
            INSTANCE ?: synchronized(this){
                INSTANCE ?: FavoriteHelper(context)
            }
    }

    @Throws(SQLException::class)
    fun open(){
        database = databaseHelper.writableDatabase
    }

    fun close(){
        databaseHelper.close()

        if(database.isOpen)
            database.close()
    }

    fun getAllData(): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            null,
            null,
            null,
            null,
            "$_ID ASC",
            null
        )
    }


    fun getDataById(id: String): ArrayList<FavoriteModel> {
        val cursor = database.query(TABLE_NAME, null, "$_ID LIKE ?", arrayOf(id), null, null, "$_ID ASC", null)
        cursor.moveToFirst()
        val arrayList = ArrayList<FavoriteModel>()
        var favoriteModel: FavoriteModel
        if (cursor.count > 0) {
            do {
                favoriteModel = FavoriteModel()
                favoriteModel.id = cursor.getInt(cursor.getColumnIndexOrThrow(_ID))
                favoriteModel.name = cursor.getString(cursor.getColumnIndexOrThrow(NAME))
                favoriteModel.avatar = cursor.getString(cursor.getColumnIndexOrThrow(AVATAR))

                arrayList.add(favoriteModel)
                cursor.moveToNext()

            } while (!cursor.isAfterLast)
        }
        cursor.close()
        return arrayList
    }

    fun queryById (id:String): Cursor{
        return database.query(DATABASE_TABLE,null,"$_ID = ?", arrayOf(id), null,null,"$_ID ASC",null)
    }

    fun insert(values: ContentValues?): Long{
        return database.insert(DATABASE_TABLE, null, values)
    }


    fun deleteById(id: String): Int{
        return database.delete(DATABASE_TABLE, "$_ID='$id'",null)
    }
}