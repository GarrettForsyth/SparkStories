package com.example.android.sparkstories.model.cue

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
import com.google.firebase.firestore.PropertyName
import com.google.gson.annotations.SerializedName
import timber.log.Timber
import java.util.*

@Entity(tableName = CueContract.TABLE_NAME)
data class Cue(
    @NonNull
    @ColumnInfo(name = CueContract.COLUMN_TEXT)
    @SerializedName("text")
    @get:PropertyName("text") @set:PropertyName("text") var text: String,

    @NonNull
    @ColumnInfo(name = CueContract.COLUMN_AUTHOR)
    @get:PropertyName("author") @set:PropertyName("author") var author: String,

    @NonNull
    @ColumnInfo(name = CueContract.COLUMN_CREATION_DATE)
    @get:PropertyName("creation_date") @set:PropertyName("creation_date") var creationDate: Long,

    @NonNull
    @ColumnInfo(name = CueContract.COLUMN_RATING)
    @get:PropertyName("rating") @set:PropertyName("rating") var rating: Int,

    @PrimaryKey
    @ColumnInfo(name = CueContract.COLUMN_ID)
    @get:PropertyName("id") @set:PropertyName("id") var id: String = UUID.randomUUID().toString()
): Datable {

    companion object { val cueDiffCallback = object :DiffUtil.ItemCallback<Cue>(){
            override fun areItemsTheSame(oldItem: Cue, newItem: Cue) = (oldItem.id == newItem.id)
            override fun areContentsTheSame(oldItem: Cue, newItem: Cue) = (oldItem == newItem)
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

    constructor(text: String, author: String) : this(
        text,
        author,
        Calendar.getInstance().timeInMillis,
        0,
        UUID.randomUUID().toString()
    )
    constructor(): this("","")

    //TODO add hash
    override fun equals(other: Any?): Boolean {
        return (other is Cue)
                && this.text.equals(other.text)
                && this.author.equals(other.author)
                && this.rating == other.rating
    }

}
