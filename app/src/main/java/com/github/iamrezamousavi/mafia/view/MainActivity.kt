package com.github.iamrezamousavi.mafia.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.iamrezamousavi.mafia.R
import com.github.iamrezamousavi.mafia.data.model.Player
import com.github.iamrezamousavi.mafia.data.repository.PlayerRepository
import com.github.iamrezamousavi.mafia.data.source.SharedPreferencesManager
import com.github.iamrezamousavi.mafia.databinding.ActivityMainBinding
import com.github.iamrezamousavi.mafia.viewmodel.PlayerViewModel
import com.github.iamrezamousavi.mafia.viewmodel.PlayerViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var playerAdapter: PlayerAdapter
    private lateinit var viewModel: PlayerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferencesManager = SharedPreferencesManager(this)
        val playerRepository = PlayerRepository(sharedPreferencesManager)
        val factory = PlayerViewModelFactory(playerRepository)
        viewModel = ViewModelProvider(this, factory)[PlayerViewModel::class.java]

        playerAdapter = PlayerAdapter(
            ArrayList(),
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

        viewModel.players.observe(this) { players ->
            playerAdapter.updatePlayers(players ?: ArrayList())
        }

        binding.button1.setOnClickListener {
            val players = viewModel.players.value ?: return@setOnClickListener
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
