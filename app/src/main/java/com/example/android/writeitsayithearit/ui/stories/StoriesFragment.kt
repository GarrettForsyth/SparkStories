package com.example.android.writeitsayithearit.ui.stories


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController

import com.example.android.writeitsayithearit.R
import com.example.android.writeitsayithearit.databinding.FragmentStoriesBinding
import com.example.android.writeitsayithearit.di.Injectable
import com.example.android.writeitsayithearit.test.OpenForTesting
import com.example.android.writeitsayithearit.ui.adapters.ClickListener
import com.example.android.writeitsayithearit.ui.adapters.StoryAdapter
import com.example.android.writeitsayithearit.ui.cues.CuesFragmentDirections
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 *
 */
@OpenForTesting
class StoriesFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var storiesViewModel: StoriesViewModel

    lateinit var binding: FragmentStoriesBinding

    lateinit var adapter: StoryAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_stories,
                container,
                false
        )
        initializeAdapter()

        return binding.root
    }

    private fun initializeAdapter() {
        adapter = StoryAdapter()
        adapter.setOnItemClickListener(object : ClickListener {
            override fun onItemClick(view: View, position: Int) {
                val story = adapter.getStoryAtPosition(position)
                navController().navigate(
                        StoriesFragmentDirections.actionStoriesFragmentToStoryFragment(
                                story.id
                        )
                )

            }
        })
        binding.storiesList.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        storiesViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(StoriesViewModel::class.java)

        storiesViewModel.stories.observe(this, Observer { story ->
            if (story != null) {
                adapter.setList(story)
                adapter.notifyDataSetChanged()
            }
        })
    }

    /**
     * Created to override during tests.
     */
    fun navController() = findNavController()


}
