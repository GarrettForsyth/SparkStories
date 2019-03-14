package com.example.android.writeitsayithearit.ui.stories


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

import com.example.android.writeitsayithearit.R
import com.example.android.writeitsayithearit.databinding.FragmentStoriesBinding
import com.example.android.writeitsayithearit.di.Injectable
import com.example.android.writeitsayithearit.test.OpenForTesting
import com.example.android.writeitsayithearit.ui.util.events.EventObserver
import timber.log.Timber
import javax.inject.Inject

@OpenForTesting
class StoriesFragment @Inject constructor(): Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var storiesViewModel: StoriesViewModel

    lateinit var binding: FragmentStoriesBinding

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

        binding.viewmodel = storiesViewModel
        binding.listAdapter = StoryAdapter(storiesViewModel)
        binding.hasResults = false
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
            if (stories != null) {
                binding.listAdapter?.setList(stories)
                binding.listAdapter?.notifyDataSetChanged()
                binding.executePendingBindings()
                storiesViewModel.setHasResults(!stories.isEmpty())
            } else {
                storiesViewModel.setHasResults(false)
            }
        })
    }

    /**
     * Created to override during tests.
     */
    fun navController() = findNavController()

}
