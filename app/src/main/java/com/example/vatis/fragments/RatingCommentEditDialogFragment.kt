package com.example.vatis.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.vatis.R
import com.example.vatis.items.RatingItem
import com.google.firebase.firestore.CollectionReference
import kotlinx.android.synthetic.main.fragment_rating_comment_edit.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.Exception


class RatingCommentEditDialogFragment(private val ratingItem: RatingItem, private val planRef: CollectionReference) : DialogFragment() {
    companion object {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_rating_comment_edit, container, false)

        view.rating_comment_edit_spot_name.text = ratingItem.spotName
        view.rating_comment_edit_text.text = ratingItem.comment

        view.rating_comment_edit_exit_button.setOnClickListener{
            dismiss()
        }

        /*********** COMMENT TEXT ***********/
        view.rating_comment_edit_text.setOnClickListener {
            // comment input
            view.rating_edit_comment_input.visibility = View.VISIBLE
            view.rating_edit_comment_input.isEnabled = true

            val oldContent =  view.rating_comment_edit_text.text
            view.rating_edit_text_input_layout.editText?.setText(oldContent)

            // comment text
            view.rating_comment_edit_text.visibility = View.INVISIBLE
            view.rating_comment_edit_text.isClickable = false
        }

        /*********** SAVE BUTTON ***********/
        view.rating_comment_edit_save_button.setOnClickListener {
            val comment = view.rating_edit_text_input_layout.editText?.text.toString()
            view.rating_comment_edit_text.text = comment
            updateRating(ratingItem.id, comment)

            // comment input
            view.rating_edit_comment_input.visibility = View.INVISIBLE

            // comment text
            view.rating_comment_edit_text.visibility = View.VISIBLE
            view.rating_comment_edit_text.isClickable = true

            dismiss()
        }

        return view
    }

    private fun updateRating(planId: String, comment: String) = CoroutineScope(Dispatchers.IO).launch{
        try {
            planRef.document(planId).update("comment", comment)
        } catch (e: Exception) {
            withContext(Dispatchers.Main){
                Toast.makeText(this@RatingCommentEditDialogFragment.context, "更新失敗，請重試", Toast.LENGTH_LONG).show()
            }
        }.run {
            withContext(Dispatchers.Main){
                Toast.makeText(this@RatingCommentEditDialogFragment.context, "更新成功!", Toast.LENGTH_LONG).show()
            }
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}