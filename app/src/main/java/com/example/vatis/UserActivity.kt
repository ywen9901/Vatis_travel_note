package com.example.vatis

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_user.*


class UserActivity : AppCompatActivity() {

    private val db = Firebase.firestore

    companion object {
        val TAG = "UserActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        verifyUserIsLoggedIn(user_text)
        user_toolbar.setOnClickListener {
            val intent = Intent(this, FolderActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
        logout_button.setOnClickListener { userSignOut() }

    }

    private fun verifyUserIsLoggedIn(textView: TextView) {
        val uid = FirebaseAuth.getInstance().uid
        if (uid == null) {
            val intent = Intent(this, TitleActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        } else {
            getUserName(textView)
            Log.d(TAG, "Successfully login with uid: $uid")
        }
    }

    private fun getUserName(textView: TextView){
        val user = Firebase.auth.currentUser
        user?.let {
            val userEmail = user.email
            val docRef = userEmail?.let { it1 -> db.collection("users").document(it1) }
            if (docRef != null) {
                docRef.get()
                    .addOnSuccessListener { document ->
                        if (document != null) {
                            Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                            val username = document.data?.get("name")
                            textView.text = "Welcome!\u2000"+username

                        } else {
                            Log.d(TAG, "No such document")
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.d(TAG, "get failed with ", exception)
                    }
            }

        }

    }

    private fun userSignOut(){
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(this, TitleActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
}