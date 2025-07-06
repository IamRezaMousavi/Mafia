package com.github.iamrezamousavi.mafia.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.github.iamrezamousavi.mafia.MainViewModel
import com.github.iamrezamousavi.mafia.R
import com.github.iamrezamousavi.mafia.databinding.FragmentPlayerRoleBinding
import com.github.iamrezamousavi.mafia.view.adapter.PlayerNameAdapter
import com.github.iamrezamousavi.mafia.view.dialog.PlayerDialog
import com.github.iamrezamousavi.mafia.view.dialog.RoleDoneDialog

class PlayerRoleFragment : Fragment() {

    private lateinit var binding: FragmentPlayerRoleBinding

    private lateinit var playerNameAdapter: PlayerNameAdapter

    private val mainViewModel: MainViewModel by activityViewModels()

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

        mainViewModel.refreshRoles()

        playerNameAdapter = PlayerNameAdapter(
            onSelect = { item ->
                val role = mainViewModel.getRole(item.player)
                val playerDialog = PlayerDialog(requireContext(), role).also {
                    it.setOnDismissListener {
                        if (mainViewModel.isAllPlayersGetRoles()) {
                            binding.playerHelpMessage.visibility = View.GONE
                            RoleDoneDialog(
                                requireContext(),
                                onOkClicked = {
                                    findNavController()
                                        .navigate(
                                            R.id.action_playerRoleFragment_to_narratorFragment
                                        )
                                },
                                onRefreshClicked = {
                                    mainViewModel.refreshRoles()
                                    binding.playerHelpMessage.visibility = View.VISIBLE
                                }
                            ).show()
                        }
                    }
                }
                playerDialog.show()
            }
        )

        binding.playerRoleList.apply {
            layoutManager = GridLayoutManager(context, 3)
            itemAnimator = DefaultItemAnimator()
            adapter = playerNameAdapter
        }

        mainViewModel.playerRoles.observe(viewLifecycleOwner) { items ->
            playerNameAdapter.submitList(items)
        }

        binding.playerRoleToolBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menuItemRefresh -> {
                    mainViewModel.refreshRoles()
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
