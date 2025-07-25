package com.github.iamrezamousavi.mafia.view.dialog

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.graphics.drawable.toDrawable
import com.github.iamrezamousavi.mafia.MainViewModel
import com.github.iamrezamousavi.mafia.R
import com.github.iamrezamousavi.mafia.data.model.RoleSide
import com.github.iamrezamousavi.mafia.databinding.DialogRoleBinding
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

        window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())

        binding.playerSizeText.text =
            context.getString(R.string.players_count, mainViewModel.playersCount)

        setIndependentSection()

        mainViewModel.citizenCount.observe(this) {
            binding.citizenCounterText.text = context.getString(R.string.citizen_count_text, it)
        }

        mainViewModel.mafiaCount.observe(this) {
            binding.mafiaCounter.value = it
        }

        mainViewModel.roles.observe(this) {
            binding.citizenRoles.text =
                it
                    .filter { role -> role.side == RoleSide.CITIZEN }
                    .joinToString(context.getString(R.string.comma) + SPACE) { role ->
                        context.getString(role.name)
                    }

            binding.mafiaRoles.text =
                it
                    .filter { role -> role.side == RoleSide.MAFIA }
                    .joinToString(context.getString(R.string.comma) + SPACE) { role ->
                        context.getString(role.name)
                    }
        }

        binding.mafiaCounter.also {
            it.setCounterListener(object : CounterViewListener {
                override fun onIncrease() {
                    val newValue = binding.mafiaCounter.value
                    mainViewModel.setMafiaCount(newValue)
                }

                override fun onDecrease() {
                    val newValue = binding.mafiaCounter.value
                    mainViewModel.setMafiaCount(newValue)
                }

                @Suppress("EmptyFunctionBlock")
                override fun onValueChanged(value: Int) {
                }
            })

            it.isReadOnly = true
            it.maxValue = mainViewModel.calculateMaxMafia()
            it.minValue = mainViewModel.calculateMinMafia()
            it.value = mainViewModel.calculateMinMafia()
            mainViewModel.setMafiaCount(it.value)
        }

        binding.okButton.setOnClickListener {
            onOkClicked()
            dismiss()
        }
    }

    private fun setIndependentSection() {
        val independentRoles = mainViewModel
            .selectedRoles
            .filter { it.side == RoleSide.INDEPENDENT }

        if (independentRoles.isEmpty()) {
            binding.independentCard.visibility = View.GONE
        } else {
            binding.independentCounterText.text =
                context.getString(R.string.independent_count_text, independentRoles.size)
            binding.independentRoles.text =
                independentRoles.joinToString(context.getString(R.string.comma) + SPACE) { role ->
                    context.getString(role.name)
                }
        }
    }

    companion object {
        const val SPACE = " "
    }
}
