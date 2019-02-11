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
import com.example.android.writeitsayithearit.AppExecutors

import com.example.android.writeitsayithearit.R
import com.example.android.writeitsayithearit.databinding.FragmentCuesBinding
import com.example.android.writeitsayithearit.di.Injectable
import com.example.android.writeitsayithearit.test.OpenForTesting
import com.example.android.writeitsayithearit.ui.adapters.ClickListener
import com.example.android.writeitsayithearit.ui.adapters.CueAdapter
import com.example.android.writeitsayithearit.ui.stories.NewStoryFragmentArgs
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

    lateinit var cuesViewModel: CuesViewModel


    private lateinit var binding: FragmentCuesBinding

    private lateinit var adapter: CueAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_cues,
                container,
                false
        )
        initializeFab()
        initializeAdapter()

        return binding.root
    }

    private fun initializeFab() {
        binding.addCueFab.setOnClickListener {
            navController().navigate(
                    CuesFragmentDirections.actionCuesFragmentToNewCueFragment()
            )
        }

    }

    private fun initializeAdapter() {
        adapter = CueAdapter()
        adapter.setOnItemClickListener(object : ClickListener {
            override fun onItemClick(view: View, position: Int) {
                val cue = adapter.getCueAtPosition(position)
                navController().navigate(
                        CuesFragmentDirections.actionCuesFragmentToNewStoryFragment(
                                cue.id
                        )
                )

            }
        })
        binding.cuesList.adapter = adapter
    }

    /**
     * TODO: Figure out why ViewModel can't be set before onStart()
     */
    override fun onStart() {
        super.onStart()
        cuesViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(CuesViewModel::class.java)

        cuesViewModel.cues.observe(this, Observer { cues ->
            if (cues != null) {
                adapter.setList(cues)
                adapter.notifyDataSetChanged()
            }
        })

    }

    /**
     * Created to override during tests.
     */
    fun navController() = findNavController()

}
