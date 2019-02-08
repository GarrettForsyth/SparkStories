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
import javax.inject.Singleton

@Module
class DatabaseModule {

    lateinit var database: WriteItSayItHearItDatabase

    @Singleton
    @Provides
    fun provideDb(app: Application, appExecutors: AppExecutors): WriteItSayItHearItDatabase {
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
                            database.cueDao().insert(TestUtils.listOfStartingCues)
                        }
                    }
                })
                .fallbackToDestructiveMigration() //todo pick a nondestructive migration
                .build()
        return database
    }
}