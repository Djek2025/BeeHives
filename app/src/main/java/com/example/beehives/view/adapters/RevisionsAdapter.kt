package com.example.beehives.view.adapters

import android.content.res.Resources
import android.graphics.Color
import android.provider.Settings.Global.getString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.beehives.R
import com.example.beehives.model.db.entities.Revision
import com.example.beehives.utils.SEPARATOR
import com.example.beehives.utils.SEPARATOR_SECONDARY
import com.example.beehives.utils.TimeConverter
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

    override fun onBindViewHolder(holder: MainHolder, position: Int) {

        if (position == 0) {
            holder.itemView.apply {
                this.date.setTextAppearance(R.style.TextAppearance_MaterialComponents_Body1)
                this.frames.setTextAppearance(R.style.TextAppearance_MaterialComponents_Body1)
                this.strength.setTextAppearance(R.style.TextAppearance_MaterialComponents_Body1)
                this.note.setTextAppearance(R.style.TextAppearance_MaterialComponents_Body1)
            }
        } else {
            //Revers adapter
            val revision = revisions[itemCount - (position + 1)]

            holder.itemView.apply {
                date.text = time.longToStringShort(revision.date!!)
//                frames.text = resources.getString(R.string.frames_rev_recycler, frm!![0],frm[1],frm[2])
                frames.text = getFramesStr(revision.frames, resources)
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

    private fun getFramesStr(str: String?, res: Resources): String {

        var compStr = ""

        str?.split(SEPARATOR_SECONDARY)?.let {
            it.forEach {
                it.split(SEPARATOR).let {
                    if (it.size >= 3){
                        compStr += res.getString(R.string.frames_rev_recycler, it[0],it[1],it[2])+"\n"
                    }
                }
            }
        }

        return compStr
    }
}