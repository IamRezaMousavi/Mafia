package com.github.iamrezamousavi.mafia.view.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.github.iamrezamousavi.mafia.MainViewModel
import com.github.iamrezamousavi.mafia.R
import com.github.iamrezamousavi.mafia.databinding.DialogRoleBinding
import com.github.iamrezamousavi.mafia.utils.getSide
import com.github.iamrezamousavi.mafia.view.counterview.CounterViewListener

class RoleDialog(
    context: Context,
    private val mainViewModel: MainViewModel,
    private val onOkClicked: () -> Unit
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
            context.getString(R.string.players_size, mainViewModel.playersSize)

        setIndependentSection()

        mainViewModel.citizenSize.observe(this) {
            binding.citizenCounterText.text = context.getString(R.string.citizen_size, it)
        }

        mainViewModel.mafiaSize.observe(this) {
            binding.mafiaCounter.value = it
        }

        mainViewModel.roles.observe(this) {
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
                    mainViewModel.setMafiaSize(newValue)
                }

                override fun onDecrease() {
                    val newValue = binding.mafiaCounter.value
                    mainViewModel.setMafiaSize(newValue)
                }

                @Suppress("EmptyFunctionBlock")
                override fun onValueChanged(value: Int) {
                }
            })

            it.isReadOnly = true
            it.maxValue = mainViewModel.calculateMaxMafia()
            it.minValue = mainViewModel.calculateMinMafia()
            it.value = mainViewModel.calculateMinMafia()
            mainViewModel.setMafiaSize(it.value)
        }

        binding.okButton.setOnClickListener {
            onOkClicked()
            dismiss()
        }
    }

    private fun setIndependentSection() {
        val independentRoles = mainViewModel.selectedRoles.filter {
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
