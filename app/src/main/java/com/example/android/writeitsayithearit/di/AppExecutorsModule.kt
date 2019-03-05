package com.example.android.writeitsayithearit.di

import com.example.android.writeitsayithearit.AppExecutors
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppExecutorsModule {
    @Singleton
    @Provides
    fun provideAppExecutors(): AppExecutors {
        return AppExecutors()
    }
}
