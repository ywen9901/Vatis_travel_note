package com.example.vatis.adapters

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ernestoyaquello.dragdropswiperecyclerview.listener.OnItemDragListener
import com.ernestoyaquello.dragdropswiperecyclerview.listener.OnItemSwipeListener
import com.example.vatis.AddSpotActivity
import com.example.vatis.R
import com.example.vatis.items.SpotItem
import com.example.vatis.items.SpotSubItem
import kotlinx.android.synthetic.main.spot_item.view.*

class SpotItemAdapter(private val planItems: ArrayList<SpotItem>) :
        RecyclerView.Adapter<SpotItemAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bindItems(item: SpotItem) {
            itemView.dayId.text = item.dayCount
            Log.e("xxx",item.dayCount)
            itemView.spot_sub_item_recyclerview.layoutManager = LinearLayoutManager(itemView.context, RecyclerView.VERTICAL, false)
            itemView.spot_sub_item_recyclerview.adapter = SpotSubItemAdapter(item.spotSubItemList)
            itemView.spot_sub_item_recyclerview.swipeListener = onItemSwipeListener
            itemView.spot_sub_item_recyclerview.dragListener = onItemDragListener

            itemView.btn_AddSpot.setOnClickListener {
                val intent = Intent(itemView.context, AddSpotActivity::class.java)
                startActivity(itemView.context, intent, null)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.spot_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(planItems[position])
    }

    override fun getItemCount(): Int {
        return planItems.size
    }

    private val onItemSwipeListener = object : OnItemSwipeListener<SpotSubItem> {
        override fun onItemSwiped(position: Int, direction: OnItemSwipeListener.SwipeDirection, item: SpotSubItem): Boolean {
            // Handle action of item swiped
            // Return false to indicate that the swiped item should be removed from the adapter's data set (default behaviour)
            // Return true to stop the swiped item from being automatically removed from the adapter's data set (in this case, it will be your responsibility to manually update the data set as necessary)
//            Log.e("xxx",position.toString())
//            Log.e("xxx",item.toString())
//            val db = Firebase.firestore
//            db.collection("users")
//                    .document("yww9901@gmail.com")
//                    .collection("台北")
//                    .document("R9GyvGw1CeglLVkUGt6B")
//                    .collection("recommendations")
//                    .addSnapshotListener(object: EventListener<QuerySnapshot> {
//                        override fun onEvent(
//                                value: QuerySnapshot?,
//                                error: FirebaseFirestoreException?) {
//                            if(error!=null){
//                                Log.e("Firestore error",error.message.toString())
//                                return
//                            }
//                            for(dc: DocumentChange in value?.documentChanges!!){
//                                if(dc.type== DocumentChange.Type.ADDED){
//                                    if(dc.document.toObject(SpotSubItem::class.java).o == locationType){
//                                        if
//                                    }
//
//                                }
//                            }
//                            //myAdapter2.notifyDataSetChanged()
//                        }
//
//
//                    })

            return false
        }
    }

    private val onItemDragListener = object : OnItemDragListener<SpotSubItem> {
        override fun onItemDragged(previousPosition: Int, newPosition: Int, item: SpotSubItem) {
            // Handle action of item being dragged from one position to another
        }

        override fun onItemDropped(initialPosition: Int, finalPosition: Int, item: SpotSubItem) {
            // Handle action of item dropped
            Log.e("xxx",initialPosition.toString())
            Log.e("xxx",finalPosition.toString())
            Log.e("xxx",item.toString())

        }
    }
}