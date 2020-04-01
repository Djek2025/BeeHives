package com.example.beehives.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.beehives.R
import com.example.beehives.model.db.entities.Hive
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_for_hives_recycler.view.*

class HivesAdapter(private val inputItems : List<Hive>, val callback : Callback) : RecyclerView.Adapter<HivesAdapter.MainHolder>() {

    interface Callback {
        fun onItemClicked(item : Hive)
    }

    override fun getItemCount(): Int {
        return  inputItems.size + 1
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {

        if (holder.adapterPosition == 0){
            holder.itemView.photo.visibility = View.GONE
        }else{
            holder.onBind(inputItems[position - 1])
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
                callback.onItemClicked(item)
            }
        }
    }


}