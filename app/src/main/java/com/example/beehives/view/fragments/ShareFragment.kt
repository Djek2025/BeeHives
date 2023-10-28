package com.example.beehives.view.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment

class ShareFragment: Fragment() {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        startActivity(Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(
                Intent.EXTRA_TEXT,
                "Hey check out my app at: https://github.com/Djek2025/BeeHives")
        })
    }

    override fun onResume() {
        super.onResume()
        activity?.onBackPressed()
    }
}