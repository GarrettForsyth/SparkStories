package com.example.android.writeitsayithearit.ui.stories


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController

import com.example.android.writeitsayithearit.R
import com.example.android.writeitsayithearit.databinding.FragmentStoriesBinding
import com.example.android.writeitsayithearit.di.Injectable
import com.example.android.writeitsayithearit.test.OpenForTesting
import com.example.android.writeitsayithearit.ui.adapters.ClickListener
import com.example.android.writeitsayithearit.ui.adapters.StoryAdapter
import com.example.android.writeitsayithearit.vo.SortOrder
import timber.log.Timber
import javax.inject.Inject

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

        initializeFilter()
        initializeSortOrderSpinner()
        initializeAdapter()

        return binding.root
    }

    private fun initializeFilter() {
        binding.filterStoriesEditText.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(text: CharSequence?, start: Int, end: Int, count: Int) {
                storiesViewModel.filterQuery(text.toString())
            }
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })
    }

    private fun initializeSortOrderSpinner() {
        val spinnerAdapter = ArrayAdapter.createFromResource(
                context!!,
                R.array.sort_order,
                android.R.layout.simple_spinner_item
        )
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.sortOrderSpinner.adapter = spinnerAdapter

        binding.sortOrderSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}

            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when (position) {
                    0 -> storiesViewModel.sortOrder(SortOrder.NEW)
                    1 -> storiesViewModel.sortOrder(SortOrder.TOP)
                    else -> storiesViewModel.sortOrder(SortOrder.HOT)
                }
            }
        }
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
