package com.github.iamrezamousavi.mafia.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.iamrezamousavi.mafia.data.model.Player
import com.github.iamrezamousavi.mafia.databinding.ItemPlayerBinding

class PlayerAdapter(
    private val onSelect: (player: Player, isChecked: Boolean) -> Unit,
    private val onDeleteClicked: (player: Player) -> Unit
) : ListAdapter<Player, PlayerAdapter.ViewHolder>(PlayerDiffUtil()) {

    inner class ViewHolder(private val binding: ItemPlayerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(player: Player) {
            binding.apply {
                playerName.text = player.name
                playerCheckBox.setOnCheckedChangeListener(null)
                playerCheckBox.isChecked = player.isChecked
                playerCheckBox.setOnCheckedChangeListener { _, isChecked ->
                    onSelect(player, isChecked)
                }
                playerRemoveButton.setOnClickListener {
                    onDeleteClicked(player)
                }
            }
        }
    }

    class PlayerDiffUtil : DiffUtil.ItemCallback<Player>() {
        override fun areItemsTheSame(oldItem: Player, newItem: Player): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Player, newItem: Player): Boolean =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPlayerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
