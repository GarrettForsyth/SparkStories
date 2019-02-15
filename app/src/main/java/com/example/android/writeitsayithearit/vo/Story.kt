package com.example.android.writeitsayithearit.vo

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = StoryContract.TABLE_NAME)
data class Story (
        @NonNull
        @ColumnInfo(name = StoryContract.COLUMN_TEXT)
        val text: String,

        @NonNull
        @ColumnInfo(name = StoryContract.COLUMN_CUE_ID)
        val cueId: Int,

        @NonNull
        @ColumnInfo(name = StoryContract.COLUMN_CREATION_DATE)
        val creationDate: Long,

        @NonNull
        @ColumnInfo(name = StoryContract.COLUMN_RATING)
        val rating: Int,

        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = StoryContract.COLUMN_ID)
        val id: Int = 0
) {
    override fun equals(other: Any?): Boolean {
        return (other is Story)
                && this.text.equals(other.text)
                && this.cueId == other.cueId
    }
}