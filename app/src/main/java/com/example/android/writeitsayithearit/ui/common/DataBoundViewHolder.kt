package com.example.android.writeitsayithearit.ui.common

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

/**
 * A generic ViewHolder that works with [ViewDataBinding]
 * @param <T> The type of the ViewDataBinding
 */
class DataBoundViewHolder<out T : ViewDataBinding> constructor(val binding: T)
    : RecyclerView.ViewHolder(binding.root)
