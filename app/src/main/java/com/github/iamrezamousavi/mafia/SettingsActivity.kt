package com.github.iamrezamousavi.mafia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.github.iamrezamousavi.mafia.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.settingsItem1.switch1.setOnCheckedChangeListener { _, isChecked ->
            val message = if (isChecked) "Switch1:ON" else "Switch1:OFF"
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }
}