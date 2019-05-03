package com.example.android.sparkstories.ui.cues

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.test.espresso.action.Swiper

import com.example.android.sparkstories.R
import com.example.android.sparkstories.binding.BindingUtil
import com.example.android.sparkstories.databinding.FragmentNewCueBinding
import com.example.android.sparkstories.di.Injectable
import com.example.android.sparkstories.model.Status
import com.example.android.sparkstories.test.OpenForTesting
import com.example.android.sparkstories.model.cue.CueTextField
import com.example.android.sparkstories.ui.signup.NewScreenNameFragmentDirections
import com.example.android.sparkstories.ui.util.events.EventObserver
import com.example.android.sparkstories.ui.util.events.hideKeyboard
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber
import javax.inject.Inject

@OpenForTesting
class NewCueFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var newCueViewModel: NewCueViewModel

    private lateinit var binding: FragmentNewCueBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_new_cue,
            container,
            false
        )

        newCueViewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(NewCueViewModel::class.java)
        binding.viewmodel = newCueViewModel
        binding.executePendingBindings()

        observeInvalidSnackBar()
        observeShouldNavigateToCues()
        observeNewCueEditTextFocus()
        observeSubmitCueResponse()

        binding.isLoading = false

        return binding.root
    }


    private fun observeNewCueEditTextFocus() {
        newCueViewModel.newCueEditTextFocusStatus.observe(this, EventObserver { hasFocus ->
            binding.newCueEditTextHasFocus = hasFocus
            binding.executePendingBindings()
        })
    }

    private fun observeShouldNavigateToCues() {
        newCueViewModel.shouldNavigateToCues.observe(this, EventObserver {
            navController().navigate(
                NewCueFragmentDirections.actionNewCueFragmentToCuesFragment()
            )
        })
    }

    private fun observeInvalidSnackBar() {
        newCueViewModel.invalidCueSnackBar.observe(this, EventObserver {
            Snackbar.make(
                binding.newCueConstraintLayout,
                getString(
                    R.string.invalid_new_cue_snackbar,
                    CueTextField.minCharacters,
                    CueTextField.maxCharacters
                ),
                Snackbar.LENGTH_SHORT
            ).show()
        })
    }

    private fun observeSubmitCueResponse() {
        hideKeyboard(activity!!, binding.newCueConstraintLayout)
        newCueViewModel.submitCueResponse.observe(this, Observer { result ->
            when (result?.status) {
                Status.SUCCESS -> {
                    navController().navigate(
                        NewCueFragmentDirections.actionNewCueFragmentToCuesFragment()
                    )
                    binding.isLoading = false
                }
                Status.LOADING -> {
                    hideKeyboard(activity!!, binding.newCueConstraintLayout)
                    binding.isLoading = true
                }
                Status.ERROR -> {
                    Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
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
