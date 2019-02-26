package com.example.android.writeitsayithearit.stories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.test.filters.SmallTest
import com.example.android.writeitsayithearit.repos.CueRepository
import com.example.android.writeitsayithearit.repos.StoryRepository
import com.example.android.writeitsayithearit.test.TestUtils
import com.example.android.writeitsayithearit.test.getValueBlocking
import com.example.android.writeitsayithearit.ui.stories.NewStoryViewModel
import com.example.android.writeitsayithearit.ui.util.events.Event
import com.example.android.writeitsayithearit.model.cue.Cue
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
    }

    @Test
    fun getCue() {
        // observe cue
        val mockObserver: Observer<Cue> = mockk(relaxed = true)
        newStoryViewModel.cue.observeForever(mockObserver)

        // mock response from repository
        val id = 0
        val liveCue = MutableLiveData<Cue>()
        val cue = Cue("test")
        liveCue.value = cue
        every { cueRepository.cue(id) } returns liveCue

        // call getCue
        newStoryViewModel.getCue(id)

        // assert repository is called and the value returned is exposed
        verify { cueRepository.cue(id) }
        assertEquals(newStoryViewModel.cue.getValueBlocking(), cue)
    }

    @Test
    fun toggleMenu() {
        val mockObserver: Observer<Event<Boolean>> = mockk(relaxed = true)
        newStoryViewModel.topMenuStatus.observeForever(mockObserver)

        // initial state should be true
        assertTrue(newStoryViewModel.topMenuStatus.getValueBlocking().peekContent())

        newStoryViewModel.onToggleMenu()
        assertFalse(newStoryViewModel.topMenuStatus.getValueBlocking().peekContent())

        newStoryViewModel.onToggleMenu()
        assertTrue(newStoryViewModel.topMenuStatus.getValueBlocking().peekContent())
    }

    @Test
    fun togglePreviewMode() {
        val mockObserver: Observer<Event<Boolean>> = mockk(relaxed = true)
        newStoryViewModel.inPreviewMode.observeForever(mockObserver)

        // initial state should be false
        assertFalse(newStoryViewModel.inPreviewMode.getValueBlocking().peekContent())

        newStoryViewModel.onTogglePreviewMode()
        assertTrue(newStoryViewModel.inPreviewMode.getValueBlocking().peekContent())

        newStoryViewModel.onTogglePreviewMode()
        assertFalse(newStoryViewModel.inPreviewMode.getValueBlocking().peekContent())
    }

    @Test(expected = KotlinNullPointerException::class )
    fun infoDialogInitiallyNull() {
        val mockObserver: Observer<Event<Boolean>> = mockk(relaxed = true)
        newStoryViewModel.newStoryInfoDialog.observeForever(mockObserver)
        newStoryViewModel
            .newStoryInfoDialog
            .getValueBlocking()
    }

    @Test
    fun infoDialog() {
        val mockObserver: Observer<Event<Boolean>> = mockk(relaxed = true)
        newStoryViewModel.newStoryInfoDialog.observeForever(mockObserver)
        newStoryViewModel.onClickInfo()

        assertFalse(
            newStoryViewModel
                .newStoryInfoDialog
                .getValueBlocking()
                .hasBeenHandled
        )
    }

    @Test(expected = KotlinNullPointerException::class )
    fun confirmationDialogInitiallyNull() {
        val mockObserver: Observer<Event<Boolean>> = mockk(relaxed = true)
        newStoryViewModel.confirmSubmissionDialog.observeForever(mockObserver)
        newStoryViewModel
            .confirmSubmissionDialog
            .getValueBlocking()
    }

    @Test
    fun confirmationDialog() {
        val mockObserver: Observer<Event<Boolean>> = mockk(relaxed = true)
        newStoryViewModel.confirmSubmissionDialog.observeForever(mockObserver)
        newStoryViewModel.onClickSubmit()

        assertFalse(
            newStoryViewModel
                .confirmSubmissionDialog
                .getValueBlocking()
                .hasBeenHandled
        )
    }

    @Test(expected = KotlinNullPointerException::class )
    fun navigateToStoriesFragmentInitiallyNull() {
        val mockObserver: Observer<Event<Boolean>> = mockk(relaxed = true)
        newStoryViewModel.shouldNavigateToStories.observeForever(mockObserver)
        newStoryViewModel
            .shouldNavigateToStories
            .getValueBlocking()
    }

    @Test
    fun confirmValidSubmission() {
        // mock a valid story
        val expectedStory = TestUtils.createTestStory()

        // set observers
        val mockObserver: Observer<Event<Boolean>> = mockk(relaxed = true)
        val cueMockObserver: Observer<Cue> = mockk(relaxed = true)
        newStoryViewModel.shouldNavigateToStories.observeForever(mockObserver)
        newStoryViewModel.cue.observeForever(cueMockObserver)

        every { newStoryViewModel.storyTextField.isValid() } returns true
        every { newStoryViewModel.storyTextField.text } returns expectedStory.text

        newStoryViewModel.getCue(expectedStory.cueId)
        newStoryViewModel.onConfirmSubmission()

        verify { storyRepository.submitStory(expectedStory) }

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

        // set observers
        val mockObserver: Observer<Event<Boolean>> = mockk(relaxed = true)
        val cueMockObserver: Observer<Cue> = mockk(relaxed = true)
        newStoryViewModel.shouldNavigateToStories.observeForever(mockObserver)
        newStoryViewModel.cue.observeForever(cueMockObserver)

        every { newStoryViewModel.storyTextField.isValid() } returns false
        every { newStoryViewModel.storyTextField.text } returns expectedStory.text

        newStoryViewModel.getCue(expectedStory.cueId)
        newStoryViewModel.onConfirmSubmission()

        verify(exactly = 0) { storyRepository.submitStory(expectedStory) }

        assertNotNull(
            newStoryViewModel
                .invalidStorySnackBar
                .getValueBlocking()
                .hasBeenHandled
        )
    }
}