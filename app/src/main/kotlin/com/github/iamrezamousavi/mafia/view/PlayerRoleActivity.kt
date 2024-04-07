package com.github.iamrezamousavi.mafia.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.github.iamrezamousavi.mafia.R
import com.github.iamrezamousavi.mafia.databinding.ActivityPlayerRoleBinding
import com.github.iamrezamousavi.mafia.utils.PlayersData
import com.github.iamrezamousavi.mafia.view.adapter.PlayerNameAdapter
import com.github.iamrezamousavi.mafia.view.dialog.PlayerDialog
import com.github.iamrezamousavi.mafia.view.dialog.RoleDoneDialog

class PlayerRoleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerRoleBinding

    private lateinit var playerNameAdapter: PlayerNameAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerRoleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        PlayersData.refresh()

        playerNameAdapter = PlayerNameAdapter(
            PlayersData.selectedPlayers,
            onSelect = { player ->
                val role = PlayersData.getRole(player)
                val playerDialog = PlayerDialog(this, role).also {
                    it.setOnDismissListener {
                        if (PlayersData.isAllPlayersGetRoles()) {
                            RoleDoneDialog(
                                this,
                                onOkClicked = {
                                    startActivity(Intent(this, NarratorActivity::class.java))
                                },
                                onRefreshClicked = {
                                    PlayersData.refresh()
                                }
                            ).show()
                        }
                    }
                }
                playerDialog.show()
            }
        )
        binding.playerRoleList.adapter = playerNameAdapter
        binding.playerRoleList.layoutManager = GridLayoutManager(this, 3)

        PlayersData.roles.observe(this) { _ ->
            playerNameAdapter.refresh()
        }

        binding.playerRoleToolBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menuItemRefresh -> {
                    PlayersData.refresh()
                    Toast.makeText(this, R.string.role_refresh, Toast.LENGTH_SHORT).show()
                    true
                }

                else -> {
                    false
                }
            }
        }
    }
}
