package com.example.android.sparkstories.test

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.ViewAction
import androidx.test.espresso.util.HumanReadables
import androidx.test.espresso.PerformException
import androidx.annotation.IdRes
import androidx.test.espresso.UiController
import org.hamcrest.Matcher
import org.hamcrest.Matchers

fun <VH : RecyclerView.ViewHolder> actionOnItemViewAtPosition(
    position: Int,
    @IdRes
    viewId: Int,
    viewAction: ViewAction
): ViewAction {
    return ActionOnItemViewAtPositionViewAction<VH>(position, viewId, viewAction)
}

private class ActionOnItemViewAtPositionViewAction<VH : RecyclerView.ViewHolder> constructor(
    private val position: Int,
    @param:IdRes private val viewId: Int,
    private val viewAction: ViewAction
) : ViewAction {

    override fun getConstraints(): Matcher<View> {
        return Matchers.allOf(
                ViewMatchers.isAssignableFrom(RecyclerView::class.java),
                ViewMatchers.isDisplayed()
        )
    }

    override fun getDescription(): String {
        return ("actionOnItemAtPosition performing ViewAction: "
                + this.viewAction.description
                + " on item at position: "
                + this.position)
    }

    override fun perform(uiController: UiController, view: View) {
        val recyclerView = view as RecyclerView
        ScrollToPositionViewAction(this.position).perform(uiController, view)
        uiController.loopMainThreadUntilIdle()

        val targetView = recyclerView.getChildAt(this.position).findViewById<View>(this.viewId)

        if (targetView == null) {
            throw PerformException.Builder().withActionDescription(this.toString())
                .withViewDescription(

                    HumanReadables.describe(view)
                )
                .withCause(
                    IllegalStateException(
                        "No view with id "
                                + this.viewId
                                + " found at position: "
                                + this.position
                    )
                )
                .build()
        } else {
            this.viewAction.perform(uiController, targetView)
        }
    }
}

private class ScrollToPositionViewAction constructor(private val position: Int) : ViewAction {

    override fun getConstraints(): Matcher<View> {
        return Matchers.allOf(
                ViewMatchers.isAssignableFrom(RecyclerView::class.java),
                ViewMatchers.isDisplayed()
        )
    }

    override fun getDescription(): String {
        return "scroll RecyclerView to position: " + this.position
    }

    override fun perform(uiController: UiController, view: View) {
        val recyclerView = view as RecyclerView
        recyclerView.scrollToPosition(this.position)
    }
}


fun withRecyclerView(recyclerViewId: Int): RecyclerViewMatcher {
    return RecyclerViewMatcher(recyclerViewId)
}

