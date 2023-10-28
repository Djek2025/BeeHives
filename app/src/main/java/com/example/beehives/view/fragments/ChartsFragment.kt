package com.example.beehives.view.fragments

import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.marginBottom
import androidx.core.view.updatePadding
import androidx.core.view.updatePaddingRelative
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.example.beehives.App
import com.example.beehives.R
import com.example.beehives.model.db.entities.Revision
import com.example.beehives.utils.InjectorUtils
import com.example.beehives.utils.TimeConverter
import com.example.beehives.utils.ViewModelFactory
import com.example.beehives.viewModels.BaseViewModel
import com.example.beehives.viewModels.ChartsViewModel
import com.example.beehives.viewModels.SharedViewModel
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import kotlinx.android.synthetic.main.fragment_charts.*
import kotlinx.android.synthetic.main.item_for_hives_recycler.view.*
import javax.inject.Inject
import kotlin.random.Random


class ChartsFragment : Fragment() {

    @Inject lateinit var viewModel: ChartsViewModel
    @Inject lateinit var baseViewModel: BaseViewModel
    @Inject lateinit var sharedViewModel : SharedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.application as App).getComponent().inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_charts, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val sets = mutableListOf<ILineDataSet>()

        baseViewModel.hives.value?.forEach { hive ->
            val data = arrayListOf<Entry>()
            val dataExp = arrayListOf<Entry>()

            viewModel.getHiveRevisions(hive.id!!).forEach {
                data.add(Entry(TimeConverter().longToShortLong(it.date!!).shr(30).toFloat(), it.strength?.toFloat()!!))
            }
            data.sortBy {
                it.x
            }
            CalculateValue(viewModel.getHiveRevisions(hive.id).sortedBy { it.date }, data.count()).retuenEntry().forEach {
                dataExp.add(it)
            }



            val color = Random.nextInt()
            val lineDataSet = LineDataSet(data, hive.name).apply {
                circleRadius = 5f
                circleHoleColor = Color.WHITE
                setCircleColor(color)
                setDrawCircleHole(true)
                valueTextSize = 10f
                valueTextColor = resources.getColor(R.color.primaryTextColor)
                this.color = color
                lineWidth = 3f
            }
            val lineDataSetExp = LineDataSet(dataExp, hive.name + "\nForecast").apply {
                circleRadius = 5f
                circleHoleColor = Color.WHITE
                setCircleColor(color)
                setDrawCircleHole(true)
                valueTextSize = 10f
                valueTextColor = resources.getColor(R.color.primaryTextColor)
                this.color = color
                lineWidth = 2f
                enableDashedLine(20f, 10f, 0f)
            }

            sets.add(lineDataSet)
            sets.add(lineDataSetExp)
        }

        chart_view.legend.textColor = resources.getColor(R.color.primaryTextColor)
        chart_view.axisLeft.textColor = resources.getColor(R.color.primaryTextColor)
        chart_view.xAxis.textColor = resources.getColor(R.color.primaryTextColor)

        chart_view.xAxis.valueFormatter = XAxisValueFormatter()
        chart_view.xAxis.granularity = 1f
        chart_view.xAxis.position = XAxis.XAxisPosition.BOTTOM

        chart_view.xAxis.setDrawGridLines(false)
        chart_view.xAxis.spaceMin = 0.5f

        chart_view.axisLeft.setDrawGridLines(false)
        chart_view.axisRight.setDrawGridLines(false)
        chart_view.axisRight.isEnabled = false

        chart_view.setExtraOffsets(20f, 20f, 20f, 20f)
        chart_view.setTouchEnabled(true)
        chart_view.setPinchZoom(true)

        chart_view.isScaleYEnabled = false
        chart_view.data = LineData(sets)
        chart_view.setDrawGridBackground(false)
        chart_view.description.isEnabled = false

        chart_view.invalidate()

    }

    val xAxisDateLong = mutableListOf<Float>()
    inner class XAxisValueFormatter: ValueFormatter() {
        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            return TimeConverter().longToStringShortShort(value.toLong())
        }
    }

    override fun onResume() {
        super.onResume()
        sharedViewModel.liveInsets.observe(viewLifecycleOwner){
            rootView.updatePadding(bottom = it.getValue("bottom"))
        }
    }
}
class CalculateValue(private val revisions: List<Revision>, val countCalculation : Int){

    val arrayStr = arrayListOf<Double>()
    val arrayExp = arrayListOf<Double>()
    var a : Double
    var Uo : Double
    val Un = arrayListOf<Entry>()

    init {
        if (revisions.isNotEmpty()){
            revisions.forEach { arrayStr.add(it.strength!!.toDouble()) }

            a = 2.0/(arrayStr.count() + 1)
            Uo = arrayStr.average()

            findExp()

            repeat(countCalculation) {
                Un.add(Entry(
                    date().shr(30).toFloat(),
                    (arrayStr.last() * a + (1 - a) * arrayExp.last()).toFloat())
                )
                findExp()
                arrayStr.add(Un.last().y.toDouble())
                a = 2.0/(arrayStr.count() + 1)
            }

        } else {
            a = 0.0
            Uo = 0.0
        }
    }

    fun findExp(){
        for (i in 0..arrayStr.count()) {
            if (i == 0) {
                arrayExp.add(Uo)
            } else {
                arrayExp.add( arrayStr[i - 1] * a + (1 - a) * arrayExp[i - 1] )
            }
        }
    }

    fun retuenEntry(): ArrayList<Entry>{

        if (revisions.isNotEmpty()){
            return  Un
        } else {
            return arrayListOf()
        }

    }

    fun date(): Long{
        val range = revisions.last().date!! - revisions.first().date!!
        val mid = range / revisions.count()

        if (Un.isNullOrEmpty()){
            return revisions.last().date!! + mid
        }else return revisions.last().date!! + mid * (Un.count() + 1)
    }

}


