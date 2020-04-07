package com.example.beehives.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.beehives.R
import com.example.beehives.model.db.entities.Hive
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_for_hives_recycler.view.*

class HivesAdapter(val callback : Callback) : RecyclerView.Adapter<HivesAdapter.MainHolder>() {

    private var hives = mutableListOf<Hive>()

    fun setHives(list: List<Hive>){
        hives.clear()
        hives.addAll(list)
        notifyDataSetChanged()
    }

    interface Callback {
        fun onItemClick(item : Hive)
        fun onItemLongClick(item: Hive)
    }

    override fun getItemCount(): Int {
        return  hives.size + 1
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {

        if (holder.adapterPosition == 0){
//            holder.itemView.photo.visibility = View.GONE
        }else{
            holder.onBind(hives[position - 1])
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        return MainHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.item_for_hives_recycler, parent, false))
    }

    inner class MainHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun onBind(item: Hive){
            Picasso.get()
                .load(item.photo)
                .into(itemView.photo)
            itemView.titel.text = item.name.toString()
            itemView.description.text = item.breed.toString()
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