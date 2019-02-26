package com.example.android.writeitsayithearit.stories

import android.content.Context
import android.os.Bundle
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.FragmentScenario.launchInContainer
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.android.writeitsayithearit.R
import com.example.android.writeitsayithearit.TestApp
import com.example.android.writeitsayithearit.test.TestUtils
import com.example.android.writeitsayithearit.ui.stories.NewStoryFragment
import com.example.android.writeitsayithearit.ui.stories.NewStoryFragmentDirections
import com.example.android.writeitsayithearit.model.story.StoryTextField
import com.example.android.writeitsayithearit.ui.util.events.Event
import com.example.android.writeitsayithearit.util.ViewModelUtil
import com.example.android.writeitsayithearit.model.cue.Cue
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.android.synthetic.main.fragment_new_story.*
import org.hamcrest.CoreMatchers.allOf
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowAlertDialog

@LargeTest
@RunWith(AndroidJUnit4::class)
@Config(application = TestApp::class)
class NewStoryFragmentTest {

    companion object {
        private val context = ApplicationProvider.getApplicationContext<Context>()
        private val MINIMUM_STORY_LENGTH = StoryTextField.minCharacters
        private val MAXIMUM_STORY_LENGTH = StoryTextField.maxCharacters

        private val invalidStoryMessage = context.getString(
            R.string.invalid_new_story_snackbar,
            MINIMUM_STORY_LENGTH, MAXIMUM_STORY_LENGTH
        )
        private val CUE_ID_EXTRA = "cue_id"
        private val CUE = TestUtils.STARTING_CUES.last()
    }

    private var scenario: FragmentScenario<TestNewStoryFragment>

    // initialize the fragment with a cue id passed as an argument
    init {
        val args = Bundle()
        args.putInt(CUE_ID_EXTRA, CUE.id)
        scenario = launchInContainer(
            NewStoryFragmentTest.TestNewStoryFragment::class.java,
            args,
            NewStoryFragmentTest.TestNewStoryFragmentFactory()
        )
    }

    @Test
    fun submitStoryButton() {
        scenario.onFragment {
            it.submit_story_button.callOnClick()
            verify(exactly = 1) { it.newStoryViewModel.onClickSubmit() }
        }
    }

    @Test
    fun showConfirmStoryDialog() {
        scenario.onFragment {
            assert(ShadowAlertDialog.getShownDialogs().isEmpty())
            it.confirmSubmissionDialog.value = Event(true)
            assert(ShadowAlertDialog.getShownDialogs()[0].isShowing)
        }
    }

    @Test
    fun confirmSubmissionButton() {
        scenario.onFragment {
            it.confirmSubmissionDialog.value = Event(true)

            // Check confirm dialog is shown and click confirm
            ShadowAlertDialog.getShownDialogs().first()
                .findViewById<Button>(android.R.id.button1).callOnClick()

            verify(exactly = 1) { it.newStoryViewModel.onConfirmSubmission() }
        }
    }

    @Test
    fun confirmStoryButton() {
        assert(ShadowAlertDialog.getShownDialogs().isEmpty())
        scenario.onFragment {
            it.confirmSubmissionDialog.value = Event(true)
        }

        // Check confirm dialog is shown and click confirm
        ShadowAlertDialog.getShownDialogs().first()
            .findViewById<Button>(android.R.id.button1).callOnClick()

        scenario.onFragment {
            verify(exactly = 1) { it.newStoryViewModel.onConfirmSubmission() }
        }
    }

    @Test
    fun showInvalidStorySnackbar() {
        scenario.onFragment {
            it.invalidStorySnackBar.value = Event(true)
        }
        onView(
            allOf(
                withId(com.google.android.material.R.id.snackbar_text),
                withText(invalidStoryMessage)
            )
        ).check(matches(isDisplayed()))
    }

    @Test
    fun infoDialogButton() {
        scenario.onFragment {
            it.new_story_info_button.callOnClick()
            verify(exactly = 1) { it.newStoryViewModel.onClickInfo() }
        }
    }

    @Test
    fun showInfoDialog() {
        scenario.onFragment {
            assert(ShadowAlertDialog.getShownDialogs().isEmpty())
            it.newStoryInfoDialog.value = Event(true)
            assert(ShadowAlertDialog.getShownDialogs()[0].isShowing)
        }
    }

    @Test
    fun toggleMenuButton() {
        scenario.onFragment {
            it.toggle_menu_button.callOnClick()
            verify(exactly = 1) { it.newStoryViewModel.onToggleMenu() }
        }
    }

    @Test
    fun showMenu() {
        scenario.onFragment {
            it.topMenuShown.value = Event(true)
            assert(it.new_story_top_menu.isShown)
        }
    }

    @Test
    fun hideMenu() {
        scenario.onFragment {
            it.topMenuShown.value = Event(false)
            assert(!it.new_story_top_menu.isShown)
        }
    }


    @Test
    fun inPreviewMode() {
        scenario.onFragment {
            it.inPreviewMode.value = Event(true)
            it.new_story_edit_text.callOnClick()
            assert(!it.new_story_edit_text.isFocused)
        }
    }

    @Test
    fun notInPreviewMode() {
        scenario.onFragment {
            it.inPreviewMode.value = Event(false)
            it.new_story_edit_text.callOnClick()
            assert(it.new_story_edit_text.isFocusable)
        }
    }

    @Test
    fun characterCount() {
        scenario.onFragment {
            it.characterCount.value = 5
            assertEquals("5",it.character_count_text_view.text.toString())
        }
    }

    @Test
    fun characterCountColour() {
        scenario.onFragment {
            val expectedColour = ContextCompat.getColor(context, R.color.character_count_invalid)
            it.characterCountColour.value = R.color.character_count_invalid
            assertEquals(expectedColour, it.character_count_text_view.currentTextColor)
        }
    }

    @Test
    fun getCue() {
        scenario.onFragment {
            verify { it.newStoryViewModel.getCue(CUE.id) }
        }
    }

    @Test
    fun displayCue() {
        scenario.onFragment {
            it.cue.postValue(CUE)
            onView(withId(R.id.new_story_constraint_layout))
                .check(matches(hasDescendant(withText(CUE.text))))
        }
    }

    @Test
    fun shouldNavigateToStories() {
         scenario.onFragment {
             it.shouldNavigateToStories.value = Event(true)
             verify {
                 it.navController.navigate(
                     NewStoryFragmentDirections.actionNewStoryFragmentToStoriesFragment())
             }
        }
    }



    class TestNewStoryFragmentFactory : FragmentFactory() {
        override fun instantiate(classLoader: ClassLoader, className: String, args: Bundle?): Fragment {
            return (super.instantiate(classLoader, className, args)
                    as TestNewStoryFragment).apply {
                this.newStoryViewModel = mockk(relaxed = true)
                this.viewModelFactory = ViewModelUtil.createFor(this.newStoryViewModel)

                this.topMenuShown.value = Event(true)

                every { newStoryViewModel.cue } returns this.cue
                every { newStoryViewModel.invalidStorySnackBar } returns this.invalidStorySnackBar
                every { newStoryViewModel.newStoryInfoDialog } returns this.newStoryInfoDialog
                every { newStoryViewModel.confirmSubmissionDialog } returns this.confirmSubmissionDialog
                every { newStoryViewModel.shouldNavigateToStories } returns this.shouldNavigateToStories
                every { newStoryViewModel.topMenuStatus } returns this.topMenuShown
                every { newStoryViewModel.inPreviewMode } returns this.inPreviewMode
                every { newStoryViewModel.characterCount } returns this.characterCount
                every { newStoryViewModel.characterCountColour } returns this.characterCountColour
            }
        }
    }

    /**
     * Expose observed objects in test fragment. This allows the
     * tests to be driven by setting the value of these objects.
     *
     * i.e. setting the value to of these objects represents the
     * viewmodel forwarding an event or data change.
     */
    class TestNewStoryFragment : NewStoryFragment() {
        val navController: NavController = mockk(relaxed = true)
        override fun navController() = navController

        val invalidStorySnackBar = MutableLiveData<Event<Boolean>>()
        val newStoryInfoDialog = MutableLiveData<Event<Boolean>>()
        val confirmSubmissionDialog = MutableLiveData<Event<Boolean>>()
        val shouldNavigateToStories = MutableLiveData<Event<Boolean>>()
        val topMenuShown = MutableLiveData<Event<Boolean>>()
        val inPreviewMode = MutableLiveData<Event<Boolean>>()
        val characterCount = MutableLiveData<Int>()
        val characterCountColour = MutableLiveData<Int>()
        val cue = MutableLiveData<Cue>()
    }

}