package com.example.android.writeitsayithearit.model.story

import android.text.format.DateFormat
import androidx.annotation.NonNull
import androidx.recyclerview.widget.DiffUtil
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.android.writeitsayithearit.model.Datable
import com.example.android.writeitsayithearit.model.author.Author
import com.example.android.writeitsayithearit.model.author.AuthorContract
import com.example.android.writeitsayithearit.model.cue.Cue
import com.example.android.writeitsayithearit.model.cue.CueContract
import java.util.*

@Entity(
    tableName = StoryContract.TABLE_NAME
    // TODO: Inserting a story throws an error indicating
    // that the author (foreign key)
    // isn't in the database. Similar setup
    // for cues works fine. Investigate later.
    // Does it even make sense to have this constraint in local
    // storage?
//    foreignKeys = [ForeignKey(
//        entity = Author::class,
//        parentColumns = [AuthorContract.COLUMN_NAME],
//        childColumns = [StoryContract.COLUMN_AUTHOR]
//    )]
)
data class Story(
    @NonNull
    @ColumnInfo(name = StoryContract.COLUMN_TEXT)
    var text: String,

    @NonNull
    @ColumnInfo(name = StoryContract.COLUMN_AUTHOR)
    var author: String,

    @NonNull
    @ColumnInfo(name = StoryContract.COLUMN_CUE_ID)
    var cueId: Int,

    @NonNull
    @ColumnInfo(name = StoryContract.COLUMN_CREATION_DATE)
    var creationDate: Long,

    @NonNull
    @ColumnInfo(name = StoryContract.COLUMN_RATING)
    var rating: Int,

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = StoryContract.COLUMN_ID)
    var id: Int = 0
): Datable {

    companion object { val storyDiffCallback = object : DiffUtil.ItemCallback<Story>(){
        override fun areItemsTheSame(oldItem: Story, newItem: Story) = (oldItem.id == newItem.id)
        override fun areContentsTheSame(oldItem: Story, newItem: Story) = (oldItem == newItem)
        }
    }

    override fun formattedDateTime(): String {
        //TODO: format date based on locale
        return DateFormat.format("hh:mm    dd/MM/yy", creationDate).toString()
    }

    override fun formattedTime(): String {
        return DateFormat.format("hh:mm", creationDate).toString()
    }

    override fun formattedDate(): String {
        return DateFormat.format("dd/MM/yy", creationDate).toString()
    }


    constructor(storyText: String, author: String, cueId: Int) :
            this(
                storyText,
                author,
                cueId,
                Calendar.getInstance().timeInMillis,
                0
            )

    override fun equals(other: Any?): Boolean {
        return (other is Story)
                && this.text.equals(other.text)
                && this.author.equals(other.author)
                && this.cueId == other.cueId
                && this.rating == other.rating
    }

}