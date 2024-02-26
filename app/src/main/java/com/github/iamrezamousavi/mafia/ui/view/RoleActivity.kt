package com.github.iamrezamousavi.mafia.ui.view

import android.content.Intent
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

        binding.chipGroup.chip1.setOnCheckedChangeListener { _, isChecked ->
            Toast.makeText(this, "Chip 1: $isChecked", Toast.LENGTH_SHORT).show()
        }

        binding.button.setOnClickListener {
            startActivity(Intent(this, PeopleRoleActivity::class.java))
        }
    }
}