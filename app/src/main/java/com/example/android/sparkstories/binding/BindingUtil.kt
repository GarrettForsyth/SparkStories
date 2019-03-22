package com.example.android.sparkstories.binding

import android.content.Context

object BindingUtil {

    fun dpFromPx(context: Context, pixels: Float): Float {
        return pixels / context.resources.displayMetrics.density
    }

    fun pxFromDp(context: Context, dp: Float): Float {
        return dp * context.resources.displayMetrics.density
    }

}