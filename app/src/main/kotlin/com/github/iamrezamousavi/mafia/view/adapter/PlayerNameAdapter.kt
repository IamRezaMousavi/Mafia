package com.github.iamrezamousavi.mafia.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.iamrezamousavi.mafia.data.model.PlayerRole
import com.github.iamrezamousavi.mafia.databinding.ItemPlayerNameBinding

class PlayerNameAdapter(private val onSelect: (PlayerRole) -> Unit) :
    ListAdapter<PlayerRole, PlayerNameAdapter.ViewHolder>(PlayerNameDiffUtil()) {

    inner class ViewHolder(private val binding: ItemPlayerNameBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PlayerRole) {
            binding.playerName.text = item.player.name
            binding.playerRoleItem.setOnClickListener {
                onSelect(item)
                notifyItemChanged(adapterPosition)
            }
        }
    }

    class PlayerNameDiffUtil : DiffUtil.ItemCallback<PlayerRole>() {
        override fun areItemsTheSame(oldItem: PlayerRole, newItem: PlayerRole): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PlayerRole, newItem: PlayerRole): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemPlayerNameBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
