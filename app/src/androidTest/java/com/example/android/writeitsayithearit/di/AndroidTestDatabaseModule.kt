package com.example.android.writeitsayithearit.di

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.android.writeitsayithearit.AppExecutors
import com.example.android.writeitsayithearit.data.WriteItSayItHearItDatabase
import com.example.android.writeitsayithearit.test.TestUtils
import dagger.Module
import dagger.Provides

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
                            database.storyDao().insert(TestUtils.listOfStartingStories)
                            database.cueDao().insert(TestUtils.listOfStartingCues)
                        }
                    }
                })
                .build()
        return database
    }

}