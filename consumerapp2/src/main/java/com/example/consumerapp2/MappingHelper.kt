package com.example.consumerapp2

import android.database.Cursor
import com.example.consumerapp2.FavoriteContract.FavColumns.Companion.AVATAR
import com.example.consumerapp2.FavoriteContract.FavColumns.Companion.NAME
import com.example.consumerapp2.FavoriteContract.FavColumns.Companion.REPOSITORY
import com.example.consumerapp2.FavoriteContract.FavColumns.Companion._ID


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
}