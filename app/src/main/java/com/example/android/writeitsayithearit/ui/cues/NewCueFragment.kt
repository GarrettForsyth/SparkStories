package com.example.android.writeitsayithearit.ui.cues

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController

import com.example.android.writeitsayithearit.R
import com.example.android.writeitsayithearit.databinding.FragmentNewCueBinding
import com.example.android.writeitsayithearit.di.Injectable
import com.example.android.writeitsayithearit.test.OpenForTesting
import com.example.android.writeitsayithearit.ui.cues.models.CueTextField
import com.example.android.writeitsayithearit.ui.util.events.EventObserver
import com.example.android.writeitsayithearit.vo.Cue
import com.google.android.material.snackbar.Snackbar
import java.util.*
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

        observeInvalidSnackBar()
        observeShouldNavigateToCues()

        return binding.root
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
