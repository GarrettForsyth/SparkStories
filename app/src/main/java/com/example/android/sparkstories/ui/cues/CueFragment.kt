package com.example.android.sparkstories.ui.cues

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.android.sparkstories.R
import com.example.android.sparkstories.databinding.FragmentCueBinding
import com.example.android.sparkstories.di.Injectable
import com.example.android.sparkstories.model.Status
import com.example.android.sparkstories.test.OpenForTesting
import com.example.android.sparkstories.ui.stories.StoriesFragment
import com.example.android.sparkstories.ui.util.events.EventObserver
import javax.inject.Inject

@OpenForTesting
class CueFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var storiesFragment: StoriesFragment

    lateinit var cueViewModel: CueViewModel

    lateinit var binding: FragmentCueBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        childFragmentManager.beginTransaction()
            .replace(R.id.stories_fragment_container, storiesFragment)
            .commit()

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_cue,
            container,
            false
        )

        cueViewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(CueViewModel::class.java)
        binding.viewmodel = cueViewModel

        observeCue()
        observeNewStoryButtonClick()
        return binding.root
    }

    private fun observeCue() {
        val args = CueFragmentArgs.fromBundle(arguments!!)
        cueViewModel.getCue(args.cueId)

        cueViewModel.cue.observe(this, Observer { cue ->
            if (cue.status == Status.SUCCESS) {
                binding.cue = cue.data
                binding.executePendingBindings()
                storiesFragment.storiesViewModel.queryParameters.filterCueId = cue.data?.id!!
            }
        })
    }

    private fun observeNewStoryButtonClick() {
        cueViewModel.newStoryButtonClick.observe(this, EventObserver {
            binding.cue?.let {
                navController().navigate(
                    CueFragmentDirections
                        .actionCueFragmentToNewStoryFragment(it.id)
                )
            }
        })
    }

    fun navController() = findNavController()

}
