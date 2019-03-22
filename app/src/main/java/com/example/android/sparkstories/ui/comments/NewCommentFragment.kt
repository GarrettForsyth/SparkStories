package com.example.android.sparkstories.ui.comments


import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.transition.TransitionManager

import com.example.android.sparkstories.R
import com.example.android.sparkstories.databinding.CommentListItemBinding
import com.example.android.sparkstories.databinding.CueListItemBinding
import com.example.android.sparkstories.databinding.FragmentNewCommentBinding
import com.example.android.sparkstories.databinding.FragmentNewStoryBinding
import com.example.android.sparkstories.di.Injectable
import com.example.android.sparkstories.model.comment.CommentTextField
import com.example.android.sparkstories.model.story.StoryTextField
import com.example.android.sparkstories.test.OpenForTesting
import com.example.android.sparkstories.ui.stories.NewStoryFragmentArgs
import com.example.android.sparkstories.ui.stories.NewStoryFragmentDirections
import com.example.android.sparkstories.ui.stories.NewStoryViewModel
import com.example.android.sparkstories.ui.util.events.EventObserver
import com.example.android.sparkstories.ui.util.events.disableEditText
import com.example.android.sparkstories.ui.util.events.enableEditText
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
@OpenForTesting
class NewCommentFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var newCommentViewModel: NewCommentViewModel

    lateinit var binding: FragmentNewCommentBinding
    lateinit var parentCommentBinding: CommentListItemBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            com.example.android.sparkstories.R.layout.fragment_new_comment,
            container,
            false
        )

        parentCommentBinding = DataBindingUtil.inflate(
            inflater, R.layout.comment_list_item, null, false)

        newCommentViewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(NewCommentViewModel::class.java)

        binding.viewmodel = newCommentViewModel
        binding.hasParentComment = false
        binding.executePendingBindings()

        handleArgs()
        observeParentComment()
        observeInvalidCommentSnackbar()
        observePreviewModeStatus()
        observeShowParentCommentButton()
        observeShouldNavigateToComments()
        observeMenuStatus()


        return binding.root
    }

    private fun handleArgs() {
        val args = NewCommentFragmentArgs.fromBundle(arguments!!)
        newCommentViewModel.getParentComment(args.parentId)
        newCommentViewModel.setStoryId(args.storyId)
    }

    private fun observeShouldNavigateToComments() {
        val args = NewCommentFragmentArgs.fromBundle(arguments!!)
        newCommentViewModel.shouldNavigateToComments.observe(this, EventObserver {
            navController().navigate(
                NewCommentFragmentDirections.actionNewCommentFragmentToCommentsFragment(
                    args.storyId
                )
            )
        })

    }

    private fun observePreviewModeStatus() {
        newCommentViewModel.inPreviewMode.observe(this, EventObserver { inPreviewMode ->
            if (inPreviewMode) {
                disableEditText(activity!!, binding.newCommentEditText)
                binding.togglePreviewButton.isActivated = true
            } else {
                enableEditText(binding.newCommentEditText)
                binding.togglePreviewButton.isActivated = false
            }
        })
    }

    private fun observeInvalidCommentSnackbar() {
        newCommentViewModel.invalidCommentSnackbar.observe(this, EventObserver {
            Snackbar.make(
                binding.newCommentConstraintLayout,
                getString(
                    R.string.invalid_new_comment_snackbar,
                    CommentTextField.maxCharacters
                ),
                Snackbar.LENGTH_SHORT
            ).show()
        })
    }

    private fun observeParentComment() {
        newCommentViewModel.parentComment.observe(this, Observer { parentComment ->
            parentComment?.let {
                binding.hasParentComment = true
                binding.parentComment = parentComment
                parentCommentBinding.comment = parentComment
                parentCommentBinding.childCommentsList.visibility = View.GONE
                parentCommentBinding.replyCommentButton.visibility = View.GONE
            }
        })
    }

    private fun observeMenuStatus() {
        val rootView = binding.newCommentConstraintLayout
        val menu = binding.newCommentTopMenu
        val text = binding.newCommentTextScrollView

        newCommentViewModel.topMenuStatus.observe(this, EventObserver { isShown ->
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

    private fun observeShowParentCommentButton() {
        val parentCommentDialog = createCommentDialog()!!
        newCommentViewModel.showParentComment.observe(this, EventObserver {
          parentCommentDialog.show()
        })
    }

    private fun createCommentDialog(): AlertDialog? {
        return activity?.let {
            val builder = AlertDialog.Builder(it, com.example.android.sparkstories.R.style.Theme_WriteItSayItHearIt_AlertDialogStyle)
            builder.setView(parentCommentBinding.root)
            builder.create()
        }
    }

    /**
     * Created to override during tests.
     */
    fun navController() = findNavController()


}
