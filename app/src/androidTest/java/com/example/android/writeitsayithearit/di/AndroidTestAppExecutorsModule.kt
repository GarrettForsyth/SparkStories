package com.example.android.writeitsayithearit.di

import com.example.android.writeitsayithearit.AppExecutors
import com.example.android.writeitsayithearit.test.CountingAppExecutors
import com.example.android.writeitsayithearit.util.CountingAppExecutorsRule
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
