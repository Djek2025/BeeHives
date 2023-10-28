package com.example.beehives.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.beehives.R
import com.example.beehives.model.db.entities.Apiary
import kotlinx.android.synthetic.main.item_for_select_apiary_recycler.view.*

class ApiaryAdapter(val callback : Callback) : RecyclerView.Adapter<ApiaryAdapter.MainHolder>() {

    private var apiaryes = mutableListOf<Apiary>()

    fun setData(list: List<Apiary>){
        apiaryes.clear()
        apiaryes.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        return MainHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_for_select_apiary_recycler, parent, false))
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {

        if (apiaryes.isNotEmpty()){
            when (position) {
                apiaryes.size -> holder.itemView.apply {
                    this.imageView.setImageResource(R.drawable.baseline_add_24)
                    this.textName.text = "Add new"
                    this.setOnClickListener {
                        callback.onAddNewClick(it)
                    }
                }
                else -> holder.onBind(apiaryes[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return apiaryes.size + 1
    }

    interface Callback{
        fun onItemClick(item : Apiary)
        fun onItemLongClick(item: Apiary)
        fun onAddNewClick(item: View)
    }

    inner class MainHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun onBind(item: Apiary){
            itemView.textName.text = item.name
            itemView.imageView.setImageResource(R.drawable.bee_chair)
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