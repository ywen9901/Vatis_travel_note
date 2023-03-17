package com.example.vatis.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.vatis.CellClickListener
import com.example.vatis.items.MemoSubItem
import com.example.vatis.R
import kotlinx.android.synthetic.main.memo_sub_item.view.*


class MemoSubItemAdapter(private val memoSubItems: ArrayList<MemoSubItem>,
                         private val cellClickListener: CellClickListener) :
    RecyclerView.Adapter<MemoSubItemAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bindItems(item: MemoSubItem) {
            val memoContent = item.content
            itemView.memo_spot_name_text.text = item.spotName
            itemView.memo_content_text.text = memoContent
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.memo_sub_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = memoSubItems[position]
        holder.itemView.setOnClickListener {
            cellClickListener.onCellClickListener(data)
        }
        holder.bindItems(data)
    }

    override fun getItemCount(): Int {
        return memoSubItems.size
    }

}