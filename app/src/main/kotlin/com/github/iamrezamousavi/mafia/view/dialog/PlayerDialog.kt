package com.github.iamrezamousavi.mafia.view.dialog

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.graphics.drawable.toDrawable
import com.github.iamrezamousavi.mafia.data.model.Role
import com.github.iamrezamousavi.mafia.databinding.DialogPlayerBinding
import com.github.iamrezamousavi.mafia.utils.descriptionStringRes
import com.github.iamrezamousavi.mafia.utils.sideStringRes

class PlayerDialog(
    context: Context,
    private val role: Role
) : AlertDialog(context) {
    private lateinit var binding: DialogPlayerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())

        binding.titleText.text = context.getString(role.name)

        binding.sideText.text = context.getString(role.sideStringRes())

        binding.descriptionText.text = context.getString(role.descriptionStringRes())

        binding.okButton.setOnClickListener {
            dismiss()
        }
    }
}
