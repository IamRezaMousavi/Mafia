package com.github.iamrezamousavi.mafia.view

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.github.iamrezamousavi.mafia.R
import com.github.iamrezamousavi.mafia.data.repository.PlayerRepository
import com.github.iamrezamousavi.mafia.data.source.SharedPreferencesManager
import com.github.iamrezamousavi.mafia.databinding.ActivityPlayerRoleBinding
import com.github.iamrezamousavi.mafia.viewmodel.PlayerViewModel
import com.github.iamrezamousavi.mafia.viewmodel.PlayerViewModelFactory
import com.github.iamrezamousavi.mafia.viewmodel.RoleViewModel
import com.github.iamrezamousavi.mafia.viewmodel.RoleViewModelFactory

class PlayerRoleActivity : AppCompatActivity() {

    companion object {
        private const val EXTRA_VALUE = "roles"
    }

    private lateinit var binding: ActivityPlayerRoleBinding

    private lateinit var playerRoleAdapter: PlayerRoleAdapter

    private lateinit var playerViewModel: PlayerViewModel
    private lateinit var roleViewModel: RoleViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerRoleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferencesManager = SharedPreferencesManager(this)
        val playerRepository = PlayerRepository(sharedPreferencesManager)
        val factory = PlayerViewModelFactory(playerRepository)
        playerViewModel = ViewModelProvider(this, factory)[PlayerViewModel::class.java]

        val roleFactory = RoleViewModelFactory(this)
        roleViewModel = ViewModelProvider(this, roleFactory)[RoleViewModel::class.java]

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