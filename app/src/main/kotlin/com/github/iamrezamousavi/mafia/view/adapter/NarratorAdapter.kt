package com.github.iamrezamousavi.mafia.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.iamrezamousavi.mafia.R
import com.github.iamrezamousavi.mafia.data.model.NarratorItem
import com.github.iamrezamousavi.mafia.databinding.ItemNarratorBinding

class NarratorAdapter(
    private val context: Context,
    private val onOffClicked: (item: NarratorItem) -> Unit
) : ListAdapter<NarratorItem, NarratorAdapter.ViewHolder>(NarratorDiffUtil()) {

    inner class ViewHolder(
        private val binding: ItemNarratorBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        @Suppress("DEPRECATION")
        fun bind(item: NarratorItem) {
            binding.apply {
                playerName.text = item.player.name
                playerRole.text = context.getString(item.role.name)
                playerRoleItemCard.setCardBackgroundColor(
                    if (item.isAlive) {
                        context.resources.getColor(R.color.citizen_md_theme_primaryContainer)
                    } else {
                        context.resources.getColor(R.color.citizen_md_theme_background)
                    }
                )
                playerOffButton.setOnClickListener {
                    item.isAlive = !item.isAlive
                    notifyItemChanged(adapterPosition)
                    onOffClicked(item)
                }
            }
        }
    }

    class NarratorDiffUtil : DiffUtil.ItemCallback<NarratorItem>() {
        override fun areItemsTheSame(
            oldItem: NarratorItem,
            newItem: NarratorItem
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: NarratorItem,
            newItem: NarratorItem
        ): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding =
            ItemNarratorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
    }
}
