package com.example.android.writeitsayithearit.stories

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.FragmentScenario.launchInContainer
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.doubleClick
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.android.writeitsayithearit.R
import com.example.android.writeitsayithearit.TestApp
import com.example.android.writeitsayithearit.model.cue.Cue
import com.example.android.writeitsayithearit.model.story.Story
import com.example.android.writeitsayithearit.test.TestUtils.createTestCue
import com.example.android.writeitsayithearit.test.TestUtils.createTestStory
import com.example.android.writeitsayithearit.ui.stories.StoryFragment
import com.example.android.writeitsayithearit.ui.util.events.Event
import com.example.android.writeitsayithearit.util.ViewModelUtil
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.android.synthetic.main.fragment_story.*
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowAlertDialog

@LargeTest
@RunWith(AndroidJUnit4::class)
@Config(application = TestApp::class)
class StoryFragmentTest {

    companion object {
        private val STORY_ID_EXTRA = "story_id"
        private val STORY = createTestStory()
        private val CUE = createTestCue()
    }

    private var scenario: FragmentScenario<TestStoryFragment>

    // initialize the fragment with a story id passed as an argument
    init {
        val args = Bundle()
        args.putInt(STORY_ID_EXTRA, STORY.id)
        scenario = launchInContainer(
            TestStoryFragment::class.java,
            args,
            TestStoryFragmentFactory()
        )
    }

    @Test
    fun getStory() {
        scenario.onFragment {
            verify { it.storyViewModel.getStory(STORY.id) }
        }
    }

    @Test
    fun displayStory() {
        scenario.onFragment {
            it.story.postValue(STORY)
            onView(withId(R.id.story_constraint_layout))
                .check(matches(hasDescendant(withText(STORY.text))))
        }
    }

    @Test
    fun showCueDialog() {
        scenario.onFragment {
            assert(ShadowAlertDialog.getShownDialogs().isEmpty())
            it.cueDialog.value = Event(true)
            assert(ShadowAlertDialog.getShownDialogs()[0].isShowing)
        }
    }

    @Test
    fun showMenu() {
        scenario.onFragment {
            it.topMenuShown.value = Event(true)
            assert(it.story_top_menu.isShown)
        }
    }

    @Test
    fun hideMenu() {
        scenario.onFragment {
            it.topMenuShown.value = Event(false)
            assertNull(it.story_top_menu)
        }
    }


    class TestStoryFragmentFactory : FragmentFactory() {
        override fun instantiate(classLoader: ClassLoader, className: String, args: Bundle?): Fragment {
            return (super.instantiate(classLoader, className, args)
                    as TestStoryFragment).apply {
                this.storyViewModel = mockk(relaxed = true)
                this.viewModelFactory = ViewModelUtil.createFor(this.storyViewModel)

                this.topMenuShown.value = Event(true)

                every { storyViewModel.story } returns this.story
                every { storyViewModel.cue } returns this.cue
                every { storyViewModel.topMenuStatus } returns this.topMenuShown
                every { storyViewModel.cueDialog } returns this.cueDialog
            }
        }
    }
    class TestStoryFragment : StoryFragment() {
        val navController: NavController = mockk(relaxed = true)
        override fun navController() = navController

        val story = MutableLiveData<Story>()
        val cue = MutableLiveData<Cue>()
        val topMenuShown = MutableLiveData<Event<Boolean>>()
        val cueDialog = MutableLiveData<Event<Boolean>>()
    }
}