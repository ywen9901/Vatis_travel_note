package com.example.vatis.fragments
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.example.vatis.R
import com.google.firebase.firestore.FieldValue
import kotlinx.android.synthetic.main.fragment_add_folder_dialog.view.*


class AddFolderDialogFragment: DialogFragment() {

    private val db = Firebase.firestore

    companion object {
        val TAG = "AddFolderDialogFragment"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_folder_dialog, container, false)

        view.add_folder_toolbar.inflateMenu(R.menu.dialog_menu)
        view.add_folder_toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.dialog_menu_clear_button -> {
                    dismiss()
                }
            }
            false
        }

        view.add_folder_button.setOnClickListener {
            val folderNameText = view.add_folder_name_text_input.text.toString()
            addFolder(folderNameText)
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        val params: ViewGroup.LayoutParams = dialog!!.window!!.attributes
        params.width = WindowManager.LayoutParams.MATCH_PARENT
        params.height = WindowManager.LayoutParams.MATCH_PARENT
        dialog!!.window!!.attributes = params as WindowManager.LayoutParams
        dialog!!.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
    }
    private fun addFolder(folderName: String) {
        if (folderName.isEmpty()) {
            Toast.makeText(activity, "Please enter the Folder Name", Toast.LENGTH_SHORT).show()
            return
        }
        val user = Firebase.auth.currentUser
        val userEmail = user?.email

        val createFolderData = hashMapOf(
            "importCode" to null,
            "setOff" to null
        )

        if (userEmail != null) {
            db.collection("users").document(userEmail).collection(folderName)
                .add(createFolderData)
                .addOnSuccessListener {
                    Log.d(TAG, "Folder successfully written!")
                    dismiss()
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding folder", e)
                }
            val dbRef = db.collection("users").document(userEmail)
            dbRef.update("folders", FieldValue.arrayUnion(folderName))
        }

    }

}