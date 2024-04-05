package com.github.iamrezamousavi.mafia.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.iamrezamousavi.mafia.R
import com.github.iamrezamousavi.mafia.databinding.ActivityMainBinding
import com.github.iamrezamousavi.mafia.utils.LangData
import com.github.iamrezamousavi.mafia.utils.SharedData
import com.github.iamrezamousavi.mafia.viewmodel.PlayerViewModel
import com.github.iamrezamousavi.mafia.viewmodel.SettingsViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var playerAdapter: PlayerAdapter

    private lateinit var settingsViewModel: SettingsViewModel

    private val viewModel by viewModels<PlayerViewModel> {
        PlayerViewModel.Factory
    }

    override fun attachBaseContext(newBase: Context?) {
        newBase?.let { context ->
            settingsViewModel = SettingsViewModel(context)
            LangData.getContextWrapper(context, settingsViewModel.language.code)
        }
        super.attachBaseContext(newBase)
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
            }
        )
        binding.peopleList.adapter = playerAdapter
        binding.peopleList.layoutManager = LinearLayoutManager(this)

        SharedData.players.observe(this) { players ->
            playerAdapter.updatePlayers(players ?: ArrayList())
            binding.button1.text =
                getString(R.string.select_roles, players.filter { it.isChecked }.size)
        }

        binding.button1.setOnClickListener {
            if (SharedData.isPlayersOk()) {
                startActivity(Intent(this, RoleActivity::class.java))
            } else {
                Toast.makeText(this, R.string.player_not_enough, Toast.LENGTH_SHORT).show()
            }
        }

        binding.textField.setEndIconOnClickListener {
            val name = binding.textField.editText?.text.toString()
            if (name.isNotEmpty()) {
                viewModel.addPlayer(name)
                binding.textField.editText?.setText("")
                Toast.makeText(this, name, Toast.LENGTH_SHORT).show()
            }
        }

        binding.mainToolBar.setNavigationOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
            @Suppress("DEPRECATION")
            overridePendingTransition(R.anim.zoom_in, R.anim.static_anim)
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

        LangData.language.observe(this) {
            // TODO
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
