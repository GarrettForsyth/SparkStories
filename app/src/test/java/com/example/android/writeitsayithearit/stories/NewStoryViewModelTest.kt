package com.example.android.writeitsayithearit.stories

import android.text.Editable
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.test.filters.SmallTest
import com.example.android.writeitsayithearit.R
import com.example.android.writeitsayithearit.repos.CueRepository
import com.example.android.writeitsayithearit.repos.StoryRepository
import com.example.android.writeitsayithearit.test.TestUtils
import com.example.android.writeitsayithearit.test.getValueBlocking
import com.example.android.writeitsayithearit.ui.stories.NewStoryViewModel
import com.example.android.writeitsayithearit.ui.util.events.Event
import com.example.android.writeitsayithearit.model.cue.Cue
import com.example.android.writeitsayithearit.test.asLiveData
import com.example.android.writeitsayithearit.util.MockUtils.mockObserversFor
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
class NewStoryViewModelTest {

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val cueRepository: CueRepository = mockk(relaxed = true)
    private val storyRepository: StoryRepository = mockk(relaxed = true)

    private lateinit var newStoryViewModel: NewStoryViewModel

    @Before
    fun init() {
        newStoryViewModel = NewStoryViewModel(cueRepository, storyRepository)
        newStoryViewModel.storyTextField = mockk(relaxed = true)

        mockObserversFor(newStoryViewModel.cue)

        mockObserversFor(
            newStoryViewModel.characterCount,
            newStoryViewModel.characterCountColour
        )

        mockObserversFor(
            newStoryViewModel.topMenuStatus,
            newStoryViewModel.inPreviewMode,
            newStoryViewModel.newStoryInfoDialog,
            newStoryViewModel.confirmSubmissionDialog,
            newStoryViewModel.shouldNavigateToStories,
            newStoryViewModel.newStoryInfoDialog
        )
    }

    @Test
    fun getCue() {
        // mock response from repository
        val id = 0
        val cue = Cue("test")
        every { cueRepository.cue(id) } returns cue.asLiveData()

        // call getCue
        newStoryViewModel.getCue(id)

        // assert repository is called and the value returned is exposed
        verify { cueRepository.cue(id) }
        assertEquals(newStoryViewModel.cue.getValueBlocking(), cue)
    }

    @Test
    fun toggleMenu() {
        // initial state should be true
        assertTrue(newStoryViewModel.topMenuStatus.getValueBlocking().peekContent())

        newStoryViewModel.onToggleMenu()
        assertFalse(newStoryViewModel.topMenuStatus.getValueBlocking().peekContent())

        newStoryViewModel.onToggleMenu()
        assertTrue(newStoryViewModel.topMenuStatus.getValueBlocking().peekContent())
    }

    @Test
    fun togglePreviewMode() {
        // initial state should be false
        assertFalse(newStoryViewModel.inPreviewMode.getValueBlocking().peekContent())

        newStoryViewModel.onTogglePreviewMode()
        assertTrue(newStoryViewModel.inPreviewMode.getValueBlocking().peekContent())

        newStoryViewModel.onTogglePreviewMode()
        assertFalse(newStoryViewModel.inPreviewMode.getValueBlocking().peekContent())
    }

    @Test
    fun storyTextChange() {
        // create the text watcher
        val textWatcher = newStoryViewModel.storyTextChangeListener()

        // mock the response given from the edit text
        val editable: Editable = mockk()
        every { editable.toString() } returns "aaa"
        // mock the response of what colour the text should be
        every { newStoryViewModel.storyTextField.characterCountColour } returns R.color.character_count_invalid

        textWatcher.afterTextChanged(editable)

        assertEquals(3, newStoryViewModel.characterCount.getValueBlocking())
        assertEquals(R.color.character_count_invalid, newStoryViewModel.characterCountColour.getValueBlocking())
    }

    @Test(expected = KotlinNullPointerException::class)
    fun infoDialogInitiallyNull() {
        newStoryViewModel
            .newStoryInfoDialog
            .getValueBlocking()
    }

    @Test
    fun infoDialog() {
        newStoryViewModel.onClickInfo()

        assertFalse(
            newStoryViewModel
                .newStoryInfoDialog
                .getValueBlocking()
                .hasBeenHandled
        )
    }

    @Test(expected = KotlinNullPointerException::class)
    fun confirmationDialogInitiallyNull() {
        newStoryViewModel
            .confirmSubmissionDialog
            .getValueBlocking()
    }

    @Test
    fun confirmationDialog() {
        newStoryViewModel.onClickSubmit()

        assertFalse(
            newStoryViewModel
                .confirmSubmissionDialog
                .getValueBlocking()
                .hasBeenHandled
        )
    }

    @Test(expected = KotlinNullPointerException::class)
    fun navigateToStoriesFragmentInitiallyNull() {
        newStoryViewModel
            .shouldNavigateToStories
            .getValueBlocking()
    }

    @Test
    fun confirmValidSubmission() {
        // mock a valid story
        val expectedStory = TestUtils.createTestStory()

        // mock cue response from repository
        val id = expectedStory.cueId
        val cue = Cue("test", 0, 12, 15)
        every { cueRepository.cue(expectedStory.cueId) } returns cue.asLiveData()
        newStoryViewModel.getCue(expectedStory.cueId)

        every { newStoryViewModel.storyTextField.isValid() } returns true
        every { newStoryViewModel.storyTextField.text } returns expectedStory.text

        // expect to update with one more rating
        val expectedCue = Cue("test", 0, 13, 15)

        newStoryViewModel.onConfirmSubmission()
        verify { storyRepository.submitStory(expectedStory) }
        verify(exactly = 1) { cueRepository.updateCue(expectedCue) }

        assertFalse(
            newStoryViewModel
                .shouldNavigateToStories
                .getValueBlocking()
                .hasBeenHandled
        )
    }

    @Test
    fun confirmInvalidSubmission() {
        val expectedStory = TestUtils.createTestStory()

        // mock cue response from repository
        val id = expectedStory.cueId
        val cue = Cue("test", 0, 12, 15)
        every { cueRepository.cue(expectedStory.cueId) } returns cue.asLiveData()
        newStoryViewModel.getCue(expectedStory.cueId)

        // expect to update with one more rating
        val expectedCue = Cue("test", 0, 13, 15)


        every { newStoryViewModel.storyTextField.isValid() } returns false
        every { newStoryViewModel.storyTextField.text } returns expectedStory.text

        newStoryViewModel.onConfirmSubmission()

        verify(exactly = 0) { storyRepository.submitStory(expectedStory) }
        verify(exactly = 0) { cueRepository.updateCue(expectedCue) }

        assertNotNull(
            newStoryViewModel
                .invalidStorySnackBar
                .getValueBlocking()
                .hasBeenHandled
        )
    }
}