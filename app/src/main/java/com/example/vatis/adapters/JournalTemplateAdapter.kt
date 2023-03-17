package com.example.vatis.adapters

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.vatis.CellClickListener
import com.example.vatis.R
import com.example.vatis.items.TemplateItem
import kotlinx.android.synthetic.main.ig_template_item.view.*
import kotlinx.android.synthetic.main.journal_template_item.view.*

class JournalTemplateAdapter(private val templateItems: ArrayList<TemplateItem>,
                             private val cellClickListener: CellClickListener)
    : RecyclerView.Adapter<JournalTemplateAdapter.ViewHolder>(){

    private var selectedPosition = 0 // first template as default

    inner class ViewHolder (itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bindItems(item: TemplateItem) {
            itemView.share_journal_template_item_image.setImageBitmap(item.bitmap)
            if (selectedPosition == absoluteAdapterPosition) {
                itemView.setBackgroundColor(Color.MAGENTA)
            } else {
                itemView.setBackgroundColor(Color.BLACK)
            }

            itemView.setOnClickListener {
                val lastPosition = selectedPosition
                selectedPosition = absoluteAdapterPosition

                if (lastPosition != selectedPosition) {
                    notifyItemChanged(lastPosition)
                    notifyItemChanged(selectedPosition)
                    selectedPosition = absoluteAdapterPosition
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.journal_template_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = templateItems[position]
        holder.bindItems(templateItems[position])
    }

    override fun getItemCount(): Int {
        return templateItems.size
    }

    public fun getSelected(): TemplateItem{
        if (selectedPosition != -1) {
            return templateItems[selectedPosition]
        }
        return templateItems[0]
    }

}