package com.example.vatis.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vatis.CellClickListener
import com.example.vatis.R
import com.example.vatis.items.MemoItem
import kotlinx.android.synthetic.main.memo_item.view.*


class MemoItemAdapter(private val memoItems: ArrayList<MemoItem>,
                      private val cellClickListener: CellClickListener) :
    RecyclerView.Adapter<MemoItemAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bindItems(item: MemoItem) {
            itemView.day_count_text.text = item.dayCount
            itemView.memo_sub_item_recyclerview.layoutManager = LinearLayoutManager(itemView.context, RecyclerView.VERTICAL, false)
            itemView.memo_sub_item_recyclerview.adapter = MemoSubItemAdapter(item.memoSubItemList, cellClickListener)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.memo_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(memoItems[position])
    }

    override fun getItemCount(): Int {
        return memoItems.size
    }


}