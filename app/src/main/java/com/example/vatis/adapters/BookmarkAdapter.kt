package com.example.vatis.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.vatis.CellClickListener
import com.example.vatis.items.BookmarkItem
import com.example.vatis.R
import kotlinx.android.synthetic.main.bookmark_item.view.*


class BookmarkAdapter(private val bookmarkItems: ArrayList<BookmarkItem>,
                      private val cellClickListener: CellClickListener
) :
    RecyclerView.Adapter<BookmarkAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bindItems(item: BookmarkItem) {
            itemView.bookmark_title_text.text = item.title
            itemView.bookmark_image.setImageBitmap(item.image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.bookmark_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = bookmarkItems[position]
        holder.bindItems(data)
        holder.itemView.setOnClickListener {
            cellClickListener.onCellClickListener(data)
        }
    }

    override fun getItemCount(): Int {
        return bookmarkItems.size
    }

}