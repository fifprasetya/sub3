package com.example.gittalk.helper

import android.database.Cursor
import com.example.gittalk.FavoriteModel
import com.example.gittalk.db.FavoriteContract.FavColumns.Companion.AVATAR
import com.example.gittalk.db.FavoriteContract.FavColumns.Companion.NAME
import com.example.gittalk.db.FavoriteContract.FavColumns.Companion.REPOSITORY
import com.example.gittalk.db.FavoriteContract.FavColumns.Companion._ID

object  MappingHelper {

    fun mapCursorToArrayList(favoriteCursor: Cursor?): ArrayList<FavoriteModel>{
        val favoriteList = ArrayList<FavoriteModel>()

        favoriteCursor?.apply{
            while(moveToNext()){
                val id = getInt(getColumnIndexOrThrow(_ID))
                val name = getString(getColumnIndexOrThrow(NAME))
                val avatar = getString(getColumnIndexOrThrow(AVATAR))
                val repository = getString(getColumnIndexOrThrow(REPOSITORY))
                favoriteList.add(FavoriteModel(id,name,avatar,repository))
            }
        }

        return favoriteList
    }

    fun mapCursorToObject(notesCursor: Cursor?): FavoriteModel {
        var favorite = FavoriteModel()
        notesCursor?.apply {
            moveToFirst()
            val id = getInt(getColumnIndexOrThrow(_ID))
            val name = getString(getColumnIndexOrThrow(NAME))
            val avatar = getString(getColumnIndexOrThrow(AVATAR))
            val repository = getString(getColumnIndexOrThrow(REPOSITORY))
            favorite = FavoriteModel(id,name,avatar,repository)
        }
        return favorite
    }
}