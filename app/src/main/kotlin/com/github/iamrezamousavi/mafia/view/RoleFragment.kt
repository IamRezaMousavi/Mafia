package com.github.iamrezamousavi.mafia.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.github.iamrezamousavi.mafia.R
import com.github.iamrezamousavi.mafia.data.model.Role
import com.github.iamrezamousavi.mafia.databinding.FragmentRoleBinding
import com.github.iamrezamousavi.mafia.utils.MafiaError
import com.github.iamrezamousavi.mafia.utils.getRoleId
import com.github.iamrezamousavi.mafia.view.dialog.RoleDialog
import com.github.iamrezamousavi.mafia.viewmodel.RoleViewModel
import com.google.android.material.chip.Chip

class RoleFragment : Fragment() {

    private lateinit var binding: FragmentRoleBinding

    private val roleViewModel: RoleViewModel by viewModels { RoleViewModel.Factory }

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
            getString(R.string.select_roles, roleViewModel.playersSize)

        val roles = getSelectedRoles()
        roleViewModel.setSelectedRoles(roles)

        listOf(
            binding.citizen.chipGroup1,
            binding.mafia.chipGroup2,
            binding.independent.chipGroup3
        ).forEach { chipGroup ->
            chipGroup.setOnCheckedStateChangeListener { _, _ ->
                val selectedRoles = getSelectedRoles()
                roleViewModel.setSelectedRoles(selectedRoles)
            }
        }

        roleViewModel.selectedRolesSize.observe(viewLifecycleOwner) {
            binding.button.text = getString(R.string.division_roles, it)
        }

        binding.button.setOnClickListener {
            val selectedRoles = getSelectedRoles()
            val checkResult = roleViewModel.checkSelectedRolesIsOk(selectedRoles)

            checkResult.onSuccess {
                val roleDialog = RoleDialog(requireContext(), roleViewModel) {
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

    private fun getSelectedRoles(): ArrayList<Role> {
        val selectedRoles = (
            binding.citizen.chipGroup1.checkedChipIds +
                binding.mafia.chipGroup2.checkedChipIds +
                binding.independent.chipGroup3.checkedChipIds
            ).map {
            Role(
                name = getRoleId(
                    requireContext(),
                    requireView().findViewById<Chip>(it).text.toString()
                )
            )
        }
        return ArrayList(selectedRoles)
    }
}