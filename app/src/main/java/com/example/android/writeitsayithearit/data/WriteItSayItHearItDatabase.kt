package com.example.android.writeitsayithearit.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.android.writeitsayithearit.vo.Cue
import com.example.android.writeitsayithearit.vo.Story

@Database(
        entities = [
            Cue::class,
            Story::class
        ],
        version = 2,
        exportSchema = false
)
abstract class WriteItSayItHearItDatabase : RoomDatabase() {

    abstract fun cueDao(): CueDao

    abstract fun storyDao(): StoryDao
}