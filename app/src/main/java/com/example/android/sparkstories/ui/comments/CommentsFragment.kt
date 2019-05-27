package com.example.android.sparkstories.ui.comments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.android.sparkstories.AppExecutors

import com.example.android.sparkstories.R
import com.example.android.sparkstories.databinding.FragmentCommentsBinding
import com.example.android.sparkstories.databinding.FragmentStoriesBinding
import com.example.android.sparkstories.di.Injectable
import com.example.android.sparkstories.test.OpenForTesting
import com.example.android.sparkstories.ui.cues.CueFragmentDirections
import com.example.android.sparkstories.ui.stories.StoriesViewModel
import com.example.android.sparkstories.ui.stories.StoryAdapter
import com.example.android.sparkstories.ui.util.events.EventObserver
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
@OpenForTesting
class CommentsFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var appExecutors: AppExecutors

    lateinit var commentsViewModel: CommentsViewModel

    lateinit var binding: FragmentCommentsBinding

    lateinit var commentAdapter: CommentAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_comments,
            container,
            false
        )

        commentsViewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(CommentsViewModel::class.java)

        commentAdapter = CommentAdapter(this, commentsViewModel, appExecutors)

        binding.viewmodel = commentsViewModel
        binding.listAdapter = commentAdapter
        binding.hasResults = false
        binding.executePendingBindings()

        setStoryId()
        observeComments()
        observeResultStatus()
        observeShouldNavigateToNewComment()

        return binding.root
    }

    private fun observeShouldNavigateToNewComment() {
        commentsViewModel.shouldNavigateToNewComment.observe(this, EventObserver { parentId ->
            navController().navigate(
                CommentsFragmentDirections.actionCommentsFragmentToNewCommentFragment(
                    parentId, commentsViewModel.queryParameters.filterStoryId
                )
            )
        })
    }

    private fun setStoryId() {
        val args = CommentsFragmentArgs.fromBundle(arguments!!)
        println("setting to ${args.storyId}")
        commentsViewModel.queryParameters.filterStoryId = args.storyId
    }

    private fun observeComments() {
        commentsViewModel.comments.observe(this, Observer { comments ->
            if (comments != null) {
                commentAdapter.submitList(comments)
                commentsViewModel.setHasResults(!comments.isEmpty())
            } else {
                commentsViewModel.setHasResults(false)
            }
        })
    }

    private fun observeResultStatus() {
        commentsViewModel.hasResultsStatus.observe(this, EventObserver { hasResults ->
            binding.hasResults = hasResults
            binding.executePendingBindings()
        })
    }

    /**
     * Created to override during tests.
     */
    fun navController() = findNavController()
}
