package com.example.android.sparkstories.di

import com.example.android.sparkstories.auth.Authenticator
import dagger.Module
import dagger.Provides
import io.mockk.mockk
import javax.inject.Singleton

@Module
class AndroidTestAuthenticationModule {

    @Singleton
    @Provides
    fun provideAuthenticator(): Authenticator = mockk(relaxed = true)

}

