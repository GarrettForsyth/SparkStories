package com.example.android.writeitsayithearit.ui.stories


import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController

import com.example.android.writeitsayithearit.R
import com.example.android.writeitsayithearit.databinding.FragmentStoryBinding
import com.example.android.writeitsayithearit.di.Injectable
import com.example.android.writeitsayithearit.test.OpenForTesting
import com.example.android.writeitsayithearit.ui.util.AnimationAdapter
import com.example.android.writeitsayithearit.ui.util.events.EventObserver
import com.example.android.writeitsayithearit.viewmodel.WriteItSayItHearItViewModelFactory
import javax.inject.Inject

@OpenForTesting
class StoryFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var storyViewModel: StoryViewModel

    lateinit var binding: FragmentStoryBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            com.example.android.writeitsayithearit.R.layout.fragment_story,
            container,
            false
        )

        storyViewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(StoryViewModel::class.java)

        binding.storyTextView.movementMethod = ScrollingMovementMethod()

        observeStory()
        observeCue()
        observeMenuStatus()

        binding.viewmodel = storyViewModel

        return binding.root
    }

    private fun observeStory() {
        val args = StoryFragmentArgs.fromBundle(arguments!!)
        storyViewModel.getStory(args.storyId)

        storyViewModel.story.observe(this, Observer { story ->
            story?.let {
                binding.story = story
            }
        })
    }

    private fun observeCue() {
        storyViewModel.cue.observe(this, Observer { cue ->
            cue?.let { binding.cue = cue }
        })
    }

    private fun observeMenuStatus() {
        val menu = binding.storyTopMenu
        val button = binding.toggleMenuButton
        val textView = binding.storyTextView
        val slideUp = setUpSlideUpAnimation(menu)
        val slideDown = setUpSlideDownAnimation(menu)

        storyViewModel.topMenuStatus.observe(this, EventObserver { isShown ->
            if (isShown) { //slide the menu and toggle button up
                menu.startAnimation(slideUp)
                button.startAnimation(slideUp)
                textView.startAnimation(slideUp)
            } else { //slide the menu and toggle button down
                menu.startAnimation(slideDown)
                button.startAnimation(slideDown)
                textView.startAnimation(slideDown)
            }
        })
    }

    private fun setUpSlideUpAnimation(menu: View): Animation {
        val slideUp: Animation = AnimationUtils.loadAnimation(context, com.example.android.writeitsayithearit.R.anim.top_menu_slide_down)
        slideUp.setAnimationListener(object : AnimationAdapter(){
            // set the view state to match the end state of the animation
            override fun onAnimationEnd(p0: Animation?) { menu.visibility = View.VISIBLE }

            // Set visibility to zero onStart so the toggle button clings to the menu
            override fun onAnimationStart(p0: Animation?){ menu.visibility = View.INVISIBLE}
        })
        return slideUp
    }

    private fun setUpSlideDownAnimation(menu: View): Animation {
        val slideDown: Animation = AnimationUtils.loadAnimation(context, com.example.android.writeitsayithearit.R.anim.top_menu_slide_up)
        slideDown.setAnimationListener(object : AnimationAdapter(){
            // set the view state to match the end state of the animation
            override fun onAnimationEnd(p0: Animation?) { menu.visibility = View.GONE }
        })
        return slideDown
    }

    fun navController() = findNavController()


}
