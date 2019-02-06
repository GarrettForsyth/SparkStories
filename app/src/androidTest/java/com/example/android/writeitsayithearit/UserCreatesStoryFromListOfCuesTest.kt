package com.example.android.writeitsayithearit

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.android.writeitsayithearit.vo.Cue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

/**
 *
 * As a writer
 * I want to create a story
 * When I find a queue that inspires me
 *
 */
@RunWith(AndroidJUnit4::class)
class UserCreatesStoryFromListOfCuesTest {

    private lateinit var scenario : ActivityScenario<MainActivity>

    @Inject
    private lateinit var listOfQueues: List<Cue>

    @Before
    fun launchActivity() {
        scenario = ActivityScenario.launch(MainActivity::class.java)
    }

    @Test
    fun userCreatesStoryFromQueue() {

        // Starting at the queues fragment
        onView(withId(R.id.queues_fragment)).check(matches(isDisplayed()))

        // A user sees a list of queues
        for(cue in listOfQueues) {
            onView(withId(R.id.cues_list)).check(matches(withText(cue.text)))
        }
    }
}