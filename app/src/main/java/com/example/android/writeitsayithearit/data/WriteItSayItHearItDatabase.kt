package com.example.android.writeitsayithearit.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.android.writeitsayithearit.vo.Cue

@Database(
        entities = [
                Cue::class
        ],
        version = 1,
        exportSchema = false
)
abstract class WriteItSayItHearItDatabase : RoomDatabase() {

    abstract fun cueDao(): CueDao
}