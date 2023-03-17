package com.example.vatis

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.example.vatis.fragments.AddFolderDialogFragment
import com.example.vatis.adapters.FolderItemAdapter
import com.example.vatis.items.FolderItem
import kotlinx.android.synthetic.main.activity_folder.*
import kotlinx.android.synthetic.main.activity_plan.*
import kotlinx.android.synthetic.main.folder_row.view.*

class FolderActivity: AppCompatActivity(), CellClickListener {
    private val db = Firebase.firestore

    companion object {
        val TAG = "FolderActivity"
        var folderItemList = ArrayList<FolderItem>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_folder)

        verifyUserIsLoggedIn()
        fetchUserFolder()

        folder_toolbar.inflateMenu(R.menu.nav_menu)
        folder_toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_profile_button -> {
                    val intent = Intent(this, UserActivity::class.java)
                    startActivity(intent)
                }
            }
            false
        }

        create_folder_fab.setOnClickListener {
            val dialog = AddFolderDialogFragment()
            dialog.show(supportFragmentManager,"AddFolderDialog")
        }

    }

    private fun verifyUserIsLoggedIn() {
        val uid = FirebaseAuth.getInstance().uid
        if (uid == null) {
            val intent = Intent(this, TitleActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        } else {
            Log.d(TAG, "Successfully login with uid: $uid")
        }
    }

    private fun fetchUserFolder() {
        val user = Firebase.auth.currentUser
        user?.let {
            val userEmail = user.email

            if (userEmail != null) {
                db.collection("users").document(userEmail)
                    .addSnapshotListener {querySnapShot, firebaseFirestoreException ->
                        firebaseFirestoreException?.let {
                            Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                            return@addSnapshotListener
                        }
                        querySnapShot?.let {
                            Log.d(TAG, querySnapShot.get("folders").toString())
                            val queryfolderItemList = ArrayList<FolderItem>()
                            if (querySnapShot.get("folders") != null) {
                                val folderList = querySnapShot.get("folders") as List<String>
                                for (folderName in folderList) {
                                    queryfolderItemList.add(FolderItem(folderName))
                                }
                                folderItemList = queryfolderItemList
                            }
                        }

                    folder_recyclerView.layoutManager = LinearLayoutManager(this)
                    folder_recyclerView.adapter = FolderItemAdapter(folderItemList, this)
                }
            }
        }
    }

    override fun onCellClickListener(data: FolderItem) {
        val intent = Intent(this, PlanActivity::class.java)
        intent.putExtra("folderName", data.folderName)
        startActivity(intent)
    }
}
