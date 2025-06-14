package com.github.iamrezamousavi.mafia.view.dialog

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.graphics.drawable.toDrawable
import com.github.iamrezamousavi.mafia.databinding.DialogRoleDoneBinding

class RoleDoneDialog(
    context: Context,
    private val onOkClicked: () -> Unit,
    private val onRefreshClicked: () -> Unit
) : AlertDialog(context) {

    private lateinit var binding: DialogRoleDoneBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogRoleDoneBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
        setCanceledOnTouchOutside(false)

        binding.apply {
            okButton.setOnClickListener {
                onOkClicked()
                dismiss()
            }
            refreshButton.setOnClickListener {
                onRefreshClicked()
                dismiss()
            }
        }
    }
}
