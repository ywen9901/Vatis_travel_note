package com.example.vatis.fragments

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.setPadding
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vatis.CellClickListener
import com.example.vatis.R
import com.example.vatis.ShareActivity
import com.example.vatis.adapters.IgTemplateAdapter
import com.example.vatis.adapters.IgTemplateArrayAdapter
import com.example.vatis.items.TemplateItem
import com.google.firebase.firestore.DocumentReference
import kotlinx.android.synthetic.main.fragment_share_ig_template.view.*
import kotlinx.android.synthetic.main.ig_template_item.view.*


class ShareIgTemplateFragment(private val planRef: DocumentReference) : Fragment(), CellClickListener {
    companion object {
        val templateList = ArrayList<TemplateItem>()
        lateinit var adapter: IgTemplateAdapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val templateNameToIdMap = sortedMapOf(
            Pair("BEST_RATED", R.drawable.best_rated),
            Pair("COMBINE", R.drawable.combine_preview),
            Pair("DELICACY_MARBLE", R.drawable.delicacy_marble_preview),
            Pair("POINT2POINT", R.drawable.point2point_preview),
            Pair("POLAROID", R.drawable.polaroid_preview),
            Pair("SIX_PHOTO", R.drawable.six_photo_preview),
            Pair("STAY_TIME", R.drawable.stay_time_preview),
            Pair("WHITE_COFFEE", R.drawable.white_coffee_preview),
        )

        buildTemplateListByMap(templateNameToIdMap)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_share_ig_template, container, false)
        val recyclerView = view.share_ig_template_list

        adapter = IgTemplateAdapter(templateList, this)
        recyclerView.adapter = adapter

        view.share_ig_template_generate_text.setOnClickListener {
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            val shareIgOutputFragment = ShareIgOutputFragment(planRef, adapter.getSelected())

            transaction
                ?.add(R.id.share_fragment_container, shareIgOutputFragment, "SHARE_IG_OUTPUT")
                ?.hide(this)
                ?.addToBackStack("SHARE_IG_OUTPUT")
                ?.commit()
        }

        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun buildTemplateListByMap(templateNameToIdMap: Map<String, Int>){
        templateNameToIdMap.keys.forEach {
            templateList.add(TemplateItem(
                it,
                BitmapFactory.decodeResource(resources, templateNameToIdMap[it]!!)
            ))
        }
    }
}