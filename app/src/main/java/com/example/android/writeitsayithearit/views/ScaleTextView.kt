package com.example.android.writeitsayithearit.views

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.widget.TextView

class ScaleTextView(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int
) : TextView(context, attrs, defStyleAttr) {
    private var scaleDetector: ScaleGestureDetector;
    private var defaultSize: Float = 1f
    private var scaleFactor = 1f
    private var zoomLimit = 3.0f

    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    init {
        defaultSize = textSize;
        scaleDetector = ScaleGestureDetector(context, PinchListener())
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        scaleDetector.onTouchEvent(event)
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



