package com.example.android.writeitsayithearit.ui.comments


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
import com.example.android.writeitsayithearit.AppExecutors

import com.example.android.writeitsayithearit.R
import com.example.android.writeitsayithearit.databinding.FragmentCommentsBinding
import com.example.android.writeitsayithearit.databinding.FragmentStoriesBinding
import com.example.android.writeitsayithearit.di.Injectable
import com.example.android.writeitsayithearit.test.OpenForTesting
import com.example.android.writeitsayithearit.ui.stories.StoriesViewModel
import com.example.android.writeitsayithearit.ui.stories.StoryAdapter
import com.example.android.writeitsayithearit.ui.util.events.EventObserver
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

        return binding.root
    }

    private fun setStoryId() {
        val args = CommentsFragmentArgs.fromBundle(arguments!!)
        commentsViewModel.queryParameters.filterId = args.storyId
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
