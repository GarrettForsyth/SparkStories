package com.example.android.sparkstories.ui.stories


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.android.sparkstories.AppExecutors

import com.example.android.sparkstories.R
import com.example.android.sparkstories.databinding.FragmentStoriesBinding
import com.example.android.sparkstories.di.Injectable
import com.example.android.sparkstories.model.Status
import com.example.android.sparkstories.test.OpenForTesting
import com.example.android.sparkstories.ui.util.events.EventObserver
import timber.log.Timber
import javax.inject.Inject

@OpenForTesting
class StoriesFragment @Inject constructor() : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var appExecutors: AppExecutors

    lateinit var storiesViewModel: StoriesViewModel

    lateinit var binding: FragmentStoriesBinding

    lateinit var storyAdapter: StoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_stories,
            container,
            false
        )

        storiesViewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(StoriesViewModel::class.java)

        storyAdapter = StoryAdapter(storiesViewModel, appExecutors)

        binding.viewmodel = storiesViewModel
        binding.listAdapter = storyAdapter
        binding.hasResults = false
        binding.isLoading = false

        binding.executePendingBindings()

        observeStories()
        observeResultStatus()
        observeStoryClick()

        return binding.root
    }

    private fun observeStoryClick() {
        storiesViewModel.storyClicked.observe(this, EventObserver { storyId ->
            navController().navigate(
                StoriesFragmentDirections
                    .actionStoriesFragmentToStoryFragment(storyId)
            )
        })
    }

    private fun observeResultStatus() {
        storiesViewModel.hasResultsStatus.observe(this, EventObserver { hasResults ->
            binding.hasResults = hasResults
            binding.executePendingBindings()
        })
    }

    private fun observeStories() {
        storiesViewModel.stories.observe(this, Observer { stories ->
            when (stories?.status) {
                Status.SUCCESS -> {
                    storyAdapter.submitList(null)
                    storyAdapter.submitList(stories.data)
                    storiesViewModel.setHasResults(!(stories.data?.isEmpty() ?: false))
                    binding.isLoading = false
                }
                Status.LOADING -> {
                    binding.isLoading = true
                }
                Status.ERROR -> {
                    binding.isLoading = false
                }
            }
        })
    }

    /**
     * Created to override during tests.
     */
    fun navController() = findNavController()

}
