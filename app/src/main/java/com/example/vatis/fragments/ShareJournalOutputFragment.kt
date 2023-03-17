package com.example.vatis.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.vatis.R
import com.example.vatis.items.TemplateItem
import com.google.firebase.firestore.DocumentReference
import kotlinx.android.synthetic.main.fragment_share_journal_output.view.*
import kotlinx.android.synthetic.main.journal_template_item.view.*


class ShareJournalOutputFragment(private val planRef: DocumentReference, private val templateItem: TemplateItem) : Fragment() {
    companion object {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_share_journal_output, container, false)

        view.share_journal_output_image.setImageBitmap(templateItem.bitmap)
        view.share_journal_output_generate_text.setOnClickListener {
            Toast.makeText(this.context, "storing ${templateItem.name} to album", Toast.LENGTH_SHORT).show()
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


}