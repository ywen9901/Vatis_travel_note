package com.example.vatis

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.example.vatis.items.PlanItem
import com.example.vatis.adapters.PlanItemAdapter
import com.example.vatis.fragments.AddPlanDialogFragment
import kotlinx.android.synthetic.main.activity_folder.*
import kotlinx.android.synthetic.main.activity_plan.*
import kotlinx.android.synthetic.main.folder_row.view.*
import kotlinx.android.synthetic.main.plan_row.view.*


class PlanActivity :AppCompatActivity(), CellClickListener {
    companion object{
        val TAG = "PlanActivity"
        val dbRef = Firebase.firestore.collection("users")
        var planItemList = ArrayList<PlanItem>()
        lateinit var folderName: String
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plan)

        verifyUserIsLoggedIn()

        plan_delete_text.visibility = View.INVISIBLE
        delete_plan_fab.visibility = View.INVISIBLE

        folderName = intent.getStringExtra("folderName").toString()
        if (folderName.isNotEmpty()) {
            fetchUserPlan(folderName)
        }

        //plan_toolbar.inflateMenu(R.menu.nav_menu)
        plan_toolbar.setOnClickListener {
            val intent = Intent(this, FolderActivity::class.java)
            startActivity(intent)
        }

        create_plan_button.setOnClickListener {
            val addPlanDialogFragment = folderName?.let { it1 -> AddPlanDialogFragment(it1) }
            addPlanDialogFragment?.show(supportFragmentManager, "addPlanDialog")
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


    private fun fetchUserPlan(folderName: String){
        val user = Firebase.auth.currentUser
        val userEmail = user?.email
        if (userEmail != null) {
            dbRef.document(userEmail).collection(folderName).orderBy("dayCount")
                .addSnapshotListener{querySnapShot, firebaseFirestoreException ->
                    firebaseFirestoreException?.let {
                        Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                        return@addSnapshotListener
                    }
                    querySnapShot?.let {
                        val queryplanItemList = ArrayList<PlanItem>()
                        for (document in it){
                            val planName = document.id
                            queryplanItemList.add(PlanItem(planName))
                        }
                        planItemList = queryplanItemList
                    }

                    plan_recyclerView.layoutManager = LinearLayoutManager(this)
                    plan_recyclerView.adapter = PlanItemAdapter(planItemList, this)
                }
        }
    }

    override fun onCellClickListener(data: PlanItem) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("folderName", folderName)
        intent.putExtra("planName", data.planName)
        startActivity(intent)
    }
}

