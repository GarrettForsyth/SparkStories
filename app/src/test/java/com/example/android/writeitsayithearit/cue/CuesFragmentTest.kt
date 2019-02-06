package com.example.android.writeitsayithearit.cue

import android.os.Bundle
import androidx.arch.core.executor.TaskExecutor
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.FragmentScenario.launchInContainer
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.MutableLiveData
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.android.writeitsayithearit.AppExecutors
import com.example.android.writeitsayithearit.R
import com.example.android.writeitsayithearit.TestApp
import com.example.android.writeitsayithearit.test.TestUtils
import com.example.android.writeitsayithearit.ui.CuesFragment
import com.example.android.writeitsayithearit.ui.CuesViewModel
import com.example.android.writeitsayithearit.ui.adapters.vh.CueViewHolder
import com.example.android.writeitsayithearit.util.ViewModelUtil
import com.example.android.writeitsayithearit.vo.Cue
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(application = TestApp::class)
class CuesFragmentTest {

    /**
     * A factory that returns a CuesFragment with mocked dependencies.
     */
    class CuesFragmentFactory : FragmentFactory() {

        override fun instantiate(classLoader: ClassLoader, className: String, args: Bundle?): Fragment {
            return (super.instantiate(classLoader, className, args) as CuesFragment).apply {
                this.cuesViewModel = mockk(relaxed = true)
                this.viewModelFactory = ViewModelUtil.createFor(this.cuesViewModel)

                TestUtils.postCueResults()
                every { cuesViewModel.cues } returns TestUtils.cues
            }
        }
    }

    private val scenario = launchInContainer(
            CuesFragment::class.java,
            null,
            CuesFragmentTest.CuesFragmentFactory()
    )

    @Test
    fun fragmentQueriesViewModelForCues() {
        scenario.onFragment {
            verify(exactly = 1) { it.cuesViewModel.cues }
        }
    }

    /**
     * Can't check whether views are displayed using robolectric,
     * so check if they exist in the view hierarchy instead.
     *
     * The views will only exist in the hierarchy if they are (at least almost)
     * on the screen. So the recyclerview must be scrolled to check for
     * views that wouldn't normally be on the screen.
     *
     * TODO: This test is really slow and probably unecessary
     */
    @Test
    fun resultsAreInsideRecyclerView() {
        TestUtils.listOfStartingCues.forEachIndexed { position, cue ->
            // Scroll to position
            onView(ViewMatchers.withId(R.id.cues_list))
                    .perform(
                            RecyclerViewActions.scrollToPosition<CueViewHolder>(position)
                    )

            //Check view exists
            onView(withId(R.id.cues_list))
                    .check(matches(hasDescendant(
                            withText(cue.text))
                    ))

        }
    }
}