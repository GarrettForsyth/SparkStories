package com.example.android.writeitsayithearit.stories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.test.filters.SmallTest
import com.example.android.writeitsayithearit.model.cue.Cue
import com.example.android.writeitsayithearit.model.story.Story
import com.example.android.writeitsayithearit.repos.CueRepository
import com.example.android.writeitsayithearit.repos.StoryRepository
import com.example.android.writeitsayithearit.test.asLiveData
import com.example.android.writeitsayithearit.test.getValueBlocking
import com.example.android.writeitsayithearit.ui.stories.StoryViewModel
import com.example.android.writeitsayithearit.ui.util.events.Event
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

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

        // observe story
        val mockStoryObserver: Observer<Story> = mockk(relaxed = true)
        val mockCueObserver: Observer<Cue> = mockk(relaxed = true)
        val mockMenuObserver: Observer<Event<Boolean>> = mockk(relaxed = true)
        storyViewModel.story.observeForever(mockStoryObserver)
        storyViewModel.cue.observeForever(mockCueObserver)
        storyViewModel.topMenuStatus.observeForever(mockMenuObserver)
    }

    @Test()
    fun getStory(){
        // Mock the story's cue
        val cueId = 12
        val cue = Cue("This is some cue", 0, 0,  cueId)
        val liveCue = MutableLiveData<Cue>()
        liveCue.value = cue
        every { cueRepository.cue(cueId) } returns liveCue

        // Mock the story
        val storyId = 0
        val story = Story("This is the story about something.", cueId)
        val liveStory = MutableLiveData<Story>()
        liveStory.value = story
        every { storyRepository.story(storyId) } returns liveStory

        // Call get story
        storyViewModel.getStory(storyId)

        verify { storyRepository.story(storyId)}
        verify { cueRepository.cue(cueId) }
        assertEquals(storyViewModel.story.getValueBlocking(), story)
        assertEquals(storyViewModel.cue.getValueBlocking(), cue)
    }

    @Test
    fun toggleMenu() {
        // initial state should be true
        assertTrue(storyViewModel.topMenuStatus.getValueBlocking().peekContent())

        storyViewModel.onToggleMenu()
        assertFalse(storyViewModel.topMenuStatus.getValueBlocking().peekContent())

        storyViewModel.onToggleMenu()
        assertTrue(storyViewModel.topMenuStatus.getValueBlocking().peekContent())
    }

    @Test
    fun likeStory() {
        val story = Story("test", 14, 0, 52, 0)
        every { storyRepository.story(0) } returns story.asLiveData()
        storyViewModel.getStory(0)

        storyViewModel.onLikeStoryClick()
        val updatedStory = Story("test", 14, 0, 53, 0)
        verify { storyRepository.update(updatedStory) }
    }

}