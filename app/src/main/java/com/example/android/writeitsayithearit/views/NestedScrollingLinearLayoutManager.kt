package com.example.android.writeitsayithearit.views

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager

class NestedScrollingLinearLayoutManager (
    context: Context,
    attrs: AttributeSet,
    defStyleAttrs: Int,
    defStyleRes: Int
): LinearLayoutManager(context, attrs, defStyleAttrs, defStyleRes) {

    var isScrollEnabled = true

    override fun canScrollVertically() = isScrollEnabled && super.canScrollVertically()
    override fun canScrollHorizontally() = isScrollEnabled && super.canScrollHorizontally()

}