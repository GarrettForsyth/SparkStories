package com.example.android.writeitsayithearit.ui.util

import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils

object WriteItSayItHearItAnimationUtils {

    fun setUpSlideUpAnimation(view: View): Animation {
        val slideUp: Animation = AnimationUtils.loadAnimation(view.context, com.example.android.writeitsayithearit.R.anim.top_menu_slide_down)
        slideUp.setAnimationListener(object : AnimationAdapter(){
            // set the view state to match the end state of the animation
            override fun onAnimationEnd(p0: Animation?) { view.visibility = View.VISIBLE }

            // Set visibility to zero onStart so the toggle button clings to the menu
            override fun onAnimationStart(p0: Animation?){ view.visibility = View.INVISIBLE}
        })
        return slideUp
    }

    fun setUpSlideDownAnimation(view: View): Animation {
        val slideDown: Animation = AnimationUtils.loadAnimation(view.context, com.example.android.writeitsayithearit.R.anim.top_menu_slide_up)
        slideDown.setAnimationListener(object : AnimationAdapter(){
            // set the view state to match the end state of the animation
            override fun onAnimationEnd(p0: Animation?) { view.visibility = View.GONE }
        })
        return slideDown
    }
}