package com.example.beehives.view.dialogs

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.example.beehives.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout


class RenameApiaryDialog : DialogFragment() {

    companion object{
        const val RENAME_APIARY_ARG1 = "current_name"
        const val RENAME_APIARY_BUNDLE_NAME = "text"

        fun newInstance(args : Bundle?): RenameApiaryDialog {
            return RenameApiaryDialog().apply { arguments = args }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = requireActivity().layoutInflater

        return MaterialAlertDialogBuilder(requireActivity(), R.style.RoundShapeTheme)
            .setView(inflater.inflate(R.layout.dialog_rename_apiary, null))
            .setPositiveButton(R.string.confirm) { _, _ ->
                val text = dialog?.window?.findViewById<TextInputEditText>(R.id.name_edit)?.text
                val intent = Intent()
                intent.putExtra(RENAME_APIARY_BUNDLE_NAME, text.toString())
                targetFragment!!.onActivityResult(targetRequestCode, Activity.RESULT_OK, intent)
            }
            .setNegativeButton(R.string.cancel) { _, _ ->

            }
            .create()
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.findViewById<TextInputEditText>(R.id.name_edit)?.text =
            SpannableStringBuilder(arguments?.getString(RENAME_APIARY_ARG1).toString());
    }
}