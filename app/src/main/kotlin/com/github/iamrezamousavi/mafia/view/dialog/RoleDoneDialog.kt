package com.github.iamrezamousavi.mafia.view.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
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

        if (window != null) {
            window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
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
