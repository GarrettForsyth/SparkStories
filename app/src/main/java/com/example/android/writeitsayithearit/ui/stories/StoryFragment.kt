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
import com.example.android.writeitsayithearit.ui.util.WriteItSayItHearItAnimationUtils
import com.example.android.writeitsayithearit.ui.util.WriteItSayItHearItAnimationUtils.setUpSlideDownAnimation
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

        binding.viewmodel = storyViewModel
        binding.storyTextView.movementMethod = ScrollingMovementMethod()

        storyViewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(StoryViewModel::class.java)

        observeStory()
        observeCue()
        observeMenuStatus()
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
        val slideUp = WriteItSayItHearItAnimationUtils.setUpSlideUpAnimation(menu)
        val slideDown = WriteItSayItHearItAnimationUtils.setUpSlideDownAnimation(menu)

        storyViewModel.topMenuStatus.observe(this, EventObserver { isShown ->
            if (isShown) { //slide the menu, and toggle button, and story text up
                menu.startAnimation(slideUp)
                button.startAnimation(slideUp)
                textView.startAnimation(slideUp)
            } else { //slide the menu and toggle button, and story text down
                menu.startAnimation(slideDown)
                button.startAnimation(slideDown)
                textView.startAnimation(slideDown)
            }
        })
    }

    fun navController() = findNavController()

}
