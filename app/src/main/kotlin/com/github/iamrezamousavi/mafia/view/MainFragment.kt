package com.github.iamrezamousavi.mafia.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.iamrezamousavi.mafia.R
import com.github.iamrezamousavi.mafia.databinding.FragmentMainBinding
import com.github.iamrezamousavi.mafia.utils.PlayersData
import com.github.iamrezamousavi.mafia.view.adapter.PlayerAdapter
import com.github.iamrezamousavi.mafia.viewmodel.PlayerViewModel

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding

    private val viewModel: PlayerViewModel by viewModels { PlayerViewModel.Factory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
            layoutManager = LinearLayoutManager(context)
            itemAnimator = DefaultItemAnimator()
            adapter = playerAdapter
        }

        PlayersData.players.observe(viewLifecycleOwner) { players ->
            playerAdapter.submitList(players.toList())
            binding.button1.text =
                getString(R.string.select_roles, players.filter { it.isChecked }.size)
        }

        binding.button1.setOnClickListener {
            if (PlayersData.isPlayersOk()) {
                findNavController().navigate(R.id.action_mainFragment_to_roleFragment)
            } else {
                Toast.makeText(context, R.string.player_not_enough, Toast.LENGTH_SHORT).show()
            }
        }

        binding.textField.setEndIconOnClickListener {
            val name = binding.textField.editText?.text.toString()
            if (name.isNotEmpty()) {
                viewModel.addPlayer(name)
                playerAdapter.notifyRebuild()
                binding.textField.editText?.setText("")
                Toast.makeText(context, name, Toast.LENGTH_SHORT).show()
            }
        }

        binding.mainToolBar.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_settingsFragment)
        }

        binding.mainToolBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menuItemSelectAll -> {
                    viewModel.selectAllPlayer()
                    playerAdapter.notifyRebuild()
                    Toast.makeText(context, R.string.select_all, Toast.LENGTH_SHORT).show()
                    true
                }

                else -> {
                    false
                }
            }
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
