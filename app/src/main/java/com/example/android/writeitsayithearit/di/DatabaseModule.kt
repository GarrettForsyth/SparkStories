package com.example.android.writeitsayithearit.di

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.android.writeitsayithearit.AppExecutors
import com.example.android.writeitsayithearit.data.WriteItSayItHearItDatabase
import com.example.android.writeitsayithearit.test.data.DatabaseSeed
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    lateinit var database: WriteItSayItHearItDatabase

    @Singleton
    @Provides
    fun provideDb(
        app: Application,
        appExecutors: AppExecutors,
        dbSeed: DatabaseSeed
    ): WriteItSayItHearItDatabase {
        database = Room
                .databaseBuilder(
                        app,
                        WriteItSayItHearItDatabase::class.java,
                        "writeitsayithearit.db"
                )
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        appExecutors.diskIO().execute {
                            database.authorDao().insert(dbSeed.SEED_AUTHORS)
                            database.cueDao().insert(dbSeed.SEED_CUES)
                            database.storyDao().insert(dbSeed.SEED_STORIES)
                        }
                    }
                })
                .fallbackToDestructiveMigration() //todo pick a nondestructive migration
                .build()
        return database
    }

    @Provides
    @Singleton
    fun provideDatabaseSeed(application: Application): DatabaseSeed {
        return DatabaseSeed(application)
    }
}