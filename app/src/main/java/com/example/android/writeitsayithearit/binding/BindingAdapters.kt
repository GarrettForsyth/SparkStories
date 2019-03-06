package com.example.android.writeitsayithearit.binding

import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Spinner
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import androidx.databinding.BindingAdapter
import com.example.android.writeitsayithearit.R
import com.example.android.writeitsayithearit.extensions.SpinnerExtensions
import com.example.android.writeitsayithearit.extensions.SpinnerExtensions.setSpinnerEntries
import com.example.android.writeitsayithearit.extensions.SpinnerExtensions.setSpinnerItemSelectedListener

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

