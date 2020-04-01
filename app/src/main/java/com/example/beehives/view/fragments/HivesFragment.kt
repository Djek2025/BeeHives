package com.example.beehives.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.beehives.R
import com.example.beehives.model.db.entities.Hive
import com.example.beehives.view.adapters.HivesAdapter
import com.example.beehives.viewModel.BaseViewModel
import kotlinx.android.synthetic.main.fragment_hives.*

private const val ARG_CURRENT_APIARY_ID = "current_apiary_id"

class HivesFragment : Fragment() {

    private var apiaryId : Int = 1
    private lateinit var viewModel : BaseViewModel
    private lateinit var navController : NavController

//    companion object {
//        @JvmStatic
//        fun newInstance(): HivesFragment =
//            HivesFragment().apply {
//                arguments = Bundle().apply {
//                    putInt(ARG_CURRENT_APIARY_ID, apiaryId)
//                }
//            }
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        viewModel = ViewModelProvider(this).get(BaseViewModel::class.java)
        arguments?.let {
            apiaryId = it.getInt(ARG_CURRENT_APIARY_ID)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_hives, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        navController = Navigation.findNavController(hivesRecycler)

        viewModel.getCurrentApiaryHives().observe(viewLifecycleOwner, Observer {
            it?.let {
                hivesRecycler.adapter = HivesAdapter(it, object : HivesAdapter.Callback {
                    override fun onItemClicked(item: Hive) {
                        navController.navigate(R.id.aboutHiveFragment, bundleOf("hive_id" to item.id ))
                    }
                })
            }
        })

        floatingActionButton.setOnClickListener {
            viewModel.insertHive(Hive(apiaryId = apiaryId))
        }
    }
}
