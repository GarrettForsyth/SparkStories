package com.example.android.sparkstories.di

import com.example.android.sparkstories.AppExecutors
import com.example.android.sparkstories.test.CountingAppExecutors
import com.example.android.sparkstories.util.CountingAppExecutorsRule
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AndroidTestAppExecutorsModule {
    @Singleton
    @Provides
    fun provideAppExecutors(): AppExecutors {
        return CountingAppExecutorsRule().appExecutors
    }
}
