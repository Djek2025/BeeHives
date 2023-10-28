package com.example.beehives.view.adapters

import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.beehives.R
import com.example.beehives.model.db.entities.BeeQueen
import kotlinx.android.synthetic.main.item_for_select_bee_queen_recycler.view.*

class BeeQueenAdapter(val callback : Callback) : RecyclerView.Adapter<BeeQueenAdapter.MainHolder>() {

    private val queens = mutableListOf<BeeQueen>()

    fun setData(list: List<BeeQueen>){
        queens.clear()
        queens.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        return MainHolder(
            LayoutInflater.from(parent.context)
            .inflate(R.layout.item_for_select_bee_queen_recycler, parent, false))
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {

//        if (position == queens.size) {
//            holder.itemView.apply {
//                this.imageView.setImageResource(R.drawable.baseline_add_24)
//                this.textName.text = "Add new"
//                this.setOnClickListener {
//                    callback.onAddNewClick(it, itemCount)
//                }
//            }
//        } else if (queens.isNotEmpty())

        holder.onBind(queens[position])

    }

    override fun getItemCount() = queens.size // When need at last position add new item, uncomment and size + 1

    interface Callback{
        fun onItemClick(item : BeeQueen)
        fun onItemLongClick(item: BeeQueen)
//        fun onAddNewClick(item: View, itemCount: Int)
    }

    inner class MainHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun onBind(item: BeeQueen){

            itemView.textName.text = item.name
            itemView.textYear.text = item.year.toString()
            itemView.imageView.setImageResource(R.drawable.bee)
            itemView.imageView.setStrokeColorResource(colorForBeeQueen(item.year))
            itemView.setOnClickListener {
                callback.onItemClick(item)
            }
            itemView.setOnLongClickListener {
                callback.onItemLongClick(item)
                true
            }
        }
    }
}