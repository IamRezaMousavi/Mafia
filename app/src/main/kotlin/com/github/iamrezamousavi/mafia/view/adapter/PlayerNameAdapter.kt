package com.github.iamrezamousavi.mafia.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.iamrezamousavi.mafia.data.model.Player
import com.github.iamrezamousavi.mafia.databinding.PlayerNameItemBinding

class PlayerNameAdapter(
    private var players: ArrayList<Player>,
    private val onSelect: (Player) -> Unit
) : RecyclerView.Adapter<PlayerNameAdapter.ViewHolder>() {

    inner class ViewHolder(
        private val binding: PlayerNameItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(player: Player) {
            binding.playerName.text = player.name
            binding.playerRoleItem.visibility = View.VISIBLE
            binding.playerRoleItem.setOnClickListener {
                onSelect(player)
                binding.playerRoleItem.visibility = View.GONE
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding =
            PlayerNameItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        holder.bind(players[position])
    }

    override fun getItemCount(): Int = players.size

    @SuppressLint("NotifyDataSetChanged")
    fun refresh() {
        notifyDataSetChanged()
    }
}
