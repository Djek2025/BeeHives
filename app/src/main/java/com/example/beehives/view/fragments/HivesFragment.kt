package com.example.beehives.view.fragments

import android.app.ActionBar
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.core.view.updatePadding
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.beehives.App
import com.example.beehives.R
import com.example.beehives.cache.SPCache
import com.example.beehives.databinding.FragmentHivesBinding
import com.example.beehives.model.db.entities.Hive
import com.example.beehives.utils.InjectorUtils
import com.example.beehives.view.adapters.HivesAdapter
import com.example.beehives.viewModels.HivesViewModel
import com.example.beehives.viewModels.SharedViewModel
import com.example.beehives.utils.ViewModelFactory
import com.example.beehives.viewModels.BaseViewModel
import javax.inject.Inject

class HivesFragment : Fragment(), HivesAdapter.Callback {

    private lateinit var binding: FragmentHivesBinding
    @Inject lateinit var viewModel : BaseViewModel
    @Inject lateinit var hivesViewModel : HivesViewModel
    @Inject lateinit var sharedViewModel : SharedViewModel
    private lateinit var navController : NavController
    @Inject lateinit var settings: SPCache
    private val adapter by lazy { HivesAdapter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.application as App).getComponent().inject(this)
    }
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_hives, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.vm = hivesViewModel
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        navController = Navigation.findNavController(binding.hivesRecycler)
        binding.hivesRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.hivesRecycler.adapter = adapter

        viewModel.hives.observe(viewLifecycleOwner, {
                adapter.setHives(it)
        })

        binding.floatingActionButton.setOnClickListener {
            viewModel.insertHive(
                Hive(apiaryId = viewModel.getCurrentApiaryId(),
                    name = "Hive ${viewModel.hives.value?.size?.plus(1)}",
                    breed = settings.breed)
            )
        }

        sharedViewModel.liveInsets.observeForever {
//            binding.hivesRecycler.updatePadding(top = binding.hivesRecycler.paddingTop + it.getValue("top"))
            binding.rootView.updatePadding(bottom = it.getValue("bottom"))
        }

    }

    override fun onItemClick(item: Hive) {
        sharedViewModel.selectedHiveId = item.id!!
        viewModel.currentHive.value = item
        navController.navigate(R.id.action_hivesFragment_to_aboutHiveFragment)
    }

    override fun onItemLongClick(item: Hive, view: View) {
        hivesViewModel.checked.value = !hivesViewModel.checked.value!!
        view.setBackgroundColor(resources.getColor(R.color.accentColor))
    }

}