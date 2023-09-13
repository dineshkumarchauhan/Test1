package com.example.test1.models

import android.net.Uri
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ItemPhoto(
    val uri: Uri ?= null,
    val title: String = "",
    val date: String = "",
    val photoResult: List<ItemPhoto> = ArrayList(),
): Parcelable