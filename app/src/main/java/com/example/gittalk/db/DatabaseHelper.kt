package com.example.gittalk.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.gittalk.db.FavoriteContract.FavColumns.Companion.AVATAR
import com.example.gittalk.db.FavoriteContract.FavColumns.Companion.NAME
import com.example.gittalk.db.FavoriteContract.FavColumns.Companion.REPOSITORY
import com.example.gittalk.db.FavoriteContract.FavColumns.Companion.TABLE_NAME
import com.example.gittalk.db.FavoriteContract.FavColumns.Companion._ID

internal class DatabaseHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION ) {

    companion object{
        private const val DATABASE_NAME = "dbfavorite"

        private const val DATABASE_VERSION = 1

        private val CREATE_TABLE_FAVORITE = "create table $TABLE_NAME ($_ID text not null, $NAME text not null, $AVATAR text not null, $REPOSITORY text not null);"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE_FAVORITE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}