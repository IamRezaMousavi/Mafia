package com.github.iamrezamousavi.mafia.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.iamrezamousavi.mafia.R
import com.github.iamrezamousavi.mafia.data.model.NarratorItem
import com.github.iamrezamousavi.mafia.databinding.ItemNarratorBinding
import kotlin.uuid.ExperimentalUuidApi

class NarratorAdapter(
    private val context: Context,
    private val onOffClicked: (item: NarratorItem) -> Unit
) : ListAdapter<NarratorItem, NarratorAdapter.ViewHolder>(NarratorDiffUtil()) {

    inner class ViewHolder(private val binding: ItemNarratorBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: NarratorItem) {
            binding.apply {
                playerName.text = item.player.name
                playerRole.text = context.getString(item.role.name)
                playerRoleItemCard.setCardBackgroundColor(
                    if (item.isAlive) {
                        ContextCompat.getColor(context, R.color.citizen_md_theme_primaryContainer)
                    } else {
                        ContextCompat.getColor(context, R.color.citizen_md_theme_background)
                    }
                )
                playerRoleItemCard.setOnClickListener {
                    item.showRole = !item.showRole
                    notifyItemChanged(adapterPosition)
                }
                playerRole.visibility = if (item.showRole) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
                playerOffButton.setOnClickListener {
                    item.isAlive = !item.isAlive
                    notifyItemChanged(adapterPosition)
                    onOffClicked(item)
                }
            }
        }
    }

    class NarratorDiffUtil : DiffUtil.ItemCallback<NarratorItem>() {
        @OptIn(ExperimentalUuidApi::class)
        override fun areItemsTheSame(oldItem: NarratorItem, newItem: NarratorItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: NarratorItem, newItem: NarratorItem): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemNarratorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    @SuppressLint("NotifyDataSetChanged")
    fun notifyRebuild() {
        notifyDataSetChanged()
    }
}
