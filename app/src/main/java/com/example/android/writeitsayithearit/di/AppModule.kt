package com.example.android.writeitsayithearit.di

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.android.writeitsayithearit.AppExecutors
import com.example.android.writeitsayithearit.WriteItSayItHearItApp
import com.example.android.writeitsayithearit.api.WriteItSayItHearItService
import com.example.android.writeitsayithearit.data.CueDao
import com.example.android.writeitsayithearit.data.WriteItSayItHearItDatabase
import com.example.android.writeitsayithearit.test.TestUtils
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class])
class AppModule {

    lateinit var database: WriteItSayItHearItDatabase

    @Singleton
    @Provides
    fun provideAppExecutors(): AppExecutors {
        return AppExecutors()
    }

    @Singleton
    @Provides
    fun provideService() : WriteItSayItHearItService {
        return WriteItSayItHearItService()
    }

    @Singleton
    @Provides
    fun provideCueDao(db: WriteItSayItHearItDatabase): CueDao {
        return db.cueDao()
    }

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