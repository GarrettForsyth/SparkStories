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
import com.example.android.writeitsayithearit.AppExecutors

import com.example.android.writeitsayithearit.R
import com.example.android.writeitsayithearit.databinding.FragmentNewCueBinding
import com.example.android.writeitsayithearit.di.Injectable
import com.example.android.writeitsayithearit.test.OpenForTesting
import com.example.android.writeitsayithearit.vo.Cue
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 *
 */
@OpenForTesting
class NewCueFragment : Fragment(), Injectable {

    @Inject
    lateinit var appExecutors: AppExecutors

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var newCueViewModel: NewCueViewModel

    private lateinit var binding: FragmentNewCueBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_new_cue,
                container,
                false
        )

        val minCueTextLength = context?.resources?.getInteger(R.integer.min_cue_text_length)!!
        val maxCueTextLength = context?.resources?.getInteger(R.integer.max_cue_text_length)!!

        binding.submitCueBtn.setOnClickListener {
            // TODO: validation checks probably shouldn't be done in the fragment
            // consider moving validation logic into the model and propagating errors.

            if (isValidCue(binding, minCueTextLength, maxCueTextLength)) {
                submitCueAndNavigate()
            } else {
                showInvalidCueSnackBar(minCueTextLength, maxCueTextLength)
            }
        }

        return binding.root
    }

    private fun isValidCue(binding: FragmentNewCueBinding, min: Int, max: Int): Boolean {
        binding.invalidateAll()
        val cueText = binding.newCueEditText.text.toString().trim()
        val cueTextLength = cueText.length
        return cueTextLength in min..max && !cueText.isBlank()
    }

    private fun submitCueAndNavigate() {
        val newCue = Cue(0, binding.newCueEditText.text.toString().trim())
        newCueViewModel.submitCue(newCue)
        navController().navigate(
                NewCueFragmentDirections.actionNewCueFragmentToCuesFragment()
        )
    }

    private fun showInvalidCueSnackBar(min: Int, max: Int) {
        Snackbar.make(
                binding.newCueConstraintLayout,
                getString(R.string.new_cue_invalid_message, min, max),
                Snackbar.LENGTH_SHORT
        ).show()
    }


    override fun onStart() {
        super.onStart()
        newCueViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(NewCueViewModel::class.java)

    }


    /**
     * Created to override during tests.
     */
    fun navController() = findNavController()

}
