package com.example.android.writeitsayithearit.ui.cues

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.example.android.writeitsayithearit.AppExecutors
import com.example.android.writeitsayithearit.R
import com.example.android.writeitsayithearit.databinding.CueListItemBinding
import com.example.android.writeitsayithearit.model.cue.Cue
import com.example.android.writeitsayithearit.ui.common.DataBoundListAdapter
import timber.log.Timber

class CueAdapter(
    private val viewModel: CuesViewModel,
    appExecutors: AppExecutors
) : DataBoundListAdapter<Cue, CueListItemBinding>(
    appExecutors = appExecutors,
    diffCallback = Cue.cueDiffCallback
) {
    override fun createBinding(parent: ViewGroup): CueListItemBinding {
        val binding : CueListItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.cue_list_item,
            parent,
            false
        )
        binding.viewmodel = viewModel
        return binding
    }

    override fun bind(binding: CueListItemBinding, item: Cue) {
        binding.cue = item
        binding.viewmodel = viewModel
    }
}