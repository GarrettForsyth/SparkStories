package com.example.android.writeitsayithearit.ui.stories


import android.os.Build.VERSION_CODES.N
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
import com.example.android.writeitsayithearit.databinding.FragmentNewStoryBinding
import com.example.android.writeitsayithearit.di.Injectable
import com.example.android.writeitsayithearit.test.OpenForTesting
import com.example.android.writeitsayithearit.vo.Story
import com.google.android.material.snackbar.Snackbar
import java.util.*
import javax.inject.Inject

@OpenForTesting
class NewStoryFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var newStoryViewModel: NewStoryViewModel

    lateinit var binding: FragmentNewStoryBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_new_story,
                container,
                false
        )

        val minStoryTextLength = context!!.resources!!.getInteger(R.integer.min_story_text_length)!!
        val maxStoryTextLength = context!!.resources!!.getInteger(R.integer.max_story_text_length)!!

        binding.submitStoryBtn.setOnClickListener {
            // TODO: validation checks probably shouldn't be done in the fragment
            // consider moving validation logic into the model and propagating errors.

            if (isValidStory(binding, minStoryTextLength, maxStoryTextLength)) {
                submitStoryAndNavigate()
            } else {
                showInvalidStorySnackBar(minStoryTextLength, maxStoryTextLength)
            }
        }

        return binding.root
    }

    private fun isValidStory(binding: FragmentNewStoryBinding, min: Int, max: Int): Boolean {
        binding.invalidateAll()
        val storyText = binding.newStoryEditText.text.toString().trim()
        val storyTextLength = storyText.length
        return storyTextLength in min..max
    }

    private fun submitStoryAndNavigate() {
        val creationTime = Calendar.getInstance().timeInMillis
        val newStory = Story(
                binding.newStoryEditText.text.toString().trim(),
                binding.cue!!.id,
                creationTime,
                0)
        newStoryViewModel.submitStory(newStory)
        navController().navigate(NewStoryFragmentDirections
                .actionNewStoryFragmentToStoriesFragment())
    }

    private fun showInvalidStorySnackBar(min: Int, max: Int) {
        Snackbar.make(
                binding.newStoryConstraintLayout,
                getString(R.string.new_story_invalid_message, min, max),
                Snackbar.LENGTH_SHORT
        ).show()
    }

    override fun onStart() {
        super.onStart()
        newStoryViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(NewStoryViewModel::class.java)

        // use arguments to display the cue
        val args = NewStoryFragmentArgs.fromBundle(arguments!!)
        newStoryViewModel.cue(args.cueId).observe(this, Observer { cue ->
            binding.cue = cue
        })
    }

    /**
     * Created to override during tests.
     */
    fun navController() = findNavController()

}
