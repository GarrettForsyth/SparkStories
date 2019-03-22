package com.example.android.sparkstories.di

import com.example.android.sparkstories.AppExecutors
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
