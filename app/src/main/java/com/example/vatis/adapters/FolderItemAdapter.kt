package com.example.vatis.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.vatis.CellClickListener
import com.example.vatis.R
import com.example.vatis.items.FolderItem
import kotlinx.android.synthetic.main.folder_row.view.*

class FolderItemAdapter(private val folderItems: ArrayList<FolderItem>,
                        private val cellClickListener: CellClickListener
) :
    RecyclerView.Adapter<FolderItemAdapter.ViewHolder>(){
    inner class ViewHolder (itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bindItems(item: FolderItem) {
            itemView.folder_name_textview.text = item.folderName
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.folder_row, parent, false)
        return ViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = folderItems[position]
        holder.bindItems(folderItems[position])
        holder.itemView.setOnClickListener { cellClickListener.onCellClickListener(data) }
    }
    override fun getItemCount(): Int {
        return folderItems.size
    }
}