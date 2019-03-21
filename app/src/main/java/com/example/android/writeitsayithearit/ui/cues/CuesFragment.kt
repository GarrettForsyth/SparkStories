package com.example.android.writeitsayithearit.ui.cues


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.android.writeitsayithearit.AppExecutors

import com.example.android.writeitsayithearit.R
import com.example.android.writeitsayithearit.databinding.FragmentCuesBinding
import com.example.android.writeitsayithearit.di.Injectable
import com.example.android.writeitsayithearit.test.OpenForTesting
import com.example.android.writeitsayithearit.ui.util.events.EventObserver
import kotlinx.android.synthetic.main.fragment_cues.*
import timber.log.Timber
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

    @Inject
    lateinit var appExecutors: AppExecutors

    lateinit var cuesViewModel: CuesViewModel

    private lateinit var binding: FragmentCuesBinding
    private lateinit var cueAdapter: CueAdapter

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

        cueAdapter = CueAdapter(cuesViewModel, appExecutors)

        binding.viewmodel = cuesViewModel
        binding.listAdapter = cueAdapter
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
                    .actionCuesFragmentToCueFragment(cueId)
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
                /**
                 * TODO: null is submitted before the list to achieve the behaviour
                 * of having the list start from the top.
                 *
                 * Without this line, the list will follow the topmost visible viewholder
                 * which leads to unintuitive effects (e.g. changing sort order from
                 * new --> top will follow the topmost vh somewhere in the middle of the list.)
                 *
                 * Telling the RecyclerView or LinearLayoutManager to scroll to the top
                 * after a data change doesn't do anything. I think this is because the
                 * list hasn't had a chance to fully update/become visible yet.
                 *
                 * A better solution might be to somehow watch until the view become visible
                 * and THEN scroll to the top of the list. This way, the benefits from DiffUtils
                 * are used (where as here, they aren't). This might be a premature optimization,
                 * but keep an eye on this.
                 */
                cueAdapter.submitList(null)
                cueAdapter.submitList(cues)
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
