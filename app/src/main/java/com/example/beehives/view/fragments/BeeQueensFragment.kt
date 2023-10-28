package com.example.beehives.view.fragments

import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.updatePadding
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.GridLayoutManager
import com.example.beehives.App
import com.example.beehives.R
import com.example.beehives.model.db.entities.BeeQueen
import com.example.beehives.utils.InjectorUtils
import com.example.beehives.utils.ViewModelFactory
import com.example.beehives.view.adapters.BeeQueenGridAdapter
import com.example.beehives.viewModels.BaseViewModel
import com.example.beehives.viewModels.SharedViewModel
import kotlinx.android.synthetic.main.fragment_bee_queens.*
import java.time.Year
import java.util.*
import javax.inject.Inject

class BeeQueensFragment : Fragment(), BeeQueenGridAdapter.Callback {

    @Inject lateinit var viewModel : BaseViewModel
    @Inject lateinit var sharedViewModel : SharedViewModel
    private val adapter by lazy { BeeQueenGridAdapter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.application as App).getComponent().inject(this)

        viewModel.getRepo().getAllBeeQueen().observe(this){
            adapter.setData(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_bee_queens, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        gridRecycler.layoutManager = GridLayoutManager(activity, spanCount())
        gridRecycler.adapter = adapter

        sharedViewModel.liveInsets.observeForever {
            rootView.updatePadding(bottom = it.getValue("bottom"))
        }

        fabAddBeeQueen.setOnClickListener {
            viewModel.insertBeeQueen(BeeQueen(
                name = "Bee Queen ${adapter.itemCount + 1}",
                year = Calendar.getInstance().get(Calendar.YEAR))
            )
        }
    }

    override fun onItemClick(item: BeeQueen) {

    }

    override fun onItemLongClick(item: BeeQueen) {

    }

    private fun spanCount(): Int {
        return if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
            3
        } else 2
    }
}