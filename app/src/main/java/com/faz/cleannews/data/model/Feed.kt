package com.faz.cleannews.data.model

import android.os.Parcel
import android.os.Parcelable
import com.faz.cleannews.utils.getRandomNumber

data class Feed(
    @Transient
    var id: Long = 0,
    var status: String? = null,
    var totalResults: Long = 0,
    var articles: List<Article>? = null
) : Parcelable {

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeLong(id)
        dest.writeString(status)
        dest.writeLong(totalResults)
        dest.writeTypedList(articles)
    }

    constructor(parcel: Parcel) : this() {
        id = getRandomNumber()
        status = parcel.readString()
        totalResults = parcel.readLong()
        articles = parcel.createTypedArrayList(Article)
    }

    companion object CREATOR : Parcelable.Creator<Feed> {
        override fun createFromParcel(parcel: Parcel): Feed {
            return Feed(parcel)
        }

        override fun newArray(size: Int): Array<Feed?> {
            return arrayOfNulls(size)
        }
    }
}