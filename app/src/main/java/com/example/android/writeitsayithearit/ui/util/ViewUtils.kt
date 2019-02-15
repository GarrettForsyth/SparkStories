package com.example.android.writeitsayithearit.ui.util

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import com.example.android.writeitsayithearit.R
import com.example.android.writeitsayithearit.viewmodel.HasFilterQuery
import com.example.android.writeitsayithearit.viewmodel.HasSortOrderSpinner
import com.example.android.writeitsayithearit.vo.SortOrder

/**
 * Common operations for setting up views shared by fragments.
 */
object ViewUtils {

    fun initializeFilter(editText: EditText, recipient: HasFilterQuery) {
        editText.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(text: CharSequence?, start: Int, end: Int, count: Int) {
                recipient.filterQuery(text.toString())
            }

            override fun afterTextChanged(p0: Editable?) {
                // Nothing happens after text change
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // Nothing happens before text change
            }
        })
    }

    fun initializeSortOrderSpinner(spinner: Spinner, recipient: HasSortOrderSpinner, context: Context) {
        val spinnerAdapter = ArrayAdapter.createFromResource(
            context,
            R.array.sort_order,
            android.R.layout.simple_spinner_item
        )
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = spinnerAdapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                // Nothing happens when no item is selected
            }

            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when (position) {
                    0 -> recipient.sortOrder(SortOrder.NEW)
                    1 -> recipient.sortOrder(SortOrder.TOP)
                    else -> recipient.sortOrder(SortOrder.HOT)
                }
            }
        }
    }
}