package com.github.iamrezamousavi.mafia

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.iamrezamousavi.mafia.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var playerAdapter: PlayerAdapter
    private lateinit var playerList: ArrayList<Player>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        playerList = ArrayList()
        playerAdapter = PlayerAdapter(
            playerList,
            onSelect = { pos, isChecked ->
                Toast.makeText(this, "$pos is $isChecked", Toast.LENGTH_SHORT).show()
            },
            onDelete = { pos ->
                playerList.removeAt(pos)
                Toast.makeText(this, "Item $pos Deleted", Toast.LENGTH_SHORT).show()
            }
        )
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        binding.peopleList.layoutManager = layoutManager
        binding.peopleList.adapter = playerAdapter

        binding.button1.setOnClickListener {
            Toast.makeText(this, "Button Clicked", Toast.LENGTH_SHORT).show()
        }

        binding.textField.setEndIconOnClickListener {
            val name = binding.textField.editText?.text.toString()
            if (name.isNotEmpty()) {
                playerList.add(Player(name))
                playerAdapter.notifyItemInserted(playerList.lastIndex)
                binding.textField.editText?.setText("")
                Toast.makeText(this, name, Toast.LENGTH_SHORT).show()
            }
        }

        binding.mainToolBar.setNavigationOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        binding.mainToolBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menuItemSelectAll -> {
                    playerList.forEachIndexed { index, player ->
                        player.isChecked = true
                        playerAdapter.notifyItemChanged(index)
                    }
                    Toast.makeText(this, "Select All", Toast.LENGTH_SHORT).show()
                    true
                }

                else -> {
                    false
                }
            }
        }
    }
}
