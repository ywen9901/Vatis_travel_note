package com.example.vatis.fragments


import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vatis.CellClickListener
import com.example.vatis.adapters.BookmarkAdapter
import com.example.vatis.items.BookmarkItem
import com.example.vatis.R
import com.example.vatis.ShareActivity
import com.example.vatis.WebActivity
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import kotlinx.android.synthetic.main.fragment_bookmark.view.*
import kotlinx.android.synthetic.main.fragment_memo.view.*
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await


class BookmarkFragment(private val fileRef: DocumentReference) : Fragment(), CellClickListener {
    companion object {
        lateinit var bookmarksRef: CollectionReference
        lateinit var bookmarksQuery: Query

        val storageRef = FirebaseStorage.getInstance().reference
        var bookmarkList = ArrayList<BookmarkItem>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bookmarksRef = fileRef.collection("bookmarks")
        bookmarksQuery = bookmarksRef.orderBy("title")

        subscribeToRealTimeUpdates()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bookmark, container, false)
    }

    private fun subscribeToRealTimeUpdates() {
        bookmarksQuery.addSnapshotListener { querySnapShot, firebaseFirestoreException ->
            firebaseFirestoreException?.let {
                Toast.makeText(this.context, it.message, Toast.LENGTH_LONG).show()
                return@addSnapshotListener
            }

            querySnapShot?.let {
                // TODO: cache bookmark item later
                bookmarkList.clear()

                for (document in it) {
                    val data = document.data
                    val id = document.id
                    val title = data["title"] as String
                    val thumbnail = data["thumbnail"] as String
                    val url = data["url"] as String

                    GlobalScope.launch(Dispatchers.IO) {
                        val favicon = fetchThumbnailImage(thumbnail)
                        bookmarkList.add(BookmarkItem(id, title, favicon, url))
                        Log.d(TAG, "bookmarkItem added: $title")

                        withContext(Dispatchers.Main){
                            Log.d(TAG, "bookmarkList size after: ${bookmarkList.size}")

                            val recyclerView = view?.bookmark_list
                            recyclerView?.adapter = BookmarkAdapter(bookmarkList, this@BookmarkFragment)
                            recyclerView?.layoutManager = LinearLayoutManager(activity)

                            val callback = getSwipeCallback()
                            val itemTouchHelper = ItemTouchHelper(callback)
                            itemTouchHelper.attachToRecyclerView(recyclerView)
                        }
                    }
                }
            }

        }
    }

    private suspend fun fetchThumbnailImage(thumbnail: String): Bitmap {
        // default favicon if failed
        var favicon: Bitmap = BitmapFactory.decodeResource(
            resources,
            R.drawable.googleg_standard_color_18
        )

        try {
            val maxDownloadSize = 5L * 1024 * 1024
            val bytes = storageRef.child(thumbnail).getBytes(maxDownloadSize).await()
            favicon = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            Log.d(TAG, "$thumbnail download success")
        } catch (e: Exception) {
            Toast.makeText(activity, e.message, Toast.LENGTH_LONG).show()
        }

        return favicon
    }


    private fun getSwipeCallback(): ItemTouchHelper.SimpleCallback {
        return object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT // add RIGHT for spot extraction maybe
        ) {
            val deleteColor = context?.let { ContextCompat.getColor(it, R.color.DELETE_COLOR) }
            val deleteIcon = R.drawable.ic_baseline_delete_24

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.absoluteAdapterPosition
                val adapter = view?.bookmark_list?.adapter
                adapter?.notifyItemRemoved(position)

                val deletedBookmark = bookmarkList.removeAt(position)
                deleteBookmark(deletedBookmark.id)
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                deleteColor?.let {
                    RecyclerViewSwipeDecorator.Builder(
                        c,
                        recyclerView,
                        viewHolder,
                        dX,
                        dY,
                        actionState,
                        isCurrentlyActive
                    )
                        .addBackgroundColor(it)
                        .addSwipeLeftActionIcon(deleteIcon)
                        .create()
                        .decorate()
                }

                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }

        }
    }

    private fun deleteBookmark(bookmarkId: String){
        bookmarksRef.document(bookmarkId).delete().addOnSuccessListener {
            Toast.makeText(this.context, "Successfully deleted!", Toast.LENGTH_LONG).show()
        }.addOnFailureListener {
            Toast.makeText(this.context, "Delete failed!", Toast.LENGTH_LONG).show()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCellClickListener(data: BookmarkItem) {
        Toast.makeText(this.context, "bookmark item pressed!", Toast.LENGTH_LONG).show()

        val intent = Intent(this.context, WebActivity::class.java).apply{
            putExtra("url", data.url)
        }
        startActivity(intent)
    }

}