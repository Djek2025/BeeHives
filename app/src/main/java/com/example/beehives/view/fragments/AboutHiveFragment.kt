package com.example.beehives.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer

import com.example.beehives.R
import com.example.beehives.model.db.entities.Revision
import com.example.beehives.view.adapters.RevisionsAdapter
import com.example.beehives.viewModel.MainViewModel
import kotlinx.android.synthetic.main.fragment_about_hive.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val ARG_ID_KEY = "ABOUT_HIVE_KEY"

class AboutHiveFragment : Fragment() {

    private var hiveId: Int? = null
    private lateinit var viewModel: MainViewModel

    companion object {
        @JvmStatic
        fun newInstance(hiveId: Int, vModel: MainViewModel) =
            AboutHiveFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_ID_KEY, hiveId)
                }
                viewModel = vModel
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        arguments?.let {
            hiveId = it.getInt(ARG_ID_KEY)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_about_hive, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                val hive = viewModel.getHiveById(hiveId!!).await()
                withContext(Dispatchers.Main) {
                    name.text = hive.name
                    breed.text = hive.breed
                    frames.text = hive.id.toString()

                    viewModel.getHiveRevisions(hiveId!!)
                        .observe(this@AboutHiveFragment as LifecycleOwner, Observer {
                            it?.let {
                                revisionsRecycler.adapter = RevisionsAdapter(it, object : RevisionsAdapter.Callback {
                                    override fun onItemClicked(item: Revision) {

                                    }

                                })
                            }
                        })
                }
            }
        }

        add_revision_btn.setOnClickListener {
            parentFragmentManager
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.container, AddRevisionFragment.newInstance(hiveId!!, viewModel!!))
                .addToBackStack(null)
                .commit()
        }
    }
}
