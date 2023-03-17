package com.example.vatis.fragments

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vatis.adapters.MemoItemAdapter
import com.example.vatis.items.MemoSubItem
import com.example.vatis.R
import com.example.vatis.items.MemoItem
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_memo.view.*
import kotlinx.android.synthetic.main.fragment_memo_edit.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.Exception


class MemoEditDialogFragment(val memoSubItem: MemoSubItem, val planRef: CollectionReference) : DialogFragment() {
    companion object {

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_memo_edit, container, false)

        view.memo_edit_spot_name.text = memoSubItem.spotName
        view.memo_edit_content_text.text = memoSubItem.content

        view.memo_edit_exit_button.setOnClickListener{
            dismiss()
        }

        // CONTENT TEXT
        view.memo_edit_content_text.setOnClickListener {
            // content input
            view.memo_edit_content_input.visibility = View.VISIBLE
            view.memo_edit_content_input.isEnabled = true

            val oldContent =  view.memo_edit_content_text.text
            view.memo_edit_text_input_layout.editText?.setText(oldContent)

            // content text
            view.memo_edit_content_text.visibility = View.INVISIBLE
            view.memo_edit_content_text.isClickable = false

            // save button
            view.memo_edit_save_button.visibility = View.VISIBLE
            view.memo_edit_save_button.isClickable = true
        }

        // SAVE BUTTON
        view.memo_edit_save_button.setOnClickListener {
            val content = view.memo_edit_text_input_layout.editText?.text.toString()
            updateMemo(content)

            // content input
            view.memo_edit_content_input.visibility = View.INVISIBLE

            // content text
//            view.memo_edit_content_text.text = view.memo_edit_text_input_layout.editText?.text
            view.memo_edit_content_text.visibility = View.VISIBLE
            view.memo_edit_content_text.isClickable = true

            // save button
            view.memo_edit_save_button.visibility = View.INVISIBLE
            view.memo_edit_save_button.isClickable = false

            dismiss()
        }

        return view
    }

    private fun updateMemo(content: String) = CoroutineScope(Dispatchers.IO).launch{
        val order = memoSubItem.order
        val orderMap: HashMap<String, Long> = hashMapOf(
            "day" to order.first,
            "position" to order.second
        )

        // perform query on plan order
        val spotQuery = planRef
            .whereEqualTo("order", orderMap)
            .get()
            .await()

        if (spotQuery.documents.isNotEmpty()) {
            for (document in spotQuery) {
                try {
                    planRef.document(document.id).update("memo", content)
                } catch (e: Exception) {
                    withContext(Dispatchers.Main){
                        Toast.makeText(this@MemoEditDialogFragment.context, "Update memo failed", Toast.LENGTH_LONG).show()
                    }
                }
            }
        } else {
            withContext(Dispatchers.Main){
                Toast.makeText(this@MemoEditDialogFragment.context, "No spot matched the query", Toast.LENGTH_LONG).show()
            }
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}