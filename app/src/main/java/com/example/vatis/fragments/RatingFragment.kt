package com.example.vatis.fragments

import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.forEach
import androidx.core.view.get
import androidx.core.view.iterator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vatis.R
import com.example.vatis.ShareActivity
import com.example.vatis.adapters.RatingAdapter
import com.example.vatis.items.RatingItem
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.fragment_memo.view.*
import kotlinx.android.synthetic.main.fragment_rating.view.*
import kotlinx.android.synthetic.main.rating_item.*
import kotlinx.android.synthetic.main.rating_item.view.*
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

class RatingFragment(private val fileRef: DocumentReference) : Fragment() {
    companion object {
        lateinit var planRef: CollectionReference
        lateinit var planQuery: Query
        lateinit var storageRef: StorageReference

        lateinit var userEmail: String

        lateinit var selectImage: (String, Int) -> Unit
        lateinit var launchGetImage: () -> Unit

        var imageFile: Uri? = null
        var docId: String? = null
        var position: Int? = null

        lateinit var showCommentEditDialog: (RatingItem) -> Unit
        lateinit var showScoreEditDialogFragment: (RatingItem) -> Unit
    }

    private val getImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        requireView().rating_list[position!!].rating_image.setImageURI(uri)
        requireView().rating_list
        imageFile = uri
        docId?.let { it -> uploadImageToStorage(planRef, it) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        planRef = fileRef.collection("plan")
        planQuery = planRef.orderBy("order.day")

        selectImage = { selectedDocId: String, selectedPosition: Int ->
            docId = selectedDocId
            position = selectedPosition
        }

        launchGetImage = {
            getImage.launch("image/*")
        }

        showCommentEditDialog = {
            val editDialogFragment = RatingCommentEditDialogFragment(it, planRef)
            editDialogFragment.show(childFragmentManager, "RatingCommentEditDialog")
        }

        showScoreEditDialogFragment = {
            val editDialogFragment = RatingScoreEditDialogFragment(it, planRef)
            editDialogFragment.show(childFragmentManager, "RatingScoreEditDialog")
        }

        userEmail = fileRef.parent.parent?.id.toString()
        storageRef = userEmail.let {
            FirebaseStorage.getInstance().reference.child(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_rating, container, false)
        subscribeToRealTimeUpdates()

        view.rating_share_button.setOnClickListener {
            Toast.makeText(this.context, "share button pressed", Toast.LENGTH_SHORT).show()
            val intent = Intent(this.context, ShareActivity::class.java).apply{
                putExtra("userEmail", userEmail)
                putExtra("folderName", planRef.parent?.id)
                putExtra("planName", planRef.id)
            }
            startActivity(intent)
        }
        return view
    }

    override fun onResume() {
        super.onResume()
    }

    // upload to Storage and update Firestore spot "image"
    private fun uploadImageToStorage(planRef: CollectionReference, docId: String) = CoroutineScope(Dispatchers.IO).launch{
        try {
            imageFile?.let {
                // image upload
                storageRef.child("$docId.jpg").putFile(it).await()
                planRef.document(docId).update("image", "$docId.jpg")
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@RatingFragment.context, "Image upload successfully!", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: Exception){
            withContext(Dispatchers.Main){
                Toast.makeText(this@RatingFragment.context, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun subscribeToRealTimeUpdates() {
        planQuery.addSnapshotListener { querySnapShot, firebaseFirestoreException ->
            firebaseFirestoreException?.let {
                Toast.makeText(this.context, it.message, Toast.LENGTH_LONG).show()
                return@addSnapshotListener
            }
            querySnapShot?.let {
                CoroutineScope(Dispatchers.IO).launch{
                    fetchPlan(it)
                }
            }
        }
    }

    private suspend fun setLayoutAndAdapter(ratingList: ArrayList<RatingItem>){
        withContext(Dispatchers.Main){
            view?.rating_list?.layoutManager = LinearLayoutManager(activity)
            view?.rating_list?.adapter = RatingAdapter(ratingList, planRef)
        }
    }

    private suspend fun fetchPlan(querySnapShot: QuerySnapshot){
        val ratingList = ArrayList<RatingItem>()
        for (document in querySnapShot) {
            Log.d(TAG, "doc id: ${document.id}")
            val data = document.data
            val spotName = data["name"] as String
            val comment = data["comment"] as String
            val imagePath = data["image"] as String
            val score = data["rating"].toString().toFloat()
            val order = data["order"] as Map<*, *>
            val dayPosPair = Pair(order["day"], order["position"])

            withContext(Dispatchers.IO){
                val image = fetchImage(imagePath)
                withContext(Dispatchers.Main){
                    ratingList.add(RatingItem(document.id, spotName, comment, score, image, dayPosPair as Pair<Long, Long>))
                }
            }
        }
        Log.d(TAG, "ratingList size after fetch: ${ratingList.size}")
        setLayoutAndAdapter(ratingList)
    }

    private suspend fun fetchImage(imagePath: String): Bitmap {
        // default spotImage if failed

        var spotImage: Bitmap = BitmapFactory.decodeResource(
            resources,
            R.drawable.default_image
        )

        if (imagePath.isEmpty()) {
            return spotImage
        }

        try {
            val maxDownloadSize = 5L * 1024 * 1024
            val bytes = storageRef.child(imagePath).getBytes(maxDownloadSize).await()
            spotImage = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            Log.d(TAG, "$imagePath download success")
        } catch (e: Exception) {
            Toast.makeText(activity, e.message, Toast.LENGTH_LONG).show()
        }

        return spotImage
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


}