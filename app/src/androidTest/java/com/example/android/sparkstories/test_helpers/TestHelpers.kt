package com.example.android.sparkstories.test_helpers

import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import io.mockk.every
import io.mockk.mockk


object TestHelper {

    private const val DEFAULT_APP_NAME = "[DEFAULT]"

    var MOCK_APP: FirebaseApp

    init {
        val app: FirebaseApp = mockk(relaxed = true)
        val auth: FirebaseAuth = mockk(relaxed = true)
        every { app.get(FirebaseAuth::class.java) } returns auth
        every { app.getName() } returns DEFAULT_APP_NAME
        MOCK_APP = app
    }
}

