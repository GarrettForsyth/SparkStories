package com.example.android.sparkstories.test

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.matcher.BoundedMatcher
import org.hamcrest.Description
import org.hamcrest.Matcher
import android.widget.TextView
import timber.log.Timber
import org.hamcrest.BaseMatcher




object CustomMatchers {

    /**
     * Matches a Matcher against the view holder in a particular position.
     */
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

    /**
     * Matches the font size of a text view in sp.
     */
    fun withFontSize(expectedSize: Float): Matcher<View> {
        return object : BoundedMatcher<View, View>(View::class.java) {

            public override fun matchesSafely(target: View): Boolean {
                if (target !is TextView) {
                    return false
                }
                val pixels = target.textSize
                val actualSize = Math.floor((pixels / target.getResources().displayMetrics.scaledDensity).toDouble()).toFloat()
                Timber.d("actual font size is $actualSize")
                return java.lang.Float.compare(actualSize, expectedSize) == 0
            }

            override fun describeTo(description: Description) {
                description.appendText("with fontSize: ")
                description.appendValue(expectedSize)
            }
        }
    }

    fun <T> first(matcher: Matcher<T>): Matcher<T> {
        return object : BaseMatcher<T>() {
            var isFirst = true

            override fun matches(item: Any): Boolean {
                if (isFirst && matcher.matches(item)) {
                    isFirst = false
                    return true
                }

                return false
            }

            override fun describeTo(description: Description) {
                description.appendText("should return first matching item")
            }
        }
    }


}
