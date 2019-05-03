package com.example.android.sparkstories.api.sparkstoriesservice

import android.content.SharedPreferences
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.example.android.sparkstories.api.SparkStoriesServiceImpl
import com.example.android.sparkstories.auth.Authenticator
import com.example.android.sparkstories.model.Status
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
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
class SparkStoriesServiceSubmitScreenNameTest {

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
    fun submitScreenName_returnsLoadingResource_WhenTheNetworkCallHasNotReturned() {
        val response = sparkStoriesService.submitScreenName("")
        assertEquals(response.value?.status, Status.LOADING)
    }

    @Test
    fun submitScreenName_returnsSuccess_WhenServerRespondsOk() {
        val task: Task<Void> = mockk(relaxed = true)
        val onSuccessListener = slot<OnSuccessListener<Void>>()
        every {
            firestore.collection(any())
                .document(any())
                .set(any())
                .addOnSuccessListener(capture(onSuccessListener))
        } returns task

        // execute
        val response = sparkStoriesService.submitScreenName("")
        onSuccessListener.captured.onSuccess(null)

        // verify
        assertEquals(Status.SUCCESS, response.value?.status)
    }

    @Test
    fun submitScreenName_returnsError_WhenServerRespondsNotOk() {
        val task: Task<Void> = mockk(relaxed = true)
        val onFailureListener = slot<OnFailureListener>()
        val responseException: Exception = mockk(relaxed = true)
        every {
            firestore.collection(any())
                .document(any())
                .set(any())
                .addOnSuccessListener(any())
                .addOnFailureListener(capture(onFailureListener))
        } returns task

        // execute
        val response = sparkStoriesService.submitScreenName("")
        onFailureListener.captured.onFailure(responseException)

        // verify
        assertEquals(Status.ERROR, response.value?.status)
    }
}