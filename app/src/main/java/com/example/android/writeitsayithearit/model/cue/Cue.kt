package com.example.android.writeitsayithearit.model.cue

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = CueContract.TABLE_NAME)
data class Cue(
        @NonNull
        @ColumnInfo(name = CueContract.COLUMN_TEXT)
        val text: String,

        @NonNull
        @ColumnInfo(name = CueContract.COLUMN_CREATION_DATE)
        val creationDate: Long,

        @NonNull
        @ColumnInfo(name = CueContract.COLUMN_RATING)
        val rating: Int,

        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = CueContract.COLUMN_ID)
        val id: Int = 0
) {

    constructor(text: String): this(text,
        Calendar.getInstance().timeInMillis,
         0,
        0
    )

    override fun equals(other: Any?): Boolean {
        return (other is Cue)
                && this.text.equals(other.text)
    }
}


