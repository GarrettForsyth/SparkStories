package com.example.android.sparkstories.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.android.sparkstories.model.author.Author
import com.example.android.sparkstories.model.comment.Comment
import com.example.android.sparkstories.model.cue.Cue
import com.example.android.sparkstories.model.story.Story

@Database(
        entities = [
            Cue::class,
            Story::class,
            Author::class,
            Comment::class
        ],
        version = 14,
        exportSchema = false
)
abstract class WriteItSayItHearItDatabase : RoomDatabase() {

    abstract fun cueDao(): CueDao

    abstract fun storyDao(): StoryDao

    abstract fun authorDao(): AuthorDao

    abstract fun commentDao(): CommentDao
}