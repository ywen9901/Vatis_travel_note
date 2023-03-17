package com.example.vatis.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.vatis.R
import com.example.vatis.fragments.RatingFragment
import com.example.vatis.fragments.RatingFragment.Companion.launchGetImage
import com.example.vatis.fragments.RatingFragment.Companion.selectImage
import com.example.vatis.fragments.RatingFragment.Companion.showCommentEditDialog
import com.example.vatis.fragments.RatingFragment.Companion.showScoreEditDialogFragment
import com.example.vatis.items.RatingItem
import com.google.firebase.firestore.CollectionReference
import kotlinx.android.synthetic.main.rating_item.view.*

class RatingAdapter(private val ratingItems: ArrayList<RatingItem>, private val planRef: CollectionReference): RecyclerView.Adapter<RatingAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bindItems(item: RatingItem) {
            itemView.rating_spot_name_text.text = item.spotName
            itemView.rating_comment_text.text = item.comment
            itemView.rating_score_text.text = item.score.toString()
            itemView.rating_image.setImageBitmap(item.image)

            /**************** rating_image ****************/
            itemView.rating_image.setOnClickListener {
                it.alpha = 0.15f

                // enable upload button
                itemView.rating_upload_button.visibility = View.VISIBLE
                itemView.rating_upload_button.isEnabled = true

                // enable cancel button
                itemView.rating_cancel_button.visibility = View.VISIBLE
                itemView.rating_cancel_button.isEnabled = true
            }

            /**************** rating_upload_button ****************/
            itemView.rating_upload_button.setOnClickListener {
                Toast.makeText(itemView.context, "upload pressed", Toast.LENGTH_SHORT).show()
                selectImage(item.id, this.bindingAdapterPosition)
                launchGetImage()
                resumeImageView(itemView)
            }

            /**************** rating_cancel_button ****************/
            itemView.rating_cancel_button.setOnClickListener {
                resumeImageView(itemView)
            }

            /**************** rating_comment_text ****************/
            itemView.rating_comment_text.setOnClickListener {
                showCommentEditDialog(item)
            }

            /**************** rating_score_text ****************/
            itemView.rating_score_text.setOnClickListener {
                showScoreEditDialogFragment(item)
            }
        }
    }

    private fun resumeImageView(itemView: View){
        itemView.rating_image.alpha = 1f

        // enable upload button
        itemView.rating_upload_button.visibility = View.INVISIBLE
        itemView.rating_upload_button.isEnabled = false

        // enable cancel button
        itemView.rating_cancel_button.visibility = View.INVISIBLE
        itemView.rating_cancel_button.isEnabled = false
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.rating_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = ratingItems[position]
        holder.bindItems(data)
    }

    override fun getItemCount(): Int {
        return ratingItems.size
    }
}