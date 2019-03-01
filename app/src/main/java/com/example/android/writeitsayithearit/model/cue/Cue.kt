package com.example.android.writeitsayithearit.model.cue

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.android.writeitsayithearit.model.author.Author
import com.example.android.writeitsayithearit.model.author.AuthorContract
import java.util.*

@Entity(
    tableName = CueContract.TABLE_NAME,
    foreignKeys = [ForeignKey(
        entity = Author::class,
        parentColumns = [AuthorContract.COLUMN_NAME],
        childColumns = [CueContract.COLUMN_AUTHOR]
    )]
)
data class Cue(
    @NonNull
    @ColumnInfo(name = CueContract.COLUMN_TEXT)
    var text: String,

    @NonNull
    @ColumnInfo(name = CueContract.COLUMN_AUTHOR)
    var author: String,

    @NonNull
    @ColumnInfo(name = CueContract.COLUMN_CREATION_DATE)
    var creationDate: Long,

    @NonNull
    @ColumnInfo(name = CueContract.COLUMN_RATING)
    var rating: Int,

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = CueContract.COLUMN_ID)
    var id: Int = 0
) {

    constructor(text: String, author: String) : this(
        text,
        author,
        Calendar.getInstance().timeInMillis,
        0,
        0
    )

    override fun equals(other: Any?): Boolean {
        return (other is Cue)
                && this.text.equals(other.text)
                && this.author.equals(other.author)
                && this.rating == other.rating
    }
}


