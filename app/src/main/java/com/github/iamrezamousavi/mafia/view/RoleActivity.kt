package com.github.iamrezamousavi.mafia.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.github.iamrezamousavi.mafia.R
import com.github.iamrezamousavi.mafia.data.repository.PlayerRepository
import com.github.iamrezamousavi.mafia.data.source.SharedPreferencesManager
import com.github.iamrezamousavi.mafia.databinding.ActivityRoleBinding
import com.github.iamrezamousavi.mafia.viewmodel.PlayerViewModel
import com.github.iamrezamousavi.mafia.viewmodel.PlayerViewModelFactory
import com.github.iamrezamousavi.mafia.viewmodel.RoleViewModel
import com.github.iamrezamousavi.mafia.viewmodel.RoleViewModelFactory
import com.github.iamrezamousavi.quantitizer.AnimationStyle
import com.github.iamrezamousavi.quantitizer.QuantitizerListener
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
        binding = ActivityRoleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // edit views
        binding.citizenCounter.textAnimationStyle = AnimationStyle.FALL_IN
        binding.citizenCounter.setPlusIcon(R.drawable.ic_plus_small)
        binding.citizenCounter.setMinusIcon(R.drawable.ic_minus_small)
        binding.citizenCounter.setPlusBackground(R.drawable.plus_icon_bg)
        binding.citizenCounter.setMinusBackground(R.drawable.plus_icon_bg)
        binding.citizenCounter.setValueTextColorInt(
            ContextCompat.getColor(
                this, R.color.md_theme_onPrimary
            )
        )

        binding.citizenCounter.setTextBackgroundColor(R.color.md_theme_primary)
        binding.citizenCounter.isReadOnly = true

        binding.mafiaCounter.textAnimationStyle = AnimationStyle.FALL_IN
        binding.mafiaCounter.setPlusIcon(R.drawable.ic_plus_small)
        binding.mafiaCounter.setMinusIcon(R.drawable.ic_minus_small)
        binding.mafiaCounter.setPlusBackground(R.drawable.plus_icon_bg)
        binding.mafiaCounter.setMinusBackground(R.drawable.plus_icon_bg)
        binding.mafiaCounter.setValueTextColorInt(
            ContextCompat.getColor(
                this, R.color.md_theme_onPrimary
            )
        )
        binding.mafiaCounter.setTextBackgroundColor(R.color.md_theme_primary)

        // implement logics

        val sharedPreferencesManager = SharedPreferencesManager(this)
        val playerRepository = PlayerRepository(sharedPreferencesManager)
        val factory = PlayerViewModelFactory(playerRepository)
        playerViewModel = ViewModelProvider(this, factory)[PlayerViewModel::class.java]

        val roleFactory = RoleViewModelFactory(this)
        roleViewModel = ViewModelProvider(this, roleFactory)[RoleViewModel::class.java]

        val roles = ArrayList<String>()
        val citizenChips0 = binding.citizen.chipGroup1.checkedChipIds
        for (id in citizenChips0) {
            roles.add(findViewById<Chip>(id).text.toString())
        }
        val mafiaChips0 = binding.mafia.chipGroup2.checkedChipIds
        for (id in mafiaChips0) {
            roles.add(findViewById<Chip>(id).text.toString())
        }
        val independentChips0 = binding.independent.chipGroup3.checkedChipIds
        for (id in independentChips0) {
            roles.add(findViewById<Chip>(id).text.toString())
        }
        roleViewModel.setPlayersAndRoles(playerViewModel.loadPlayers(), roles)

        binding.mafia.chipGroup2.setOnCheckedStateChangeListener { _, checkedIds ->
            val hasSimpleMafia = binding.mafia.chip201.isChecked
            roleViewModel.updateMaxSimpleMafia(checkedIds.size, hasSimpleMafia)
        }
        roleViewModel.maxSimpleMafia.observe(this) {
            binding.mafiaCounter.maxValue = roleViewModel.maxSimpleMafia.value ?: 1
            roleViewModel.updateSimpleMafiaCounter()
            binding.mafiaCounter.value = roleViewModel.simpleMafiaCounter.value ?: 1
        }
        binding.mafiaCounter.setQuantitizerListener(object : QuantitizerListener {
            override fun onIncrease() {}

            override fun onDecrease() {}

            override fun onValueChanged(value: Int) {
                roleViewModel.setSimpleMafiaCounter(value)
            }
        })
        binding.mafiaCounter.value = 1

        binding.citizen.chip101.setOnCheckedChangeListener { _, isChecked ->
            Toast.makeText(this, "Chip 1: $isChecked", Toast.LENGTH_SHORT).show()
        }

        roleViewModel.simpleMafiaCounter.observe(this) { _ ->
            roleViewModel.updateSimpleCitizenCounter()
            binding.citizenCounter.value = roleViewModel.simpleCitizenCounter
        }


        binding.button.setOnClickListener {
            val selectedRoles = ArrayList<String>()

            val citizenChips = binding.citizen.chipGroup1.checkedChipIds
            for (id in citizenChips) {
                selectedRoles.add(findViewById<Chip>(id).text.toString())
            }

            val mafiaChips = binding.mafia.chipGroup2.checkedChipIds
            for (id in mafiaChips) {
                selectedRoles.add(findViewById<Chip>(id).text.toString())
            }

            val independentChips = binding.independent.chipGroup3.checkedChipIds
            for (id in independentChips) {
                selectedRoles.add(findViewById<Chip>(id).text.toString())
            }

            Toast.makeText(this, "$selectedRoles", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, PlayerRoleActivity::class.java)
            intent.putExtra(EXTRA_VALUE, selectedRoles.toString())
            startActivity(intent)
        }
    }
}