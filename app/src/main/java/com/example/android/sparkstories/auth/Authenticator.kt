package com.example.android.sparkstories.auth

import android.app.Activity

interface Authenticator {

    fun authenticateUser(activity: Activity)
    fun getUserId(): String

}