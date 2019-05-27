package com.example.android.sparkstories.model.story

import android.text.format.DateFormat
import androidx.annotation.NonNull
import androidx.recyclerview.widget.DiffUtil
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.android.sparkstories.model.Datable
import com.example.android.sparkstories.model.author.Author
import com.example.android.sparkstories.model.author.AuthorContract
import com.example.android.sparkstories.model.cue.Cue
import com.example.android.sparkstories.model.cue.CueContract
import com.google.firebase.firestore.PropertyName
import com.google.gson.annotations.SerializedName
import java.util.*


@Entity(tableName = StoryContract.TABLE_NAME)
data class Story(
    @NonNull
    @ColumnInfo(name = StoryContract.COLUMN_TEXT)
    @SerializedName("text")
    @get:PropertyName("text") @set:PropertyName("text") var text: String,

    @NonNull
    @ColumnInfo(name = StoryContract.COLUMN_AUTHOR)
    @get:PropertyName("author") @set:PropertyName("author") var author: String,

    @NonNull
    @ColumnInfo(name = StoryContract.COLUMN_CUE_ID)
    @get:PropertyName("cue_id") @set:PropertyName("cue_id") var cueId: String = "",

    @NonNull
    @ColumnInfo(name = StoryContract.COLUMN_CREATION_DATE)
    @get:PropertyName("creation_date") @set:PropertyName("creation_date") var creationDate: Long,

    @NonNull
    @ColumnInfo(name = StoryContract.COLUMN_RATING)
    @get:PropertyName("rating") @set:PropertyName("rating") var rating: Int,

    @PrimaryKey
    @ColumnInfo(name = StoryContract.COLUMN_ID)
    @get:PropertyName("id") @set:PropertyName("id") var id: String = UUID.randomUUID().toString()


) : Datable {

    companion object {
        val storyDiffCallback = object : DiffUtil.ItemCallback<Story>() {
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

    // no arg constructor needed by firestore to deserialize
    constructor(): this("", "", "")

    constructor(storyText: String, author: String, cueId: String) :
            this(
                storyText,
                author,
                cueId,
                Calendar.getInstance().timeInMillis,
                0,
                UUID.randomUUID().toString()
            )

    override fun equals(other: Any?): Boolean {
        return (other is Story)
                && this.text.equals(other.text)
                && this.author.equals(other.author)
                && this.cueId == other.cueId
                && this.rating == other.rating
    }

}