package com.example.beehives.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.beehives.R
import com.example.beehives.model.db.entities.Revision
import kotlinx.android.synthetic.main.item_for_revisions_recycler.view.*

class RevisionsAdapter (private val inputItems : List<Revision>, val callback : Callback)
    : RecyclerView.Adapter<RevisionsAdapter.MainHolder>() {


    interface Callback {
        fun onItemClicked(item : Revision)
    }

    override fun getItemCount(): Int {
        return  inputItems.size + 1
    }

    private fun setHeaderBackground(view: View) {
        view.setBackgroundResource(R.color.colorPrimary)
    }

    private fun setContentBackground(view: View) {
        view.setBackgroundResource(R.color.design_default_color_background)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
//        holder.onBind(inputItems[position])

        if (holder.adapterPosition == 0) {
            holder.itemView.apply {
                setHeaderBackground(date)
                setHeaderBackground(frames)
                setHeaderBackground(strength)
                setHeaderBackground(note)
            }
        } else {
            val revision = inputItems[holder.adapterPosition - 1]

            holder.itemView.apply {
                setContentBackground(date)
                setContentBackground(frames)
                setContentBackground(strength)
                setContentBackground(note)

                date.text = revision.date.toString()
                frames.text = revision.frames.toString()
                strength.text = revision.strength.toString() + "%"
                note.text = revision.note.toString()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        return MainHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.item_for_revisions_recycler, parent, false))
    }

    inner class MainHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun onBind(item: Revision){
            itemView.setOnClickListener {
                callback.onItemClicked(item)
            }
        }
    }

}