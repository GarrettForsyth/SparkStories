package com.example.android.sparkstories.api

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.android.sparkstories.auth.Authenticator
import com.example.android.sparkstories.model.Resource
import com.example.android.sparkstories.model.SortOrder
import com.example.android.sparkstories.model.author.Author
import com.example.android.sparkstories.model.cue.Cue
import com.example.android.sparkstories.ui.stories.NewStoryViewModel.Companion.PREFERENCE_AUTHOR
import com.example.android.sparkstories.ui.util.QueryParameters
import com.google.firebase.firestore.*
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class SparkStoriesServiceImpl @Inject constructor(
    private val authenticator: Authenticator,
    private val firestore: FirebaseFirestore,
    private val sharedPreferences: SharedPreferences
) : SparkStoriesService {

    companion object {
        const val COLLECTION_USERS = "users"
        const val COLLECTION_CUES = "cues"

        const val FIELD_SCREEN_NAME = "screen_name"
        const val FIELD_CREATION_DATE = "creation_date"
        const val FIELD_RATING = "rating"
        const val FIELD_UID = "uid"

        const val CUE_NETWORK_PAGE_LIMIT = 120L
    }

    private var lastCueDocumentReceived: DocumentSnapshot? = null
    private var lastQueryParameters: QueryParameters? = null

    override fun checkUserExists(): LiveData<Resource<Boolean>> {
        val response = MutableLiveData<Resource<Boolean>>()
        response.value = Resource.loading(null)

        firestore.collection(COLLECTION_USERS)
            .document(authenticator.getUserId())
            .get()
            .addOnSuccessListener { documentSnapshot ->
                response.value = Resource.success(documentSnapshot.exists())
                Timber.e("existence test: successful network response: ${documentSnapshot.data}")
            }
            .addOnFailureListener { exception ->
                response.value = Resource.error("Unknown network error", null)
                Timber.e("existence test: Network error while checking for user existence: ${exception.message}")
            }
        return response
    }

    override fun checkUserHasScreenName(): LiveData<Resource<Boolean>> {
        val response = MutableLiveData<Resource<Boolean>>()
        response.value = Resource.loading(null)

        firestore.collection(COLLECTION_USERS)
            .document(authenticator.getUserId())
            .get()
            .addOnSuccessListener { documentSnapshot ->
                // should be null if name is not set
                val user: Author? = documentSnapshot.toObject(Author::class.java)
                response.value = Resource.success(user?.name?.isNotBlank() ?: false )
                Timber.d("screenName test: successful network response: ${documentSnapshot.data}")
                Timber.d("screenName test: serialized data is $user")
                Timber.d("screenName test: set result to ${user?.name?.isNotBlank() ?: false}")
            }
            .addOnFailureListener { exception ->
                response.value = Resource.error("Unknown network error", null)
                Timber.e("screenName test: Network error while checking for user existence: ${exception.message}")
            }
        return response
    }

    override fun checkScreenNameAvailable(screenName: String): LiveData<Resource<Pair<Boolean, String>>> {
        val response = MutableLiveData<Resource<Pair<Boolean, String>>>()
        response.value = Resource.loading(null)

        firestore.collection(COLLECTION_USERS)
            .whereEqualTo(FIELD_SCREEN_NAME, screenName)
            .get()
            .addOnSuccessListener { querySnapshot ->
                response.value = Resource.success(Pair(querySnapshot.isEmpty, screenName))
            }
            .addOnFailureListener() {
                response.value = Resource.error("Unknown network error", null)
                Timber.e("Network error while checking screen name availability: ${it.message}")
            }
        return response
    }

    override fun submitScreenName(screenName: String): LiveData<Resource<Unit>> {
        val response = MutableLiveData<Resource<Unit>>()
        response.value = Resource.loading(null)

        val data = HashMap<String, Any>()
        data[FIELD_SCREEN_NAME] = screenName
        data[FIELD_UID] = authenticator.getUserId()

        firestore
            .collection(COLLECTION_USERS)
            .document(authenticator.getUserId())
            .set(data)
            .addOnSuccessListener {
                response.postValue(Resource.success(null))
                sharedPreferences.edit()
                    .putString(PREFERENCE_AUTHOR, screenName)
                    .apply()
            }
            .addOnFailureListener { response.postValue(Resource.error("Unable to submit username.", null)) }

        return response
    }

    override fun getCues(queryParameters: QueryParameters): LiveData<ApiResponse<List<Cue>>> {
        Timber.d("PaginationTest creating service call starting at $lastCueDocumentReceived")
        Timber.d("PaginationTest using query paramters: $queryParameters")
        val response = MutableLiveData<ApiResponse<List<Cue>>>()

        Timber.d("query test \n query params = $queryParameters \n  last query = $lastQueryParameters \n  is equal ${queryParameters == lastQueryParameters}")

        if (queryParameters != lastQueryParameters){
            Timber.d("PaginationTest new query parameters: resetting last document received to null.")
            lastCueDocumentReceived = null
            Timber.d("PaginationTest query test setting last query parameters to $queryParameters")
            lastQueryParameters = queryParameters.copy()
        }

        val sortOrder = if (queryParameters.sortOrder == SortOrder.TOP) {
            FIELD_RATING
        } else {
            FIELD_CREATION_DATE
        }

        val query = if (lastCueDocumentReceived == null) {
            firestore.collection(COLLECTION_CUES)
                .orderBy(sortOrder, Query.Direction.DESCENDING)
                .limit(CUE_NETWORK_PAGE_LIMIT)
        } else {
            firestore.collection(COLLECTION_CUES)
                .orderBy(sortOrder)
                .startAfter(lastCueDocumentReceived!!)
                .limit(CUE_NETWORK_PAGE_LIMIT)
        }

        if (queryParameters.sortOrder == SortOrder.HOT) {
            val now = Calendar.getInstance().timeInMillis
            query.whereGreaterThan(FIELD_CREATION_DATE, now)
        }

        query.get()
            .addOnSuccessListener { querySnapshot ->
                Timber.d("PaginationTest document results: ${querySnapshot.documents}")
                val results = getCuesFromResponse(querySnapshot)
                if (results.isNotEmpty()) {
                    Timber.d("PaginationTest successful response.")
                    response.postValue(ApiSuccessResponse(results))
                    lastCueDocumentReceived = querySnapshot.documents.last()
                } else {
                    Timber.d("PaginationTest empty response.")
                    response.postValue(ApiEmptyResponse<List<Cue>>())
                }
            }
            .addOnFailureListener() {
                Timber.e("PaginationTest Error response: ${it.message}")
                response.value = ApiErrorResponse("An unknown network error has occurred.")
            }

        return response
    }

    private fun getCuesFromResponse(querySnapshot: QuerySnapshot): List<Cue> {
        val results = mutableListOf<Cue>()
        for (document in querySnapshot) {
            try {
                results.add(document.toObject(Cue::class.java))
            } catch (e: Exception) {
                Timber.d("PaginationTest exception while parsing: ${e.message}")
            }
        }
        return results
    }

    override fun submitCue(cue: Cue): LiveData<Resource<Boolean>> {
        val response = MutableLiveData<Resource<Boolean>>()
        response.value = Resource.loading(null)

        val fireStoreCue = HashMap<String, Any>()
        fireStoreCue["author"] = cue.author
        fireStoreCue["creation_date"] = cue.creationDate
        fireStoreCue["rating"] = cue.rating
        fireStoreCue["text"] = cue.text
        fireStoreCue["id"] = cue.id
        fireStoreCue["user_id"] = authenticator.getUserId()

        firestore.collection(COLLECTION_CUES)
            .document(cue.id)
            .set(fireStoreCue, SetOptions.merge())
            .addOnSuccessListener { response.postValue(Resource.success(true)) }
            .addOnFailureListener { response.postValue(Resource.error("Unable to submit Cue.", null)) }
        return response
    }
}

