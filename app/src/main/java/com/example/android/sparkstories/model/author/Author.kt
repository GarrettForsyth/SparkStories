package com.example.android.sparkstories.model.author

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = AuthorContract.TABLE_NAME)
data class Author(
    @PrimaryKey
    @ColumnInfo(name = AuthorContract.COLUMN_NAME)
    val name: String
) {

    override fun equals(other: Any?): Boolean {
        return (other is Author)
                && this.name.equals(other.name)
    }
}