package com.example.beehives.viewModel

import androidx.lifecycle.ViewModel

class SharedViewModel: ViewModel() {

    var currentApiary = 1

    var selectedHive = 1

    var mapRequest: String? = "show"

    var scanRequest: String? = "reed"

}