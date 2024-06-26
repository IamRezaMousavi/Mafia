package com.github.iamrezamousavi.mafia.view

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.github.iamrezamousavi.mafia.R
import com.github.iamrezamousavi.mafia.data.model.Role
import com.github.iamrezamousavi.mafia.databinding.ActivityRoleBinding
import com.github.iamrezamousavi.mafia.utils.LangData
import com.github.iamrezamousavi.mafia.utils.MafiaError
import com.github.iamrezamousavi.mafia.utils.getRoleId
import com.github.iamrezamousavi.mafia.view.dialog.RoleDialog
import com.github.iamrezamousavi.mafia.viewmodel.RoleViewModel
import com.github.iamrezamousavi.mafia.viewmodel.SettingsViewModel
import com.google.android.material.chip.Chip

class RoleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRoleBinding

    private val roleViewModel: RoleViewModel by viewModels { RoleViewModel.Factory }

    override fun attachBaseContext(newBase: Context?) {
        newBase?.let { context ->
            val settingsViewModel = SettingsViewModel(context)
            LangData.getContextWrapper(context, settingsViewModel.language.code)
        }
        super.attachBaseContext(newBase)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRoleBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

        roleViewModel.selectedRolesSize.observe(this) {
            binding.button.text = getString(R.string.division_roles, it)
        }

        binding.button.setOnClickListener {
            val selectedRoles = getSelectedRoles()
            val checkResult = roleViewModel.checkSelectedRolesIsOk(selectedRoles)

            checkResult.onSuccess {
                val roleDialog = RoleDialog(this, roleViewModel)
                roleDialog.show()
            }.onError { error ->
                when (error) {
                    MafiaError.SelectedRoleTooMuch ->
                        Toast.makeText(this, R.string.roles_not_match, Toast.LENGTH_SHORT)
                            .show()

                    MafiaError.MafiaRoleTooMatch ->
                        Toast.makeText(this, R.string.mafia_roles_too_much, Toast.LENGTH_SHORT)
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
            Role(name = getRoleId(this, findViewById<Chip>(it).text.toString()))
        }
        return ArrayList(selectedRoles)
    }
}
