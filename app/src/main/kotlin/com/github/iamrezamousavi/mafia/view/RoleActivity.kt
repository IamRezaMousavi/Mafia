package com.github.iamrezamousavi.mafia.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.github.iamrezamousavi.mafia.R
import com.github.iamrezamousavi.mafia.data.model.Role
import com.github.iamrezamousavi.mafia.databinding.ActivityRoleBinding
import com.github.iamrezamousavi.mafia.utils.getRoleId
import com.github.iamrezamousavi.mafia.view.counterview.CounterViewListener
import com.github.iamrezamousavi.mafia.viewmodel.RoleViewModel
import com.google.android.material.chip.Chip

class RoleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRoleBinding

    private val roleViewModel: RoleViewModel by viewModels {
        RoleViewModel.Factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRoleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.roleToolBar.title =
            getString(R.string.select_roles, roleViewModel.playersSize)

        val roles = getSelectedRoles()
        roleViewModel.setSelectedRoles(roles)

        binding.citizenCounter.apply {
            isReadOnly = true
        }

        binding.mafiaCounter.apply {
            isReadOnly = true
            setCounterListener(object : CounterViewListener {
                override fun onIncrease() {
                    roleViewModel.setSimpleMafiaCounter(value)
                }

                override fun onDecrease() {
                    roleViewModel.setSimpleMafiaCounter(value)
                }

                override fun onValueChanged(value: Int) {}
            })
        }

        roleViewModel.maxSimpleMafia.observe(this) {
            binding.mafiaCounter.maxValue = it ?: 1
        }

        roleViewModel.simpleMafiaCounter.observe(this) {
            binding.mafiaCounter.value = it ?: 1
        }

        roleViewModel.minSimpleMafia.observe(this) {
            binding.mafiaCounter.minValue = it ?: 1
        }

        roleViewModel.simpleCitizenCounter.observe(this) {
            binding.citizenCounter.value = it ?: 1
        }

        binding.citizen.chipGroup1.setOnCheckedStateChangeListener { _, _ ->
            val selectedRoles = getSelectedRoles()
            roleViewModel.setSelectedRoles(selectedRoles)
        }

        binding.mafia.chipGroup2.setOnCheckedStateChangeListener { _, _ ->
            val selectedRoles = getSelectedRoles()
            roleViewModel.setSelectedRoles(selectedRoles)
        }

        binding.independent.chipGroup3.setOnCheckedStateChangeListener { _, _ ->
            val selectedRoles = getSelectedRoles()
            roleViewModel.setSelectedRoles(selectedRoles)
        }

        binding.citizen.chip101.setOnCheckedChangeListener { _, isChecked ->
            Toast.makeText(this, "Chip 1: $isChecked", Toast.LENGTH_SHORT).show()
        }

        roleViewModel.selectedRolesSize.observe(this) {
            binding.button.text = getString(R.string.division_roles, it)
        }

        binding.button.setOnClickListener {
            if (roleViewModel.checkRolesIsOk()) {
                roleViewModel.generateRoles()
                val intent = Intent(this, PlayerRoleActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, R.string.roles_not_match, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getSelectedRoles(): ArrayList<Role> {
        val selectedRoles = ArrayList((
                binding.citizen.chipGroup1.checkedChipIds +
                        binding.mafia.chipGroup2.checkedChipIds +
                        binding.independent.chipGroup3.checkedChipIds
                ).map {
                Role(
                    name = getRoleId(
                        this,
                        findViewById<Chip>(it).text.toString()
                    )
                )
            })
        return selectedRoles
    }
}