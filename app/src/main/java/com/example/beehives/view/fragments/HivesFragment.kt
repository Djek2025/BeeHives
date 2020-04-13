package com.example.beehives.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.beehives.R
import com.example.beehives.databinding.FragmentHivesBinding
import com.example.beehives.model.db.entities.Hive
import com.example.beehives.utils.InjectorUtils
import com.example.beehives.view.adapters.HivesAdapter
import com.example.beehives.viewModels.HivesViewModel
import com.example.beehives.viewModels.SharedViewModel
import com.example.beehives.viewModels.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_hives.*

class HivesFragment : Fragment(), HivesAdapter.Callback {

    private var apiaryId : Int? = null
    private lateinit var binding: FragmentHivesBinding
    private lateinit var viewModel : HivesViewModel
    private lateinit var factory: ViewModelFactory
    private lateinit var sharedViewModel : SharedViewModel
    private lateinit var navController : NavController
    private val adapter by lazy { HivesAdapter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        factory = InjectorUtils.provideViewModelFactory(activity!!.application)
        viewModel = ViewModelProvider(this, factory).get(HivesViewModel::class.java)
        sharedViewModel = ViewModelProvider(activity as ViewModelStoreOwner).get(SharedViewModel::class.java)
        apiaryId = viewModel.getCurrentApiaryId()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_hives, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.vm = viewModel
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        navController = Navigation.findNavController(hivesRecycler)
        hivesRecycler.adapter = adapter

        viewModel.currentApiaryHives.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.setHives(it)
            }
        })

        floatingActionButton.setOnClickListener {
            viewModel.insertHive(Hive(apiaryId = apiaryId))
        }
    }

    override fun onItemClick(item: Hive) {
        sharedViewModel.selectedHive = item.id!!
        navController.navigate(R.id.action_hivesFragment_to_aboutHiveFragment)
    }

    override fun onItemLongClick(item: Hive) {
        viewModel.checked.value = !viewModel.checked.value!!
    }
}
