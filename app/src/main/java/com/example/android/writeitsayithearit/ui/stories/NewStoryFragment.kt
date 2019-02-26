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

import com.example.android.writeitsayithearit.databinding.FragmentNewStoryBinding
import com.example.android.writeitsayithearit.di.Injectable
import com.example.android.writeitsayithearit.test.OpenForTesting
import com.example.android.writeitsayithearit.model.story.StoryTextField
import com.example.android.writeitsayithearit.ui.util.AnimationAdapter
import com.example.android.writeitsayithearit.ui.util.events.EventObserver
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject
import android.widget.EditText
import android.content.Context.INPUT_METHOD_SERVICE
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import com.example.android.writeitsayithearit.BR
import timber.log.Timber


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
            com.example.android.writeitsayithearit.R.layout.fragment_new_story,
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

        newStoryViewModel.inPreviewMode.observe(this, EventObserver { inPreviewMode ->
            if (inPreviewMode) {
                disableEditText(binding.newStoryEditText)
            }else {
                enableEditText(binding.newStoryEditText)
            }
        })

        newStoryViewModel.characterCount.observe(this, Observer { count ->
            binding.characterCountTextView.text = count.toString()
        })

        newStoryViewModel.characterCountColour.observe(this, Observer { colour ->
            binding.characterCountTextView.setTextColor(
                ContextCompat.getColor(context!!, colour)
            )
        })

        return binding.root
    }

    private fun enableEditText(editText: EditText) {
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
    private fun disableEditText(editText: EditText) {
        editText.apply {
            isFocusableInTouchMode = false
            isCursorVisible = false
            hideKeyboard()
            clearFocus()
        }
    }

    private fun hideKeyboard() {
        val imm = activity?.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager?
        imm!!.hideSoftInputFromWindow(binding.newStoryEditText.windowToken, 0)
    }

    private fun observeMenuStatus() {
        val menu = binding.newStoryTopMenu
        val button = binding.toggleMenuButton
        val editText = binding.newStoryEditText
        val slideUp = setUpSlideUpAnimation(menu)
        val slideDown = setUpSlideDownAnimation(menu)

        newStoryViewModel.topMenuStatus.observe(this, EventObserver { isShown ->
            if (isShown) { //slide the menu and toggle button up
                menu.startAnimation(slideUp)
                button.startAnimation(slideUp)
                editText.startAnimation(slideUp)
            } else { //slide the menu and toggle button down
                menu.startAnimation(slideDown)
                button.startAnimation(slideDown)
                editText.startAnimation(slideDown)
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
                    com.example.android.writeitsayithearit.R.string.invalid_new_story_snackbar,
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
            val builder = AlertDialog.Builder(it, com.example.android.writeitsayithearit.R.style.Theme_WriteItSayItHearIt_AlertDialogStyle)
            builder.apply {
                this.setTitle(com.example.android.writeitsayithearit.R.string.create_a_new_story)
                this.setMessage(com.example.android.writeitsayithearit.R.string.new_story_info_text)
            }
            builder.create()
        }
    }

    private fun createConfirmSubmitDialog(): AlertDialog? {
        return activity?.let {
            val builder = AlertDialog.Builder(it, com.example.android.writeitsayithearit.R.style.Theme_WriteItSayItHearIt_AlertDialogStyle)
            builder.apply {
                this.setTitle(getString(com.example.android.writeitsayithearit.R.string.confirm_submission_dialog_title))

                this.setPositiveButton(getString(com.example.android.writeitsayithearit.R.string.submit)) { _, _ ->
                    newStoryViewModel.onConfirmSubmission()
                }

                this.setNegativeButton(getString(com.example.android.writeitsayithearit.R.string.cancel)) { _, _ -> }
            }
            builder.create()
        }
    }

    /**
     * Created to override during tests.
     */
    fun navController() = findNavController()

}
