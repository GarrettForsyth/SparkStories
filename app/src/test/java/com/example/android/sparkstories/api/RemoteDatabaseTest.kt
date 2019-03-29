package com.example.android.sparkstories.api

import androidx.test.filters.SmallTest
import com.example.android.sparkstories.auth.Authenticator
import com.example.android.sparkstories.data.remote.RemoteDatabaseImpl
import io.mockk.mockk
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

//@SmallTest
//@RunWith(JUnit4::class)
//class RemoteDatabaseTest {
//
//    private val authenticator: Authenticator = mockk(relaxed = true)
//    private val remoteDatabase = RemoteDatabaseImpl(authenticator)
//
//}