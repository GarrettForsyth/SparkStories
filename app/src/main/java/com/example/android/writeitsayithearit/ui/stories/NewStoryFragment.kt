package com.example.android.writeitsayithearit.ui.stories


import android.app.AlertDialog
import android.os.Bundle
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
import com.example.android.writeitsayithearit.databinding.FragmentNewStoryBinding
import com.example.android.writeitsayithearit.di.Injectable
import com.example.android.writeitsayithearit.test.OpenForTesting
import com.example.android.writeitsayithearit.ui.stories.models.StoryTextField
import com.example.android.writeitsayithearit.ui.util.AnimationAdapter
import com.example.android.writeitsayithearit.ui.util.events.EventObserver
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

@OpenForTesting
class NewStoryFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var newStoryViewModel: NewStoryViewModel

    lateinit var binding: FragmentNewStoryBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_new_story,
            container,
            false
        )

        newStoryViewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(NewStoryViewModel::class.java)

        binding.viewmodel = newStoryViewModel
        binding.executePendingBindings()

        observeCue()
        observeSnackbar()
        observeInfoDialog()
        observeConfirmationDialog()
        observeNavigateToStoriesFragment()
        observeMenuStatus()

        return binding.root
    }

    private fun observeMenuStatus() {
        val menu = binding.newStoryTopMenu
        val button = binding.toggleMenuButton
        val slideUp = setUpSlideUpAnimation(menu)
        val slideDown = setUpSlideDownAnimation(menu)

        newStoryViewModel.topMenuStatus.observe(this, EventObserver { isShown ->
            if (isShown) { //slide the menu and toggle button up
                menu.startAnimation(slideUp)
                button.startAnimation(slideUp)
            } else { //slide the menu and toggle button down
                menu.startAnimation(slideDown)
                button.startAnimation(slideDown)
            }
        })
    }

    private fun setUpSlideUpAnimation(menu: View): Animation {
        val slideUp: Animation = AnimationUtils.loadAnimation(context, R.anim.top_menu_slide_down)
        slideUp.setAnimationListener(object : AnimationAdapter(){
            // set the view state to match the end state of the animation
            override fun onAnimationEnd(p0: Animation?) { menu.visibility = View.VISIBLE }

            // Set visibility to zero onStart so the toggle button clings to the menu
            override fun onAnimationStart(p0: Animation?){ menu.visibility = View.INVISIBLE}
        })
        return slideUp
    }

    private fun setUpSlideDownAnimation(menu: View): Animation {
        val slideDown: Animation = AnimationUtils.loadAnimation(context, R.anim.top_menu_slide_up)
        slideDown.setAnimationListener(object : AnimationAdapter(){
            // set the view state to match the end state of the animation
            override fun onAnimationEnd(p0: Animation?) { menu.visibility = View.GONE }
        })
        return slideDown
    }

    private fun observeNavigateToStoriesFragment() {
        newStoryViewModel.shouldNavigateToStories.observe(this, EventObserver {
            navController().navigate(
                NewStoryFragmentDirections
                    .actionNewStoryFragmentToStoriesFragment()
            )
        })
    }

    private fun observeConfirmationDialog() {
        val confirmSubmitDialog = createConfirmSubmitDialog()!!
        newStoryViewModel.confirmSubmissionDialog.observe(this, EventObserver {
            confirmSubmitDialog.show()
        })
    }

    private fun observeInfoDialog() {
        val infoFragment = createInfoDialog()!!
        newStoryViewModel.newStoryInfoDialog.observe(this, EventObserver {
            infoFragment.show()
        })
    }

    private fun observeSnackbar() {
        newStoryViewModel.invalidStorySnackBar.observe(this, EventObserver {
            Snackbar.make(
                binding.newStoryConstraintLayout,
                getString(
                    R.string.invalid_new_story_snackbar,
                    StoryTextField.minCharacters,
                    StoryTextField.maxCharacters
                ),
                Snackbar.LENGTH_SHORT
            ).show()
        })
    }

    private fun observeCue() {
        val args = NewStoryFragmentArgs.fromBundle(arguments!!)
        newStoryViewModel.getCue(args.cueId)

        newStoryViewModel.cue.observe(this, Observer { cue ->
            cue?.let { binding.cue = cue }
        })
    }

    private fun createInfoDialog(): AlertDialog? {
        return activity?.let {
            val builder = AlertDialog.Builder(it, R.style.Theme_WriteItSayItHearIt_AlertDialogStyle)
            builder.apply {
                this.setTitle(R.string.create_a_new_story)
                this.setMessage(R.string.new_story_info_text)
            }
            builder.create()
        }
    }

    private fun createConfirmSubmitDialog(): AlertDialog? {
        return activity?.let {
            val builder = AlertDialog.Builder(it, R.style.Theme_WriteItSayItHearIt_AlertDialogStyle)
            builder.apply {
                this.setTitle(getString(R.string.confirm_submission_dialog_title))

                this.setPositiveButton(getString(R.string.submit)) { _, _ ->
                    newStoryViewModel.onConfirmSubmission()
                }

                this.setNegativeButton(getString(R.string.cancel)) { _, _ -> }
            }
            builder.create()
        }
    }

    /**
     * Created to override during tests.
     */
    fun navController() = findNavController()

}
