package com.example.vatis.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.FragmentActivity
import com.example.vatis.CellClickListener
import com.example.vatis.R
import com.example.vatis.items.TemplateItem
import kotlinx.android.synthetic.main.ig_template_item.view.*

class IgTemplateArrayAdapter(private val context: FragmentActivity?,
                             private val templateItems: ArrayList<TemplateItem>)
    : ArrayAdapter<TemplateItem>(context!!, R.layout.ig_template_item, templateItems){


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = LayoutInflater.from(context).inflate(R.layout.ig_template_item, parent, false)
        val imageView = view.share_ig_template_item_image
        imageView.setImageBitmap(templateItems[position].bitmap)

        return view
    }



}