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
import kotlinx.android.synthetic.main.fragment_share_ig_options.view.*


class ShareIgOptionsFragment(private val planRef: DocumentReference) : Fragment() {
    companion object {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_share_ig_options, container, false)

        view.share_ig_select_next_button.setOnClickListener {
            Toast.makeText(this.context, "NEXT clicked", Toast.LENGTH_SHORT).show()
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.show(ShareActivity.shareIgTemplateFragment)
                ?.addToBackStack("SHARE_IG_TEMPLATE")
                ?.hide(this)
                ?.commit()
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


}