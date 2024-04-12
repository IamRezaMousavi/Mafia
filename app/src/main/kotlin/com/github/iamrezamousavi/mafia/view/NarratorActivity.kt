package com.github.iamrezamousavi.mafia.view

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.iamrezamousavi.mafia.data.model.NarratorItem
import com.github.iamrezamousavi.mafia.databinding.ActivityNarratorBinding
import com.github.iamrezamousavi.mafia.utils.LangData
import com.github.iamrezamousavi.mafia.utils.PlayersData
import com.github.iamrezamousavi.mafia.view.adapter.PlayerRoleAdapter
import com.github.iamrezamousavi.mafia.viewmodel.SettingsViewModel

class NarratorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNarratorBinding

    override fun attachBaseContext(newBase: Context?) {
        newBase?.let { context ->
            val settingsViewModel = SettingsViewModel(context)
            LangData.getContextWrapper(context, settingsViewModel.language.code)
        }
        super.attachBaseContext(newBase)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNarratorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val playerRoleAdapter = PlayerRoleAdapter(this) {
        }

        binding.playerRoleList.apply {
            layoutManager = LinearLayoutManager(this@NarratorActivity)
            itemAnimator = DefaultItemAnimator()
            adapter = playerRoleAdapter
        }

        playerRoleAdapter.submitList(
            PlayersData.selectedPlayers.map { player ->
                NarratorItem(
                    id = player.id,
                    player = player,
                    role = PlayersData.getRole(player),
                    isAlive = true
                )
            }
        )
    }
}
