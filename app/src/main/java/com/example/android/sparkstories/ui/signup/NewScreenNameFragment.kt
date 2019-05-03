package com.example.android.sparkstories.ui.signup


import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController

import com.example.android.sparkstories.R
import com.example.android.sparkstories.databinding.FragmentNewScreenNameBinding
import com.example.android.sparkstories.di.Injectable
import com.example.android.sparkstories.model.Status
import com.example.android.sparkstories.model.cue.CueTextField
import com.example.android.sparkstories.test.CustomMatchers.first
import com.example.android.sparkstories.test.OpenForTesting
import com.example.android.sparkstories.ui.stories.NewStoryViewModel.Companion.PREFERENCE_AUTHOR
import com.example.android.sparkstories.ui.util.events.EventObserver
import com.example.android.sparkstories.ui.util.events.hideKeyboard
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.fragment_new_screen_name.*
import timber.log.Timber
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
@OpenForTesting
class NewScreenNameFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory


    lateinit var newScreenNameViewModel: NewScreenNameViewModel

    private lateinit var binding: FragmentNewScreenNameBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_new_screen_name,
            container,
            false
        )

        newScreenNameViewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(NewScreenNameViewModel::class.java)

        binding.viewmodel = newScreenNameViewModel
        binding.isLoading = false

        observeShouldNavigateToCues()
        observeSubmitScreenNameResponse()
        observeScreenNameFieldError()
        observeScreenNameAvailability()
        observeInvalidScreenNameSnackbar()

        return binding.root
    }

    private fun observeShouldNavigateToCues() {
        newScreenNameViewModel.shouldNavigateToCues.observe(this, EventObserver {
            navController().navigate(
                NewScreenNameFragmentDirections.actionCreateScreenNameFragmentToCuesFragment()
            )
        })
    }


    private fun observeSubmitScreenNameResponse() {
        hideKeyboard(activity!!, binding.newScreenNameConstraintLayout)
        newScreenNameViewModel.submitScreenNameResponse.observe(this, Observer { result ->
            when (result?.status) {
                Status.SUCCESS -> {
                    navController().navigate(
                        NewScreenNameFragmentDirections.actionCreateScreenNameFragmentToCuesFragment()
                    )
                    binding.isLoading = false
                }
                Status.LOADING -> {
                    hideKeyboard(activity!!, binding.newScreenNameConstraintLayout)
                    binding.isLoading = true
                }
                Status.ERROR -> {
                    Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                    binding.isLoading = false
                }
            }
        })
    }

    private fun observeScreenNameFieldError() {
        newScreenNameViewModel.screenNameFieldErrorMessage.observe(this, Observer { message ->
            message?.let {
                binding.textInputLayout.error = message
            }
        })
    }

    fun observeScreenNameAvailability() {
        newScreenNameViewModel.isScreenNameAvailableResponse.observe(this, Observer { availabilityResponse ->
            // Ignore Loading and Error results
            if (availabilityResponse?.status == Status.SUCCESS) {
                    val isAvailable = availabilityResponse.data?.first!!
                    val screenName = availabilityResponse.data.second

                    if (newScreenNameViewModel.screenName.text == screenName && !isAvailable) {
                        binding.textInputLayout.error = getString(R.string.name_unavailable)
                    } else {
                        binding.textInputLayout.error = ""
                    }
            }
        })
    }

    private fun observeInvalidScreenNameSnackbar() {
        newScreenNameViewModel.invalidScreenNameSnackBar.observe(this, EventObserver {
            hideKeyboard(activity!!, binding.newScreenNameConstraintLayout)
            Snackbar.make(
                binding.newScreenNameConstraintLayout,
                text_input_layout.error?.toString() ?: getString(R.string.screen_name_cannot_be_blank),
                Snackbar.LENGTH_SHORT
            ).show()
        })
    }

    override fun onResume() {
        binding.root.rootView.bottom_navigation?.visibility = View.GONE
        super.onResume()
    }

    override fun onPause() {
        binding.root.rootView.bottom_navigation?.visibility = View.VISIBLE
        super.onPause()
    }

    fun navController() = findNavController()

}
