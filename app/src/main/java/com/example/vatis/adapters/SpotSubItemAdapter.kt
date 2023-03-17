package com.example.vatis.adapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.ernestoyaquello.dragdropswiperecyclerview.DragDropSwipeAdapter
import com.example.vatis.R
import com.example.vatis.items.SpotSubItem

class SpotSubItemAdapter(spotSubItemList : List<SpotSubItem> = emptyList())
    : DragDropSwipeAdapter<SpotSubItem, SpotSubItemAdapter.ViewHolder>(spotSubItemList) {

    class ViewHolder(itemView: View) : DragDropSwipeAdapter.ViewHolder(itemView) {
        val typeIcon: ImageView = itemView.findViewById(R.id.type_icon)
        val itemText: TextView = itemView.findViewById(R.id.item_text)
        val dragIcon: ImageView = itemView.findViewById(R.id.drag_icon)
    }

    override fun getViewHolder(itemView: View) = ViewHolder(itemView)

    override fun onBindViewHolder(item: SpotSubItem, viewHolder: ViewHolder, position: Int) {
        // Here we update the contents of the view holder's views to reflect the item's data
        //viewHolder.itemText.text = item
//        val spotSubItem : SpotSubItem = spotSubItemList[position]
        viewHolder.itemText.text = item.name
        when (item.type) {
            "hotel" -> {
                viewHolder.typeIcon.setImageResource(R.drawable.hotel)
            }
            "restaurant" -> {
                viewHolder.typeIcon.setImageResource(R.drawable.restaurant)
            }
            else -> {
                viewHolder.typeIcon.setImageResource(R.drawable.scenic)
            }
        }

    }

    override fun getViewToTouchToStartDraggingItem(item: SpotSubItem, viewHolder: ViewHolder, position: Int): View? {
        // We return the view holder's view on which the user has to touch to drag the item
        return viewHolder.dragIcon
    }
}
