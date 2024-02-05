package com.github.iamrezamousavi.mafia

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.iamrezamousavi.mafia.databinding.PeopleItemBinding

class PlayerAdapter(
    private val playerList: ArrayList<Player>,
    val onSelect: (Int, Boolean) -> Unit,
    val onDelete: (Int) -> Unit,
) : RecyclerView.Adapter<PlayerAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: PeopleItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = PeopleItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = playerList[position]

        with(holder) {
            with(item) {
                binding.playerName.text = this.name
                binding.playerCheckBox.isChecked = this.isChecked
                binding.playerCheckBox.setOnCheckedChangeListener { _, isChecked ->
                    item.isChecked = isChecked
                    onSelect(position, isChecked)
                    notifyDataSetChanged()
                }
                binding.playerRemoveButton.setOnClickListener {
                    onDelete(position)
                    notifyDataSetChanged()
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return playerList.size
    }
}