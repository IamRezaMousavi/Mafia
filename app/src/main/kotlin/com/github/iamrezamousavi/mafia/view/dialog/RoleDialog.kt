package com.github.iamrezamousavi.mafia.view.dialog

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.github.iamrezamousavi.mafia.R
import com.github.iamrezamousavi.mafia.databinding.DialogRoleBinding
import com.github.iamrezamousavi.mafia.utils.getSide
import com.github.iamrezamousavi.mafia.view.PlayerRoleActivity
import com.github.iamrezamousavi.mafia.view.counterview.CounterViewListener
import com.github.iamrezamousavi.mafia.viewmodel.RoleViewModel

class RoleDialog(
    context: Context,
    private val roleViewModel: RoleViewModel
) : AlertDialog(context) {
    private lateinit var binding: DialogRoleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogRoleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (window != null) {
            window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        binding.playerSizeText.text =
            context.getString(R.string.players_size, roleViewModel.playersSize)

        setIndependentSection()

        roleViewModel.citizenSize.observe(this) {
            binding.citizenCounterText.text = context.getString(R.string.citizen_size, it)
        }

        roleViewModel.mafiaSize.observe(this) {
            binding.mafiaCounter.value = it
        }

        roleViewModel.generatedRoles.observe(this) {
            binding.citizenRoles.text =
                it.filter { role ->
                    getSide(role.name) == R.string.citizen_side
                }.map { role ->
                    context.getString(role.name)
                }.toString()
                    .replace("[", "")
                    .replace("]", "")
                    .replace(",", context.getString(R.string.comma))

            binding.mafiaRoles.text =
                it.filter { role ->
                    getSide(role.name) == R.string.mafia_side
                }.map { role ->
                    context.getString(role.name)
                }.toString()
                    .replace("[", "")
                    .replace("]", "")
                    .replace(",", context.getString(R.string.comma))
        }

        binding.mafiaCounter.also {
            it.setCounterListener(object : CounterViewListener {
                override fun onIncrease() {
                    val newValue = binding.mafiaCounter.value
                    roleViewModel.setMafiaSize(newValue)
                }

                override fun onDecrease() {
                    val newValue = binding.mafiaCounter.value
                    roleViewModel.setMafiaSize(newValue)
                }

                override fun onValueChanged(value: Int) {
                }
            })

            it.isReadOnly = true
            it.maxValue = roleViewModel.calculateMaxMafia()
            it.minValue = roleViewModel.calculateMinMafia()
            it.value = roleViewModel.calculateMinMafia()
            roleViewModel.setMafiaSize(it.value)
        }

        binding.okButton.setOnClickListener {
            val intent = Intent(context, PlayerRoleActivity::class.java)
            context.startActivity(intent)
            dismiss()
        }
    }

    private fun setIndependentSection() {
        val independentRoles = roleViewModel.selectedRoles.filter {
            getSide(it.name) == R.string.independent_side
        }
        if (independentRoles.isEmpty()) {
            binding.independentCard.visibility = View.GONE
        } else {
            binding.independentCounterText.text =
                context.getString(R.string.independent_size, independentRoles.size)
            binding.independentRoles.text =
                independentRoles.map { role ->
                    context.getString(role.name)
                }.toString()
                    .replace("[", "")
                    .replace("]", "")
                    .replace(",", context.getString(R.string.comma))
        }
    }
}
