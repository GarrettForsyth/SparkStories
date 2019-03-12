package com.example.android.writeitsayithearit.ui.stories


import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.transition.Fade
import androidx.transition.TransitionInflater
import androidx.transition.TransitionManager

import com.example.android.writeitsayithearit.R
import com.example.android.writeitsayithearit.databinding.CueListItemBinding
import com.example.android.writeitsayithearit.databinding.FragmentStoryBinding
import com.example.android.writeitsayithearit.di.Injectable
import com.example.android.writeitsayithearit.test.OpenForTesting
import com.example.android.writeitsayithearit.ui.util.events.EventObserver
import javax.inject.Inject

@OpenForTesting
class StoryFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var storyViewModel: StoryViewModel

    lateinit var binding: FragmentStoryBinding
    lateinit var cueBinding: CueListItemBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            com.example.android.writeitsayithearit.R.layout.fragment_story,
            container,
            false
        )

        cueBinding = DataBindingUtil.inflate(
            inflater, R.layout.cue_list_item, null, false
        )

        storyViewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(StoryViewModel::class.java)

        binding.viewmodel = storyViewModel
        binding.executePendingBindings()

        observeStory()
        observeCue()
        observeMenuStatus()
        observeCueDialog()

        return binding.root
    }

    private fun observeCueDialog() {
        val cueDialog = createCueDialog()!!
        storyViewModel.cueDialog.observe(this, EventObserver<Boolean> {
            cueDialog.show()
        })
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
            cue?.let {
                binding.cue = cue
                cueBinding.cue = cue
                binding.executePendingBindings()
                cueBinding.executePendingBindings()
            }
        })
    }

    private fun observeMenuStatus() {
        val rootView = binding.storyConstraintLayout
        val menu = binding.storyTopMenu
        val text = binding.storyTextScrollView

        storyViewModel.topMenuStatus.observe(this, EventObserver { isShown ->
            if (isShown) { //slide the menu, and toggle button, and story text up
                TransitionManager.beginDelayedTransition(rootView)
                rootView.removeView(menu)
                rootView.removeView(text)
                rootView.addView(text)
                rootView.addView(menu)

            } else { //slide the menu and toggle button, and story text down
                TransitionManager.beginDelayedTransition(rootView)
                rootView.removeView(menu)
                rootView.removeView(text)
                rootView.addView(text)
            }
        })
    }

    private fun createCueDialog(): AlertDialog? {
        return activity?.let {
            val builder = AlertDialog.Builder(
                it,
                com.example.android.writeitsayithearit.R.style.Theme_WriteItSayItHearIt_AlertDialogStyle
            )
            builder.setView(cueBinding.root)
            builder.create()
        }
    }

    fun navController() = findNavController()

}
