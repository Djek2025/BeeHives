package com.example.beehives.view.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.beehives.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class AboutDialog: DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return MaterialAlertDialogBuilder(requireActivity(), R.style.RoundShapeTheme)
            .setTitle("About")
            .setMessage("resources.getString(R.string.supporting_text)")
            .setPositiveButton(resources.getString(R.string.ok)) { dialog, which ->
                //TODO
            }
            .show()
    }
}