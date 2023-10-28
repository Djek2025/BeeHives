package com.example.beehives.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.beehives.utils.*

class SharedViewModel: ViewModel() {

    var selectedHiveId = 1

    var mapRequest = SHOW_MAP_REQUEST

    var scanRequest = REED_SCAN_REQUEST

    var chartRequest = ALL_CHART_REQUEST

    var beeQueenRequest = SELECT_BEE_QUEEN_REQUEST

    val liveInsets = MutableLiveData<Map<String, Int>>()

}