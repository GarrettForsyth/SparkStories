package com.example.android.sparkstories.auth

import android.text.Editable
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.test.filters.SmallTest
import com.example.android.sparkstories.model.Resource
import com.example.android.sparkstories.repos.author.AuthorRepository
import com.example.android.sparkstories.test.getValueBlocking
import com.example.android.sparkstories.ui.signup.NewScreenNameViewModel
import com.example.android.sparkstories.util.MockUtils.mockObserverFor
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@SmallTest
@RunWith(JUnit4::class)
class NewScreenNameViewModelTest {

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val authorRepository: AuthorRepository = mockk(relaxed = true)

    lateinit var newScreenNameViewModel: NewScreenNameViewModel

    @Before
    fun init() {
        newScreenNameViewModel = NewScreenNameViewModel(authorRepository)
        newScreenNameViewModel.screenName = mockk(relaxed = true)

        mockObserverFor(newScreenNameViewModel.shouldNavigateToCues)
        mockObserverFor(newScreenNameViewModel.submitScreenNameResponse)
        mockObserverFor(newScreenNameViewModel.isScreenNameAvailableResponse)
    }


    @Test
    fun onSubmitScreenName() {
        val screenName = "TestScreenName123"
        every { newScreenNameViewModel.screenName.text } returns screenName

        // When client side validations pass
        every { newScreenNameViewModel.screenName.isValid() } returns true

        // And the user name is available
        val availabilityResponse = MutableLiveData<Resource<Pair<Boolean, String>>>()
        availabilityResponse.postValue(Resource.success(Pair(true, screenName)))
        every { authorRepository.checkScreenNameAvailable(screenName) } returns availabilityResponse

        val textWatcher = newScreenNameViewModel.screenNameTextChangeListener()
        val editable: Editable = mockk()

        // mock the error messages
        val errorMessage = ""
        every { newScreenNameViewModel.screenName.getErrorMessages() } returns  errorMessage

        textWatcher.afterTextChanged(editable)
        newScreenNameViewModel.onSubmitScreenName()
        verify(exactly = 1) { authorRepository.submitScreenName(screenName)}
    }

    @Test
    fun onSubmitInvalidScreenName() {
        val screenName = "  ..  ... "
        every { newScreenNameViewModel.screenName.text } returns screenName

        newScreenNameViewModel.onSubmitScreenName()
        verify(exactly = 0) { authorRepository.submitScreenName(screenName)}
    }

    @Test
    fun onSubmitUnavailableScreenName() {
        val screenName = "TestScreenName123"
        every { newScreenNameViewModel.screenName.text } returns screenName

        // When client side validations pass
        every { newScreenNameViewModel.screenName.isValid() } returns true

        // And the user name is available
        val availabilityResponse = MutableLiveData<Resource<Pair<Boolean, String>>>()
        availabilityResponse.postValue(Resource.success(Pair(false, screenName)))
        every { authorRepository.checkScreenNameAvailable(screenName) } returns availabilityResponse

        val textWatcher = newScreenNameViewModel.screenNameTextChangeListener()
        val editable: Editable = mockk()

        // mock the error messages
        val errorMessage = ""
        every { newScreenNameViewModel.screenName.getErrorMessages() } returns  errorMessage

        textWatcher.afterTextChanged(editable)
        newScreenNameViewModel.onSubmitScreenName()

        // Then the screen name is not submitted
        verify(exactly = 0) { authorRepository.submitScreenName(screenName)}
    }

    @Test
    fun screenNameTextChangeWithErrors() {
        // create the text watcher
        val textWatcher = newScreenNameViewModel.screenNameTextChangeListener()

        // mock the response given from the edit text
        val editable: Editable = mockk()
        val screenName = "aaa"
        every { newScreenNameViewModel.screenName.text } returns screenName

        // mock the error messages
        val errorMessage = "This is an error message."
        every { newScreenNameViewModel.screenName.getErrorMessages() } returns  errorMessage

        textWatcher.afterTextChanged(editable)

        assertEquals(errorMessage, newScreenNameViewModel.screenNameFieldErrorMessage.getValueBlocking())
        // Do not do network call if client side validations fail
        verify(exactly = 0) { authorRepository.checkScreenNameAvailable(screenName)}
    }

    @Test
    fun screenNameTextChangeWithNoErrors() {
        // create the text watcher
        val textWatcher = newScreenNameViewModel.screenNameTextChangeListener()

        // mock the response given from the edit text
        val editable: Editable = mockk()
        val screenName = "aaa"
        every { newScreenNameViewModel.screenName.text } returns screenName

        // mock the error messages
        val errorMessage = ""
        every { newScreenNameViewModel.screenName.getErrorMessages() } returns  errorMessage

        textWatcher.afterTextChanged(editable)

        assertEquals(errorMessage, newScreenNameViewModel.screenNameFieldErrorMessage.getValueBlocking())
        // Do not do network call if client side validations fail
        verify(exactly = 1) { authorRepository.checkScreenNameAvailable(screenName)}
    }

}

