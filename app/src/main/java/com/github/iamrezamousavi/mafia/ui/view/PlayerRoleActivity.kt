package com.github.iamrezamousavi.mafia.ui.view

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.github.iamrezamousavi.mafia.data.model.Player
import com.github.iamrezamousavi.mafia.data.repository.PlayerRepository
import com.github.iamrezamousavi.mafia.data.source.SharedPreferencesManager
import com.github.iamrezamousavi.mafia.databinding.ActivityPlayerRoleBinding
import com.github.iamrezamousavi.mafia.ui.viewmodel.PlayerRoleViewModel
import com.github.iamrezamousavi.mafia.ui.viewmodel.PlayerRoleViewModelFactory
import com.github.iamrezamousavi.mafia.ui.viewmodel.PlayerViewModel
import com.github.iamrezamousavi.mafia.ui.viewmodel.PlayerViewModelFactory

class PlayerRoleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlayerRoleBinding

    private lateinit var playerRoleAdapter: PlayerRoleAdapter
    private lateinit var playerViewModel: PlayerViewModel
    private lateinit var playerRoleViewModel: PlayerRoleViewModel
    private lateinit var players: ArrayList<Player>
    private lateinit var roles: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerRoleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferencesManager = SharedPreferencesManager(this)
        val playerRepository = PlayerRepository(sharedPreferencesManager)
        val factory = PlayerViewModelFactory(playerRepository)
        playerViewModel = ViewModelProvider(this, factory)[PlayerViewModel::class.java]
        playerViewModel.loadPlayers()

        Log.d("TAG", "onCreate: Player Loaded")

        val playerRoleFactory = PlayerRoleViewModelFactory()
        playerRoleViewModel =
            ViewModelProvider(this, playerRoleFactory)[PlayerRoleViewModel::class.java]

        Log.d("TAG", "onCreate: create playerRoleViewModel")

        players = ArrayList(
            playerViewModel.getPlayers().value?.filter { it.isChecked } ?: ArrayList()
        )
        playerRoleViewModel.setPlayers(players)

        Log.d("TAG", "onCreate: set players: $players")

        roles = ArrayList(
            intent.extras?.getString("roles")?.split(",")?.map { it.trim(' ', '[', ']') }
                ?: ArrayList()
        )

        while (roles.size < players.size) {
            roles.add("شهروند")
        }
        roles.shuffled()
        roles.shuffled()

        Log.d("TAG", "onCreate: create roles: $roles")

        playerRoleAdapter = PlayerRoleAdapter(
            players,
            onSelect = { player ->
                val index = players.indexOf(player)
                val playerRole = roles[index]

                AlertDialog.Builder(this)
                    .setTitle(playerRole)
                    .setMessage("${player.name}: $playerRole")
                    .setPositiveButton("Ok", DialogInterface.OnClickListener { dialog, _ ->
                        dialog.cancel()
                    })
                    .show()
            }
        )
        binding.playerRoleList.adapter = playerRoleAdapter
        binding.playerRoleList.layoutManager = GridLayoutManager(this, 3)

        Log.d("TAG", "onCreate: create playerRoleAdapter")

        playerRoleViewModel.getPlayers().observe(this) { players ->
            playerRoleAdapter.updatePlayers(players)
        }

        Log.d("TAG", "onCreate: bind playerRoleAdapter to viewModel")
    }
}