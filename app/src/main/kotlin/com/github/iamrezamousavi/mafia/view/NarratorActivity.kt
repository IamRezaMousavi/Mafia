package com.github.iamrezamousavi.mafia.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.iamrezamousavi.mafia.databinding.ActivityNarratorBinding
import com.github.iamrezamousavi.mafia.utils.PlayersData
import com.github.iamrezamousavi.mafia.view.adapter.PlayerRoleAdapter

class NarratorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNarratorBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNarratorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val playerRoleAdapter = PlayerRoleAdapter {
        }

        binding.playerRoleList.apply {
            layoutManager = LinearLayoutManager(this@NarratorActivity)
            addItemDecoration(
                DividerItemDecoration(
                    this@NarratorActivity,
                    DividerItemDecoration.VERTICAL
                )
            )
            itemAnimator = DefaultItemAnimator()
            adapter = playerRoleAdapter
        }

        playerRoleAdapter.submitList(PlayersData.selectedPlayers)
    }
}
