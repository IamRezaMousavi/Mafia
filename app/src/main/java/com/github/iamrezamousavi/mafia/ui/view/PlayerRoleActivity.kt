package com.github.iamrezamousavi.mafia.ui.view

import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.github.iamrezamousavi.mafia.data.model.Player
import com.github.iamrezamousavi.mafia.data.repository.PlayerRepository
import com.github.iamrezamousavi.mafia.data.source.SharedPreferencesManager
import com.github.iamrezamousavi.mafia.databinding.ActivityPlayerRoleBinding
import com.github.iamrezamousavi.mafia.ui.viewmodel.PlayerViewModel
import com.github.iamrezamousavi.mafia.ui.viewmodel.PlayerViewModelFactory

class PlayerRoleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlayerRoleBinding

    private lateinit var playerRoleAdapter: PlayerRoleAdapter
    private lateinit var viewModel: PlayerViewModel
    private lateinit var players: ArrayList<Player>
    private lateinit var roles: List<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerRoleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        roles = intent.extras?.getString("roles")?.split(",")?.map { it.trim(' ', '[', ']') }
            ?: listOf()

        val sharedPreferencesManager = SharedPreferencesManager(this)
        val playerRepository = PlayerRepository(sharedPreferencesManager)
        val factory = PlayerViewModelFactory(playerRepository)
        viewModel = ViewModelProvider(this, factory)[PlayerViewModel::class.java]
        viewModel.loadPlayers()


        players = ArrayList(
            viewModel.getPlayers().value?.filter { it.isChecked } ?: ArrayList()
        )
        playerRoleAdapter = PlayerRoleAdapter(
            players,
            onSelect = { player ->
                AlertDialog.Builder(this)
                    .setTitle("Citizen")
                    .setMessage("${player.name} must find mafias")
                    .setPositiveButton("Ok", DialogInterface.OnClickListener { dialog, _ ->
                        dialog.cancel()
                    })
                    .show()
            }
        )
        binding.playerRoleList.adapter = playerRoleAdapter
        binding.playerRoleList.layoutManager = GridLayoutManager(this, 3)
    }
}