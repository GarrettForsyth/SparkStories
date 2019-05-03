package com.example.android.sparkstories.authors

import androidx.test.filters.SmallTest
import com.example.android.sparkstories.data.local.AuthorDao
import com.example.android.sparkstories.api.SparkStoriesService
import com.example.android.sparkstories.repos.author.AuthorRepository
import com.example.android.sparkstories.util.InstantAppExecutors
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@SmallTest
@RunWith(JUnit4::class)
class AuthorRepositoryTest {

    private val dao: AuthorDao = mockk(relaxed = true)
    private val remoteDatabase: SparkStoriesService = mockk(relaxed = true)

    private val authorRepository = AuthorRepository(
        InstantAppExecutors(),
        dao,
        remoteDatabase
    )

    @Test
    fun checkUserExists() {
        // When checkUserExists() is called
        authorRepository.checkUserExists()

        // Then it should make a call to the remote database
        verify(exactly = 1) { remoteDatabase.checkUserExists() }
    }

    @Test
    fun checkUserHasScreenName() {
        authorRepository.checkUserHasScreenName()
        verify(exactly = 1) { remoteDatabase.checkUserHasScreenName() }
    }


    @Test
    fun submitScreenNameTest() {
        //When submitScreenName() is called
        val newScreenName = "newname21"
        authorRepository.submitScreenName(newScreenName)

        // Then it should make a call to the remote database
        verify(exactly = 1) { remoteDatabase.submitScreenName(newScreenName) }
    }

    @Test
    fun checkScreenNameAvailableTest() {
        //When submitScreenName() is called
        val newScreenName = "newname21"
        authorRepository.checkScreenNameAvailable(newScreenName)

        // Then it should make a call to the remote database
        verify(exactly = 1) { remoteDatabase.checkScreenNameAvailable(newScreenName) }
    }
}

