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
import com.example.android.writeitsayithearit.ui.util.events.EventObserver
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject
import android.widget.EditText
import android.content.Context.INPUT_METHOD_SERVICE
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.transition.TransitionManager
import com.example.android.writeitsayithearit.R
import com.example.android.writeitsayithearit.databinding.CueListItemBinding


@OpenForTesting
class NewStoryFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var newStoryViewModel: NewStoryViewModel

    lateinit var binding: FragmentNewStoryBinding
    lateinit var cueBinding: CueListItemBinding

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

        cueBinding = DataBindingUtil.inflate(
            inflater, R.layout.cue_list_item, null, false)

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
        observePreviewModeStatus()
        observeCharacterCount()
        observeCharacterCountColour()
        observeCueDialog()

        return binding.root
    }

    private fun observeCueDialog() {
        val cueDialog = createCueDialog()!!
        newStoryViewModel.cueDialog.observe(this, EventObserver<Boolean> {
            cueDialog.show()
        })
    }

    private fun observeCharacterCountColour() {
        newStoryViewModel.characterCountColour.observe(this, Observer { colour ->
            binding.characterCountTextView.setTextColor(
                ContextCompat.getColor(context!!, colour)
            )
            binding.executePendingBindings()
        })
    }

    private fun observeCharacterCount() {
        newStoryViewModel.characterCount.observe(this, Observer { count ->
            binding.characterCountTextView.text = count.toString()
            binding.executePendingBindings()
        })
    }

    private fun observePreviewModeStatus() {
        newStoryViewModel.inPreviewMode.observe(this, EventObserver { inPreviewMode ->
            if (inPreviewMode) {
                disableEditText(binding.newStoryEditText)
                binding.togglePreviewButton.isActivated = true
            } else {
                enableEditText(binding.newStoryEditText)
                binding.togglePreviewButton.isActivated = false
            }
        })
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
        val rootView = binding.newStoryConstraintLayout
        val menu = binding.newStoryTopMenu
        val text = binding.newStoryTextScrollView

        newStoryViewModel.topMenuStatus.observe(this, EventObserver { isShown ->
            if (isShown) {
                TransitionManager.beginDelayedTransition(rootView)
                rootView.removeView(menu)
                rootView.removeView(text)
                rootView.addView(text)
                rootView.addView(menu)
            } else {
                TransitionManager.beginDelayedTransition(rootView)
                rootView.removeView(menu)
                rootView.removeView(text)
                rootView.addView(text)
            }
        })
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
            cue?.let {
                binding.cue = cue
                cueBinding.cue = cue
                binding.executePendingBindings()
                cueBinding.executePendingBindings()
            }
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

    private fun createCueDialog(): AlertDialog? {
        return activity?.let {
            val builder = AlertDialog.Builder(it, com.example.android.writeitsayithearit.R.style.Theme_WriteItSayItHearIt_AlertDialogStyle)
            builder.setView(cueBinding.root)
            builder.create()
        }
    }

    /**
     * Created to override during tests.
     */
    fun navController() = findNavController()

}
