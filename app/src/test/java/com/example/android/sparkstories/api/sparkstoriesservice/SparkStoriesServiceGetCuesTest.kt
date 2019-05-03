package com.example.android.sparkstories.api.sparkstoriesservice

import android.content.SharedPreferences
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.espresso.action.Swiper
import androidx.test.filters.SmallTest
import com.example.android.sparkstories.api.ApiEmptyResponse
import com.example.android.sparkstories.api.ApiErrorResponse
import com.example.android.sparkstories.api.ApiSuccessResponse
import com.example.android.sparkstories.api.SparkStoriesServiceImpl
import com.example.android.sparkstories.api.SparkStoriesServiceImpl.Companion.COLLECTION_CUES
import com.example.android.sparkstories.api.SparkStoriesServiceImpl.Companion.CUE_NETWORK_PAGE_LIMIT
import com.example.android.sparkstories.api.SparkStoriesServiceImpl.Companion.FIELD_CREATION_DATE
import com.example.android.sparkstories.api.SparkStoriesServiceImpl.Companion.FIELD_RATING
import com.example.android.sparkstories.auth.Authenticator
import com.example.android.sparkstories.model.SortOrder
import com.example.android.sparkstories.model.cue.Cue
import com.example.android.sparkstories.test.TestUtils.createTestCueList
import com.example.android.sparkstories.ui.util.QueryParameters
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*
import com.google.firebase.firestore.model.Document
import io.mockk.*
import junit.framework.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.spy

@SmallTest
@RunWith(JUnit4::class)
class SparkStoriesServiceGetCuesTest {

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val firestore: FirebaseFirestore = mockk(relaxed = true)
    private val authenticator: Authenticator = mockk(relaxed = true)
    private val queryParameters: QueryParameters = mockk(relaxed = true)
    private val sharedPreferences: SharedPreferences = mockk(relaxed = true)

    private val sparkStoriesService =
        spyk(SparkStoriesServiceImpl(authenticator, firestore, sharedPreferences), recordPrivateCalls = true)

    init {
        every { authenticator.getUserId() } returns "fakeid123"
    }

    @Test
    fun getCues_returnsSuccess_WhenServerRespondsWithCues() {
        val getCuesTask: Task<QuerySnapshot> = mockk(relaxed = true)
        every { queryParameters.sortOrder } returns SortOrder.NEW

        val onSuccessListener = slot<OnSuccessListener<in QuerySnapshot>>()
        every {
            firestore.collection(COLLECTION_CUES)
                .orderBy(FIELD_CREATION_DATE, Query.Direction.DESCENDING)
                .limit(CUE_NETWORK_PAGE_LIMIT)
                .get()
                .addOnSuccessListener(capture(onSuccessListener))
        } returns getCuesTask

        val responseCuesQuerySnapshot: QuerySnapshot = mockk(relaxed = true)
        val lastDocument: DocumentSnapshot = mockk(relaxed = true)
        every { sparkStoriesService["getCuesFromResponse"](responseCuesQuerySnapshot) } returns createTestCueList(5)
        every { responseCuesQuerySnapshot.documents.last() } returns lastDocument

        // execute
        val response = sparkStoriesService.getCues(queryParameters)
        onSuccessListener.captured.onSuccess(responseCuesQuerySnapshot)

        // verify
        assertTrue(response.value is ApiSuccessResponse<List<Cue>>)
    }

    @Test
    fun getCues_returnsEmptyApiResponse_WhenServerRespondsWithNothing() {
        val getCuesTask: Task<QuerySnapshot> = mockk(relaxed = true)
        every { queryParameters.sortOrder } returns SortOrder.NEW


        val onSuccessListener = slot<OnSuccessListener<in QuerySnapshot>>()
        every {
            firestore.collection(COLLECTION_CUES)
                .orderBy(FIELD_CREATION_DATE, Query.Direction.DESCENDING)
                .limit(CUE_NETWORK_PAGE_LIMIT)
                .get()
                .addOnSuccessListener(capture(onSuccessListener))
        } returns getCuesTask

        val responseCuesQuerySnapshot: QuerySnapshot = mockk(relaxed = true)
        every { sparkStoriesService["getCuesFromResponse"](responseCuesQuerySnapshot) } returns emptyList<Cue>()

        // execute
        val response = sparkStoriesService.getCues(queryParameters)
        onSuccessListener.captured.onSuccess(responseCuesQuerySnapshot)

        // verify
        assertTrue(response.value is ApiEmptyResponse<List<Cue>>)
    }

    @Test
    fun getCues_returnsError_WhenNetworkFailure() {
        val getCuesTask: Task<QuerySnapshot> = mockk(relaxed = true)
        val onFailureListener = slot<OnFailureListener>()
        val responseException: Exception = mockk(relaxed = true)

        every { queryParameters.sortOrder } returns SortOrder.NEW
        every {
            firestore.collection(COLLECTION_CUES)
                .orderBy(FIELD_CREATION_DATE, Query.Direction.DESCENDING)
                .limit(CUE_NETWORK_PAGE_LIMIT)
                .get()
                .addOnSuccessListener(any())
                .addOnFailureListener(capture(onFailureListener))
        } returns getCuesTask

        // execute
        val response = sparkStoriesService.getCues(queryParameters)
        onFailureListener.captured.onFailure(responseException)

        // verify
        assertTrue(response.value is ApiErrorResponse<List<Cue>>)
    }

    @Test
    fun getCues_sortNew_setsSortOrderToCreationDateDescending() {
        val queryParameters: QueryParameters = mockk(relaxed = true)
        every { queryParameters.sortOrder } returns SortOrder.NEW

        sparkStoriesService.getCues(queryParameters)
        verify {
            firestore.collection(COLLECTION_CUES)
                .orderBy(FIELD_CREATION_DATE, Query.Direction.DESCENDING)
                .limit(CUE_NETWORK_PAGE_LIMIT)
        }
    }

    @Test
    fun getCues_sortTop_setsSortOrderToRatingDescending() {
        val queryParameters: QueryParameters = mockk(relaxed = true)
        every { queryParameters.sortOrder } returns SortOrder.TOP

        sparkStoriesService.getCues(queryParameters)
        verify {
            firestore.collection(COLLECTION_CUES)
                .orderBy(FIELD_RATING, Query.Direction.DESCENDING)
                .limit(CUE_NETWORK_PAGE_LIMIT)
        }
    }

    @Test
    fun getCues_sortHot_setsSortOrderToRatingDescendingAndCreatedInLastDay() {
        val queryParameters: QueryParameters = mockk(relaxed = true)
        every { queryParameters.sortOrder } returns SortOrder.HOT

        sparkStoriesService.getCues(queryParameters)
        verify {
            firestore.collection(COLLECTION_CUES)
                .orderBy(FIELD_CREATION_DATE, Query.Direction.DESCENDING)
                .limit(CUE_NETWORK_PAGE_LIMIT)
                .whereGreaterThan(FIELD_CREATION_DATE, any())
        }
    }
}
