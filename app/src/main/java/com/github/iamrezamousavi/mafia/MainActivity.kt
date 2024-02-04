package com.github.iamrezamousavi.mafia

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.github.iamrezamousavi.mafia.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button1.setOnClickListener {
            Toast.makeText(this, "Button Clicked", Toast.LENGTH_SHORT).show()
        }

        binding.textField.setEndIconOnClickListener {
            val message = binding.textField.editText?.text.toString()
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }

        binding.mainToolBar.setNavigationOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        binding.people1.checkBox.setOnCheckedChangeListener { _, isChecked ->
            val message = if (isChecked) "Switch1:ON" else "Switch1:OFF"
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }

        binding.people1.imageButton.setOnClickListener {
            Toast.makeText(this, "ImageButton clicked", Toast.LENGTH_SHORT).show()
        }
    }
}
