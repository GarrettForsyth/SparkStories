package com.example.android.writeitsayithearit.views

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.core.view.get
import androidx.recyclerview.widget.RecyclerView
import timber.log.Timber

class NestedRecyclerView (
    context: Context,
    attrs: AttributeSet?,
    defStyle: Int
): RecyclerView(context, attrs, defStyle) {

    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet): this(context, attrs, 0)

    override fun onTouchEvent(e: MotionEvent?): Boolean {
        if(e?.actionMasked == MotionEvent.ACTION_MOVE) {
            parent.requestDisallowInterceptTouchEvent(true)
        }else {
            parent.requestDisallowInterceptTouchEvent(false)
        }
        return super.onTouchEvent(e)
    }

    val itemTouchListener = object: SimpleOnItemTouchListener() {
        override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
            val coords = IntArray(2)
            rv.getLocationOnScreen(coords)
            val nestedRecyclerViewTop = coords[1]
            val nestedRecyclerViewBottom = coords[1] + rv.height
            Timber.d("click=${e.y.toInt()}; top=$nestedRecyclerViewTop; bottom=$nestedRecyclerViewBottom")
                if (e.y.toInt() in nestedRecyclerViewTop..nestedRecyclerViewBottom) {
                    Timber.d("mytest setting scrolling enabled to false")
                    (layoutManager as NestedScrollingLinearLayoutManager).isScrollEnabled = false
                }else {
                    Timber.d("mytest setting scrolling enabled to true")
                    (layoutManager as NestedScrollingLinearLayoutManager).isScrollEnabled = true
                }

            return false
        }
    }


    init {
        this.addOnItemTouchListener(itemTouchListener)
    }

}
