package com.example.gittalk

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FavoriteModel (
    var id: Int = 0,
    var name: String? = null,
    var avatar: String? = null,
    var repository: String?= null
): Parcelable