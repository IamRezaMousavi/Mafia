package com.github.iamrezamousavi.mafia.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.github.iamrezamousavi.mafia.R
import com.github.iamrezamousavi.mafia.databinding.ActivityPlayerRoleBinding
import com.github.iamrezamousavi.mafia.viewmodel.PlayerViewModel
import com.github.iamrezamousavi.mafia.viewmodel.RoleViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlayerRoleActivity : AppCompatActivity() {

    companion object {
        private const val EXTRA_VALUE = "roles"
    }

    private lateinit var binding: ActivityPlayerRoleBinding

    private lateinit var playerRoleAdapter: PlayerRoleAdapter

    private val playerViewModel: PlayerViewModel by viewModels()
    private val roleViewModel: RoleViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerRoleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        playerViewModel.loadPlayers()

        roleViewModel.setPlayers(
            playerViewModel.players.value ?: ArrayList()
        )

        roleViewModel.setSimpleCitizenText(getString(R.string.simple_citizen))

        val roles = ArrayList(
            intent.extras?.getString(EXTRA_VALUE)?.split(",")?.map { it.trim(' ', '[', ']') }
                ?: ArrayList()
        )
        roleViewModel.setRoles(roles)

        playerRoleAdapter = PlayerRoleAdapter(
            ArrayList(roleViewModel.players.value ?: ArrayList()),
            onSelect = { player ->
                val role = roleViewModel.getRole(player)
                val roleDialog = RoleDialog(this, role)
                roleDialog.show()
            }
        )
        binding.playerRoleList.adapter = playerRoleAdapter
        binding.playerRoleList.layoutManager = GridLayoutManager(this, 3)

        roleViewModel.roles.observe(this) { _ ->
            playerRoleAdapter.refresh()
        }

        binding.peopleRoleToolBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menuItemRefresh -> {
                    roleViewModel.shuffled()
                    Toast.makeText(this, R.string.refresh, Toast.LENGTH_SHORT).show()
                    true
                }

                else -> {
                    false
                }
            }
        }
    }
}