package com.github.iamrezamousavi.mafia.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.iamrezamousavi.mafia.R
import com.github.iamrezamousavi.mafia.data.model.Player
import com.github.iamrezamousavi.mafia.databinding.ActivityMainBinding
import com.github.iamrezamousavi.mafia.utils.SharedData
import com.github.iamrezamousavi.mafia.viewmodel.PlayerViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var playerAdapter: PlayerAdapter
    private val viewModel: PlayerViewModel by viewModels {
        PlayerViewModel.Factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        playerAdapter = PlayerAdapter(
            SharedData.getPlayers(),
            onSelect = { player, isChecked ->
                player.isChecked = isChecked
                viewModel.updatePlayer(player)
            },
            onDeleteClicked = { player ->
                viewModel.removePlayer(player.id)
            },
        )
        binding.peopleList.adapter = playerAdapter
        binding.peopleList.layoutManager = LinearLayoutManager(this)

        SharedData.players.observe(this) { players ->
            playerAdapter.updatePlayers(players ?: ArrayList())
        }

        binding.button1.setOnClickListener {
            val players = SharedData.getPlayers()
            if (players.size == 0) return@setOnClickListener

            val playerCheckedSize = players.filter { it.isChecked }.size
            if (playerCheckedSize < 3) {
                Toast.makeText(this, R.string.player_not_enough, Toast.LENGTH_SHORT).show()
            } else {
                startActivity(Intent(this, RoleActivity::class.java))
            }
        }

        binding.textField.setEndIconOnClickListener {
            val name = binding.textField.editText?.text.toString()
            if (name.isNotEmpty()) {
                viewModel.addPlayer(Player(name = name))
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
                    viewModel.selectAllPlayer()
                    Toast.makeText(this, R.string.select_all, Toast.LENGTH_SHORT).show()
                    true
                }

                else -> {
                    false
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.savePlayers()
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadPlayers()
    }
}
