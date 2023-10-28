package com.example.beehives.view.adapters

import android.R.color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.beehives.R
import com.example.beehives.model.db.entities.BeeQueen
import kotlinx.android.synthetic.main.item_for_grid_recycler.view.*

class BeeQueenGridAdapter(val callback : Callback) : RecyclerView.Adapter<BeeQueenGridAdapter.MainHolder>() {

    private val queens = mutableListOf<BeeQueen>()

    fun setData(list: List<BeeQueen>){
        queens.clear()
        queens.addAll(list)
        notifyDataSetChanged()
    }

    override fun getItemCount() = queens.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        return MainHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_for_grid_recycler, parent, false))
    }

    override fun onBindViewHolder(holder: BeeQueenGridAdapter.MainHolder, position: Int) {
        holder.onBind(queens[position])
    }

    interface Callback{
        fun onItemClick(item : BeeQueen)
        fun onItemLongClick(item: BeeQueen)
    }

    inner class MainHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun onBind(item: BeeQueen){
            itemView.imageView.setBackgroundResource(colorForBeeQueen(item.year))
            itemView.textHead.text = item.name
            itemView.textSub.text = item.description
            itemView.setOnClickListener {
                callback.onItemClick(item)
            }
        }
    }
}
fun colorForBeeQueen (year: Int?): Int {
    return when (year?.rem(10)){
        0,5 -> color.holo_blue_dark
        1,6 -> color.white
        2,7 -> color.holo_orange_light
        3,8 -> color.holo_red_light
        4,9 -> color.holo_green_light
        else -> color.holo_purple
    }
}