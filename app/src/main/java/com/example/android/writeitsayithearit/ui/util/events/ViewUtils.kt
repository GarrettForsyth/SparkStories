package com.example.android.writeitsayithearit.ui.util.events

import android.app.Activity
import android.content.Context.INPUT_METHOD_SERVICE
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

fun enableEditText(editText: EditText) {
    editText.apply {
        isCursorVisible = true
        isFocusableInTouchMode = true
    }
}

/**
 * TODO:
 * There is a bug here where the user can still input using a keyboard even
 * when the edit text is disabled. Hiding the keyboard should stop users on
 * a phone from having trouble, but users using a keyboard input will be able to
 * continue typing into the edit text even though it is disabled.
 *
 * Detaching and reattaching the key listener fixes this problem, but for some reason
 * it prevents the edit text from ever gaining focus again
 */
fun disableEditText(activity: Activity, editText: EditText) {
    editText.apply {
        isFocusableInTouchMode = false
        isCursorVisible = false
        hideKeyboard(activity, editText)
        clearFocus()
    }
}

fun hideKeyboard(activity: Activity, view: View) {
    val imm = activity?.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager?
    imm!!.hideSoftInputFromWindow(view.windowToken, 0)
}
