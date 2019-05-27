package com.example.android.sparkstories.stories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.example.android.sparkstories.model.Resource
import com.example.android.sparkstories.repos.cue.CueRepository
import com.example.android.sparkstories.repos.story.StoryRepository
import com.example.android.sparkstories.test.TestUtils.createTestCue
import com.example.android.sparkstories.test.TestUtils.createTestStory
import com.example.android.sparkstories.test.asLiveData
import com.example.android.sparkstories.test.getValueBlocking
import com.example.android.sparkstories.ui.stories.StoryViewModel
import com.example.android.sparkstories.util.MockUtils.mockObserverFor
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.util.*

@SmallTest
@RunWith(JUnit4::class)
class StoryViewModelTest {

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val storyRepository: StoryRepository = mockk(relaxed = true)
    private val cueRepository: CueRepository = mockk(relaxed = true)

    private lateinit var storyViewModel: StoryViewModel

    @Before
    fun init() {
        storyViewModel = StoryViewModel(storyRepository, cueRepository)

        mockObserverFor(storyViewModel.story)
        mockObserverFor(storyViewModel.cue)
        mockObserverFor(storyViewModel.topMenuStatus)
        mockObserverFor(storyViewModel.cueDialog)
        mockObserverFor(storyViewModel.viewCommentsEvent)
    }

    @Test()
    fun showCue() {
        storyViewModel.onShowCueClick()

        assertFalse(
            storyViewModel
                .cueDialog
                .getValueBlocking()
                .hasBeenHandled
        )
    }

    @Test()
    fun getStory(){
        val cue = Resource.success(
            createTestCue().apply{ id = UUID.randomUUID().toString()})

        every { cueRepository.cue(cue.data?.id!!) } returns cue.asLiveData()

        val story = createTestStory().apply { cueId = cue.data?.id!! }
        every { storyRepository.story(story.id) } returns story.asLiveData()

        // Call get story
        storyViewModel.getStory(story.id)

        verify { storyRepository.story(story.id)}
        verify { cueRepository.cue(cue.data?.id!!) }
        assertEquals(storyViewModel.story.getValueBlocking(), story)
        assertEquals(storyViewModel.cue.getValueBlocking(), cue)
    }

    @Test
    fun toggleMenu() {
        // initial state should be true
        assertTrue(storyViewModel.topMenuStatus.getValueBlocking())

        storyViewModel.onToggleMenu()
        assertFalse(storyViewModel.topMenuStatus.getValueBlocking())

        storyViewModel.onToggleMenu()
        assertTrue(storyViewModel.topMenuStatus.getValueBlocking())
    }

    @Test
    fun likeStory() {
        val story = createTestStory().apply { id = UUID.randomUUID().toString() }
        every { storyRepository.story(story.id) } returns story.asLiveData()
        storyViewModel.getStory(story.id)

        storyViewModel.onLikeStoryClick()
        val updatedStory = story.copy().apply { rating++ }
        verify { storyRepository.update(updatedStory) }
    }

    @Test
    fun onViewCommentsClick() {
        storyViewModel.onViewCommentsClick()
        assertTrue(storyViewModel.viewCommentsEvent.getValueBlocking().peekContent())
    }

}