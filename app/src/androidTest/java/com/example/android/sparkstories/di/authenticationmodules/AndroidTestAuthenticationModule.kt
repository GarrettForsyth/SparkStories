package com.example.android.sparkstories.di.authenticationmodules

import com.example.android.sparkstories.auth.Authenticator
import dagger.Module
import dagger.Provides
import io.mockk.every
import io.mockk.mockk
import javax.inject.Singleton

@Module
class AndroidTestAuthenticationModule {

    @Singleton
    @Provides
    fun provideAuthenticator(): Authenticator {
        val mockedAuthenticator: Authenticator = mockk(relaxed = true)
        every { mockedAuthenticator.isNotAuthenticated() } returns false
        return mockedAuthenticator
    }

}

