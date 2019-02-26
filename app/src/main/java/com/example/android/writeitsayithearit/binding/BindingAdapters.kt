package com.example.android.writeitsayithearit.binding

import android.app.Application
import android.content.Context
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.test.core.app.ApplicationProvider
import com.example.android.writeitsayithearit.R
import com.example.android.writeitsayithearit.extensions.SpinnerExtensions
import com.example.android.writeitsayithearit.extensions.SpinnerExtensions.setSpinnerEntries
import com.example.android.writeitsayithearit.extensions.SpinnerExtensions.setSpinnerItemSelectedListener

@BindingAdapter("visibleGone")
fun showHide(view: View, show: Boolean) {
    view.visibility = if (show) View.VISIBLE else View.GONE
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

