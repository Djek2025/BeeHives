package com.example.beehives.view.dialogs

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.example.beehives.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText

class RenameHiveDialog : DialogFragment() {

    companion object{
        const val RENAME_HIVE_ARG = "current_hive_name"
        const val RENAME_HIVE_BUNDLE_NAME = "hive_name"

        const val RENAME_BREED_ARG = "current_breed_name"
        const val RENAME_BREED_BUNDLE_NAME = "breed_name"

        fun newInstance(args : Bundle?): RenameHiveDialog {
            return RenameHiveDialog().apply { arguments = args }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = requireActivity().layoutInflater

        return MaterialAlertDialogBuilder(requireActivity(), R.style.RoundShapeTheme)
            .setView(inflater.inflate(R.layout.dialog_rename_hive_breed, null))
            .setPositiveButton(R.string.confirm) { _, _ ->
                val textName = dialog?.window?.findViewById<TextInputEditText>(R.id.name_edit)?.text
                val textBreed = dialog?.window?.findViewById<TextInputEditText>(R.id.breed_edit)?.text
                val intent = Intent()
                intent.putExtra(RENAME_HIVE_BUNDLE_NAME, textName.toString())
                intent.putExtra(RENAME_BREED_BUNDLE_NAME, textBreed.toString())
                targetFragment!!.onActivityResult(targetRequestCode, Activity.RESULT_OK, intent)
            }
            .setNegativeButton(R.string.cancel) { _, _ ->

            }
            .create()
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.findViewById<TextInputEditText>(R.id.name_edit)?.text =
            SpannableStringBuilder(arguments?.getString(RENAME_HIVE_ARG).toString())
        dialog?.window?.findViewById<TextInputEditText>(R.id.breed_edit)?.text =
            SpannableStringBuilder(arguments?.getString(RENAME_BREED_ARG).toString())
    }

}