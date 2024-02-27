package com.github.iamrezamousavi.mafia.ui.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.iamrezamousavi.mafia.databinding.ActivityRoleBinding
import com.google.android.material.chip.Chip

class RoleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRoleBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRoleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.citizen.chip1.setOnCheckedChangeListener { _, isChecked ->
            Toast.makeText(this, "Chip 1: $isChecked", Toast.LENGTH_SHORT).show()
        }

        binding.button.setOnClickListener {
            val selectedChips = binding.citizen.chipGroup.checkedChipIds
            val selectedRoles = ArrayList<String>()
            for (id in selectedChips) {
                selectedRoles.add(findViewById<Chip>(id).text.toString())
            }
            Toast.makeText(this, "$selectedRoles", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, PlayerRoleActivity::class.java)
            intent.putExtra("roles", selectedRoles.toString())
            startActivity(intent)
        }
    }
}