package com.github.iamrezamousavi.mafia.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.iamrezamousavi.mafia.data.model.Player
import com.github.iamrezamousavi.mafia.databinding.PlayerRoleAliveItemBinding

class PlayerRoleAdapter(
    private val onOffClicked: (player: Player) -> Unit
) : ListAdapter<Player, PlayerRoleAdapter.ViewHolder>(PlayerDiffUtil()) {

    inner class ViewHolder(
        private val binding: PlayerRoleAliveItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(player: Player) {
            binding.apply {
                playerName.text = player.name
                playerRole.text = player.name
                playerOffButton.setOnClickListener {
                    onOffClicked(player)
                }
            }
        }
    }

    class PlayerDiffUtil : DiffUtil.ItemCallback<Player>() {
        override fun areItemsTheSame(
            oldItem: Player,
            newItem: Player
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Player,
            newItem: Player
        ): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding =
            PlayerRoleAliveItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
    }
}
