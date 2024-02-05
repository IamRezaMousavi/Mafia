package com.github.iamrezamousavi.mafia

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.iamrezamousavi.mafia.databinding.ActivityRoleBinding

class RoleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRoleBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRoleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.chip1.setOnCheckedChangeListener { _, isChecked ->
            Toast.makeText(this, "Chip 1: $isChecked", Toast.LENGTH_SHORT).show()
        }
        binding.chip2.setOnCheckedChangeListener { _, isChecked ->
            Toast.makeText(this, "Chip 2: $isChecked", Toast.LENGTH_SHORT).show()
        }
        binding.chip3.setOnCheckedChangeListener { _, isChecked ->
            Toast.makeText(this, "Chip 3: $isChecked", Toast.LENGTH_SHORT).show()
        }
    }
}