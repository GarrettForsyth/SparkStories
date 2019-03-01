package com.example.android.writeitsayithearit.cues

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.example.android.writeitsayithearit.repos.CueRepository
import com.example.android.writeitsayithearit.test.TestUtils
import com.example.android.writeitsayithearit.test.TestUtils.createTestCue
import com.example.android.writeitsayithearit.test.getValueBlocking
import com.example.android.writeitsayithearit.ui.cues.NewCueViewModel
import com.example.android.writeitsayithearit.ui.cues.NewCueViewModel.Companion.DEFAULT_AUTHOR
import com.example.android.writeitsayithearit.ui.cues.NewCueViewModel.Companion.PREFERENCE_AUTHOR
import com.example.android.writeitsayithearit.util.MockUtils.mockObserverFor
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertNotNull
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

        assertFalse(
            newCueViewModel
                .shouldNavigateToCues
                .getValueBlocking()
                .hasBeenHandled
        )
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