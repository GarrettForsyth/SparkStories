package com.example.android.sparkstories.model.comment

import android.text.format.DateFormat
import androidx.annotation.NonNull
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import androidx.recyclerview.widget.DiffUtil
import androidx.room.*
import com.example.android.sparkstories.model.Datable
import com.example.android.sparkstories.model.author.Author
import com.example.android.sparkstories.model.author.AuthorContract
import com.example.android.sparkstories.model.story.Story
import com.example.android.sparkstories.model.story.StoryContract
import java.util.*

@Entity(tableName = CommentContract.TABLE_NAME)
data class Comment(
    @NonNull
    @ColumnInfo(name = CommentContract.COLUMN_TEXT)
    var text: String,

    @NonNull
    @ColumnInfo(name = CommentContract.COLUMN_AUTHOR)
    var author: String,

    @NonNull
    @ColumnInfo(name = CommentContract.COLUMN_STORY_ID)
    var storyId: String,

    @NonNull
    @ColumnInfo(name = CommentContract.COLUMN_PARENT_ID)
    var parentId: Int,

    @NonNull
    @ColumnInfo(name = CommentContract.COLUMN_DEPTH)
    var depth: Int,

    @NonNull
    @ColumnInfo(name = CommentContract.COLUMN_CREATION_DATE)
    var creationDate: Long,

    @NonNull
    @ColumnInfo(name = CommentContract.COLUMN_RATING)
    var rating: Int,

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = CommentContract.COLUMN_ID)
    var id: Int = 0
) : Datable {

    companion object {
        val commentDiffCallback = object : DiffUtil.ItemCallback<Comment>() {
            override fun areItemsTheSame(oldItem: Comment, newItem: Comment) = (oldItem.id == newItem.id)
            override fun areContentsTheSame(oldItem: Comment, newItem: Comment) = (oldItem == newItem)
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

    constructor(text: String, author: String, depth: Int = 0, storyId: String = UUID.randomUUID().toString(), parentId: Int = -1) : this(
        text,
        author,
        storyId,
        parentId,
        depth,
        Calendar.getInstance().timeInMillis,
        0,
        0
    )

    //TODO add hash
    override fun equals(other: Any?): Boolean {
        return (other is Comment)
                && this.text.equals(other.text)
                && this.author.equals(other.author)
                && this.storyId.equals(other.storyId)
                && this.parentId.equals(other.parentId)
                && this.depth.equals(other.depth)
                && this.rating == other.rating
    }

}

