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
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import kotlinx.android.synthetic.main.fragment_add_plan_dialog.view.*


class AddPlanDialogFragment(private val folderName: String) : DialogFragment() {
    companion object {
        val TAG = "AddPlanDialogFragment"
        val user = Firebase.auth.currentUser
        val userEmail = user?.email!!
        val initData = hashMapOf(
            "init" to null
        )
    }

    private val db = Firebase.firestore
    private val folderRef = db.collection("users")
                              .document(userEmail)
                              .collection(folderName)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_plan_dialog, container, false)

        view.add_plan_toolbar.inflateMenu(R.menu.dialog_menu)
        view.add_plan_toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.dialog_menu_clear_button -> {
                    dismiss()
                }
            }
            false
        }

        view.add_plan_button.setOnClickListener {
            val planNameText = view.add_plan_name_text_input.text.toString()
            val planDays = Integer.parseInt(view.add_plan_edit_days.text.toString())
            val timeString = System.currentTimeMillis().toString()
            createPlanWithImportCode(timeString, planNameText, planDays)
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

    private fun addPlan(planName: String, planDays: Number, importCode: String) {
        if (planName.isEmpty()) {
            Toast.makeText(activity, "Please enter the Folder Name", Toast.LENGTH_SHORT).show()
            return
        }

        val createPlanData = hashMapOf(
            "dayCount" to planDays,
            "importCode" to importCode,
            "setOff" to true
        )

        val planRef = folderRef.document(planName)
        planRef.set(createPlanData)
            .addOnSuccessListener {
                Log.d(TAG, "Plan successfully written!")
                initCollectionsByName(planRef, "bookmarks")
                initCollectionsByName(planRef, "plan")
                initCollectionsByName(planRef, "recommendations")
                dismiss()
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding plan", e)
            }
    }

    private fun initCollectionsByName(planRef: DocumentReference, collectionName: String){
        planRef.collection(collectionName).add(initData)
            .addOnSuccessListener {
                Log.d(TAG, "init $collectionName success!")
            }
            .addOnFailureListener { e ->
                Log.w(AddFolderDialogFragment.TAG, "init $collectionName failed", e)
            }
    }

    private fun createPlanWithImportCode(timeString: String, planName: String, planDays: Number){
        val docRef = userEmail.let { db.collection("users").document(it) }
        docRef.get().addOnSuccessListener { document ->
            if (document != null) {
                Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                val username = document.data?.get("name")
                val usernameString = username.toString().filterNot { it.isWhitespace() }
                val importCode = usernameString + timeString
                Log.d(TAG, "importCode: $importCode")

                addPlan(planName, planDays, importCode)

            } else {
                Log.d(TAG, "No such document")
            }
        }.addOnFailureListener { exception ->
            Log.d(TAG, "get failed with ", exception)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}