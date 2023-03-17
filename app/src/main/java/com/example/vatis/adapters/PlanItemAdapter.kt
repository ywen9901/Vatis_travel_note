package com.example.vatis.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.vatis.CellClickListener
import com.example.vatis.R
import com.example.vatis.items.PlanItem
import kotlinx.android.synthetic.main.plan_row.view.*

class PlanItemAdapter(private val planItems: ArrayList<PlanItem>,
                      private val cellClickListener: CellClickListener
) :
    RecyclerView.Adapter<PlanItemAdapter.ViewHolder>(){
    inner class ViewHolder (itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bindItems(item: PlanItem) {
            itemView.plan_name_textview.text = item.planName
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.plan_row, parent, false)
        return ViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = planItems[position]
        holder.bindItems(planItems[position])
        holder.itemView.setOnClickListener { cellClickListener.onCellClickListener(data) }
    }
    override fun getItemCount(): Int {
        return planItems.size
    }
}