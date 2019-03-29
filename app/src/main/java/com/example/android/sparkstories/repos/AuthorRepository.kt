package com.example.android.sparkstories.repos

import androidx.lifecycle.LiveData
import com.example.android.sparkstories.AppExecutors
import com.example.android.sparkstories.api.ApiResponse
import com.example.android.sparkstories.auth.Authenticator
import com.example.android.sparkstories.auth.AuthenticatorImpl
import com.example.android.sparkstories.data.local.AuthorDao
import com.example.android.sparkstories.data.remote.RemoteDatabase
import com.example.android.sparkstories.model.Resource
import com.example.android.sparkstories.model.author.Author
import com.example.android.sparkstories.repos.utils.NetworkBoundResource
import javax.inject.Inject

class AuthorRepository @Inject constructor(
    private val appExecutors: AppExecutors,
    private val authorDao: AuthorDao,
    private val remoteDatabase: RemoteDatabase
) {

    fun checkUserExists(): LiveData<Resource<Boolean>> = remoteDatabase.checkUserExists()

    fun submitScreenName(screenName: String): LiveData<Resource<Unit>> = remoteDatabase.submitScreenName(screenName)

    fun checkScreenNameAvailable(screenName: String): LiveData<Resource<Pair<Boolean, String>>> {
        return remoteDatabase.checkScreenNameAvailable(screenName)
    }
}
