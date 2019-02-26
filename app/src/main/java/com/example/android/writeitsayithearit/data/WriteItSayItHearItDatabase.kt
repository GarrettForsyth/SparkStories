package com.example.android.writeitsayithearit.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.android.writeitsayithearit.model.cue.Cue
import com.example.android.writeitsayithearit.model.story.Story

@Database(
        entities = [
            Cue::class,
            Story::class
        ],
        version = 5,
        exportSchema = false
)
abstract class WriteItSayItHearItDatabase : RoomDatabase() {

    abstract fun cueDao(): CueDao

    abstract fun storyDao(): StoryDao
}