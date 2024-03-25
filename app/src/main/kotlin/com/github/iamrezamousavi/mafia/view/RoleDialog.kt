package com.github.iamrezamousavi.mafia.view

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

        val citizenSize =
            roleViewModel.selectedRoles.filter { getSide(it.name) == R.string.citizen_side }.size
        binding.citizenCounterText.text =
            context.getString(R.string.citizen_size, citizenSize)

        val independentSize =
            roleViewModel.selectedRoles.filter {
                getSide(it.name) == R.string.independent_side
            }.size
        if (independentSize == 0) {
            binding.independentCard.visibility = View.GONE
        } else {
            binding.independentCounterText.text =
                context.getString(R.string.independent_size, independentSize)
        }

        val mafiaSize =
            roleViewModel.selectedRoles.filter { getSide(it.name) == R.string.mafia_side }.size
        binding.mafiaCounter.value = mafiaSize

        binding.okButton.setOnClickListener {
            val intent = Intent(context, PlayerRoleActivity::class.java)
            context.startActivity(intent)
            dismiss()
        }
    }
}
