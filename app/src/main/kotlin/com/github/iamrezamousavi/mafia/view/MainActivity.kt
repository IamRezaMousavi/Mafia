package com.github.iamrezamousavi.mafia.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.iamrezamousavi.mafia.R
import com.github.iamrezamousavi.mafia.databinding.ActivityMainBinding
import com.github.iamrezamousavi.mafia.utils.LangData
import com.github.iamrezamousavi.mafia.utils.PlayersData
import com.github.iamrezamousavi.mafia.view.adapter.PlayerAdapter
import com.github.iamrezamousavi.mafia.viewmodel.PlayerViewModel
import com.github.iamrezamousavi.mafia.viewmodel.SettingsViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var settingsViewModel: SettingsViewModel

    private val viewModel: PlayerViewModel by viewModels { PlayerViewModel.Factory }

    override fun attachBaseContext(newBase: Context?) {
        newBase?.let { context ->
            settingsViewModel = SettingsViewModel(context)
            LangData.getContextWrapper(context, settingsViewModel.language.code)
        }
        super.attachBaseContext(newBase)
    }

    @Suppress("LongMethod")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val playerAdapter = PlayerAdapter(
            onSelect = { player, isChecked ->
                player.isChecked = isChecked
                viewModel.updatePlayer(player)
            },
            onDeleteClicked = { player ->
                viewModel.removePlayer(player.id)
            }
        )

        binding.playersList.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            itemAnimator = DefaultItemAnimator()
            adapter = playerAdapter
        }

        PlayersData.players.observe(this) { players ->
            playerAdapter.submitList(players.toList())
            binding.button1.text =
                getString(R.string.select_roles, players.filter { it.isChecked }.size)
        }

        binding.button1.setOnClickListener {
            if (PlayersData.isPlayersOk()) {
                startActivity(Intent(this, RoleActivity::class.java))
            } else {
                Toast.makeText(this, R.string.player_not_enough, Toast.LENGTH_SHORT).show()
            }
        }

        binding.textField.setEndIconOnClickListener {
            val name = binding.textField.editText?.text.toString()
            if (name.isNotEmpty()) {
                viewModel.addPlayer(name)
                playerAdapter.notifyRebuild()
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
                    playerAdapter.notifyRebuild()
                    Toast.makeText(this, R.string.select_all, Toast.LENGTH_SHORT).show()
                    true
                }

                else -> {
                    false
                }
            }
        }

        LangData.language.observe(this) {
            // TODO: Change Language in settings page must change language here too
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
