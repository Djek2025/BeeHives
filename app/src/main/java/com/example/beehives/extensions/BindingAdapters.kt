package com.example.beehives.extensions

import android.text.SpannableStringBuilder
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.SeekBar
import androidx.databinding.BindingAdapter
import com.example.beehives.R
import com.example.beehives.utils.SEPARATOR
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.squareup.picasso.Picasso


@BindingAdapter("app:loadPhoto")
fun loadPhoto (view: ImageView, url: String?){
    Picasso.get().load(url).into(view)
}



@BindingAdapter("android:progress")
fun setProgress(view: SeekBar, progress: Int) {
    if (progress != view.progress) {
        view.progress = progress
    }
}

@BindingAdapter("app:lastRevInstalled")
fun setLastRevisionFramesInstall(view: EditText, str: String?){
    val splited = str?.split(SEPARATOR)
    if (!splited.isNullOrEmpty()){
        view.text = SpannableStringBuilder(splited[0])
    }
}

@BindingAdapter("app:lastRevInstalledOf")
fun setLastRevisionFramesInstallOf(view: EditText, str: String?){
    val splited = str?.split(SEPARATOR)
    if (!splited.isNullOrEmpty()){
        view.text = SpannableStringBuilder(splited[1])
    }
}

@BindingAdapter("app:lastRevFramesSize")
fun setLastRevisionFramesSize(view: EditText, str: String?){
    val splited = str?.split(SEPARATOR)
    if (!splited.isNullOrEmpty()){
        view.text = SpannableStringBuilder(splited[2])
    }
}

@BindingAdapter("app:visMapBtn")
fun setButtonInMap(view: FloatingActionButton, str: String){
    when(str){
        "show" -> view.visibility = View.GONE
        else -> view.visibility = View.VISIBLE
    }
}

@BindingAdapter("app:geoBtn")
fun setAboutApiaryGeoBtn(view: FloatingActionButton, str: String?){
    if (str.isNullOrEmpty() || str == SEPARATOR){
        view.setImageResource(R.drawable.baseline_add_location_24)
    } else view.setImageResource(R.drawable.baseline_edit_location_24)

}