package com.example.android.sparkstories.api.sparkstoriesservice

import android.content.SharedPreferences
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.example.android.sparkstories.api.SparkStoriesServiceImpl
import com.example.android.sparkstories.api.SparkStoriesServiceImpl.Companion.FIELD_SCREEN_NAME
import com.example.android.sparkstories.auth.Authenticator
import com.example.android.sparkstories.model.Status
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import junit.framework.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@SmallTest
@RunWith(JUnit4::class)
class SparkStoriesServiceCheckScreenNameAvailableTest {

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val firestore: FirebaseFirestore = mockk(relaxed = true)
    private val authenticator: Authenticator = mockk(relaxed = true)
    private val sharedPreferences: SharedPreferences = mockk(relaxed = true)

    private val sparkStoriesService =
        SparkStoriesServiceImpl(authenticator, firestore, sharedPreferences)

    init {
        every { authenticator.getUserId() } returns "fakeid123"
    }

    @Test
    fun checkScreenNameAvailable_returnsLoadingResource_WhenTheNetworkCallHasNotReturned() {
        val response = sparkStoriesService.checkScreenNameAvailable("")
        assertEquals(response.value?.status, Status.LOADING)
    }

    @Test
    fun checkScreenNameAvailable_returnsTrue_WhenServerReturnsEmptyQuerySnapshot() {
        val screenName = "test_screen_name"
        val task: Task<QuerySnapshot> = mockk(relaxed = true)
        val onSuccessListener = slot<OnSuccessListener<QuerySnapshot>>()
        every {
            firestore.collection(any())
                .whereEqualTo(FIELD_SCREEN_NAME, screenName)
                .get()
                .addOnSuccessListener(capture(onSuccessListener))
        } returns task

        // mock the response from the server
        val responseQuerySnapshot: QuerySnapshot = mockk(relaxed = true)
        every { responseQuerySnapshot.isEmpty } returns true

        // execute
        val response = sparkStoriesService.checkScreenNameAvailable(screenName)
        onSuccessListener.captured.onSuccess(responseQuerySnapshot)

        // verify
        assertEquals(Status.SUCCESS, response.value?.status)
        assertEquals(Pair(true, screenName), response.value?.data)
    }

    @Test
    fun checkScreenNameAvailable_returnsFalse_WhenServerReturnsNonEmptyQuerySnapshot() {
        val screenName = "test_screen_name"
        val task: Task<QuerySnapshot> = mockk(relaxed = true)
        val onSuccessListener = slot<OnSuccessListener<QuerySnapshot>>()
        every {
            firestore.collection(any())
                .whereEqualTo(FIELD_SCREEN_NAME, screenName)
                .get()
                .addOnSuccessListener(capture(onSuccessListener))
        } returns task

        // mock the response from the server
        val responseQuerySnapshot: QuerySnapshot = mockk(relaxed = true)
        every { responseQuerySnapshot.isEmpty } returns false

        // execute
        val response = sparkStoriesService.checkScreenNameAvailable(screenName)
        onSuccessListener.captured.onSuccess(responseQuerySnapshot)

        // verify
        assertEquals(Status.SUCCESS, response.value?.status)
        assertEquals(Pair(false, screenName), response.value?.data)
    }

    @Test
    fun checkScreenNameAvailable_returnsError_WhenNetworkError() {
        val screenName = "test_screen_name"
        val task: Task<QuerySnapshot> = mockk(relaxed = true)
        val onFailureListener = slot<OnFailureListener>()
        val responseException: Exception = mockk(relaxed = true)
        every {
            firestore.collection(any())
                .whereEqualTo(FIELD_SCREEN_NAME, screenName)
                .get()
                .addOnSuccessListener(any())
                .addOnFailureListener(capture(onFailureListener))
        } returns task

        // execute
        val response = sparkStoriesService.checkScreenNameAvailable(screenName)
        onFailureListener.captured.onFailure(responseException)

        // verify
        assertEquals(Status.ERROR, response.value?.status)
    }

}
