package com.example.android.sparkstories.stories

import android.text.Editable
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.example.android.sparkstories.R
import com.example.android.sparkstories.model.Resource
import com.example.android.sparkstories.repos.cue.CueRepository
import com.example.android.sparkstories.repos.story.StoryRepository
import com.example.android.sparkstories.test.TestUtils
import com.example.android.sparkstories.test.getValueBlocking
import com.example.android.sparkstories.ui.stories.NewStoryViewModel
import com.example.android.sparkstories.test.TestUtils.createTestCue
import com.example.android.sparkstories.test.TestUtils.createTestStory
import com.example.android.sparkstories.test.asLiveData
import com.example.android.sparkstories.ui.cues.NewCueViewModel.Companion.DEFAULT_AUTHOR
import com.example.android.sparkstories.ui.stories.NewStoryViewModel.Companion.PREFERENCE_AUTHOR
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
import java.util.*

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
        newStoryViewModel.sharedPreferences = mockk(relaxed = true)

        mockObserverFor(newStoryViewModel.cue)

        mockObserverFor(
            newStoryViewModel.characterCount,
            newStoryViewModel.characterCountColour
        )

        mockObserverFor(newStoryViewModel.topMenuStatus)

        mockObserverFor(
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
        val id = UUID.randomUUID().toString()
        val cue = Resource.success(createTestCue())
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
        assertTrue(newStoryViewModel.topMenuStatus.getValueBlocking())

        newStoryViewModel.onToggleMenu()
        assertFalse(newStoryViewModel.topMenuStatus.getValueBlocking())

        newStoryViewModel.onToggleMenu()
        assertTrue(newStoryViewModel.topMenuStatus.getValueBlocking())
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
        every { newStoryViewModel.storyTextField.text } returns "aaa"
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

    @Test()
    fun showCue() {
        newStoryViewModel.onShowCueClick()

        assertFalse(
            newStoryViewModel
                .cueDialog
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
        val expectedStory = createTestStory()

        // mock cue response from repository
        val cue = Resource.success(createTestCue().apply { rating =14})
        every { cueRepository.cue(expectedStory.cueId) } returns cue.asLiveData()
        newStoryViewModel.getCue(expectedStory.cueId)

        every { newStoryViewModel.storyTextField.isValid() } returns true
        every { newStoryViewModel.storyTextField.text } returns expectedStory.text
        every {
            newStoryViewModel.sharedPreferences
                .getString(PREFERENCE_AUTHOR, DEFAULT_AUTHOR )
        } returns expectedStory.author

        // expect to update with one more rating
        val expectedCue = cue.data?.copy()!!.apply { rating++ }


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
        val cue = Resource.success(createTestCue().apply { rating =14})
        every { cueRepository.cue(expectedStory.cueId) } returns cue.asLiveData()
        newStoryViewModel.getCue(expectedStory.cueId)

        // expect to update with one more rating (if valid)
        val expectedCue = cue.data?.copy()!!.apply { rating++ }

        every { newStoryViewModel.storyTextField.isValid() } returns false
        every { newStoryViewModel.storyTextField.text } returns expectedStory.text
        every {
            newStoryViewModel.sharedPreferences
                .getString(PREFERENCE_AUTHOR, DEFAULT_AUTHOR )
        } returns expectedStory.author

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