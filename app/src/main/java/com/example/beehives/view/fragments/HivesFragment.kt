package com.example.beehives.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import com.example.beehives.R
import com.example.beehives.model.db.entities.Hive
import com.example.beehives.view.adapters.HivesAdapter
import com.example.beehives.viewModel.MainViewModel
import kotlinx.android.synthetic.main.fragment_hives.*

private const val ARG_CURRENT_APIARY_ID = "current_apiary_id"

class HivesFragment : Fragment() {

    private var apiaryId : Int = 1
    private lateinit var viewModel : MainViewModel

    companion object {
        var INSTANCE : HivesFragment? = null
        @JvmStatic
        fun newInstance(vModel : MainViewModel,apiaryId : Int): HivesFragment {
            val tempInstance = INSTANCE
            return if (tempInstance == null){
                val instance = HivesFragment()
                INSTANCE = instance
                instance.apply {
                    arguments = Bundle().apply {
                        putInt(ARG_CURRENT_APIARY_ID, apiaryId)
                    }
                    viewModel = vModel
                }
            } else tempInstance
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        arguments?.let {
            apiaryId = it.getInt(ARG_CURRENT_APIARY_ID)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_hives, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.getApiaryHives(apiaryId).observe(viewLifecycleOwner, Observer {
            it?.let {
                hivesRecycler.adapter = HivesAdapter(it, object : HivesAdapter.Callback {
                    override fun onItemClicked(item: Hive) {
                        parentFragmentManager
                            .beginTransaction()
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .replace(R.id.container, AboutHiveFragment.newInstance(item.id!!, viewModel))
                            .addToBackStack(null)
                            .commit()
                    }
                })
            }
        })

        floatingActionButton.setOnClickListener {
            viewModel.insertHive(Hive(apiaryId = apiaryId))
        }
    }
}
