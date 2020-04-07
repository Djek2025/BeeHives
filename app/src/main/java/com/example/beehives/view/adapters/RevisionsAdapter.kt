package com.example.beehives.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.beehives.R
import com.example.beehives.model.db.entities.Revision
import com.example.beehives.utils.TimeConverter
import com.example.beehives.view.activities.SEPARATOR
import kotlinx.android.synthetic.main.item_for_revisions_recycler.view.*

class RevisionsAdapter (val callback : Callback)
    : RecyclerView.Adapter<RevisionsAdapter.MainHolder>() {

    private val time = TimeConverter()
    private var revisions = listOf<Revision>()

    fun setRevisions(list: List<Revision>){
        revisions = list
        notifyDataSetChanged()
    }

    interface Callback {
        fun onItemClick(item : Revision)
        fun onItemLongClick(item : Revision)
    }

    override fun getItemCount(): Int {
        return revisions.size + 1
    }

    private fun setHeaderBackground(view: View) {
        view.setBackgroundResource(R.color.colorPrimary)
    }

    private fun setContentBackground(view: View) {
        view.setBackgroundResource(R.color.colorBackground)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {

        if (position == 0) {
            holder.itemView.apply {
                setHeaderBackground(date)
                setHeaderBackground(frames)
                setHeaderBackground(strength)
                setHeaderBackground(note)
            }
        } else {
            //Revers adapter
            val revision = revisions[itemCount - (position + 1)]
            val frm = revision.frames?.split(SEPARATOR)

            holder.itemView.apply {
                setContentBackground(date)
                setContentBackground(frames)
                setContentBackground(strength)
                setContentBackground(note)

                date.text = time.longToStringShort(revision.date!!)
                frames.text = resources.getString(R.string.frames_rev_recycler, frm!![0],frm[1],frm[2])
                strength.text = resources.getString(R.string.percents, revision.strength)
                note.text = revision.note.toString()
                holder.itemView.setOnClickListener {
                    callback.onItemClick(revision)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        return MainHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.item_for_revisions_recycler, parent, false))
    }

    inner class MainHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun onBind(item: Revision){
            itemView.setOnLongClickListener {
                callback.onItemLongClick(item)
                true
            }
            itemView.setOnClickListener {
                callback.onItemClick(item)
            }
        }
    }
}