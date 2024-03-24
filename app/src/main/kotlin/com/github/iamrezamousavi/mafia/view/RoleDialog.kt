package com.github.iamrezamousavi.mafia.view

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.github.iamrezamousavi.mafia.data.model.Role
import com.github.iamrezamousavi.mafia.databinding.DialogRoleBinding
import com.github.iamrezamousavi.mafia.utils.getDescription
import com.github.iamrezamousavi.mafia.utils.getSide

class RoleDialog(
    context: Context,
    private val role: Role
) : AlertDialog(context) {
    private lateinit var binding: DialogRoleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogRoleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (window != null) {
            window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        binding.titleText.text = context.getString(role.name)

        binding.sideText.text = context.getString(getSide(role.name))

        binding.descriptionText.text = context.getString(getDescription(role.name))

        binding.okButton.setOnClickListener {
            dismiss()
        }
    }
}
