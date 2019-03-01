package com.example.android.writeitsayithearit.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.android.writeitsayithearit.model.author.Author
import com.example.android.writeitsayithearit.model.cue.Cue
import com.example.android.writeitsayithearit.model.story.Story

@Database(
        entities = [
            Cue::class,
            Story::class,
            Author::class
        ],
        version = 7,
        exportSchema = false
)
abstract class WriteItSayItHearItDatabase : RoomDatabase() {

    abstract fun cueDao(): CueDao

    abstract fun storyDao(): StoryDao

    abstract fun authorDao(): AuthorDao
}