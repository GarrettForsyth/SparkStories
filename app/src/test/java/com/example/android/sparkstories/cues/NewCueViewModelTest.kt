package com.example.android.sparkstories.cues

import android.widget.EditText
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.example.android.sparkstories.repos.cue.CueRepository
import com.example.android.sparkstories.test.TestUtils
import com.example.android.sparkstories.test.TestUtils.createTestCue
import com.example.android.sparkstories.test.getValueBlocking
import com.example.android.sparkstories.ui.cues.NewCueViewModel
import com.example.android.sparkstories.ui.cues.NewCueViewModel.Companion.DEFAULT_AUTHOR
import com.example.android.sparkstories.ui.cues.NewCueViewModel.Companion.PREFERENCE_AUTHOR
import com.example.android.sparkstories.util.MockUtils.mockObserverFor
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@SmallTest
@RunWith(JUnit4::class)
class NewCueViewModelTest {

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val cueRepository: CueRepository = mockk(relaxed = true)
    private lateinit var newCueViewModel: NewCueViewModel

    @Before
    fun init() {
        newCueViewModel = NewCueViewModel(cueRepository)
        newCueViewModel.cueTextField = mockk(relaxed = true)
        newCueViewModel.sharedPreferences = mockk(relaxed = true)

        mockObserverFor(
            newCueViewModel.shouldNavigateToCues,
            newCueViewModel.invalidCueSnackBar
        )
        mockObserverFor(newCueViewModel.submitCueResponse)
    }

    @Test
    fun newCueEditTextFocusChange() {
        val cueEditText: EditText = mockk(relaxed = true)

        // create a focus change listener
        val focusChangeListener = newCueViewModel.newCueEditTextFocusChangeListener()

        focusChangeListener.onFocusChange( cueEditText, true)
        assertTrue(newCueViewModel.newCueEditTextFocusStatus.getValueBlocking().peekContent())

        focusChangeListener.onFocusChange( cueEditText, false)
        assertFalse(newCueViewModel.newCueEditTextFocusStatus.getValueBlocking().peekContent())
    }

    @Test(expected = KotlinNullPointerException::class )
    fun navigateToCuesFragmentInitiallyNull() {
        newCueViewModel
            .shouldNavigateToCues
            .getValueBlocking()
    }

    @Test
    fun submitValidCue() {
        // mock a valid cue
        val expectedCue = createTestCue()

        every { newCueViewModel.cueTextField.isValid() } returns true
        every { newCueViewModel.cueTextField.text } returns expectedCue.text
        every {
            newCueViewModel.sharedPreferences
                .getString(PREFERENCE_AUTHOR, DEFAULT_AUTHOR )
        } returns expectedCue.author

        newCueViewModel.onClickSubmitCue()

        verify { cueRepository.submitCue(expectedCue) }

    }

    @Test
    fun submitInvalidCue() {
        // mock a valid cue
        val expectedCue = TestUtils.createTestCue()

        every { newCueViewModel.cueTextField.isValid() } returns false
        every { newCueViewModel.cueTextField.text } returns expectedCue.text
        every {
            newCueViewModel.sharedPreferences
                .getString(PREFERENCE_AUTHOR, DEFAULT_AUTHOR )
        } returns expectedCue.author

        newCueViewModel.onClickSubmitCue()

        verify(exactly = 0) { cueRepository.submitCue(expectedCue) }

        assertNotNull(
            newCueViewModel
                .invalidCueSnackBar
                .getValueBlocking()
                .hasBeenHandled
        )
    }
}