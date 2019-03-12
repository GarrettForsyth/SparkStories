package com.example.android.writeitsayithearit.binding

import android.content.Context
import android.text.TextWatcher
import android.view.GestureDetector
import android.view.MotionEvent.ACTION_UP
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Spinner
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import androidx.databinding.BindingAdapter
import androidx.test.core.app.ApplicationProvider
import com.example.android.writeitsayithearit.R
import com.example.android.writeitsayithearit.extensions.SpinnerExtensions
import com.example.android.writeitsayithearit.extensions.SpinnerExtensions.setSpinnerEntries
import com.example.android.writeitsayithearit.extensions.SpinnerExtensions.setSpinnerItemSelectedListener
import timber.log.Timber

@BindingAdapter("visibleGone")
fun showHide(view: View, show: Boolean) {
    view.visibility = if (show) View.VISIBLE else View.GONE
}

@BindingAdapter(value = ["fillCondition", "unFilledWidth", "unFilledHeight" ], requireAll = true)
fun fillIf(view: View, fillCondition: Boolean, unFilledWidth: Int, unFilledHeight: Int) {
    if (fillCondition) {
        view.updateLayoutParams<ViewGroup.LayoutParams> {
            this.width = ViewGroup.LayoutParams.MATCH_PARENT
            this.height = ViewGroup.LayoutParams.MATCH_PARENT
        }
    } else {
        view.updateLayoutParams<ViewGroup.LayoutParams> {
            this.width = BindingUtil.pxFromDp(view.context, unFilledWidth.toFloat()).toInt()
            this.height = BindingUtil.pxFromDp(view.context, unFilledHeight.toFloat()).toInt()
        }
    }
}

@BindingAdapter("onDoubleClickListener")
fun doubleClickListener(view: View, listener: GestureDetector.SimpleOnGestureListener) {
    val doubleTapDetector = GestureDetector(view.context, listener)
    view.setOnTouchListener { _, event ->
        if (event.actionMasked == ACTION_UP) {
            view.performClick()
        }
        doubleTapDetector.onTouchEvent(event)
    }
}

@BindingAdapter("entries")
fun Spinner.setEntries(entries: Array<String>) {
    setSpinnerEntries(entries.asList())
}

@BindingAdapter("onItemSelected")
fun Spinner.setItemSelectedListener(itemSelectedListener: SpinnerExtensions.ItemSelectedListener?) {
    setSpinnerItemSelectedListener(itemSelectedListener)
}

@BindingAdapter("addOnTextChangeListener")
fun setOnTextChangeListener(view: EditText, listener: TextWatcher) {
    view.addTextChangedListener(listener)
}

