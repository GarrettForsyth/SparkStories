package com.example.android.sparkstories.di

import com.example.android.sparkstories.auth.Authenticator
import com.example.android.sparkstories.data.remote.RemoteDatabase
import com.example.android.sparkstories.data.remote.RemoteDatabaseImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RemoteDatabaseModule {

    @Singleton
    @Provides
    fun provideRemoteDatabase(authenticator: Authenticator
    ): RemoteDatabase = RemoteDatabaseImpl(authenticator)


}
