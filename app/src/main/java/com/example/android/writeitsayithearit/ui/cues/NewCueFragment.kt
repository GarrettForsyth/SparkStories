package com.example.android.writeitsayithearit.ui.cues

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController

import com.example.android.writeitsayithearit.R
import com.example.android.writeitsayithearit.binding.BindingUtil
import com.example.android.writeitsayithearit.databinding.FragmentNewCueBinding
import com.example.android.writeitsayithearit.di.Injectable
import com.example.android.writeitsayithearit.test.OpenForTesting
import com.example.android.writeitsayithearit.model.cue.CueTextField
import com.example.android.writeitsayithearit.ui.util.events.EventObserver
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

    /**
     * Created to override during tests.
     */
    fun navController() = findNavController()

}
