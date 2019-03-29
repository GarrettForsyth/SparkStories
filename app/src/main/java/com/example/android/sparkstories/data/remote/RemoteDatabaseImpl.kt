package com.example.android.sparkstories.data.remote

import android.provider.Settings.Global.getString
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.android.sparkstories.R
import com.example.android.sparkstories.api.ApiResponse
import com.example.android.sparkstories.api.LiveDataTaskAdapter
import com.example.android.sparkstories.auth.Authenticator
import com.example.android.sparkstories.model.Resource
import com.google.firebase.firestore.FirebaseFirestore
import timber.log.Timber
import javax.inject.Inject

class RemoteDatabaseImpl @Inject constructor(
    private val authenticator: Authenticator
) : RemoteDatabase {

    private companion object {
        private const val COLLECTION_USERS = "users"
        private const val FIELD_SCREEN_NAME = "screen_name"
    }

    private val db = FirebaseFirestore.getInstance()

    override fun checkUserExists(): LiveData<Resource<Boolean>> {
        val response = MutableLiveData<Resource<Boolean>>()

        db.collection(COLLECTION_USERS)
            .document(authenticator.getUserId())
            .get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    Timber.d("mytest  Response: username already exists.")
                    response.value = Resource.success(true)
                } else {
                    Timber.d("mytest Response: username does not exist.")
                    response.value = Resource.success(false)
                }
            }
            .addOnFailureListener() {
                Timber.e("mytest Response: error fetching username.")
                response.value = Resource.error("Unknown network error", null)
            }
        return response
    }

    override fun submitScreenName(screenName: String): LiveData<Resource<Unit>> {
        val response = MutableLiveData<Resource<Unit>>()
        val data = HashMap<String, Any>()
        data[FIELD_SCREEN_NAME] = screenName
        val task = db.collection(COLLECTION_USERS).document(authenticator.getUserId())
            .set(data)
            .addOnSuccessListener { response.postValue(Resource.success(null)) }
            .addOnFailureListener { response.postValue(Resource.error("Unable to submit username.", null)) }
        return response
    }

    override fun checkScreenNameAvailable(screenName: String): LiveData<Resource<Pair<Boolean, String>>> {
        val response = MutableLiveData<Resource<Pair<Boolean,String>>>()

        db.collection(COLLECTION_USERS)
            .whereEqualTo(FIELD_SCREEN_NAME, screenName)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (querySnapshot.isEmpty) {
                    Timber.d("mytest  Response: username is available.")
                    response.value = Resource.success(Pair(true, screenName))
                } else {
                    Timber.d("mytest Response: username is not available")
                    response.value = Resource.success(Pair(false, screenName))
                }
            }
            .addOnFailureListener() {
                Timber.e("mytest Response: error checking availability.")
                response.value = Resource.error("Unknown network error", null)
            }
        return response
    }
}

