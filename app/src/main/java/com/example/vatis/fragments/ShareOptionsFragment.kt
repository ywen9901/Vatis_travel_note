package com.example.vatis.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.vatis.R
import com.example.vatis.ShareActivity
import com.google.firebase.firestore.DocumentReference
import kotlinx.android.synthetic.main.fragment_share_options.view.*


class ShareOptionsFragment(private val planRef: DocumentReference) : Fragment() {
    companion object {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_share_options, container, false)

        view.share_ig_text.setOnClickListener {
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.show(ShareActivity.shareIgOptionsFragment)
                ?.addToBackStack("SHARE_OPTION")
                ?.hide(this)
                ?.commit()
        }

        view.share_journal_text.setOnClickListener {
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.show(ShareActivity.shareJournalOptionsFragment)
                ?.addToBackStack("SHARE_OPTION")
                ?.hide(this)
                ?.commit()
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


}