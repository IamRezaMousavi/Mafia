package com.github.iamrezamousavi.mafia.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.github.iamrezamousavi.mafia.MainViewModel
import com.github.iamrezamousavi.mafia.R
import com.github.iamrezamousavi.mafia.data.model.Role
import com.github.iamrezamousavi.mafia.data.model.RoleSide
import com.github.iamrezamousavi.mafia.databinding.FragmentRoleBinding
import com.github.iamrezamousavi.mafia.utils.MafiaError
import com.github.iamrezamousavi.mafia.utils.getRoleId
import com.github.iamrezamousavi.mafia.view.dialog.RoleDialog
import com.google.android.material.chip.Chip

class RoleFragment : Fragment() {

    private lateinit var binding: FragmentRoleBinding

    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRoleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.roleToolBar.title =
            getString(R.string.select_roles, mainViewModel.playersSize)

        val roles = getSelectedRoles()
        mainViewModel.setSelectedRoles(roles)

        listOf(
            binding.citizen.chipGroup1,
            binding.mafia.chipGroup2,
            binding.independent.chipGroup3
        ).forEach { chipGroup ->
            chipGroup.setOnCheckedStateChangeListener { _, _ ->
                val selectedRoles = getSelectedRoles()
                mainViewModel.setSelectedRoles(selectedRoles)
            }
        }

        mainViewModel.selectedRolesSize.observe(viewLifecycleOwner) {
            binding.button.text = getString(R.string.division_roles, it)
        }

        binding.button.setOnClickListener {
            val checkResult = mainViewModel.checkSelectedRolesIsOk()

            checkResult.onSuccess {
                val roleDialog = RoleDialog(requireContext(), mainViewModel) {
                    findNavController().navigate(R.id.action_roleFragment_to_playerRoleFragment)
                }
                roleDialog.show()
            }.onError { error ->
                when (error) {
                    MafiaError.SelectedRoleTooMuch ->
                        Toast.makeText(context, R.string.roles_not_match, Toast.LENGTH_SHORT)
                            .show()

                    MafiaError.MafiaRoleTooMatch ->
                        Toast.makeText(context, R.string.mafia_roles_too_much, Toast.LENGTH_SHORT)
                            .show()
                }
            }
        }
    }

    private fun getSelectedRoles(): List<Role> {
        val citizenRoles = binding.citizen.chipGroup1.checkedChipIds.map {
            Role(
                name = getRoleId(
                    requireContext(),
                    requireView().findViewById<Chip>(it).text.toString()
                ),
                side = RoleSide.CITIZEN
            )
        }
        val mafiaRoles = binding.mafia.chipGroup2.checkedChipIds.map {
            Role(
                name = getRoleId(
                    requireContext(),
                    requireView().findViewById<Chip>(it).text.toString()
                ),
                side = RoleSide.MAFIA
            )
        }
        val independentRoles = binding.independent.chipGroup3.checkedChipIds.map {
            Role(
                name = getRoleId(
                    requireContext(),
                    requireView().findViewById<Chip>(it).text.toString()
                ),
                side = RoleSide.INDEPENDENT
            )
        }

        return citizenRoles + mafiaRoles + independentRoles
    }
}
