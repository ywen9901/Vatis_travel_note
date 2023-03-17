package com.example.vatis.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vatis.*
import com.example.vatis.R
import com.example.vatis.adapters.SpotItemAdapter
import com.example.vatis.items.SpotItem
import com.example.vatis.items.SpotSubItem
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_plan.*
import kotlinx.android.synthetic.main.fragment_plan.view.*
import kotlinx.android.synthetic.main.spot_item.*


@SuppressLint("ResourceType")
class PlanFragment(private val fileRef: DocumentReference) : Fragment() {
    companion object {
        lateinit var planRef: Query
        var spotItemList = ArrayList<SpotItem>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        planRef = fileRef.collection("plan").orderBy("order.day")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_plan, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToRealTimeUpdates()
    }

    private fun buildPlanList(queryPlanItemList: ArrayList<SpotSubItem>): ArrayList<SpotItem> {
        val planItemList = ArrayList<SpotItem>()
        val dayPlanMap = queryPlanItemList.sortedWith(
                compareBy( { it.order.first}, {it.order.second} )
        ).groupBy {
            it.order.first
        }

        for ((day, planSubItemList) in dayPlanMap) {
            planItemList.add(SpotItem("Day $day", planSubItemList as ArrayList<SpotSubItem>))
        }
        return planItemList
    }

    private fun subscribeToRealTimeUpdates() {
        planRef.addSnapshotListener { querySnapShot, firebaseFirestoreException ->
            firebaseFirestoreException?.let {
                Toast.makeText(this.context, it.message, Toast.LENGTH_LONG).show()
                return@addSnapshotListener
            }
            querySnapShot?.let {
                val queryPlanItemList = ArrayList<SpotSubItem>()
                for (document in it) {
                    val data = document.data
                    val spotName = data["name"] as String
                    val type = data["type"] as String
                    val order = data["order"] as Map<*, *>
                    val dayPosPair = Pair(order["day"], order["position"])

                    // add to planSubItemList here
                    queryPlanItemList.add(SpotSubItem(spotName,type,dayPosPair as Pair<Long, Long>))
                }
                spotItemList = buildPlanList(queryPlanItemList)
            }

            view?.spot_item_recyclerview?.layoutManager = LinearLayoutManager(activity)
            view?.spot_item_recyclerview?.adapter = SpotItemAdapter(spotItemList)
        }
    }



}