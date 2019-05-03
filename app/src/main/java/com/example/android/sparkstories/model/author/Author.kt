package com.example.android.sparkstories.model.author

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.PropertyName
import com.google.gson.annotations.SerializedName

@Entity(tableName = AuthorContract.TABLE_NAME)
data class Author(
    @PrimaryKey
    @ColumnInfo(name = AuthorContract.COLUMN_NAME)
    @get:PropertyName("screen_name") @set:PropertyName("screen_name") var name: String
) {
    constructor(): this(BLANK_NAME)

    override fun equals(other: Any?): Boolean {
        return (other is Author)
                && this.name.equals(other.name)
    }

    companion object {
        const val BLANK_NAME = ""
    }
}