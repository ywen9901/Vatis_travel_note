package com.example.vatis.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.vatis.CellClickListener
import com.example.vatis.R
import com.example.vatis.items.RecommendationItem

class RecommendationAdapter(
    private val recommendList : ArrayList<RecommendationItem>,
    private val cellClickListener: CellClickListener
) :RecyclerView.Adapter<RecommendationAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val name : TextView = itemView.findViewById(R.id.name)
        //val location : TextView = itemView.findViewById(R.id.location)
        //val type : TextView = itemView.findViewById(R.id.type)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recommendation_item,parent,false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recommend : RecommendationItem = recommendList[position]
        holder.name.text = recommend.name

        val data = recommendList[position]
        holder.itemView.setOnClickListener {
            cellClickListener.onCellClickListener(data)
        }
    }

    override fun getItemCount(): Int {
        return recommendList.size
    }

}