package com.example.android.sparkstories.views

import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.util.AttributeSet
import android.util.TypedValue
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.content.getSystemService
import com.example.android.sparkstories.R
import com.google.android.material.textfield.TextInputEditText
import timber.log.Timber

class ScaleTextInputEditText(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int
) : TextInputEditText(context, attrs, defStyleAttr) {
    private var scaleDetector: ScaleGestureDetector;
    private var defaultSize: Float = 1f
    private var scaleFactor = 1f
    private var zoomLimit = 3.0f

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, R.attr.editTextStyle)

    init {
        defaultSize = textSize;
        scaleDetector = ScaleGestureDetector(context, PinchListener())
    }

    override fun onKeyPreIme(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            clearFocus()
        }
        return super.onKeyPreIme(keyCode, event)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        scaleDetector.onTouchEvent(event)
        if (scaleDetector.isInProgress) {
            parent.requestDisallowInterceptTouchEvent(true)
        }
        return super.onTouchEvent(event)
    }

    private inner class PinchListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            scaleFactor *= detector.scaleFactor
            scaleFactor = Math.max(1.0f, Math.min(scaleFactor, zoomLimit))
            setTextSize(TypedValue.COMPLEX_UNIT_PX, defaultSize * scaleFactor)
            return true
        }
    }
}

