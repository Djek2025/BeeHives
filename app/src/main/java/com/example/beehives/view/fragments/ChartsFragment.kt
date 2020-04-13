package com.example.beehives.view.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.beehives.R
import im.dacer.androidcharts.LineView
import kotlinx.android.synthetic.main.fragment_charts.*


class ChartsFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_charts, container, false)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        var strList = arrayListOf<String>("","","","","")
        val table = arrayListOf<ArrayList<Int>>(
            arrayListOf(67, 70, 73,77,81),
            arrayListOf(76, 78, 79,75,75),
            arrayListOf(60, 68, 72,71,78)
        )

        val lineView = line_view as LineView
        lineView.setDrawDotLine(false)
        lineView.setBottomTextList(strList)
        lineView.setShowPopup(LineView.SHOW_POPUPS_MAXMIN_ONLY)
        lineView.setColorArray(intArrayOf(Color.BLACK, Color.GREEN, Color.GRAY, Color.CYAN))
        lineView.setDataList(table)

    }
}
