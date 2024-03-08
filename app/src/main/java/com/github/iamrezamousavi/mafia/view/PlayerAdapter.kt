package com.github.iamrezamousavi.mafia.view

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.iamrezamousavi.mafia.data.model.Player
import com.github.iamrezamousavi.mafia.databinding.PlayerItemBinding

class PlayerAdapter(
    private var players: ArrayList<Player>,
    private val onSelect: (Player, Boolean) -> Unit,
    private val onDeleteClicked: (Player) -> Unit,
) : RecyclerView.Adapter<PlayerAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: PlayerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(player: Player) {
            binding.playerName.text = player.name
            binding.playerCheckBox.setOnCheckedChangeListener(null)
            binding.playerCheckBox.isChecked = player.isChecked
            binding.playerCheckBox.setOnCheckedChangeListener { _, isChecked ->
                onSelect(player, isChecked)
            }
            binding.playerRemoveButton.setOnClickListener {
                onDeleteClicked(player)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = PlayerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(players[position])
    }

    override fun getItemCount(): Int {
        return players.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updatePlayers(newPlayers: ArrayList<Player>) {
        players = newPlayers
        notifyDataSetChanged()
    }
}