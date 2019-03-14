package com.example.android.writeitsayithearit.ui.cues

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
import com.example.android.writeitsayithearit.R
import com.example.android.writeitsayithearit.databinding.FragmentCueBinding
import com.example.android.writeitsayithearit.di.Injectable
import com.example.android.writeitsayithearit.test.OpenForTesting
import com.example.android.writeitsayithearit.ui.stories.StoriesFragment
import com.example.android.writeitsayithearit.ui.util.events.EventObserver
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
            cue?.let {
                binding.cue = cue
                binding.executePendingBindings()
                storiesFragment.storiesViewModel.filterCue(cue.id)
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
