package com.example.android.writeitsayithearit.cues

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.test.filters.SmallTest
import com.example.android.writeitsayithearit.repos.CueRepository
import com.example.android.writeitsayithearit.test.TestUtils
import com.example.android.writeitsayithearit.test.getValueBlocking
import com.example.android.writeitsayithearit.ui.cues.NewCueViewModel
import com.example.android.writeitsayithearit.ui.util.events.Event
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
    }

    @Test(expected = KotlinNullPointerException::class )
    fun navigateToCuesFragmentInitiallyNull() {
        val mockObserver: Observer<Event<Boolean>> = mockk(relaxed = true)
        newCueViewModel.shouldNavigateToCues.observeForever(mockObserver)
        newCueViewModel
            .shouldNavigateToCues
            .getValueBlocking()
    }

    @Test
    fun submitValidCue() {
        // mock a valid cue
        val expectedCue = TestUtils.createTestCue()

        // set observers
        val mockObserver: Observer<Event<Boolean>> = mockk(relaxed = true)
        newCueViewModel.shouldNavigateToCues.observeForever(mockObserver)

        every { newCueViewModel.cueTextField.isValid() } returns true
        every { newCueViewModel.cueTextField.text } returns expectedCue.text

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

        // set observers
        val mockObserver: Observer<Event<Boolean>> = mockk(relaxed = true)
        newCueViewModel.shouldNavigateToCues.observeForever(mockObserver)
        newCueViewModel.invalidCueSnackBar.observeForever(mockObserver)

        every { newCueViewModel.cueTextField.isValid() } returns false
        every { newCueViewModel.cueTextField.text } returns expectedCue.text

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