package com.example.gittalk.db

import android.net.Uri
import android.provider.BaseColumns

object FavoriteContract {

    const val AUTHORITY = "com.example.gittalk"
    const val SCHEME = "content"
    class FavColumns: BaseColumns{
        companion object{
            const val TABLE_NAME = "favorite"
            const val _ID = "_id"
            const val NAME = "name"
            const val AVATAR = "avatar"
            const val REPOSITORY = "repository"

        val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
            .authority(AUTHORITY)
            .appendPath(TABLE_NAME)
            .build()
        }
    }
}