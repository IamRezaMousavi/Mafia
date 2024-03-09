package com.github.iamrezamousavi.mafia.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.github.iamrezamousavi.mafia.R
import com.github.iamrezamousavi.mafia.databinding.ActivityRoleBinding
import com.github.iamrezamousavi.quantitizer.AnimationStyle
import com.google.android.material.chip.Chip

class RoleActivity : AppCompatActivity() {

    companion object {
        private const val EXTRA_VALUE = "roles"
    }

    private lateinit var binding: ActivityRoleBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRoleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.citizen.chip101.setOnCheckedChangeListener { _, isChecked ->
            Toast.makeText(this, "Chip 1: $isChecked", Toast.LENGTH_SHORT).show()
        }

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