package com.github.iamrezamousavi.mafia.view

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.github.iamrezamousavi.mafia.R
import com.github.iamrezamousavi.mafia.databinding.ActivityPlayerRoleBinding
import com.github.iamrezamousavi.mafia.utils.SharedData

class PlayerRoleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerRoleBinding

    private lateinit var playerRoleAdapter: PlayerRoleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerRoleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        SharedData.refresh()

        playerRoleAdapter = PlayerRoleAdapter(
            SharedData.getPlayers(),
            onSelect = { player ->
                val role = SharedData.getRole(player)
                val playerDialog = PlayerDialog(this, role).also {
                    it.setOnDismissListener {
                        if (SharedData.isAllPlayersGetRoles()) {
                            RoleDoneDialog(
                                this,
                                onOkClicked = {
                                },
                                onRefreshClicked = {
                                    SharedData.refresh()
                                }
                            ).show()
                        }
                    }
                }
                playerDialog.show()
            }
        )
        binding.playerRoleList.adapter = playerRoleAdapter
        binding.playerRoleList.layoutManager = GridLayoutManager(this, 3)

        SharedData.roles.observe(this) { _ ->
            playerRoleAdapter.refresh()
        }

        binding.peopleRoleToolBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menuItemRefresh -> {
                    SharedData.refresh()
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
