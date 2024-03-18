package com.github.iamrezamousavi.mafia.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.github.iamrezamousavi.mafia.R
import com.github.iamrezamousavi.mafia.data.model.Role
import com.github.iamrezamousavi.mafia.databinding.ActivityRoleBinding
import com.github.iamrezamousavi.mafia.utils.SharedData
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

        Log.d("TAG", "ROLE: started")

        binding = ActivityRoleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("TAG", "ROLE: binding is ok")

        val roles = getSelectedRoles()
        roleViewModel.setSelectedRoles(roles)

        Log.d("TAG", "ROLE: players: ${SharedData.players.value}")

        binding.citizenCounter.also {
            it.isReadOnly = true
        }

        binding.mafiaCounter.also {
            it.setCounterListener(object : CounterViewListener {
                override fun onIncrease() {}

                override fun onDecrease() {}

                override fun onValueChanged(value: Int) {
                    Log.d("TAG", "ROLE: onValueChanged: $value")
                }
            })
        }

        Log.d("TAG", "ROLE: counter callbacks is ok")

        roleViewModel.maxSimpleMafia.observe(this) {
            binding.mafiaCounter.maxValue = it ?: 1
        }

        roleViewModel.simpleMafiaCounter.observe(this) {
            binding.mafiaCounter.value = it ?: 1
        }

        roleViewModel.minSimpleMafia.observe(this) {
            binding.mafiaCounter.minValue = it ?: 1
        }

        Log.d("TAG", "ROLE: counters is ok")

        binding.mafia.chipGroup2.setOnCheckedStateChangeListener { _, _ ->
            val selectedRoles = getSelectedRoles()
            Log.d("TAG", "ROLE: onClick groupChip $selectedRoles")
            roleViewModel.setSelectedRoles(selectedRoles)
        }

        Log.d("TAG", "ROLE: mafia chipGroup is ok")

        binding.citizen.chip101.setOnCheckedChangeListener { _, isChecked ->
            Toast.makeText(this, "Chip 1: $isChecked", Toast.LENGTH_SHORT).show()
        }

        binding.button.setOnClickListener {
            val selectedRoles = getSelectedRoles()
            Toast.makeText(this, "$selectedRoles", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, PlayerRoleActivity::class.java)
            startActivity(intent)
        }

        Log.d("TAG", "ROLE: everything is ok")
    }

    private fun getSelectedRoles(): ArrayList<Role> {
        Log.d("TAG", "getSelectedRoles: started")
        val selectedRoles = ArrayList(
            binding.citizen.chipGroup1.checkedChipIds.map {
                Role(
                    name = findViewById<Chip>(it).text.toString(),
                    side = getString(R.string.citizen_side)
                )
            } +
                    binding.mafia.chipGroup2.checkedChipIds.map {
                        Role(
                            name = findViewById<Chip>(it).text.toString(),
                            side = getString(R.string.mafia_side)
                        )
                    } +
                    binding.independent.chipGroup3.checkedChipIds.map {
                        Role(
                            name = findViewById<Chip>(it).text.toString(),
                            side = getString(R.string.independent_side)
                        )
                    }
        )
        Log.d("TAG", "getSelectedRoles: $selectedRoles")
        return selectedRoles
    }
}