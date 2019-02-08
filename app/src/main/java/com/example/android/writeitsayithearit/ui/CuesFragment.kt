package com.example.android.writeitsayithearit.ui


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
import com.example.android.writeitsayithearit.ui.adapters.CueAdapter
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
    lateinit var appExecutors: AppExecutors

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var cuesViewModel: CuesViewModel


    private lateinit var binding: FragmentCuesBinding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_cues,
                container,
                false
        )

        binding.addCueFab.setOnClickListener {
            navController().navigate(
                    CuesFragmentDirections.actionQueuesDestToNewCueFragment()
            )
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        cuesViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(CuesViewModel::class.java)
        initializeAdapter()
    }

    private fun initializeAdapter() {
        val adapter = CueAdapter()
        binding.cuesList.adapter = adapter
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
