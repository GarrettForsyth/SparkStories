package com.example.android.sparkstories.comments

import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.android.sparkstories.MainActivity
import com.example.android.sparkstories.R
import com.example.android.sparkstories.di.SparkStoriesTestConfigurations
import com.example.android.sparkstories.model.comment.CommentTextField
import com.example.android.sparkstories.test.CustomMatchers.first
import com.example.android.sparkstories.test.data.DatabaseSeed
import com.example.android.sparkstories.util.CountingAppExecutorsRule
import com.example.android.sparkstories.util.DataBindingIdlingResourceRule
import com.example.android.sparkstories.util.TaskExecutorWithIdlingResourceRule
import org.hamcrest.CoreMatchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * As a writer
 * I want to create comments on stories
 * To give and respond to feedback and learn/teach
 */
@LargeTest
@RunWith(AndroidJUnit4::class)
class NewCommentTest {

    init { SparkStoriesTestConfigurations.injectAndroidTestAppComponent() }

    @Rule
    @JvmField
    val scenarioRule = ActivityScenarioRule<MainActivity>(MainActivity::class.java)

    @Rule
    @JvmField
    val executorRule = TaskExecutorWithIdlingResourceRule()

    @Rule
    @JvmField
    val countingAppExecutorsRule = CountingAppExecutorsRule()

    private val dbSeed = DatabaseSeed(ApplicationProvider.getApplicationContext())

    private val story = dbSeed.SEED_STORIES.first()
    private val parentComment = dbSeed.SEED_COMMENTS.first()

    @Before
    fun navigateToNewCommentFragment() {
        DataBindingIdlingResourceRule(scenarioRule)

        // And navigated to the stories list
        onView(withId(R.id.storiesFragment)).perform(click())

        // And clicked on the first story
        onView(withText(story.text)).perform(click())
        onView(withId(R.id.story_comments_button)).perform(click())
    }

    @Test
    fun toggleMenu() {
        onView(withId(R.id.new_comment_button)).perform(click())
        // I should see the top menu
        onView(withId(R.id.new_comment_top_menu)).check(matches(isDisplayed()))

        // When I click the toggle menu button
        onView(withId(R.id.new_comment_edit_text)).perform(doubleClick())

        // I should no longer see the top menu
        onView(withId(R.id.new_comment_top_menu)).check(doesNotExist())
    }

    @Test
    fun viewParentComment() {
        onView(first(withId(R.id.reply_comment_button))).perform(click())
        onView(withId(R.id.view_parent_comment_button)).perform(click())

        onView(withText(parentComment.text))
            .check(matches(isDisplayed()))

        onView(withText(parentComment.rating.toString()))
            .check(matches(isDisplayed()))

        onView(withText(parentComment.author))
            .check(matches(isDisplayed()))

        onView(withText(parentComment.formattedDate()))
            .check(matches(isDisplayed()))

        onView(withText(parentComment.formattedTime()))
            .check(matches(isDisplayed()))
    }

    @Test
    fun hideShowParentCommentButtonWhenTopLevelComment() {
        onView(withId(R.id.new_comment_button)).perform(click())
        onView(withId(R.id.view_parent_comment_button)).
                check(matches(not(isDisplayed())))
    }

    @Test
    fun togglePreviewMode() {
        onView(withId(R.id.new_comment_button)).perform(click())

        // When I click the new story editText
        onView(withId(R.id.new_comment_edit_text)).perform(click())

        // Then it should have focus
        onView(withId(R.id.new_comment_edit_text)).check(matches(hasFocus()))

        // When I click preview button
        onView(withId(R.id.toggle_preview_button)).perform(click())
        // And I click the new story editText
        onView(withId(R.id.new_comment_edit_text)).perform(click())

        // Then it should not have focus
        onView(withId(R.id.new_comment_edit_text)).check(matches(not(hasFocus())))
        // And the keyboard should not be shown

        // TODO: learn how to check if keyboard is shown
        // also: espresso throws a performException trying to click the
        // toggle view button after adding state transitions to it

//        // When I click preview button again
//        onView(withId(R.id.toggle_preview_button)).perform(click())
//        // And I click the new story editText
//        onView(withId(R.id.new_story_edit_text)).perform(click())
//
//        // Then it should  have focus
//        onView(withId(R.id.new_story_edit_text)).check(matches(hasFocus()))
    }


//    @Test
//    fun zoom() {
//
//        val defaultFontSize = 14f
//
//        // Font size starts at default size
//        onView((withId(R.id.new_story_edit_text)))
//            .check(matches(withFontSize(defaultFontSize)))
//
//        // And pinch the screen out (twice to get max value)
//        onView((withId(R.id.new_story_edit_text))).perform(pinchOut())
//        onView((withId(R.id.new_story_edit_text))).perform(pinchOut())
//
//        // Font size now 3 times larger
//        onView((withId(R.id.new_story_edit_text)))
//            .check(matches(withFontSize(3*defaultFontSize)))
//
//        // And pinch the screen in (twice to get min value)
//        onView((withId(R.id.new_story_edit_text))).perform(pinchIn())
//        onView((withId(R.id.new_story_edit_text))).perform(pinchIn())
//
//        // Font size is back to normal
//        onView((withId(R.id.new_story_edit_text)))
//            .check(matches(withFontSize(defaultFontSize)))
//    }

    @Test
    fun createInvalidComment() {
        onView(withId(R.id.new_comment_button)).perform(click())

        // When I leave the comm text field blank
        // And I press the submit story button
        onView(withId(R.id.new_comment_edit_text))
        onView(withId(R.id.submit_comment_button)).perform(click())

        scenarioRule.scenario.onActivity {
            // I should see a snackbar explaining why the story is invalid
            val invalidCommentMessage = it.getString(
                R.string.invalid_new_comment_snackbar, CommentTextField.maxCharacters)
            onView(withText(invalidCommentMessage))

        }


    }

    @Test
    fun createAValidComment() {
        onView(withId(R.id.new_comment_button)).perform(click())

        // When I type a valid comment into the new comment edit text field
        val validCommentText = "a".repeat(10)
        onView(withId(R.id.new_comment_edit_text))
            .perform(typeText(validCommentText))

        Espresso.closeSoftKeyboard()

        // And I press the submit story button
        onView(withId(R.id.submit_comment_button)).perform(click())

        // I should see a list of comments
        onView(withId(R.id.comments_list))
            .check(matches(isDisplayed()))
    }
}

