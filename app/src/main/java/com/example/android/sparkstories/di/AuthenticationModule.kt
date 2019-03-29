package com.example.android.sparkstories.di

import com.example.android.sparkstories.auth.Authenticator
import com.example.android.sparkstories.auth.AuthenticatorImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AuthenticationModule {

    @Singleton
    @Provides
    fun provideAuthenticator() : Authenticator = AuthenticatorImpl()


}
