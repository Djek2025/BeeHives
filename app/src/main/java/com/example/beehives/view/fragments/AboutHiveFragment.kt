package com.example.beehives.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation

import com.example.beehives.R
import com.example.beehives.model.db.entities.Revision
import com.example.beehives.view.adapters.RevisionsAdapter
import com.example.beehives.viewModel.BaseViewModel
import kotlinx.android.synthetic.main.fragment_about_hive.*

private const val ARG_ID_KEY = "hive_id"

class AboutHiveFragment : Fragment() {

    private var hiveId: Int? = null
    private lateinit var viewModel: BaseViewModel
    private lateinit var navController: NavController

    companion object {
        @JvmStatic
        fun newInstance() =
            AboutHiveFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        viewModel = ViewModelProvider(this).get(BaseViewModel::class.java)
        arguments?.let {
            hiveId = it.getInt(ARG_ID_KEY)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_about_hive, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        navController = Navigation.findNavController(revisionsRecycler)

        viewModel.getHiveByIdLd(hiveId!!).observe(this as LifecycleOwner, Observer {
            name.text = it.name
            breed.text = it.breed
            frames.text = it.id.toString()
        })

        viewModel.getHiveRevisions(hiveId!!).observe(this as LifecycleOwner, Observer {
                it?.let {
                    revisionsRecycler.adapter =
                        RevisionsAdapter(it, context!!, object : RevisionsAdapter.Callback {
                            override fun onItemClicked(item: Revision) {

                            }
                        })
                }
        })

        add_revision_btn.setOnClickListener {
            navController.navigate(R.id.addRevisionFragment, bundleOf("current_hive_id" to hiveId))
        }
    }
}
