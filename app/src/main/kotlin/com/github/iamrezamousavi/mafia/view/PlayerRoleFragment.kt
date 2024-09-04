package com.github.iamrezamousavi.mafia.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.github.iamrezamousavi.mafia.R
import com.github.iamrezamousavi.mafia.databinding.FragmentPlayerRoleBinding
import com.github.iamrezamousavi.mafia.utils.PlayersData
import com.github.iamrezamousavi.mafia.view.adapter.PlayerNameAdapter
import com.github.iamrezamousavi.mafia.view.dialog.PlayerDialog
import com.github.iamrezamousavi.mafia.view.dialog.RoleDoneDialog

class PlayerRoleFragment : Fragment() {

    private lateinit var binding: FragmentPlayerRoleBinding

    private lateinit var playerNameAdapter: PlayerNameAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlayerRoleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        PlayersData.refresh()

        playerNameAdapter = PlayerNameAdapter(
            PlayersData.selectedPlayers,
            onSelect = { player ->
                val role = PlayersData.getRole(player)
                val playerDialog = PlayerDialog(requireContext(), role).also {
                    it.setOnDismissListener {
                        if (PlayersData.isAllPlayersGetRoles()) {
                            RoleDoneDialog(
                                requireContext(),
                                onOkClicked = {
                                    findNavController()
                                        .navigate(
                                            R.id.action_playerRoleFragment_to_narratorFragment
                                        )
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
        @Suppress("MagicNumber")
        binding.playerRoleList.layoutManager = GridLayoutManager(context, 3)

        PlayersData.roles.observe(viewLifecycleOwner) { _ ->
            playerNameAdapter.refresh()
        }

        binding.playerRoleToolBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menuItemRefresh -> {
                    PlayersData.refresh()
                    Toast.makeText(context, R.string.role_refresh, Toast.LENGTH_SHORT).show()
                    true
                }

                else -> {
                    false
                }
            }
        }
    }
}
