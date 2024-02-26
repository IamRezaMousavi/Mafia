package com.github.iamrezamousavi.mafia.ui.view

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.iamrezamousavi.mafia.data.model.Player
import com.github.iamrezamousavi.mafia.databinding.PeopleItemBinding

class PlayerAdapter(
    private var players: ArrayList<Player>,
    private val onSelect: (Player, Boolean) -> Unit,
    private val onDeleteClicked: (Player) -> Unit,
) : RecyclerView.Adapter<PlayerAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: PeopleItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = PeopleItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val player = players[position]

        with(holder) {
            with(player) {
                binding.playerName.text = this.name
                binding.playerCheckBox.isChecked = this.isChecked
                binding.playerCheckBox.setOnCheckedChangeListener { _, isChecked ->
                    onSelect(this, isChecked)
                }
                binding.playerRemoveButton.setOnClickListener {
                    onDeleteClicked(player)
                }
            }
        }
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