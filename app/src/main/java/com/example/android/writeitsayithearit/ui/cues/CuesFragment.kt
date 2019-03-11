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
import com.example.android.writeitsayithearit.databinding.FragmentCuesBinding
import com.example.android.writeitsayithearit.di.Injectable
import com.example.android.writeitsayithearit.test.OpenForTesting
import com.example.android.writeitsayithearit.ui.util.events.EventObserver
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 *
 * Note: can use field injections after onStart
 *
 */
@OpenForTesting
class CuesFragment : Fragment(), Injectable {



    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var cuesViewModel: CuesViewModel

    private lateinit var binding: FragmentCuesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_cues,
            container,
            false
        )

        cuesViewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(CuesViewModel::class.java)

        binding.viewmodel = cuesViewModel
        binding.listAdapter = CueAdapter(cuesViewModel)
        binding.hasResults = false
        binding.executePendingBindings()

        observeCues()
        observeResultStatus()
        observeCueClick()
        observeNewCueFabClick()

        return binding.root
    }

    private fun observeNewCueFabClick() {
        cuesViewModel.newCueFabClick.observe(this, EventObserver {
            navController().navigate(
                CuesFragmentDirections
                    .actionCuesFragmentToNewCueFragment()
            )
        })
    }

    private fun observeCueClick() {
        cuesViewModel.cueClicked.observe(this, EventObserver { cueId ->
            navController().navigate(
                CuesFragmentDirections
                    .actionCuesFragmentToNewStoryFragment(cueId)
            )
        })
    }

    private fun observeResultStatus() {
        cuesViewModel.hasResultsStatus.observe(this, EventObserver { hasResults ->
            binding.hasResults = hasResults
            binding.executePendingBindings()
        })
    }

    private fun observeCues() {
        cuesViewModel.cues.observe(this, Observer { cues ->
            if (cues != null) {
                binding.listAdapter?.setList(cues)
                binding.listAdapter?.notifyDataSetChanged()
                binding.executePendingBindings()
                cuesViewModel.setHasResults(!cues.isEmpty())
            } else {
                cuesViewModel.setHasResults(false)
            }
        })
    }

    /**
     * Created to override during tests.
     */
    fun navController() = findNavController()

}
