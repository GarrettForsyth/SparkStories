package com.example.android.writeitsayithearit.di

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.android.writeitsayithearit.AppExecutors
import com.example.android.writeitsayithearit.data.WriteItSayItHearItDatabase
import com.example.android.writeitsayithearit.test.TestUtils
import com.example.android.writeitsayithearit.test.TestUtils.STARTING_AUTHORS
import com.example.android.writeitsayithearit.test.TestUtils.STARTING_CUES
import com.example.android.writeitsayithearit.test.TestUtils.STARTING_STORIES
import dagger.Module
import dagger.Provides
import timber.log.Timber

@Module
class AndroidTestDatabaseModule {

    lateinit var database: WriteItSayItHearItDatabase

    /**
     * Override db with an in memory db for testing.
     */
    @Provides
    fun provideDb(app: Application, appExecutors: AppExecutors): WriteItSayItHearItDatabase {

        database = Room.inMemoryDatabaseBuilder(app, WriteItSayItHearItDatabase::class.java)
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        appExecutors.diskIO().execute {
                            Timber.d("--> populating database")
                            database.authorDao().insert(STARTING_AUTHORS)
                            database.cueDao().insert(STARTING_CUES)
                            database.storyDao().insert(STARTING_STORIES)
                        }
                    }
                })
                .build()
        return database
    }

}