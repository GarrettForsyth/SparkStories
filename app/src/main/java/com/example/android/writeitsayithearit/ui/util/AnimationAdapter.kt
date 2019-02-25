package com.example.android.writeitsayithearit.ui.util

import android.view.animation.Animation

/**
 * Overrides AnimationListener's methods with no-ops.
 */
open class AnimationAdapter(): Animation.AnimationListener {
    override fun onAnimationRepeat(p0: Animation?) {
        // no op
    }
    override fun onAnimationEnd(p0: Animation?) {
        // no op
    }
    override fun onAnimationStart(p0: Animation?) {
        // no op
    }
}