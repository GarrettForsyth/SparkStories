package com.example.android.writeitsayithearit.di

import android.app.Application
import androidx.room.Room
import com.example.android.writeitsayithearit.AppExecutors
import com.example.android.writeitsayithearit.data.WriteItSayItHearItDatabase
import dagger.Module
import dagger.Provides

@Module
class AndroidTestDatabaseModule {

    /**
     * Override db with an in memory db for testing.
     */
    @Provides
    fun provideDb(app: Application, appExecutors: AppExecutors): WriteItSayItHearItDatabase {
        return Room.inMemoryDatabaseBuilder(app, WriteItSayItHearItDatabase::class.java)
                .build()
    }

}