package com.github.iamrezamousavi.mafia.ui.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.iamrezamousavi.mafia.databinding.ActivityRoleBinding
import com.google.android.material.chip.Chip

class RoleActivity : AppCompatActivity() {
    private val extraValue = "roles"

    private lateinit var binding: ActivityRoleBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRoleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.citizen.chip101.setOnCheckedChangeListener { _, isChecked ->
            Toast.makeText(this, "Chip 1: $isChecked", Toast.LENGTH_SHORT).show()
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
            intent.putExtra(extraValue, selectedRoles.toString())
            startActivity(intent)
        }
    }
}