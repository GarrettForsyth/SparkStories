package com.example.android.writeitsayithearit.di

import com.example.android.writeitsayithearit.AppExecutors
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes =  [ ViewModelModule::class ])
class AppModule {

    @Singleton
    @Provides
    fun provideAppExecutors() : AppExecutors {
        return AppExecutors()
    }

    @Singleton
    @Provides
    fun provideHello() : String {
        return "Hello injection!"
    }

}