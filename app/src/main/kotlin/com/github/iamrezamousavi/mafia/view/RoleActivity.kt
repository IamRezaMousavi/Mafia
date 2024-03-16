package com.github.iamrezamousavi.mafia.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.github.iamrezamousavi.mafia.R
import com.github.iamrezamousavi.mafia.data.model.Role
import com.github.iamrezamousavi.mafia.data.repository.PlayerRepository
import com.github.iamrezamousavi.mafia.data.source.SharedPreferencesManager
import com.github.iamrezamousavi.mafia.databinding.ActivityRoleBinding
import com.github.iamrezamousavi.mafia.view.counterview.CounterViewListener
import com.github.iamrezamousavi.mafia.viewmodel.PlayerViewModel
import com.github.iamrezamousavi.mafia.viewmodel.PlayerViewModelFactory
import com.github.iamrezamousavi.mafia.viewmodel.RoleViewModel
import com.github.iamrezamousavi.mafia.viewmodel.RoleViewModelFactory
import com.google.android.material.chip.Chip

class RoleActivity : AppCompatActivity() {

    companion object {
        private const val EXTRA_VALUE = "roles"
    }

    private lateinit var binding: ActivityRoleBinding

    private lateinit var playerViewModel: PlayerViewModel
    private lateinit var roleViewModel: RoleViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("TAG", "ROLE: started")

        binding = ActivityRoleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("TAG", "ROLE: binding is ok")

        val sharedPreferencesManager = SharedPreferencesManager(this)
        val playerRepository = PlayerRepository(sharedPreferencesManager)
        val factory = PlayerViewModelFactory(playerRepository)
        playerViewModel = ViewModelProvider(this, factory)[PlayerViewModel::class.java]

        Log.d("TAG", "ROLE: viewmodel1 is ok")

        val roleFactory = RoleViewModelFactory(this)
        roleViewModel = ViewModelProvider(this, roleFactory)[RoleViewModel::class.java]

        val roles = getSelectedRoles()
        roleViewModel.setPlayersAndSelectedRoles(playerViewModel.loadPlayers(), roles)

        Log.d("TAG", "ROLE: viewmodels is setup")

        binding.citizenCounter.also {
            it.isReadOnly = true
        }

        binding.mafiaCounter.also {
            it.setCounterListener(object : CounterViewListener {
                override fun onIncrease() {
                    roleViewModel.increaseSimpleMafia()
                    Log.d("TAG", "ROLE: onIncrease")
                }

                override fun onDecrease() {
                    roleViewModel.decreaseSimpleMafia()
                    Log.d("TAG", "ROLE: onDecrease")
                }

                override fun onValueChanged(value: Int) {
                    roleViewModel.setSimpleMafiaCounter(value)
                    Log.d("TAG", "ROLE: simpleMafiaCounter changed $value")
                }
            })
        }

        Log.d("TAG", "ROLE: counters is ok")

        roleViewModel.maxSimpleMafia.observe(this) {
            binding.mafiaCounter.maxValue = it ?: 1
            Log.d("TAG", "ROLE: maxSimpleMafia changed $it")
        }
        roleViewModel.simpleMafiaCounter.observe(this) {
            binding.mafiaCounter.value = it ?: 1
            Log.d("TAG", "ROLE: simpleMafia changed $it")
        }
        roleViewModel.simpleCitizenCounter.observe(this) {
            binding.citizenCounter.value = it ?: 1
            Log.d("TAG", "ROLE: simpleCitizen changed $it")
        }

        Log.d("TAG", "ROLE: counter callbacks is ok")

        binding.citizen.chipGroup1.setOnCheckedStateChangeListener { _, checkedIds ->
            val selectedRoles = getSelectedRoles()
            roleViewModel.setSelectedRoles(selectedRoles)
            Log.d("TAG", "ROLE: citizen chipGroup changed ${checkedIds.size}")
        }

        binding.mafia.chipGroup2.setOnCheckedStateChangeListener { _, checkedIds ->
            val selectedRoles = getSelectedRoles()
            roleViewModel.setSelectedRoles(selectedRoles)
            Log.d("TAG", "ROLE: mafia chipGroup changed ${checkedIds.size}")
        }

        binding.independent.chipGroup3.setOnCheckedStateChangeListener { _, checkedIds ->
            val selectedRoles = getSelectedRoles()
            roleViewModel.setSelectedRoles(selectedRoles)
            Log.d("TAG", "ROLE: independent chipGroup changed ${checkedIds.size}")
        }

        Log.d("TAG", "ROLE: mafia chipGroup is ok")

        binding.citizen.chip101.setOnCheckedChangeListener { _, isChecked ->
            Toast.makeText(this, "Chip 1: $isChecked", Toast.LENGTH_SHORT).show()
        }

        binding.button.setOnClickListener {
            val selectedRoles = getSelectedRoles()
            Toast.makeText(this, "$selectedRoles", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, PlayerRoleActivity::class.java)
            intent.putExtra(EXTRA_VALUE, selectedRoles.toString())
            startActivity(intent)
        }

        Log.d("TAG", "ROLE: everything is ok")
    }

    private fun getSelectedRoles(): ArrayList<Role> {
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
        return selectedRoles
    }
}