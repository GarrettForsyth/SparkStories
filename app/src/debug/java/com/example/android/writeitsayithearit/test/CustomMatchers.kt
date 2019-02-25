package com.example.android.writeitsayithearit.test

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.matcher.BoundedMatcher
import org.hamcrest.Description
import org.hamcrest.Matcher


class CustomMatchers {

    companion object {

        fun hasItemAtPosition(matcher: Matcher<View>, position: Int): Matcher<View> {
            return object : BoundedMatcher<View, RecyclerView>(RecyclerView::class.java) {

                override fun describeTo(description: Description) {
                    description.appendText("has item at position $position: ")
                    matcher.describeTo(description)
                }

                override fun matchesSafely(recyclerView: RecyclerView): Boolean {
                    val viewHolder = recyclerView.findViewHolderForAdapterPosition(position)
                    return matcher.matches(viewHolder!!.itemView)
                }
            }
        }
    }
}
