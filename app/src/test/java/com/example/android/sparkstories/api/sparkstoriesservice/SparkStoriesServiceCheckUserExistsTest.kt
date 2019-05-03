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
import io.mockk.*
import junit.framework.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@SmallTest
@RunWith(JUnit4::class)
class SparkStoriesServiceCheckUserExistsTest {

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
    fun checkUserExists_returnsLoadingResource_WhenTheNetworkCallHasNotReturned() {
        val response = sparkStoriesService.checkUserExists()
        assertEquals(response.value?.status, Status.LOADING)
    }

    @Test
    fun checkUserExists_returnsTrue_WhenUserExists() {
        val task: Task<DocumentSnapshot> = mockk(relaxed = true)
        val onSuccessListener = slot<OnSuccessListener<DocumentSnapshot>>()
        every {
            firestore.collection(any())
                .document(any())
                .get()
                .addOnSuccessListener(capture(onSuccessListener))
        } returns task

        // mock the response from the server
        val responseDocumentSnapshot: DocumentSnapshot = mockk(relaxed = true)
        every { responseDocumentSnapshot.exists() } returns true

        // execute
        val response = sparkStoriesService.checkUserExists()
        onSuccessListener.captured.onSuccess(responseDocumentSnapshot)

        // verify
        assertEquals(Status.SUCCESS, response.value?.status)
        assertEquals(true, response.value?.data)
    }

    @Test
    fun checkUserExists_returnsFalse_WhenUserDoesNotExists() {
        val task: Task<DocumentSnapshot> = mockk(relaxed = true)
        val onSuccessListener = slot<OnSuccessListener<DocumentSnapshot>>()
        every {
            firestore.collection(any())
                .document(any())
                .get()
                .addOnSuccessListener(capture(onSuccessListener))
        } returns task

        // mock the response from the server
        val responseDocumentSnapshot: DocumentSnapshot = mockk(relaxed = true)
        every { responseDocumentSnapshot.exists() } returns false

        // execute
        val response = sparkStoriesService.checkUserExists()
        onSuccessListener.captured.onSuccess(responseDocumentSnapshot)

        // verify
        assertEquals(Status.SUCCESS, response.value?.status)
        assertEquals(false, response.value?.data)
    }

    @Test
    fun checkUserExists_returnsError_WhenTheNetworkFails() {
        val task: Task<DocumentSnapshot> = mockk(relaxed = true)
        val onFailureListener = slot<OnFailureListener>()
        every {
            firestore.collection(any())
                .document(any())
                .get()
                .addOnSuccessListener(any())
                .addOnFailureListener(capture(onFailureListener))
        } returns task

        // mock the response from the server
        val responseException: Exception = mockk(relaxed = true)

        // execute
        val response = sparkStoriesService.checkUserExists()
        onFailureListener.captured.onFailure(responseException)

        // verify
        assertEquals(Status.ERROR, response.value?.status)
    }

}