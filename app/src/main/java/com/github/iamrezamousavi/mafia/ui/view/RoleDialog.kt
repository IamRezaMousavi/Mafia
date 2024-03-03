package com.github.iamrezamousavi.mafia.ui.view

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.github.iamrezamousavi.mafia.databinding.DialogRoleBinding

class RoleDialog(context: Context, private val role: String) : AlertDialog(context) {
    private lateinit var binding: DialogRoleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogRoleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (window != null) {
            window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        binding.titleText.text = role

        binding.okButton.setOnClickListener {
            dismiss()
        }

    }
}