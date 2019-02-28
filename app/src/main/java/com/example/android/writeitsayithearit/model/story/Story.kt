package com.example.android.writeitsayithearit.model.story

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = StoryContract.TABLE_NAME)
data class Story(
    @NonNull
    @ColumnInfo(name = StoryContract.COLUMN_TEXT)
    var text: String,

    @NonNull
    @ColumnInfo(name = StoryContract.COLUMN_CUE_ID)
    val cueId: Int,

    @NonNull
    @ColumnInfo(name = StoryContract.COLUMN_CREATION_DATE)
    val creationDate: Long,

    @NonNull
    @ColumnInfo(name = StoryContract.COLUMN_RATING)
    var rating: Int,

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = StoryContract.COLUMN_ID)
    val id: Int = 0
) {

    constructor(storyText: String, cueId: Int) :
            this(
                storyText,
                cueId,
                Calendar.getInstance().timeInMillis,
                0
            )

    override fun equals(other: Any?): Boolean {
        return (other is Story)
                && this.text.equals(other.text)
                && this.cueId == other.cueId
                && this.rating == other.rating
    }

}