package com.example.vatis


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_sign_up.*


class SignUpActivity : AppCompatActivity() {

    private val db = Firebase.firestore

    companion object {
        val TAG = "SignUpActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        signup_finish_button.setOnClickListener {
            performRegister(email_signup.text.toString(), password_signup.text.toString(), username_signup.text.toString())
        }

        already_have_account_text.setOnClickListener {
            Log.d(TAG, "Try to show login activity")
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

    }

    private fun performRegister(email: String, password: String, username: String) {

        if (username.isEmpty()||email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter text in User name/Email/Password", Toast.LENGTH_SHORT).show()
            return
        }

        Log.d(TAG, "Attempting to create user with email: $email")

        // Firebase Authentication to create a user with email and password
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener

                // else if successful
                Log.d(TAG, "Successfully created user with uid: ${it.result?.user?.uid}")

                saveUserToFirestore(username, email)

                val intent = Intent(this, FolderActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)

            }
            .addOnFailureListener{
                Log.d(TAG, "Failed to create user: ${it.message}")
                Toast.makeText(this, "Failed to create user: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveUserToFirestore(username: String, email: String) {
        val uid = FirebaseAuth.getInstance().uid ?: ""

        val user = hashMapOf(
            "uid" to uid,
            "name" to username,
            "folders" to ArrayList<String>()
        )

        // init user DB structure
        db.collection("users").document(email)
            .set(user)
            .addOnSuccessListener {
                Log.d(TAG, "DocumentSnapshot successfully written!")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
    }


}