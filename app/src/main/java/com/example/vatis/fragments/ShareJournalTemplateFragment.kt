package com.example.vatis.fragments

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vatis.CellClickListener
import com.example.vatis.R
import com.example.vatis.ShareActivity
import com.example.vatis.adapters.IgTemplateAdapter
import com.example.vatis.adapters.JournalTemplateAdapter
import com.example.vatis.items.TemplateItem
import com.google.firebase.firestore.DocumentReference
import kotlinx.android.synthetic.main.fragment_share_ig_template.view.*
import kotlinx.android.synthetic.main.fragment_share_journal_template.view.*


class ShareJournalTemplateFragment(private val planRef: DocumentReference) : Fragment(), CellClickListener {
    companion object {
        val templateList = ArrayList<TemplateItem>()
        lateinit var adapter: JournalTemplateAdapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val templateNameToIdMap = sortedMapOf(
            Pair("TIMELINE", R.drawable.timeline_preview),
            Pair("ROADMAP", R.drawable.roadmap),
        )

        buildTemplateListByMap(templateNameToIdMap)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        val view = inflater.inflate(R.layout.fragment_share_journal_template, container, false)
        val recyclerView = view.share_journal_template_list

        adapter = JournalTemplateAdapter(templateList, this)
        recyclerView.adapter = adapter

        view.share_journal_template_generate_text.setOnClickListener {
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            val shareJournalOutputFragment = ShareJournalOutputFragment(planRef, adapter.getSelected())

            transaction
                ?.add(R.id.share_fragment_container, shareJournalOutputFragment, "SHARE_JOURNAL_OUTPUT")
                ?.hide(this)
                ?.addToBackStack("SHARE_JOURNAL_OUTPUT")
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