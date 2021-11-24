package com.ys.basicandroid.domain.model

import android.os.Parcelable
import androidx.databinding.ObservableBoolean
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BookInfo(
    val authors: List<String>,
    val translators: List<String>,
    val contents: String,
    val datetime: String,
    val price: Int,
    val publisher: String,
    val sale_price: Int,
    val status: String,
    val thumbnail: String,
    val title: String,
    val url: String,
    val bookLike: Boolean = false
): Parcelable {
    val isLike by lazy { ObservableBoolean(bookLike) }
}