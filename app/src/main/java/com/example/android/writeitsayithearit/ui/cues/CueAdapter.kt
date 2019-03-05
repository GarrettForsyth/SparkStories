package com.example.android.writeitsayithearit.ui.cues

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.example.android.writeitsayithearit.R
import com.example.android.writeitsayithearit.databinding.CueListItemBinding
import com.example.android.writeitsayithearit.model.cue.Cue

class CueAdapter(private val viewModel: CuesViewModel) : RecyclerView.Adapter<CueViewHolder>() {

    private var cues: List<Cue>?  = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CueViewHolder {


        val binding : CueListItemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.cue_list_item,
                parent,
                false
        )


        binding.viewmodel = viewModel
        return CueViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CueViewHolder, position: Int) {
        val cue = cues!!.get(position)
        holder.bind(cue)
    }

    override fun getItemCount() = cues?.size ?: 0

    fun setList(cues: List<Cue>) {
        this.cues = cues
    }
}