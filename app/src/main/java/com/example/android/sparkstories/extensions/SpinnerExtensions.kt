package com.example.android.sparkstories.extensions

import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner

object SpinnerExtensions {

    /**
     * Sets spinner entries using an [ArrayAdapter].
     */
    fun Spinner.setSpinnerEntries(entries: List<Any>?) {
        entries?.let {
            val arrayAdapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, entries)
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            adapter = arrayAdapter
        }
    }

    fun Spinner.setSpinnerItemSelectedListener(listener: ItemSelectedListener?) {
        onItemSelectedListener = if (listener == null) null
        else object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (tag != position) {
                    listener.onItemSelected(parent.getItemAtPosition(position))
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // no op
            }
        }
    }

    interface ItemSelectedListener {
        fun onItemSelected(item: Any)
    }

}